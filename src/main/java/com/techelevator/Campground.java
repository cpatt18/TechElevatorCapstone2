package com.techelevator;

import java.time.Month;

public class Campground {
	
	private long campground_id;
	private long park_id;
	private String name;
	private String open_from;
	private String open_to;
	private double daily_fee;
	
	public long getCampground_id() {
		return campground_id;
	}
	public void setCampground_id(long campground_id) {
		this.campground_id = campground_id;
	}
	public long getPark_id() {
		return park_id;
	}
	public void setPark_id(long park_id) {
		this.park_id = park_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOpen_from() {
		return open_from;
	}
	public void setOpen_from(String open_from) {
		this.open_from = open_from;
	}
	public String getOpen_to() {
		return open_to;
	}
	public void setOpen_to(String open_to) {
		this.open_to = open_to;
	}
	
	public double getDaily_fee() {
		return daily_fee;
	}
	public void setDaily_fee(double daily_fee) {
		this.daily_fee = daily_fee;
	}
	public String toString() {//Changed format to display the month name
		String from_month = Month.of(Integer.parseInt(open_from)).toString();
		String to_month = Month.of(Integer.parseInt(open_to)).toString();
		return String.format("%-5s%-32s%-10s%-12s$%.2f", "#" + campground_id, name, from_month, to_month, daily_fee);
	}
}
