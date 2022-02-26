package com.covidTracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.covidTracker.service.CovidTrackerService;

@Controller
public class HomeController {
	@Autowired
	private  CovidTrackerService covidTrackerService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("locationStats", covidTrackerService.getAllStats());
		return "home";
	}
}
