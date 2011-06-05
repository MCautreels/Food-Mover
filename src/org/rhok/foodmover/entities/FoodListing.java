package org.rhok.foodmover.entities;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

public class FoodListing extends BaseEntity {

	public static final String LAT_KEY = "lat";
	public static final String LONGITUDE_KEY = "longitude";
	public static final String QUANTITY_KEY = "quantity";
	public static final String DESCRIPTION_KEY = "descr";
	public static final String OWNER_KEY = "owner";

	public FoodListing() {
		entity = new Entity("FoodListing");
	}
	
	public FoodListing(Key key)
	{
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
		//TODO: Query for the real FoodMoverUser object instead of creating a dummy one
		FoodMoverUser owner = new FoodMoverUser();
		owner.setUser((User)entity.getProperty(OWNER_KEY));
		return owner;
		//return new FoodMoverUser() entity.getProperty(OWNER_KEY);
	}
}