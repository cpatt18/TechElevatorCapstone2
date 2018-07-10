package com.techelevator;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO 
{
	public List<Reservation> getAllReservations();
	public Reservation getReservationsById(long resId);
	public List<SiteWithFee> getSitesByToFromDate(long campgroundId, LocalDate fromDate, LocalDate toDate);
	public Reservation createReservation(Reservation newReservation);
	public void saveReservation(Reservation savedReservation);

}
