package com.covidTracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CovidTrackerService {
	
	private static String COVID_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	@PostConstruct
	@Scheduled(cron="* * 1 * * *")  // run every second
	public void fetchCovidData() throws IOException, InterruptedException {
		HttpClient client =  HttpClient.newHttpClient();  //creating new client
		HttpRequest request = HttpRequest.newBuilder()      // to use builder pattern
		       .uri(URI.create(COVID_DATA_URL)) // convert string url to URI object pass to uri() method, send along with request
		       .build();  // build the request
		HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());  // add exceptions since throws exception when client.send fails
		//client.send() - first argument - request itself, second arg - what to do with response, here to string
//		System.out.println(httpResponse.body());
		StringReader csvBodyReader = new StringReader(httpResponse.body()); // we have reader from string instance
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader); //pass reader and parse to csv
		for (CSVRecord record : records) {
		    String state = record.get("Province/State");
		    System.out.println(state);
		}
		     
		
	}
}
