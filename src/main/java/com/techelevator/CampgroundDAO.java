package com.techelevator;

import java.util.List;

public interface CampgroundDAO {
	public List<Campground> getAllCampgrounds();
	public List<Campground> getCampgroundsById(long id);
}
