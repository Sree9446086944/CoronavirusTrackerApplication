package com.covidTracker.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.covidTracker.models.LocationStats;

@Service
public class CovidTrackerService {
	
	private static String COVID_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<LocationStats> allStats = new ArrayList<>();
	
	
	public List<LocationStats> getAllStats() {
		return allStats;
	}

	@PostConstruct
	@Scheduled(cron="* * 1 * * *")  // run every second
	public void fetchCovidData() throws IOException, InterruptedException {
		List<LocationStats> newStats = new ArrayList<>(); //creating instance of LocationStats
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
			LocationStats locationStat = new LocationStats();
			locationStat.setState(record.get("Province/State"));
			locationStat.setCountry(record.get("Country/Region"));
//			locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));  // since no specificcolumn headername , column adds up every day with header as latest date, so fetch last column
//		    System.out.println(locationStat);
			int latestCases = Integer.parseInt(record.get(record.size()-1));
			//second last column is prev day cases
			int prevDayCases = Integer.parseInt(record.get(record.size()-2)); 
			locationStat.setLatestTotalCases(latestCases);
			locationStat.setDiffFromPrevDay(latestCases-prevDayCases);
		    newStats.add(locationStat);
		}
		this.allStats = newStats;
		
	}
}
