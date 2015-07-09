package com.macys.sstpac.spring.nested.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import com.macys.sstpac.spring.nested.beans.ExportRef;
import com.macys.sstpac.spring.nested.beans.Registry;

public class RegistryBean implements Registry {

	final class ExportBeanTargetSource extends
			AbstractBeanFactoryBasedTargetSource {
		
		private static final long serialVersionUID = -5410088823237543235L;
		private volatile Object target;

		ExportBeanTargetSource(Class<?> clazz, String name) {
			setTargetClass(clazz);
			setTargetBeanName(name);
		}

		public Object getTarget() throws Exception {
			if (target == null) {
				synchronized (this) {
					if (target == null) {
						ExportRef exportRef = exports.get(getTargetBeanName());
						target = resolve(getTargetBeanName(), getTargetClass(),
								exportRef);
					}
				}
			}
			return target;
		}

		public void resetTarget() {
			target = null;
		}
	}

	private static final Logger log = Logger.getLogger(RegistryBean.class);

	private Map<String, ExportRef> exports = new LinkedHashMap<String, ExportRef>();
	private boolean lookupMonitored = false;
	private Map<String, List<ExportBeanTargetSource>> exportedTargetSources = new HashMap<String, List<ExportBeanTargetSource>>();

	public void setLookupMonitored(final boolean lookupMonitored) {
		this.lookupMonitored = lookupMonitored;
	}// setLookupMonitored

	public Void export(ExportRef ref) {
		ExportRef senior = exports.put(ref.getTarget(), ref);
		if (senior != null) {
			// Export ref is replaced, so we must invalidate all exposed target
			// sources for it
			// log.info("Replacing {}", ref);
			List<ExportBeanTargetSource> targetSources = exportedTargetSources
					.get(ref.getTarget());
			for (ExportBeanTargetSource targetSource : targetSources) {
				targetSource.resetTarget();
			}
		}
		return null;
	}

	public <T> T lookup(final String name, final Class<T> clazz) {
		ExportBeanTargetSource targetSource = new ExportBeanTargetSource(clazz,
				name);
		List<ExportBeanTargetSource> targets = exportedTargetSources.get(name);
		if (targets == null) {
			targets = new ArrayList<ExportBeanTargetSource>();
			exportedTargetSources.put(name, targets);
		}
		targets.add(targetSource);
		return ProxyFactory.getProxy(clazz, targetSource);
	}// lookupActual

	public <T> Collection<String> lookupByInterface(Class<T> clazz) {
		Collection<String> rez = new ArrayList<String>();
		for (ExportRef ref : exports.values()) {
			if (clazz.isAssignableFrom(ref.getInterfaceClass())) {
				rez.add(ref.getTarget());
			}
		}
		return rez;
	}

	@SuppressWarnings("unchecked")
	protected <T> T resolve(final String name, final Class<T> clazz,
			ExportRef ref) {
		if (ref == null) {
			throw new NoSuchBeanDefinitionException(name,
					"can't find export declaration for lookup(" + name + ","
							+ clazz + ")");
		}
		if (!clazz.isAssignableFrom(ref.getInterfaceClass())) {
			throw new BeanNotOfRequiredTypeException(name, clazz,
					ref.getInterfaceClass());
		}
		Object bean = ref.getBeanFactory().getBean(name, clazz);
		return (T) bean;
	}

}// RegistryBean
