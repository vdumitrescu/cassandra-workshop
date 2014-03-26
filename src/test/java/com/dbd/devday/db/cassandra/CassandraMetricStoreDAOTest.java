package com.dbd.devday.db.cassandra;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.db.MetricStoreDAO;
import com.dbd.devday.model.Datapoint;

/**
 * Sample integration test - ideally cassandra would be run as part of gradle task
 */
public class CassandraMetricStoreDAOTest {

	final String testName = "orders";
	final String testShipped = "US";
	final Date testTimestamp = new Date();
	final Double testValue = 5.0;
	
	
	ConnectionManager<Session> connectionManager = null;
	MetricStoreDAO dao = null;
	
	@Before
	public void setup() {
		connectionManager = new CassandraConnectionManager(new String[]{"127.0.0.1"}, 9042);
		dao = new CassandraMetricStoreDAO(connectionManager);
	}
	
	@Test
	public void testInsert() throws Exception {
		
		dao.insert(testName, testShipped, new Datapoint(testTimestamp, testValue));
	
		List<Datapoint> points = dao.findByName(testName, testShipped, testTimestamp, testTimestamp);
			
		if (points == null)
			fail("Found no data points");
		
		assertEquals("Expected to find a single data point", 1, points.size());
	}	
}
