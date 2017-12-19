package com.qshare.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qshare.model.User;

@Controller
public class WelcomeController {

	@RequestMapping("hi")
	@ResponseBody
	public String sayHi(@ModelAttribute User user){
		
		return "Hi"+user.getUser_name();
	}
	
	
}
