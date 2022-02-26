package com.covidTracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.covidTracker.models.LocationStats;
import com.covidTracker.service.CovidTrackerService;

@Controller
public class HomeController {
	@Autowired
	private  CovidTrackerService covidTrackerService;

	@GetMapping("/")
	public String home(Model model) {
		List<LocationStats> allStats =  covidTrackerService.getAllStats();
		//sum of all the cases in globe
		int totalReportedCases = allStats.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();
		model.addAttribute("locationStats",allStats);
		model.addAttribute("totalReportedCases",totalReportedCases);
		return "home";
	}
}
