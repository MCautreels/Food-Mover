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
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

public class ListingTest {

	private final LocalServiceTestHelper dataStoreHelper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(), new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true)
			.setEnvEmail("foo@bar.com").setEnvAuthDomain("Google");

	@Before
	public void setUp() {
		dataStoreHelper.setUp();
	}

	@After
	public void tearDown() {
		dataStoreHelper.tearDown();
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testMakeNewFoodListing() {
		float lat = 3;
		float longitude = 4;
		String description = "oooo body massage";
		int quantity = 50;
		Date expirationDate = new Date();
		expirationDate.setHours(expirationDate.getHours() + 4);

		Key key = ApiMethods.makeNewFoodListing(lat, longitude, description, quantity, expirationDate);

		FoodListing result = new FoodListing(key);

		assertEquals(lat, result.getLat(), .2);
		assertEquals(longitude, result.getLongitude(), .2);
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
		notification.setLongitude(lng);
		notification.setRadiusKm(new Long(200));

		FoodListing listing = new FoodListing();
		listing.setLat(lat + 1);
		listing.setLongitude(lng + 1);

		assertTrue(notification.closeEnoughTo(listing));
	}

	@Test
	public void testNotificationDistanceTooFar() {
		float lat = 3;
		float lng = 4;
		FoodListingNotification notification = new FoodListingNotification();

		notification.setLat(lat);
		notification.setLongitude(lng);
		notification.setRadiusKm(new Long(2));

		FoodListing listing = new FoodListing();
		listing.setLat(lat + 100);
		listing.setLongitude(lng + 100);

		assertFalse(notification.closeEnoughTo(listing));
	}
}