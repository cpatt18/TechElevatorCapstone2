package com.techelevator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.Park;

public class CampgroundCLI {
	private static final String MAIN_MENU_OPTION_ACADIA = "Acadia National Park";
	private static final String MAIN_MENU_OPTION_ARCHES = "Arches National Park";
	private static final String MAIN_MENU_OPTION_CUYAHOGA_VALLEY = "Cuyahoga Valley National Park";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_ACADIA, 
			MAIN_MENU_OPTION_ARCHES, 
			MAIN_MENU_OPTION_CUYAHOGA_VALLEY, 
			MAIN_MENU_OPTION_EXIT };
	private static final String SUB_MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";

	private static final String SUB_MENU_OPTION_VIEW_CAMPGROUNDS = "View Campgrounds";
	private static final String SUB_MENU_OPTION_SEARCH_RESERVATIONS = "Search for Reservation";
	private static final String[] SUB_MENU_OPTIONS = new String[] { SUB_MENU_OPTION_VIEW_CAMPGROUNDS,
			SUB_MENU_OPTION_SEARCH_RESERVATIONS,
			SUB_MENU_OPTION_RETURN_TO_MAIN};	

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
	}

	public void run() {
		while(true) {
			printHeading("Select a Park for further details....");
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(choice.equals(MAIN_MENU_OPTION_ACADIA)) {
				handlePark(0);
			} 
			else if(choice.equals(MAIN_MENU_OPTION_ARCHES)) {
				handlePark(1);
			} else if(choice.equals(MAIN_MENU_OPTION_CUYAHOGA_VALLEY)) {
				handlePark(2);
			} 
			else if(choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(0);
			}
		}
	}

	private void handlePark(int parkChoice) {
		Park park = parkDAO.getAllParks().get(parkChoice);
		printHeading(park.getName());
		System.out.println(park.toString());
		String choice = (String)menu.getChoiceFromOptions(SUB_MENU_OPTIONS);

		if(choice.equals(SUB_MENU_OPTION_VIEW_CAMPGROUNDS)) {
			handleViewCampgrounds(park);
		}
		else if(choice.equals(SUB_MENU_OPTION_SEARCH_RESERVATIONS)) {
			printHeading("Search for a Campground Reservation");
			handleViewCampgrounds(park);
			String userCampChoice = getUserInput("\nWhich campground (enter 0 to cancel)? ");
			long result = Long.parseLong(userCampChoice);
			if (result == 0)
			{
				return;
			}

			LocalDate arrivalDate = getDateInput("What is the arrival date? (MM/DD/YYYY) ");
			LocalDate departureDate = getDateInput("What is the departure date? (MM/DD/YYYY) ");
			long diff = ChronoUnit.DAYS.between(arrivalDate, departureDate);
			if (diff < 1)
			{
				System.out.println("Please select another time range");
			}
			else
			{
				printHeading("Results Matching Your Search Criteria");
				printHeading("Site No.    Max Occup.    Accessible?    Max RV Length    Utility?    Cost");
				List<SiteWithFee> sites = reservationDAO.getSitesByToFromDate(result, arrivalDate, departureDate);
				if (sites.size() == 0)
				{
					System.out.println("Unvaliable sites for that date range. Please try again.");
					return; // might not need this
				}
				else
				{
					for (SiteWithFee st : sites)
					{
						System.out.println(st.toString() + diff * st.getDailyFee());
					}

					userCampChoice = getUserInput("Which site should be reserved (enter 0 to cancel)? ");//Added this and the below if statement to return to main menu if the user chooses 0
					if (result == 0)
					{
						return;
					}
					//Added this
					handleAddReservation(result, arrivalDate, departureDate, LocalDate.now());

					//	try {
					//	reservationDAO.setReservation(result, arrivalDate, departureDate);
					//	return;
					//	} catch (Exception e) {
					//	e.printStackTrace();
					//	}

					//Not Sure what this is for??
					//	reservationDAO.setReservation(result, arrivalDate, departureDate);
					//	return;
				}
			}
		} 
		else if(choice.equals(SUB_MENU_OPTION_RETURN_TO_MAIN)) {
			return;
		}
	}
	//  Made this method to create and save a method in the table
	private void handleAddReservation(long site_id, LocalDate fromDate, LocalDate toDate, LocalDate createDate ) {
		String newReservationName = getUserInput("What name should the reservation be made under? ");
		Reservation newReservation = new Reservation();
		newReservation.setSite_id(site_id);
		newReservation.setName(newReservationName);
		newReservation.setFrom_date(fromDate);
		newReservation.setTo_date(toDate);
		newReservation.setCreate_date(createDate);
		newReservation = reservationDAO.createReservation(newReservation);
		System.out.println("The reservation has been made and the confirmation id is " + newReservation.getReservation_id());
	}

	private void handleViewCampgrounds(Park park) {
		printHeading(park.getName() + " Campgrounds");
		System.out.println(String.format("%-5s%-32s%-10s%-12s%s", " ", "Name", "Open", "Close", "Daily Fee"));
		List<Campground> campgrounds = campgroundDAO.getCampgroundsById(park.getPark_id());
		for (Campground camp : campgrounds)
		{
			System.out.println(camp);
		}
	}

	private String getUserInput(String prompt) { //Changed to make sure user doesn't leave prompt blank
		String userInput = "";
		Scanner kb = new Scanner(System.in);
		while (userInput.isEmpty())
		{
			System.out.print(prompt + " >>> ");
			userInput = kb.nextLine();
		}
		return userInput;
	}

	private LocalDate getDateInput(String prompt)//Made this to convert the month number given from the table to the month name in the menu
	{
		LocalDate userDate = null;
		DateTimeFormatter inputFormat = DateTimeFormatter.ofPattern("M/d/yyyy");

		while (userDate == null)
		{
			try
			{
				String dateChoice = getUserInput(prompt);
				userDate = LocalDate.parse(dateChoice, inputFormat);
			}
			catch (DateTimeParseException e)
			{
				System.out.println("Invalid date format.");
			}
		}

		return userDate;
	}

	private void printHeading(String headingText) {
		System.out.println("\n"+headingText);
		for(int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}
}
