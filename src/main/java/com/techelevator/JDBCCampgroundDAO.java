package com.techelevator;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCCampgroundDAO implements CampgroundDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgrounds() {
		
		List<Campground> campgrounds = new ArrayList<Campground>();
		String selectAllCampgrounds = "SELECT * FROM campground";
		SqlRowSet result = jdbcTemplate.queryForRowSet(selectAllCampgrounds);
		
		while(result.next()) {
			campgrounds.add(mapRowToCampground(result));
		
	}
		return campgrounds;
	}
	
	@Override
	public List<Campground> getCampgroundsById(long id) {
		
		List<Campground> searchById = new ArrayList<Campground>();
		String searchByCampgroundId = "SELECT * FROM campground WHERE park_id = ?";
		SqlRowSet searchResult = jdbcTemplate.queryForRowSet(searchByCampgroundId,  id);

		while(searchResult.next()) {
			searchById.add(mapRowToCampground(searchResult));
		
	}
		return searchById;
	}
	
	private Campground mapRowToCampground(SqlRowSet result) {
		Campground campgrounds = new Campground();
		campgrounds.setCampground_id(result.getLong("campground_id"));
		campgrounds.setPark_id(result.getLong("park_id"));
		campgrounds.setName(result.getString("name"));
		campgrounds.setOpen_from(result.getString("open_from_mm"));
		campgrounds.setOpen_to(result.getString("open_to_mm"));
		campgrounds.setDaily_fee(result.getDouble("daily_fee"));
		
		return campgrounds;
	}
}
