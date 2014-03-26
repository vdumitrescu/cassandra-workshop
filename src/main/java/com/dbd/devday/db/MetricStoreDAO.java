package com.dbd.devday.db;

import java.util.Date;
import java.util.List;

import com.datastax.driver.core.Session;
import com.dbd.devday.model.Datapoint;


public interface MetricStoreDAO {

	   public void setDataSource(ConnectionManager<Session> ds);
	   public List<Datapoint> findByName(String name, String shipped, Date start, Date end) throws Exception;
	   public void insert(String name, String shipped, Datapoint data) throws Exception;
}
