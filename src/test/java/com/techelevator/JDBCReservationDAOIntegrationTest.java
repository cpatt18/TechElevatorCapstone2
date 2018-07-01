package com.techelevator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JDBCReservationDAOIntegrationTest 
{
	private static final long TEST_ID = 3720;

	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO dao;

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
		String sqlInsertReservation = "insert into reservation (reservation_id, site_id, name, from_date, to_date, create_date) "
									+ "values (?, 1, 'Henry Family Reservation', '2018-07-18', '2018-07-26', now())";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertReservation, TEST_ID);
		dao = new JDBCReservationDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException
	{
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void test_get_all_reservations()
	{			
		List<Reservation> results = dao.getAllReservations();

		assertNotNull(results);
		assertTrue(results.size() >= 1);
	}
	
	@Test
	public void test_get_Reservation_by_id()//Changed Test
	{
		Reservation results = dao.getReservationsById(TEST_ID);

		assertNotNull(results);
		assertTrue(results.getReservation_id() == 3720);
	}
	
	@Test
	public void get_sites_by_to_from_date()//Changed the test
	{
		List<SiteWithFee> results = dao.getSitesByToFromDate(1, LocalDate.of(2018, 07, 01), LocalDate.of(2018, 07, 26));
		
		assertNotNull(results);
		assertTrue(results.size() >= 1);
	}
	
	@Test
	public void test_create_reservation()
	{
		Reservation newReservation = getReservation(TEST_ID, 1, "Henry Family Reservation", LocalDate.of(2018, 07, 18), LocalDate.of(2018, 07, 26), LocalDate.now());
		
		dao.saveReservation(newReservation);
		Reservation savedReservation = dao.getReservationsById(newReservation.getReservation_id());
		
		assertNotEquals(null, newReservation.getReservation_id());
		assertReservationsAreEqual(newReservation, savedReservation);
	}
	
	private Reservation getReservation(long id, long siteId, String name, LocalDate fromDate, LocalDate toDate, LocalDate createDate) 
	{
		Reservation reservations = new Reservation();
		reservations.setReservation_id(id);
		reservations.setSite_id(siteId);
		reservations.setName(name);
		reservations.setFrom_date(fromDate);
		reservations.setTo_date(toDate);
		reservations.setCreate_date(createDate);
	
		return reservations;
	}
	
	private void assertReservationsAreEqual(Reservation expected, Reservation actual) 
	{
		assertEquals(expected.getReservation_id(), actual.getReservation_id());
		assertEquals(expected.getSite_id(), actual.getSite_id());
		assertEquals(expected.getName(), actual.getName());
		assertEquals(expected.getFrom_date(), actual.getFrom_date());
		assertEquals(expected.getTo_date(), actual.getTo_date());
		assertEquals(expected.getCreate_date(), actual.getCreate_date());
	}
}
