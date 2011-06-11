package org.rhok.foodmover.entities;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Id;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class FoodListingNotification {

	@Id
	private long id;

	// these have to be the exact variable names for `lat` and `lng`, for
	// querying
	private static final String LAT_VAR_NAME = "lat";
	private static final String LNG_VAR_NAME = "lng";

	private float lat;
	private float lng;
	private Key<FoodMoverUser> owner;
	private float radiusKm;

	// no radius may be larger than this
	// this makes queries a bit easier by limiting the number we need to check
	// while notifying for a given new listing.
	private static final float RADIUS_LIMIT = 20;

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
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(owner);
	}

	public void setOwner(Key<FoodMoverUser> owner) {
		this.owner = owner;
	}

	public float getRadiusKm() {
		return radiusKm;
	}

	public void setRadiusKm(float radiusKm) {
		if (radiusKm > RADIUS_LIMIT) {
			throw new IllegalArgumentException(String.format(
					"Radius must be less than '%d', but was: '%d'",
					RADIUS_LIMIT, radiusKm));
		}
		this.radiusKm = radiusKm;
	}

	public void notifyUser(FoodListing listing) {
		if (!closeEnoughTo(listing)) {
			return;
		}

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "...";

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("no-reply@foodmover.com",
					"Food Mover Notification Service"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(this
					.getOwner().getRawUserObject().getEmail(), this.getOwner()
					.getRawUserObject().getEmail()));
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

	public boolean closeEnoughTo(FoodListing listing) {
		// Spherical Law of Cosines
		// http://www.movable-type.co.uk/scripts/latlong.html
		final int EARTH_RADIUS_KM = 6371;

		double lat1 = Math.toRadians(listing.getLat());
		double lng1 = Math.toRadians(listing.getLng());

		double lat2 = Math.toRadians(getLat());
		double lng2 = Math.toRadians(getLng());

		// distance is in KM
		double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1))
				* EARTH_RADIUS_KM;

		return distance < getRadiusKm();
	}

	public static void notifyOfNewListing(FoodListing listing) {
		Objectify objectify = ObjectifyService.begin();
		
		// This query will probably fail for longitude wrap around	
		Query<FoodListingNotification> notifications = objectify.query(FoodListingNotification.class)
				.filter(LAT_VAR_NAME + " >", listing.getLat() - kmToLatitude(RADIUS_LIMIT))
				.filter(LAT_VAR_NAME + " <", listing.getLat() + kmToLatitude(RADIUS_LIMIT))
				.filter(LNG_VAR_NAME + " >", listing.getLng() - kmToLongitude(RADIUS_LIMIT))
				.filter(LNG_VAR_NAME + " <", listing.getLng() + kmToLongitude(RADIUS_LIMIT));
		
		for (FoodListingNotification notification : notifications) {
			notification.notifyUser(listing);
		}
	}
	
	private static float kmToLatitude(float km) {
		// approximation from http://geography.about.com/library/faq/blqzdistancedegree.htm
		return km / 111;
	}
	
	private static float kmToLongitude(float km) {
		// approximation from http://geography.about.com/library/faq/blqzdistancedegree.htm
		return km / 90;
	}
}
