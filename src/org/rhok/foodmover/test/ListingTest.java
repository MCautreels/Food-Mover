package org.rhok.foodmover.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ObjectifyUtil;
import org.rhok.foodmover.api.Util;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

public class ListingTest {

	private final LocalServiceTestHelper testHelper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true)
			.setEnvEmail("foo@bar.com").setEnvAuthDomain("Google");

	@Before
	public void setUp() {
		testHelper.setUp();
	}

	@After
	public void tearDown() {
		testHelper.tearDown();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testMakeNewFoodListing() {
		Objectify objectify = ObjectifyUtil.get();
		
		float lat = 3;
		float longitude = 4;
		String description = "oooo body massage";
		int quantity = 50;
		Date expirationDate = new Date();
		expirationDate.setHours(expirationDate.getHours() + 4);

		Key<FoodListing> key = ApiMethods.makeNewFoodListing(lat, longitude, description, quantity, expirationDate);

		FoodListing result = objectify.get(key);

		assertEquals(lat, result.getLat(), .2);
		assertEquals(longitude, result.getLng(), .2);
		assertEquals(description, result.getDescription());
		assertSame(quantity, result.getQuantity());
		assertEquals(expirationDate, result.getExpirationDate());
	}

	@Test
	public void testNotificationDistance() {
		float lat = 3;
		float lng = 4;
		FoodListingNotification notification = new FoodListingNotification();

		notification.setLat(lat);
		notification.setLng(lng);
		notification.setRadiusKm(new Long(15));

		FoodListing listing = new FoodListing();
		listing.setLat(lat + Util.kmToLatitude(2));
		listing.setLng(lng + Util.kmToLongitude(2));

		assertTrue(notification.closeEnoughTo(listing));
	}

	@Test
	public void testNotificationDistanceTooFar() {
		float lat = 3;
		float lng = 4;
		FoodListingNotification notification = new FoodListingNotification();

		notification.setLat(lat);
		notification.setLng(lng);
		notification.setRadiusKm(new Long(2));

		FoodListing listing = new FoodListing();
		listing.setLat(lat + 100);
		listing.setLng(lng + 100);

		assertFalse(notification.closeEnoughTo(listing));
	}
}