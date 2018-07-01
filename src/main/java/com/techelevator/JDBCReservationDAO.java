package com.techelevator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public class JDBCReservationDAO implements ReservationDAO {
	private JdbcTemplate jdbcTemplate;
	
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> getAllReservations() {
		
		List<Reservation> reservations = new ArrayList<Reservation>();
		String selectAllReservations = "SELECT * FROM reservation";
		SqlRowSet result = jdbcTemplate.queryForRowSet(selectAllReservations);
		
		while(result.next()) {
			reservations.add(mapRowToReservation(result));
		
	}
		return reservations;
	}
	
	@Override
	public List<SiteWithFee> getSitesByToFromDate(long campgroundId, LocalDate fromDate, LocalDate toDate) {
		List<SiteWithFee> searchById = new ArrayList<>();
		String searchBySiteId = "select site_number, max_occupancy, accessible, max_rv_length, utilities, daily_fee from site join campground on site.campground_id = campground.campground_id where site.campground_id = ? and site_id not in " + 
							    "(select site_id from reservation where (?, ?) overlaps (from_date, to_date) group by site_id) limit 5";
		//adding one day to the departureDate because same start and end date are not considered overlapping in postgres
		SqlRowSet searchResult = jdbcTemplate.queryForRowSet(searchBySiteId, campgroundId, fromDate, toDate.plusDays(1));

		while(searchResult.next()) {
			searchById.add(mapRowToSite(searchResult));
		}
		return searchById;
	}
//	@Override
//	public boolean setReservation(long campgroundId, LocalDate fromDate, LocalDate toDate) {    // method in progress
//		Scanner input = new Scanner(System.in);
//		System.out.println("Which site should be reserved? (enter 0 to cancel)");
//		int userChoice = input.nextInt();
//		input.nextLine();
//		
//		if (userChoice == 0) {
//			input.close();
//			return false;
//		}
//		
//		System.out.println("What name should the reservation be made under?");
//		String userName = input.nextLine();
//		
//		String sqlSiteId = "SELECT site_id FROM site " +
//							"JOIN campground ON campground.campground_id = site.campground_id " +
//							"WHERE site.campground_id = ? AND site_number = ? ";
//		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlSiteId, campgroundId, userChoice);
//		
//		int id = 0;
//		while (result.next()) {
//			id = result.getInt("site_id");
//		}
//		
//		String sql = 	"INSERT INTO reservation (site_id, name, from_date, to_date) " +
//						"VALUES (?, ?, ?, ?); ";
//		jdbcTemplate.update(sql, id, userName, fromDate, toDate);
//		
//		String sqlResId = "SELECT reservation_id FROM reservation " +
//							"WHERE site_id = ? AND name = ? AND from_date = ? ";
//		SqlRowSet resIdResult = jdbcTemplate.queryForRowSet(sqlResId, id, userName, toDate);
//		
//		int reservationId = 0; // hard coded, not sure how to say reservations.length + 1
//		while (resIdResult.next()) {
//			reservationId = resIdResult.getInt("reservation_id");
//		}
//		
//		System.out.println("The reservation has been made and the confirmation id is " + reservationId + "\n\n");
//		input.close();
//		return true;
//	}
	
	@Override
	public Reservation getReservationsById(long resId) 
	{
		Reservation reservations = null;
		String sqlFindReservationName = "select * from reservation where reservation_id = ?";
		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlFindReservationName, resId);
		
		while (result.next())
		{
			reservations = mapRowToReservation(result);
		}
		
		return reservations;
	}
	
	@Override//Added
	public Reservation createReservation(Reservation newReservation) 
	{
		String sqlInsertDepartment = "insert into reservation(site_id, name, to_date, from_date, create_date)" +
				 					 "values (?,?,?,?,?)";
		newReservation.setReservation_id(getNextReservationId());
		jdbcTemplate.update(sqlInsertDepartment, newReservation.getSite_id(),
							 					 newReservation.getName(),
												 newReservation.getTo_date(),
												 newReservation.getFrom_date(),
												 newReservation.getCreate_date());
		return newReservation;
	}
	
	@Override//Added
	public void saveReservation(Reservation savedReservation) 
	{
		String sqlSavedReservation = "update reservation set name = ? where reservation_id = ?";
		jdbcTemplate.update(sqlSavedReservation, savedReservation.getName(), savedReservation.getReservation_id());
	}
	
	private Reservation mapRowToReservation(SqlRowSet result) {
		Reservation reservations = new Reservation();
		reservations.setReservation_id(result.getLong("reservation_id"));
		reservations.setSite_id(result.getLong("site_id"));
		reservations.setName(result.getString("name"));
		reservations.setFrom_date(result.getDate("from_date").toLocalDate());
		reservations.setTo_date(result.getDate("to_date").toLocalDate());
		reservations.setCreate_date(result.getDate("create_date").toLocalDate());
		
		return reservations;
	}
	
	private SiteWithFee mapRowToSite(SqlRowSet result)
	{
		SiteWithFee sites = new SiteWithFee();
		sites.setSiteNumber(result.getLong("site_number"));
		sites.setMaxOccupancy(result.getInt("max_occupancy"));
		sites.setAccessible(result.getBoolean("accessible"));
		sites.setMaxRvLength(result.getInt("max_rv_length"));
		sites.setHasUtilities(result.getBoolean("utilities"));
		sites.setDailyFee(result.getDouble("daily_fee"));
		
		return sites;
	}
	//Added
	public long getNextReservationId()
	{
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("select nextval('reservation_reservation_id_seq')");
		
		if (nextIdResult.next())
		{
			return nextIdResult.getLong(1);
		}
		else
		{
			throw new RuntimeException("An error occurred while getting the id for the reservation.");
		}
	}
}
