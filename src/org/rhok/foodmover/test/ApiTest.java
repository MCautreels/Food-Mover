package org.rhok.foodmover.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ObjectifyUtil;
import org.rhok.foodmover.api.Util;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

public class ApiTest {

	// it's necessary to have LocalDatastoreServiceTestConfig here, because then we don't leak state across tests
	private final LocalServiceTestHelper testHelper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
			new LocalUserServiceTestConfig()).setEnvIsLoggedIn(true).setEnvEmail("foo@bar.com")
			.setEnvAuthDomain("Google");

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
		listing.setLat(lat + 5);
		listing.setLng(lng + 5);

		assertFalse(notification.closeEnoughTo(listing));
	}

	@Test
	public void testFindInArea() {
		int lat = 3;
		int lng = 4;
		Set<FoodListing> originalListings = Sets.newHashSet(new FoodListing(lat, lng), new FoodListing(lat + .1f, lng + .1f),
				new FoodListing(lat - .1f, lng - .1f));

		ObjectifyUtil.get().put(originalListings);

		Set<FoodListing> localListings = Sets.newHashSet(Util.findWithinDistance(lat, lng, 20,
				FoodListing.LAT_VAR_NAME, FoodListing.class));
		
		assertTrue(localListings.equals(originalListings));
	}

	@Test
	public void testFindOutsideArea() {
		int lat = 3;
		int lng = 4;
		Set<FoodListing> inBoundsListings = Sets.newHashSet(new FoodListing(lat, lng), new FoodListing(lat + .1f, lng + .1f),
				new FoodListing(lat - .1f, lng - .1f));
		
		FoodListing outsider = new FoodListing(lat + 1, lng + 1);

		ObjectifyUtil.get().put(outsider);
		ObjectifyUtil.get().put(inBoundsListings);

		Set<FoodListing> localListings = Sets.newHashSet(Util.findWithinDistance(lat, lng, 20,
				FoodListing.LAT_VAR_NAME, FoodListing.class));
		
		assertTrue(localListings.equals(inBoundsListings));
	}
}