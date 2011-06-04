package org.rhok.foodmover.entities;

import com.google.appengine.api.datastore.Entity;

public class FoodListing extends BaseEntity {

	private static final String LAT_KEY = "lat";
	private static final String LONGITUDE_KEY = "longitude";
	private static final String QUANTITY_KEY = "quantity";
	private static final String DESCRIPTION_KEY = "descr";

	public FoodListing() {
		entity = new Entity("FoodListing");
	}

	public void setLat(float lat) {
		entity.setProperty(LAT_KEY, lat);
	}

	public void setLongitude(float longitude) {
		entity.setProperty(LONGITUDE_KEY, longitude);
	}

	public void setQuantity(int quantity) {
		entity.setProperty(QUANTITY_KEY, quantity);
	}

	public void setDescription(String description) {
		entity.setProperty(DESCRIPTION_KEY, description);
	}

	public float getLat() {
		return (Float) entity.getProperty(LAT_KEY);
	}

	public float getLongitude() {
		return (Float) entity.getProperty(LONGITUDE_KEY);
	}

	public int getQuantity() {
		return (Integer) entity.getProperty(QUANTITY_KEY);
	}

	public String getDescription() {
		return (String) entity.getProperty(DESCRIPTION_KEY);
	}
}