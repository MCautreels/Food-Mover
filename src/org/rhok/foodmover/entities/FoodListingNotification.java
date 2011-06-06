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

import org.rhok.foodmover.ArgNames;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;

public class FoodListingNotification extends BaseEntity {
	public static final String NOTIFICATION_ARG_NAME = "Notification";
	
	public FoodListingNotification() {
		entity = new Entity(NOTIFICATION_ARG_NAME);
	}

	public FoodListingNotification(Entity entity) {
		this.entity = entity;
	}

	public void setLat(float lat) {
		entity.setProperty(ArgNames.LAT_ARG_NAME, lat);
	}

	public void setOwner(FoodMoverUser owner) {
		entity.setProperty(ArgNames.OWNER_ARG_NAME, owner.getRawUserObject());
	}

	public void setLongitude(float longitude) {
		entity.setProperty(ArgNames.LONGITUDE_ARG_NAME, longitude);
	}

	public void setNotificationType(String type) {
		entity.setProperty(ArgNames.NOTIFICATION_TYPE_ARG_NAME, type);
	}

	public void setRadiusKm(float radius) {
		entity.setProperty(ArgNames.RADIUS_ARG_NAME, radius);
	}

	public float getLat() {
		Object lat = entity.getProperty(ArgNames.LAT_ARG_NAME);
		if (lat instanceof Double) {
			lat = ((Double) lat).floatValue();
		}
		return (Float) lat;
	}

	public float getLongitude() {
		Object lng = entity.getProperty(ArgNames.LONGITUDE_ARG_NAME);
		if (lng instanceof Double) {
			lng = ((Double) lng).floatValue();
		}
		return (Float) lng;
	}

	public String getNotificationType() {
		return (String) entity.getProperty(ArgNames.NOTIFICATION_TYPE_ARG_NAME);
	}

	public float getRadiusKm() {
		return ((Float) entity.getProperty(ArgNames.RADIUS_ARG_NAME)).floatValue();
	}

	public FoodMoverUser getOwner() {
		//TODO: Fix hack
		User u = (User) entity.getProperty(ArgNames.OWNER_ARG_NAME);
		FoodMoverUser result = new FoodMoverUser();
		result.setUser(u);
		return result;
		//return (FoodMoverUser) entity.getProperty(OWNER_KEY);
	}

	public void notifyUser(FoodListing listing) {
		if (!closeEnoughTo(listing)) {
			return;
		}

		if (getNotificationType().equals("email")) {
			Properties props = new Properties();
	        Session session = Session.getDefaultInstance(props, null);

	        String msgBody = "...";

	        try {
	            Message msg = new MimeMessage(session);
	            msg.setFrom(new InternetAddress("no-reply@foodmover.com", "Food Mover Notification Service"));
	            msg.addRecipient(Message.RecipientType.TO,
	                             new InternetAddress(this.getOwner().getRawUserObject().getEmail(), this.getOwner().getRawUserObject().getEmail()));
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
	        
			/*Properties props = new Properties();
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
			}*/ 
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

		return distance < getRadiusKm();
	}

	@Override
	protected String getEntityName() {
		return NOTIFICATION_ARG_NAME;
	}
}
