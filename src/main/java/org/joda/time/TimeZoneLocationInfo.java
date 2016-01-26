package org.joda.time;

import java.util.HashMap;

public class TimeZoneLocationInfo {
    public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "LocationInfo [latitude=" + latitude + ", longitude="
				+ longitude + ", comments=" + comments + ", countries="
				+ countries + "]";
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public HashMap<String, String> getCountries() {
		return countries;
	}

	public void setCountries(HashMap<String, String> countries) {
		this.countries = countries;
	}

	private String latitude;
    private String longitude;
    private String comments;
    private HashMap<String, String> countries;

    public TimeZoneLocationInfo(String lat, String lon, String com, HashMap<String, String> countries) {
        latitude = lat;
        longitude = lon;
        comments = com;
        this.countries = countries;
    }
    
    public TimeZoneLocationInfo(String lat, String lon, String com, String countries) {
    	countries = countries.substring(1, countries.length()-1);
        HashMap<String, String> countriesMap = new HashMap<String, String>();
        String[] pairs = countries.split(",");
        
        for(int index=0; index<pairs.length; index++) {
        	String pair = pairs[index];
        	String[] values = pair.split("=");
        	if(values.length==2) {
        		countriesMap.put(values[0].trim(), values[1].trim());
        	}
        }
        latitude = lat;
        longitude = lon;
        comments = com;
        this.countries = countriesMap;
    }
}