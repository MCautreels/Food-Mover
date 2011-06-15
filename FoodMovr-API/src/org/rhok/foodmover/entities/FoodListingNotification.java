package org.rhok.foodmover.entities;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Id;

import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ObjectifyUtil;
import org.rhok.foodmover.api.Util;
import org.rhok.foodmover.test.ApiTest;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.annotation.Entity;

/**
 * Represents a user's wish to be notified when new food listings are created for a specified area. 
 * A listing is said to be "in range" of a notification if that listing is close enough
 * that a the notification should fire when the listing is created.
 */
// this @Entity notification is necessary so we can mock notifications
@Entity
public class FoodListingNotification implements GeoItem {

	@Id
	private Long id = null;

	// these have to be the exact variable names for `lat` and `lng`, for
	// querying
	public static final String LAT_VAR_NAME = "lat";
	public static final String LNG_VAR_NAME = "lng";

	private float lat;
	private float lng;
	private Key<FoodMoverUser> owner;
	private float radiusKm;

	// no radius may be larger than this
	// this makes queries a bit easier by limiting the number we need to check
	// while notifying for a given new listing.
	public static final float RADIUS_LIMIT_KM = 20;

	// no support for notification type for now

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

	public FoodMoverUser getOwner() {
		Objectify ofy = ObjectifyUtil.ofy();
		return ofy.get(owner);
	}

	public void setOwner(Key<FoodMoverUser> owner) {
		this.owner = owner;
	}

	public float getRadiusKm() {
		return radiusKm;
	}

	public void setRadiusKm(float radiusKm) {
		if (radiusKm > RADIUS_LIMIT_KM) {
			throw new IllegalArgumentException(String.format("Radius must be less than '%f', but was: '%f'",
					RADIUS_LIMIT_KM, radiusKm));
		}
		this.radiusKm = radiusKm;
	}

	/**
	 * Notify the owner of this notification that a new listing has been created.
	 * If `listing` is too far away, do nothing.
	 * 
	 * @param listing	the listing to notify the owner about
	 */
	public void notifyUser(FoodListing listing) {
		if (!closeEnoughTo(listing)) {
			return;
		}

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Description: " + listing.getDescription();

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("no-reply@foodmover.com", "Food Mover Notification Service"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(this.getOwner().getRawUserObject()
					.getEmail(), this.getOwner().getRawUserObject().getEmail()));
			msg.setSubject("Food has been found near your location! Go check on our site: http://foodmovr.appspot.com");
			msg.setText(msgBody);
			Transport.send(msg);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Check if `listing` is within range of this notification.
	 * 
	 * @param listing
	 * @return
	 */
	public boolean closeEnoughTo(FoodListing listing) {
		double distance = Util.distanceBetween(getLat(), getLng(), listing);

		return distance < getRadiusKm();
	}

	

	/**
	 * Fire all notifications for which `listing` is within range.
	 * 
	 * @param listing
	 */
	public static void notifyOfNewListing(FoodListing listing) {
		Collection<FoodListingNotification> notifications = ApiMethods.findWithinDistance(listing.getLat(), listing.getLng(),
				RADIUS_LIMIT_KM, LAT_VAR_NAME, FoodListingNotification.class);

		for (FoodListingNotification notification : notifications) {
			notification.notifyUser(listing);
		}
	}

	// should equals() and hashCode() only be a function of id?
	// I'm not sure.
	// id isn't set until the item is saved, and it would be nice to support equality before that.

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + Float.floatToIntBits(lat);
		result = prime * result + Float.floatToIntBits(lng);
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + Float.floatToIntBits(radiusKm);
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
		FoodListingNotification other = (FoodListingNotification) obj;
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
		if (Float.floatToIntBits(radiusKm) != Float.floatToIntBits(other.radiusKm))
			return false;
		return true;
	}

}
