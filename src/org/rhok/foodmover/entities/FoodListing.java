package org.rhok.foodmover.entities;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

public class FoodListing extends BaseEntity {

	private static final String FOOD_LISTING_KEY = "FoodListing";
	public static final String LAT_KEY = "lat";
	public static final String LONGITUDE_KEY = "longitude";
	public static final String QUANTITY_KEY = "quantity";
	public static final String DESCRIPTION_KEY = "descr";
	public static final String DATE_OF_CREATION_KEY = "dateOfCreation";
	public static final String DATE_OF_EXPIRATION_KEY = "dateOfExpiration";
	public static final String OWNER_KEY = "owner";
	private static final int EXPIRATION_HOURS = 3;

	@SuppressWarnings("deprecation")
	public FoodListing() {
		entity = new Entity(FOOD_LISTING_KEY);
		setDateOfCreation(new Date());
		
		final Date expiration = new Date();
		expiration.setHours(expiration.getHours() + EXPIRATION_HOURS);
		setDateOfExpiration(expiration);
	}

	public FoodListing(Key key) {
		try {
			this.entity = DatastoreServiceFactory.getDatastoreService().get(key);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
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

	public void setDateOfCreation(Date date) {
		entity.setProperty(DATE_OF_CREATION_KEY, date.getTime());
	}

	public void setDateOfExpiration(Date date) {
		entity.setProperty(DATE_OF_EXPIRATION_KEY, date.getTime());
	}

	public float getLat() {
		Object lat = entity.getProperty(LAT_KEY);
		if (lat instanceof Double) {
			lat = ((Double) lat).floatValue();
		}
		return (Float) lat;
	}

	public float getLongitude() {
		Object lng = entity.getProperty(LONGITUDE_KEY);
		if (lng instanceof Double) {
			lng = ((Double) lng).floatValue();
		}
		return (Float) lng;
	}

	public int getQuantity() {
		return ((Long) entity.getProperty(QUANTITY_KEY)).intValue();
	}

	public String getDescription() {
		return (String) entity.getProperty(DESCRIPTION_KEY);
	}

	public Date getDateOfCreation() {
		return new Date((Long) entity.getProperty(DATE_OF_CREATION_KEY));
	}

	public Date getDateOfExpiration() {
		final Object time = entity.getProperty(DATE_OF_EXPIRATION_KEY);
		return new Date((Long) time);
	}

	public FoodMoverUser getOwner() {
		// TODO: Query for the real FoodMoverUser object instead of creating a
		// dummy one
		FoodMoverUser owner = new FoodMoverUser();
		owner.setUser((User) entity.getProperty(OWNER_KEY));
		return owner;
		// return new FoodMoverUser() entity.getProperty(OWNER_KEY);
	}

	public boolean expired() {
		final Date now = new Date();
		return !(now.after(getDateOfCreation()) && now.before(getDateOfExpiration()));
	}
}