package com.dbd.devday.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dbd.devday.db.MetricStoreDAO;
import com.dbd.devday.model.Datapoint;

@Controller
@RequestMapping("/metrics")
public class MetricController {

	private final MetricStoreDAO metricDAO;
	
	@Autowired
	public MetricController(MetricStoreDAO metricDAO) {
		this.metricDAO = metricDAO;
	}
	
	@RequestMapping(value = "{name}/{shipped}", method = RequestMethod.GET)
	@ResponseBody
	public List<Datapoint> list(@PathVariable String name, @PathVariable String shipped,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date start,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) throws Exception {
		
		System.out.println("Start: " + start);
		System.out.println("End: " + end);
		return this.metricDAO.findByName(name, shipped, start, end);
	}
	
	@RequestMapping(value = "{name}/{shipped}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.ACCEPTED)
	public void save(@PathVariable String name, @PathVariable String shipped,
			@RequestBody Datapoint data) throws Exception {
		this.metricDAO.insert(name, shipped, data);
	}
	
}
