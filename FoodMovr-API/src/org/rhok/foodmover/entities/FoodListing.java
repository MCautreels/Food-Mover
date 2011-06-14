package org.rhok.foodmover.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ObjectifyUtil;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.common.collect.Ordering;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

/**
 * Represents some amount of food ready to be picked up.
 */
// TODO: use indices as appropriate to only index what we need:
// http://code.google.com/p/objectify-appengine/wiki/IntroductionToObjectify#Optimizing_Storage
@JsonAutoDetect(JsonMethod.NONE)
public class FoodListing implements GeoItem, Serializable {

	private static final long serialVersionUID = 1L;

	// id must be a Long, and it must be init'd to null
	// otherwise Objectify won't successfully create a new backing instance in the datastore.
	@Id
	private Long id = null;

	// these have to be the exact variable names, for querying
	public static final String LAT_VAR_NAME = "lat";
	private static final String OWNER_VAR_NAME = "owner";

	private float lat;
	private float lng;
	private int quantity;
	private String description;
	private Date creationDate;
	private Date expirationDate;

	// I think this needs to be a Key, because objectify can't serialize a FoodMoverUser directly.
	private Key<FoodMoverUser> owner;

	// the default amount of time that a food listing is up for
	private static final int EXPIRATION_HOURS = 3;

	@SuppressWarnings("deprecation")
	public FoodListing() {
		setCreationDate(new Date());

		final Date expiration = new Date();
		expiration.setHours(expiration.getHours() + EXPIRATION_HOURS);
		setExpirationDate(expiration);
	}

	public FoodListing(float lat, float lng) {
		this();

		this.lat = lat;
		this.lng = lng;
	}

	public boolean expired() {
		final Date now = new Date();
		return !(now.after(getCreationDate()) && now.before(getExpirationDate()));
	}

	// use JsonProperty to explicitly annotate which fields get sent back as JSON
	// If the getter is called getFoo(), the name of the field in JSON will be `foo`,
	// so be careful about renaming getters - it could break the API.
	@JsonProperty
	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	@JsonProperty
	public float getLng() {
		return lng;
	}

	public void setLng(float lng) {
		this.lng = lng;
	}
	
	@JsonProperty
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@JsonProperty
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonProperty
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	@JsonProperty
	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	// if we throw @JsonProperty on this method directly, and `owner` is null,
	// Restlet will complain. Thus, we use getOwnerId() instead.
	public FoodMoverUser getOwner() {
		Objectify ofy = ObjectifyUtil.get();
		return ofy.get(owner);
	}

	public void setOwner(FoodMoverUser owner) {
		this.owner = owner.getKey();
	}
	
	@JsonProperty
	public String getOwnerId() {
		if (owner == null) {
			return null;
		}
		
		return KeyFactory.keyToString(owner.getRaw());
	}

	/**
	 * Find food listings in a certain area.
	 * 
	 * @param lat
	 *            the latitude of the center point
	 * @param longitude
	 *            the longitude of the center point
	 * @param distanceKM
	 *            the radius from the center point, in kilometers
	 * @return a collection of food listings in the specified area
	 */
	public static List<FoodListing> findFoodListings(float lat, float longitude, float distanceKM) {
		return ApiMethods.findWithinDistance(lat, longitude, distanceKM, LAT_VAR_NAME, FoodListing.class);
	}

	/**
	 * Find food listings owned by a given user.
	 * 
	 * @param user
	 *            the owner of the food listings
	 * @return the food listings owned by `user`
	 * 
	 */
	public static List<FoodListing> getListingsFor(FoodMoverUser user) {
		Objectify ofy = ObjectifyUtil.get();
		List<FoodListing> unsortedResults = ofy.query(FoodListing.class).filter(OWNER_VAR_NAME, user.getKey()).list();
		
		return Ordering.from(new Comparator<FoodListing>() {

			public int compare(FoodListing listingA, FoodListing listingB) {
				return listingA.getCreationDate().compareTo(listingB.getCreationDate());
			}
			
		}).sortedCopy(unsortedResults);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Float.floatToIntBits(lat);
		result = prime * result + Float.floatToIntBits(lng);
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + quantity;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FoodListing other = (FoodListing) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (expirationDate == null) {
			if (other.expirationDate != null)
				return false;
		} else if (!expirationDate.equals(other.expirationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (Float.floatToIntBits(lat) != Float.floatToIntBits(other.lat))
			return false;
		if (Float.floatToIntBits(lng) != Float.floatToIntBits(other.lng))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("FoodListing '%s' at (%f, %f)", description, lat, lng);
	}
}