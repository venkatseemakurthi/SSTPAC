package com.macys.sstpac.common.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.macys.sstpac.cas.constants.AuthStatus;
import com.macys.sstpac.cas.service.AuthenticationService;
import com.macys.sstpac.menuloading.dao.MenuLoadingDAO;
import com.macys.sstpac.menuloading.treeStructure.MenuTree;

@Component
@Controller
public class LoginController {

	@Autowired
	private AuthenticationService authService;

	@Autowired
	private MenuLoadingDAO menuLoadingDAO;

	public static final Logger log = Logger.getLogger(LoginController.class);

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public ModelAndView showLoginForm() {
		return new ModelAndView("index", "command",
				new UsernamePasswordCredentials());
	}

	@RequestMapping(value = "/loginForm", method = RequestMethod.POST)
	public ModelAndView loginForm(
			@ModelAttribute("SpringWeb") UsernamePasswordCredentials credentials,
			HttpServletRequest request, HttpServletResponse response) {
		AuthStatus loginStatus = null;
		ModelAndView modelAndView = null;
		HttpSession session = request.getSession();
		MenuTree menuTree = null;
		try {
			System.out.println("UserName"+credentials.getUsername());
			System.out.println("Password"+credentials.getPassword());
			loginStatus = authService.login(credentials);
			if (AuthStatus.SUCESSS.equals(loginStatus)) {
				System.out.println("User successfully logged  with userId:"
						+ credentials.getUsername());
				session.setAttribute("userName", credentials.getUsername()
						.trim());
				menuTree = menuLoadingDAO.get(0);
				session.setAttribute("MenuTree", menuTree);
				modelAndView = new ModelAndView("loginmenu");
			} else {
				request.setAttribute("Errorcode",
						"Please login with correct credentials");
				System.out.println("User logging attempt with userId:"
						+ credentials.getUsername() + " got failed");
				modelAndView = new ModelAndView("index", "command",
						new UsernamePasswordCredentials());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelAndView;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView logoutForm(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		log.info("User successfully logged out  with userId:"
				+ session.getAttribute("userName"));
		session.removeAttribute("userName");
		return new ModelAndView("index", "command",
				new UsernamePasswordCredentials());
	}

	@RequestMapping(value = "/logOut", method = RequestMethod.GET)
	public ModelAndView logOutForm(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		log.info("User successfully logged out  with userId:"
				+ session.getAttribute("userName"));
		session.removeAttribute("userName");
		return new ModelAndView("index", "command",
				new UsernamePasswordCredentials());
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView showIndexPage(HttpServletRequest request) {
		ModelAndView modelAndView = null;
		HttpSession session = request.getSession();
		if (session.getAttribute("userName") != null)
			modelAndView = new ModelAndView("loginmenu");
		else {
			modelAndView = new ModelAndView("index", "command",
					new UsernamePasswordCredentials());
		}
		return modelAndView;
	}

}
