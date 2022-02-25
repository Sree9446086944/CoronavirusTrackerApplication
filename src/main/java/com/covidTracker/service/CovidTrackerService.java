package com.covidTracker.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class CovidTrackerService {
	
	private static String COVID_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	@PostConstruct 
	public void fetchCovidData() throws IOException, InterruptedException {
		HttpClient client =  HttpClient.newHttpClient();  //creating new client
		HttpRequest request = HttpRequest.newBuilder()      // to use builder pattern
		       .uri(URI.create(COVID_DATA_URL)) // convert string url to URI object pass to uri() method, send along with request
		       .build();  // build the request
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());  // add exceptions since throws exception when client.send fails
		//client.send() - first argument - request itself, second arg - what to do with response, here to string
		System.out.println(httpResponse.body());
		
	}
}
