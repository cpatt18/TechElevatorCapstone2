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

public class JDBCParkDAOIntegrationTest 
{
	private static final long TEST_ID = 391;

	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;

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
		String sqlInsertPark = "insert into park (park_id, name, location, establish_date, area, visitors, description) "
							 + "values (?, 'Parkless', 'Delaware', '1901-03-12', 71326, 38103762, 'Nothing there')";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark, TEST_ID);
		dao = new JDBCParkDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException
	{
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void test_get_all_parks()
	{
		List<Park> results = dao.getAllParks();

		assertNotNull(results);
		assertTrue(results.size() >= 1);
	}
}
