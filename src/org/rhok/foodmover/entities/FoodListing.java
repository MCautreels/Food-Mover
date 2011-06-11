package org.rhok.foodmover.entities;

import java.util.Date;

import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class FoodListing {
	
	@Id
	private long id;
	
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
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(owner);
	}

	public void setOwner(FoodMoverUser owner) {
		this.owner = owner.getKey();
	}
}