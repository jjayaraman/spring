package com.jai.spring.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyConroller {

	@RequestMapping(value = "/security", method = RequestMethod.GET)
	public String security(Model modelAndView) {
		System.out.println("calling ... /security");
		modelAndView.addAttribute("name", "Jay");
		return "hello";
	}
}
