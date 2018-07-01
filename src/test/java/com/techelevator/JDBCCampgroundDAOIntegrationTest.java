package com.techelevator;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JDBCCampgroundDAOIntegrationTest 
{
	private static final long TEST_ID = 498;

	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO dao;

	@BeforeClass
	public static void setupDataSource()
	{
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void closeDataSource()
	{
		dataSource.destroy();
	}

	@Before
	public void setup()
	{
		String sqlInsertCampground = "insert into campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "
				+ "values (?, 1, 'Camp Jelly Storm Party', '05', '11', 25.00)";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertCampground, TEST_ID);
		dao = new JDBCCampgroundDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException
	{
		dataSource.getConnection().rollback();
	}

	@Test
	public void test_get_all_campgrounds()
	{			
		List<Campground> results = dao.getAllCampgrounds();

		assertNotNull(results);
		assertTrue(results.size() >= 1);
	}

	@Test
	public void test_get_Campground_by_id()
	{
		List<Campground> results = dao.getCampgroundsById(TEST_ID);

		assertNotNull(results);
		assertTrue(results.size() >= 1);
	}
}
