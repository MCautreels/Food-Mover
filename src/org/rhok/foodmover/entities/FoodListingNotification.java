package org.rhok.foodmover.entities;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.management.RuntimeErrorException;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.mail.MailService.Message;

public class FoodListingNotification extends BaseEntity {
	public static final String NOTIFICATION_KEY = "Notification";
	public static final String LAT_KEY = "lat";
	public static final String LONGITUDE_KEY = "longitude";
	public static final String RADIUS_KEY = "radius";
	public static final String NOTIFICATION_TYPE_KEY = "type";
	public static final String OWNER_KEY = "owner";

	public FoodListingNotification() {
		entity = new Entity(NOTIFICATION_KEY);
	}

	public FoodListingNotification(Entity entity) {
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

	public void setRadiusKm(int radius) {
		entity.setProperty(RADIUS_KEY, radius);
	}

	public float getLat() {
		return ((Float) entity.getProperty(LAT_KEY)).floatValue();
	}

	public float getLongitude() {
		return ((Float) entity.getProperty(LONGITUDE_KEY)).floatValue();
	}

	public String getNotificationType() {
		return (String) entity.getProperty(NOTIFICATION_TYPE_KEY);
	}

	public int getRadius() {
		return ((Integer) entity.getProperty(RADIUS_KEY)).intValue();
	}

	public FoodMoverUser getOwner() {
		return (FoodMoverUser) entity.getProperty(OWNER_KEY);
	}

	public void notifyUser(FoodListing listing) {
		if (!closeEnoughTo(listing)) {
			return;
		}

		if (getNotificationType().equals("email")) {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			MimeMessage msg = new MimeMessage(session);

			try {
				msg.setFrom(new InternetAddress("nick.heiner@gmail.com", "FoodMovr"));
				msg.setSubject("[FoodMovr] New Listing");
				msg.setText("New food listing in your area! " + listing.getDescription());
				Transport.send(msg);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public boolean closeEnoughTo(FoodListing listing) {
		// Spherical Law of Cosines
		// http://www.movable-type.co.uk/scripts/latlong.html
		final int EARTH_RADIUS_KM = 6371;

		double lat1 = Math.toRadians(listing.getLat());
		double lng1 = Math.toRadians(listing.getLongitude());

		double lat2 = Math.toRadians(getLat());
		double lng2 = Math.toRadians(getLongitude());

		// distance is in KM
		double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2)
				* Math.cos(lng2 - lng1))
				* EARTH_RADIUS_KM;

		return distance < getRadius();
	}
}
