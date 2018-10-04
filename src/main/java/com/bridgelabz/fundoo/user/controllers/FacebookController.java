package com.bridgelabz.fundoo.user.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.user.services.FacebookService;

@RefreshScope
@RestController
public class FacebookController {

	@Autowired
	private FacebookService facebookService;

	@GetMapping("/createFacebookAuthorization")
	public String createFacebookAuthorization() {
		return facebookService.createFacebookAuthorizationURL();
	}

	@GetMapping("/getName")
	public String getNameResponse() {
		return facebookService.getName();
	}

	@GetMapping("/facebook")
	public void createFacebookAccessToken(@RequestParam("code") String code) {
		facebookService.createFacebookAccessToken(code);
	}

}
