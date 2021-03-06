package com.macys.sstpac.spring.nested.beans;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class DependencySorter {
    private static final Logger log = Logger.getLogger(DependencySorter.class);
    private static final Logger logGraph = Logger.getLogger("spring.nested.dependencies.graph");
    private List<Location> conflictContextGroup = Collections.emptyList();
    private static Pattern locationNamePattern = Pattern.compile("[A-Za-z0-9\\.\\$_-]*\\.xml");

    interface Predicate {
        boolean isValid(Location s);
    }

    static class Location {
        String locationName;
        Set<BeanReferenceInfo> importBeans;
        Set<BeanReferenceInfo> exportBeans;

        public Location(String locationName, Set<BeanReferenceInfo> importBeans, Set<BeanReferenceInfo> exportBeans) {
            this.locationName = locationName;
            this.importBeans = importBeans;
            this.exportBeans = exportBeans;
        }

        public String getLocationName() {
            return locationName;
        }

        public Set<BeanReferenceInfo> getImportBeans() {
            return importBeans;
        }

        public Set<BeanReferenceInfo> getExportBeans() {
            return exportBeans;
        }
    }

    List<Location> locations = new LinkedList<Location>();

    public DependencySorter(String[] configLocations, Map<String, List<BeanReferenceInfo>> imports, Map<String, BeanReferenceInfo> exports) {
        this.locations = prepareLocations(configLocations, imports, exports);
    }

    private List<Location> prepareLocations(String[] configLocations, Map<String, List<BeanReferenceInfo>> imports, Map<String, BeanReferenceInfo> exports) {
        List<Location> locations = new LinkedList<Location>();
        Map<String, Location> locationsMap = new HashMap<String, Location>();
        for (String locationName : configLocations) {
            Location location = new Location(locationName, new HashSet<BeanReferenceInfo>(), new HashSet<BeanReferenceInfo>());
            locations.add(location);
            locationsMap.put(location.getLocationName(), location);
        }
        Set<String> allImportedBeans = fillLocationImportVectors(locationsMap, imports);
        fillLocationExportVectors(locationsMap, exports, allImportedBeans);
        showContextGraphStructure(locationsMap, exports);
        return locations;
    }

    private void showContextGraphStructure(Map<String, Location> locationsMap, Map<String, BeanReferenceInfo> exports) {
        if (logGraph.isDebugEnabled()) {
            logGraph.debug("digraph G {");
            for (String locationName : locationsMap.keySet()) {
                Map<String, StringBuilder> exportingLocations = new HashMap<String, StringBuilder>();
                for (BeanReferenceInfo bean : locationsMap.get(locationName).getImportBeans()) {
                    String exportingLocation = exports.get(bean.getBeanName()).getLocation();
                    if (!exportingLocations.containsKey(exportingLocation)) {
                        exportingLocations.put(exportingLocation, new StringBuilder());
                    }
                    exportingLocations.get(exportingLocation).append(bean.getBeanName()).append("\\n");
                }
                for (String exportingLocationName : exportingLocations.keySet()) {
                    String beans = exportingLocations.get(exportingLocationName).toString();
                    /*logGraph.debug("\"{}\" -> \"{}\" [label = \"{}\"];", new Object[]{getSimpleLocationName(locationName),
                            getSimpleLocationName(exportingLocationName), beans})*/;
                }
            }
            logGraph.debug("}");
        }
    }

    private void fillLocationExportVectors(Map<String, Location> locations, Map<String, BeanReferenceInfo> beans, Set<String> allImportedBeans) {
        for (String beanName : beans.keySet()) {
            if (allImportedBeans.contains(beanName)) {
                Location location = locations.get(beans.get(beanName).getLocation());
                location.getExportBeans().add(beans.get(beanName));
            }
        }
    }

    private Set<String> fillLocationImportVectors(Map<String, Location> locations, Map<String, List<BeanReferenceInfo>> importedBeans) {
        Set<String> allImportBeans = new HashSet<String>();
        for (String beanName : importedBeans.keySet()) {
            List<BeanReferenceInfo> beans = importedBeans.get(beanName);
            for (BeanReferenceInfo bean : beans) {
                Location location = locations.get(bean.getLocation());
                location.getImportBeans().add(bean);
                allImportBeans.add(bean.getBeanName());
            }
        }
        return allImportBeans;
    }

    public String[] sort() {
        return collectLocationNames(sortLocations());
    }


    public String[] collectLocationNames(List<Location> locations) {
        String[] result = new String[locations.size()];
        int i = 0;
        for (Location location : locations) {
            result[i++] = location.getLocationName();
        }
        return result;
    }

    public String[] getConflictContextGroup() {
        return collectLocationNames(conflictContextGroup);
    }


    private List<Location> sortLocations() {
        LinkedList<Location> list = new LinkedList<Location>(locations);
        List<Location> orderedHead = pullLocationListHead(list);
        if (list.isEmpty()) {
            return orderedHead;
        }

        List<Location> tail = pullLocationListTail(list);
        if (!list.isEmpty()) {
            conflictContextGroup = list;
            warningMessage(list);
        } else {
            conflictContextGroup = Collections.emptyList();
        }
        orderedHead.addAll(list);
        orderedHead.addAll(tail);
        return orderedHead;
    }

    private List<Location> pullLocationListTail(LinkedList<Location> list) {
        LinkedList<Location> tail = new LinkedList<Location>();
        final LinkedList<String> annihilatedExports = new LinkedList<String>();
        Predicate hasNoDependencies = new Predicate() {
            public boolean isValid(Location s) {
                return s.getExportBeans().isEmpty() || annihilatedExports.containsAll(collectBeanNames(s.getExportBeans()));
            }
        };
        Collections.reverse(list);
        Location location;
        while (!list.isEmpty() && (location = removeLocation(list, hasNoDependencies)) != null) {
            for (BeanReferenceInfo imp : location.getImportBeans()) {
                if (thereIsNoImport(list, imp)) {
                    annihilatedExports.add(imp.getBeanName());
                }
            }
            tail.add(0, location);
        }
        Collections.reverse(list);
        return tail;
    }

    private List<Location> pullLocationListHead(LinkedList<Location> list) {
        final Set<String> definedBeans = new LinkedHashSet<String>();
        Predicate canBeFired = new Predicate() {
            public boolean isValid(Location location) {
                return definedBeans.containsAll(collectBeanNames(location.getImportBeans()));
            }
        };
        Location location;
        LinkedList<Location> orderedHead = new LinkedList<Location>();
        while (!list.isEmpty() && (location = removeLocation(list, canBeFired)) != null) {
            definedBeans.addAll(collectBeanNames(location.getExportBeans()));
            orderedHead.add(location);
        }
        return orderedHead;
    }

    private void warningMessage(LinkedList<Location> list) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Location> listIterator = list.iterator(); listIterator.hasNext();) {
            Location context = listIterator.next();
            sb.append(getSimpleLocationName(context.getLocationName()));
            if (listIterator.hasNext()) {
                sb.append(", ");
            }
        }
        log.warn("Conflict in nested context dependencies was found. Please check the following list of suspect contexts: " + sb);
    }

    private String getSimpleLocationName(String contextName) {
        Matcher matcher = locationNamePattern.matcher(contextName);
        if (matcher.find()) {
            return matcher.group();
        }
        return contextName;
    }

    private boolean thereIsNoImport(LinkedList<Location> list, BeanReferenceInfo importedBean) {
        for (Location location : list) {
            for (BeanReferenceInfo bean : location.getImportBeans()) {
                if (bean.getBeanName().equals(importedBean.getBeanName())) {
                    return false;
                }
            }
        }
        return true;
    }

    Location removeLocation(Collection<Location> list, Predicate p) {
        for (Iterator<Location> iterator = list.iterator(); iterator.hasNext();) {
            Location location = iterator.next();
            if (p.isValid(location)) {
                iterator.remove();
                return location;
            }
        }
        return null;
    }

    Collection<String> collectBeanNames(Collection<BeanReferenceInfo> beanInfoCollection) {
        Set<String> set = new LinkedHashSet<String>();
        for (BeanReferenceInfo beanReferenceInfo : beanInfoCollection) {
            set.add(beanReferenceInfo.getBeanName());
        }
        return set;
    }

}
