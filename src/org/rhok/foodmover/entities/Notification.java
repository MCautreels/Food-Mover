package org.rhok.foodmover.entities;

import com.google.appengine.api.datastore.Entity;

public class Notification extends BaseEntity {
	public static final String LAT_KEY = "lat";
	public static final String LONGITUDE_KEY = "longitude";
	public static final String RADIUS_KEY = "radius";
	public static final String NOTIFICATION_TYPE_KEY = "type";
	public static final String OWNER_KEY = "owner";
	
	public Notification() {
		entity = new Entity("Notification");
	}
	
	public Notification(Entity entity) {
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

	public void setNotificationType(String type) {
		entity.setProperty(NOTIFICATION_TYPE_KEY, type);
	}

	public void setRadius(int radius) {
		entity.setProperty(RADIUS_KEY, radius);
	}

	public float getLat() {
		return ((Double) entity.getProperty(LAT_KEY)).floatValue();
	}

	public float getLongitude() {
		return ((Double) entity.getProperty(LONGITUDE_KEY)).floatValue();
	}

	public String getNotificationType() {
		return (String)entity.getProperty(NOTIFICATION_TYPE_KEY);
	}

	public int getRadius() {
		return ((Integer)entity.getProperty(RADIUS_KEY)).intValue();
	}
	
	public FoodMoverUser getOwner() {
		return (FoodMoverUser) entity.getProperty(OWNER_KEY);
	}
}
