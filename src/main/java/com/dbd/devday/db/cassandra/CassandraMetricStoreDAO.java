package com.dbd.devday.db.cassandra;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.dbd.devday.db.ConnectionManager;
import com.dbd.devday.db.MetricStoreDAO;
import com.dbd.devday.model.Datapoint;

public class CassandraMetricStoreDAO implements MetricStoreDAO {

	public static final String DEFAULT_KEYSPACE_NAME = "metrics";
	
	private ConnectionManager<Session> dataSource = null;
	private Session session = null;
	private String keyspaceName = null;
	
	private PreparedStatement findByMetricStatement;
	private PreparedStatement insertStatement;
	
	
	public CassandraMetricStoreDAO(ConnectionManager<Session> dataSource) {
		this.dataSource = dataSource;
		prepareStatements();
	}
	
	public CassandraMetricStoreDAO(ConnectionManager<Session> ds, String keyspaceName) {
		this.dataSource = ds;
		this.keyspaceName = keyspaceName;
		prepareStatements();
	}
	
	private void prepareStatements() {
		Session session = getSession();
		findByMetricStatement = session.prepare("SELECT ts, value FROM metrics WHERE name = ? AND shipped = ? AND ts >= ? AND ts <= ?");
		insertStatement = session.prepare("INSERT INTO metrics (name, shipped, ts, value) VALUES (?,?,?,?)");
	}
	
	private Session getSession() {
		if (session == null) {
			if(keyspaceName == null) {
				session = dataSource.getSession(DEFAULT_KEYSPACE_NAME);
			}
			else { 
				session = dataSource.getSession(keyspaceName);
			}
		}
		return session;
	}
	
	@Override
	public List<Datapoint> findByName(String name, String shipped, Date start, Date end) throws Exception {
		BoundStatement boundStatement = new BoundStatement(findByMetricStatement);
		boundStatement.bind(name, shipped, start, end);
		
		List<Datapoint> results = new ArrayList<Datapoint>();
		ResultSet resultSet = getSession().execute(boundStatement);
		for(Row row : resultSet.all()) {
			results.add(new Datapoint(row.getDate(0), row.getDouble(1)));
		}

		return results;
	}

	@Override
	public void insert(String name, String shipped, Datapoint data) throws Exception {
		BoundStatement boundStatement = new BoundStatement(insertStatement);
		boundStatement.bind(name, shipped, data.getTimestamp(), data.getValue());
		
		getSession().execute(boundStatement);
	}

	@Autowired
	public void setDataSource(ConnectionManager<Session> ds) {
		this.dataSource = ds;
	}
}
