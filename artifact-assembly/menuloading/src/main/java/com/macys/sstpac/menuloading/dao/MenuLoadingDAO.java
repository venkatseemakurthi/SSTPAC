package com.macys.sstpac.menuloading.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.macys.sstpac.databaseConfigure.DBConfiguration;
import com.macys.sstpac.menuloading.bean.MenuLoadingVO;
import com.macys.sstpac.menuloading.treeStructure.MenuTree;
import com.macys.sstpac.menuloading.treeStructure.Node;

@Component("menuLoadingDAO")
public class MenuLoadingDAO {
	
	@Autowired
	private DBConfiguration dbConfiguration;

	public List<MenuLoadingVO> getMenus(int parentMenuId,int roleId) throws Exception {
		return dbConfiguration.getJdbcTemplate("MySql").query(
				(String) dbConfiguration.getMySqlQuery("SELECT_MENU"), new Object[]{parentMenuId},
				new RowMapper<MenuLoadingVO>() {
					@Override
					public MenuLoadingVO mapRow(ResultSet rs, int rownumber)
							throws SQLException {
						MenuLoadingVO menuLoadingVO = new MenuLoadingVO();
						menuLoadingVO.setId(rs.getInt("ID"));
						menuLoadingVO.setMenuName(rs.getString("MENU_NAME"));
						menuLoadingVO.setMenuId(rs.getInt("MENU_ID"));
						menuLoadingVO.setParentMenuId(rs.getInt("PARENT_MENU_ID"));
						return menuLoadingVO;
					}
				});
	}

	public MenuTree get(int roleId) throws Exception{
		MenuTree menuTree = new MenuTree();
		Node<MenuLoadingVO> rootElement = new Node<MenuLoadingVO>(getMenus(
				0,roleId).get(0));
		getRecursiveTree(rootElement, menuTree,roleId);
		menuTree.setRootElement(rootElement);
		return menuTree;
	}
	

	private void getRecursiveTree(Node<MenuLoadingVO> menuElement,
			MenuTree menuTree,int roleId) throws Exception{
		Node<MenuLoadingVO> childMenuElement = null;
		MenuLoadingVO childMenu = null;
		List<MenuLoadingVO> children = getMenus(menuElement.getData()
				.getMenuId(),roleId);
		List<Node<MenuLoadingVO>> childMenuElements = new ArrayList<Node<MenuLoadingVO>>();
		for (Iterator<MenuLoadingVO> it = children.iterator(); it.hasNext();) {
			childMenu = it.next();
			childMenuElement = new Node<MenuLoadingVO>(childMenu);
			childMenuElements.add(childMenuElement);
			getRecursiveTree(childMenuElement, menuTree,roleId);
		}
		menuElement.setChildren(childMenuElements);
	}

}
