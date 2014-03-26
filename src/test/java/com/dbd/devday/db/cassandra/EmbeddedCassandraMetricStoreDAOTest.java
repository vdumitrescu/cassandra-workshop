package com.dbd.devday.db.cassandra;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.db.MetricStoreDAO;
import com.dbd.devday.model.Datapoint;

/**
 * 
 * In the most recent version of the driver the following issue causes some nasty stack trace output
 * 
 * See : https://issues.apache.org/jira/browse/CASSANDRA-6639
 * 
 */
public class EmbeddedCassandraMetricStoreDAOTest {

	final String testKeyspace = "metrics_embedded";
	final String testName = "orders";
	final String testShipped = "US";
	final Date testTimestamp = new Date();
	final Double testValue = 5.0;

	ConnectionManager<Session> connectionManager = null;
	MetricStoreDAO dao = null;

    @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("create.cql", testKeyspace));

	@Before
	public void setup() {
		if(connectionManager == null) {
			connectionManager = new CassandraConnectionManager(
					new String[] { "127.0.0.1" }, 9142);
			dao = new CassandraMetricStoreDAO(connectionManager, testKeyspace);
		}
	}

	@Test
	public void testCreate() throws Exception {

		dao.insert(testName, testShipped, new Datapoint(testTimestamp, testValue));
		
		List<Datapoint> points = dao.findByName(testName, testShipped, testTimestamp, testTimestamp);
			
		if (points == null)
			fail("Found no data points");
		
		assertEquals("Expected to find a single data point", 1, points.size());
	}
}
