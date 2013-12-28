package com.jai.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyConroller {

	@RequestMapping(value = "/security", method = RequestMethod.GET)
	public String security(Model model) {
		System.out.println("calling ... /security");
		model.addAttribute("name", "Jay");
		return "pages/hello";
	}


	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(Model model) {
		System.out.println("calling ... /profile");
		return "user/profile";
	}

}
