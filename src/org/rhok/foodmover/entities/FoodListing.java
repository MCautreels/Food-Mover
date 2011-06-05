package org.rhok.foodmover.entities;

import com.google.appengine.api.datastore.Entity;

public class FoodListing extends BaseEntity {

	public static final String LAT_KEY = "lat";
	public static final String LONGITUDE_KEY = "longitude";
	public static final String QUANTITY_KEY = "quantity";
	public static final String DESCRIPTION_KEY = "descr";
	public static final String OWNER_KEY = "owner";

	public FoodListing() {
		entity = new Entity("FoodListing");
	}
	
	public FoodListing(Entity entity) {
		this.entity = entity;
	}

	public void setLat(float lat) {
		entity.setProperty(LAT_KEY, lat);
	}
	
	public void setOwner(FoodMoverUser owner) {
		entity.setProperty(OWNER_KEY, owner.getRawUserObject());
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
		return ((Double) entity.getProperty(LAT_KEY)).floatValue();
	}

	public float getLongitude() {
		return ((Double) entity.getProperty(LONGITUDE_KEY)).floatValue();
	}

	public int getQuantity() {
		return ((Long)entity.getProperty(QUANTITY_KEY)).intValue();
	}

	public String getDescription() {
		return (String) entity.getProperty(DESCRIPTION_KEY);
	}
	
	public FoodMoverUser getOwner() {
		return (FoodMoverUser) entity.getProperty(OWNER_KEY);
	}
}