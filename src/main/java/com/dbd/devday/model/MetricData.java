package com.dbd.devday.model;

import java.util.Set;

public class MetricData {
	private String metricName;
	private String shipped;
	private Set<Datapoint> datapoints;
	public String getMetricName() {
		return metricName;
	}
	public void setMetricName(String metricName) {
		this.metricName = metricName;
	}
	public String getShipped() {
		return shipped;
	}
	public void setShipped(String shipped) {
		this.shipped = shipped;
	}
	public Set<Datapoint> getDatapoints() {
		return datapoints;
	}
	public void setDatapoints(Set<Datapoint> datapoints) {
		this.datapoints = datapoints;
	}
}
