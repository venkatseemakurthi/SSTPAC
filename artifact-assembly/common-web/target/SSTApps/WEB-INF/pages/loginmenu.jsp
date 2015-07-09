<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="com.macys.sstpac.menuloading.bean.MenuLoadingVO,com.macys.sstpac.menuloading.treeStructure.Node,
                 com.macys.sstpac.menuloading.treeStructure.MenuTree"%>
<!DOCTYPE html>
<html lang="en" class="no-js">
<head>
<title>SSTPAC</title>
<link rel="shortcut icon" href="../favicon.ico">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/default.css" />
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/resources/css/menu-component.css" />
<script
	src="<%=request.getContextPath()%>/resources/js/modernizr.custom.js"></script>
</head>
<body>
	<div id="hd">
		<header class="container">
			<nav id="topnav">
				<ul>
					<li><a id="globalMastheadUserSalutation">Welcome, Test</a></li>
					<li><a href="/signout" target="_top" id="globalMastheadSignIn">(sign
							out)</a></li>
				</ul>
			</nav>
			<table>
				<tr>
					<td><a href="#"><div title="Macy's" alt="Macy's"
								class="globalMastheadLogo" id="macysHomePageLogo"></div> </a></td>
					<td>Site Support Team Production Application Checker</td>
			</table>
		</header>
	</div>
	<div class="main">
		<nav id="cbp-hrmenu" class="cbp-hrmenu">
			<ul>
				<%
					Node<MenuLoadingVO> rootNode = ((MenuTree)session.getAttribute("MenuTree")).getRootElement();
								for (Node<MenuLoadingVO> menuNodes : rootNode.getChildren()) {
				%>
				<li><a href="#"><%=menuNodes.data.getMenuName()%></a>
					<div class="cbp-hrsub">
						<div class="cbp-hrsub-inner">
							<%
								for (Node<MenuLoadingVO> childNodes : menuNodes.getChildren()) {
							%>
							<div>
								<h4><%=childNodes.data.getMenuName()%></h4>
								<ul>
								   <%
										for (Node<MenuLoadingVO> subChildNodes : childNodes.getChildren()) {
									%>
									<li><a href="#"><%=subChildNodes.data.getMenuName()%></a></li>
									<%
										}
									%>
								</ul>
							</div>
							<%
								}
							%>
						</div>
					</div> <%
 	}
 %>
		</nav>
	</div>
	</div>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
	<script
		src="<%=request.getContextPath()%>/resources/js/cbpHorizontalMenu.min.js"></script>
	<script>
		$(function() {
			alert();
			cbpHorizontalMenu.init();
		});
	</script>
</body>
</html>
