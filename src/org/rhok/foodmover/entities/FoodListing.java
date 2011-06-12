package org.rhok.foodmover.entities;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.rhok.foodmover.api.ObjectifyUtil;
import org.rhok.foodmover.api.Util;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

public class FoodListing {
	
	// id must be a Long, and it must be init'd to null
	// otherwise Objectify won't successfully create a new backing instance in the datastore.
	@Id
	private Long id = null;

	// these have to be the exact variable names for `lat` and `lng`, for
	// querying
	private static final String LAT_VAR_NAME = "lat";
	private static final String LNG_VAR_NAME = "lng";
	private static final String OWNER_VAR_NAME = "owner";

	private float lat;
	private float lng;
	private int quantity;
	private String description;
	private Date creationDate;
	private Date expirationDate;
	
	// I think this needs to be a Key, because objectify can't serialize a FoodMoverUser directly.
	private Key<FoodMoverUser> owner;
	
	private static final int EXPIRATION_HOURS = 3;

	@SuppressWarnings("deprecation")	
	public FoodListing() {
		setCreationDate(new Date());
		
		final Date expiration = new Date();
		expiration.setHours(expiration.getHours() + EXPIRATION_HOURS);
		setExpirationDate(expiration);
	}

	public boolean expired() {
		final Date now = new Date();
		return !(now.after(getCreationDate()) && now.before(getExpirationDate()));
	}

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public FoodMoverUser getOwner() {
		Objectify ofy = ObjectifyUtil.get();
		return ofy.get(owner);
	}

	public void setOwner(FoodMoverUser owner) {
		this.owner = owner.getKey();
	}
	
	public static List<FoodListing> findFoodListings(float lat, float longitude, float distanceKM) {
		return Util.findWithinDistance(lat, longitude, distanceKM, LAT_VAR_NAME, LNG_VAR_NAME, FoodListing.class).list();
	}

	public static Collection<FoodListing> getListingsFor(FoodMoverUser currentUser) {
		Objectify ofy = ObjectifyUtil.get();
		return ofy.query(FoodListing.class).filter(OWNER_VAR_NAME, currentUser.getKey()).list();
	}
}