package com.techelevator;

public class SiteWithFee 
{
	private long siteId;
	private long campgroundId;
	private long siteNumber;
	private int maxOccupancy;
	private boolean isAccessible;
	private int maxRvLength;
	private boolean hasUtilities;
	private double dailyFee;

	public long getSiteId() 
	{
		return siteId;
	}

	public void setSiteId(long siteId) 
	{
		this.siteId = siteId;
	}

	public long getCampgroundId() 
	{
		return campgroundId;
	}

	public void setCampgroundId(long campgroundId) 
	{
		this.campgroundId = campgroundId;
	}

	public long getSiteNumber() 
	{
		return siteNumber;
	}

	public void setSiteNumber(long siteNumber) 
	{
		this.siteNumber = siteNumber;
	}

	public int getMaxOccupancy() 
	{
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) 
	{
		this.maxOccupancy = maxOccupancy;
	}

	public boolean isAccessible() 
	{
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) 
	{
		this.isAccessible = isAccessible;
	}

	public int getMaxRvLength() 
	{
		return maxRvLength;
	}

	public void setMaxRvLength(int maxRvLength) 
	{
		this.maxRvLength = maxRvLength;
	}

	public boolean hasUtilities() 
	{
		return hasUtilities;
	}

	public void setHasUtilities(boolean hasUtilities) 
	{
		this.hasUtilities = hasUtilities;
	}

	public double getDailyFee() 
	{
		return dailyFee;
	}

	public void setDailyFee(double dailyFee) 
	{
		this.dailyFee = dailyFee;
	}

	@Override
	public String toString() //Changed format
	{
		String accessible = isAccessible ? "Yes" : "No";
		String utilities = hasUtilities ? "Yes" : "N/A";
		return String.format("%-15d %-11d %-18s %-11d %-10s $", siteNumber, maxOccupancy, accessible, maxRvLength, utilities);
	}
}
