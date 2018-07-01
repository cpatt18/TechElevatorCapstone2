package com.techelevator;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public class JDBCParkDAO implements ParkDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Park> getAllParks() {
		
		List<Park> parks = new ArrayList<Park>();
		String selectAllParks = "SELECT * FROM park order by name";
		SqlRowSet result = jdbcTemplate.queryForRowSet(selectAllParks);
		
		while(result.next()) {
			parks.add(mapRowToPark(result));
		
	}
		return parks;
	}
	
	private Park mapRowToPark(SqlRowSet result) {
		Park parks = new Park();
		parks.setPark_id(result.getLong("park_id"));
		parks.setName(result.getString("name"));
		parks.setLocation(result.getString("location"));
		parks.setEstablish_date(result.getDate("establish_date"));
		parks.setArea(result.getInt("area"));
		parks.setVisitors(result.getLong("visitors"));
		parks.setDescription(result.getString("description"));
		return parks;
	}
	
}
