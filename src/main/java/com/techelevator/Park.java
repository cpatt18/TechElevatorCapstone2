package com.techelevator;

import java.sql.Date;

public class Park {

	private long park_id;
	private String name;
	private String location;
	private Date establish_date;
	private int area;
	private long visitors;
	private String description;


	public long getPark_id() {
		return park_id;
	}
	public String getName() {
		return name;
	}
	public String getLocation() {
		return location;
	}
	public Date getEstablish_date() {
		return establish_date;
	}
	public int getArea() {
		return area;
	}
	public long getVisitors() {
		return visitors;
	}
	public String getDescription() {
		return description;
	}
	public void setPark_id(long park_id) {
		this.park_id = park_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public void setEstablish_date(Date establish_date) {
		this.establish_date = establish_date;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public void setVisitors(long visitors) {
		this.visitors = visitors;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String toString() {
		return "Location:        " + this.location + "\n" + "Established:     " + establish_date + "\n" + "Area:            " + area + " sq km\n" + "Annual Visitors: "  + visitors + "\n";
	}
}
