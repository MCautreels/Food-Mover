package org.rhok.foodmover.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ObjectifyUtil;
import org.rhok.foodmover.api.Util;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.repackaged.com.google.common.collect.Collections2;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Subclass;

// TODO refactor this into different test classes, bound together in a TestSuite
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
		float lat = 3;
		float longitude = 4;
		String description = "oooo body massage";
		int quantity = 50;
		Date expirationDate = new Date();
		expirationDate.setHours(expirationDate.getHours() + 4);

		Key<FoodListing> key = ApiMethods.makeNewFoodListing(lat, longitude, description, quantity, expirationDate);

		Date afterListingCreation = new Date();
		Objectify objectify = ObjectifyUtil.get();
		FoodListing result = objectify.get(key);

		assertEquals(lat, result.getLat(), .2);
		assertEquals(longitude, result.getLng(), .2);
		assertEquals(description, result.getDescription());
		assertSame(quantity, result.getQuantity());
		assertEquals(expirationDate, result.getExpirationDate());
		assertTrue(afterListingCreation.after(result.getCreationDate()));
	}

	@Subclass
	public static class MockNotification extends FoodListingNotification {

		private boolean notifyWasCalled;

		@Override
		public void notifyUser(FoodListing listing) {
			notifyWasCalled = true;
			ObjectifyUtil.get().put(this);
		}

		public boolean wasNotifyCalled() {
			return notifyWasCalled;
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testNotifyOnNewListing() {
		float lat = 3;
		float lng = 4;

		Date expirationDate = new Date();
		expirationDate.setHours(expirationDate.getHours() + 4);

		MockNotification mockNotification = new MockNotification();
		mockNotification.setLat(lat);
		mockNotification.setLng(lng);
		ObjectifyService.register(mockNotification.getClass());

		Key<MockNotification> mockNotificationKey = ObjectifyUtil.get().put(mockNotification);
		assertNotNull(ObjectifyUtil.get().find(mockNotificationKey));
		ApiMethods.makeNewFoodListing(lat, lng, "weasels", 3, expirationDate);

		assertTrue(ObjectifyUtil.get().find(mockNotificationKey).wasNotifyCalled());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testNotifyNotCalledTooFarFromNewListing() {
		float lat = 3;
		float lng = 4;

		Date expirationDate = new Date();
		expirationDate.setHours(expirationDate.getHours() + 4);

		MockNotification mockNotification = new MockNotification();
		mockNotification.setLat(lat + 1);
		mockNotification.setLng(lng + 1);
		ObjectifyService.register(mockNotification.getClass());

		Key<MockNotification> mockNotificationKey = ObjectifyUtil.get().put(mockNotification);
		assertNotNull(ObjectifyUtil.get().find(mockNotificationKey));
		ApiMethods.makeNewFoodListing(lat, lng, "weasels", 3, expirationDate);

		assertFalse(ObjectifyUtil.get().find(mockNotificationKey).wasNotifyCalled());
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
		Set<FoodListing> originalListings = Sets.newHashSet(new FoodListing(lat, lng), new FoodListing(lat + .1f,
				lng + .1f), new FoodListing(lat - .1f, lng - .1f));

		ObjectifyUtil.get().put(originalListings);

		Set<FoodListing> localListings = Sets.newHashSet(ApiMethods.findWithinDistance(lat, lng, 20,
				FoodListing.LAT_VAR_NAME, FoodListing.class));

		assertTrue(localListings.equals(originalListings));
	}

	@Test
	public void testFindInAreaSortedByDistance() {
		int lat = 3;
		int lng = 4;
		FoodListing closest = new FoodListing(lat, lng);
		FoodListing close = new FoodListing(lat + .01f, lng + .01f);
		FoodListing furthest = new FoodListing(lat - .1f, lng - .1f);

		List<FoodListing> listingsInOrder = Lists.newArrayList(closest, close, furthest);
		List<FoodListing> listingsNotInOrder = Lists.newArrayList(furthest, closest, close);
		
		ObjectifyUtil.get().put(listingsNotInOrder);

		List<FoodListing> localListings = ApiMethods.findWithinDistance(lat, lng, 20, FoodListing.LAT_VAR_NAME,
				FoodListing.class);

		assertEquals(listingsInOrder, localListings);
	}

	@Test
	public void testFindOutsideArea() {
		int lat = 3;
		int lng = 4;
		Set<FoodListing> inBoundsListings = Sets.newHashSet(new FoodListing(lat, lng), new FoodListing(lat + .1f,
				lng + .1f), new FoodListing(lat - .1f, lng - .1f));

		FoodListing outsider = new FoodListing(lat + 1, lng + 1);

		ObjectifyUtil.get().put(outsider);
		ObjectifyUtil.get().put(inBoundsListings);

		Set<FoodListing> localListings = Sets.newHashSet(ApiMethods.findWithinDistance(lat, lng, 20,
				FoodListing.LAT_VAR_NAME, FoodListing.class));

		assertTrue(localListings.equals(inBoundsListings));
	}

	@Test
	public void testListingsForCurrentUser() {
		int lat = 3;
		int lng = 4;
		Set<FoodListing> ownedListings = Sets.newHashSet(new FoodListing(lat, lng), new FoodListing(lat + .1f,
				lng + .1f), new FoodListing(lat - .1f, lng - .1f));

		for (FoodListing listing : ownedListings) {
			listing.setOwner(FoodMoverUser.getCurrentUser());
		}

		Set<FoodListing> unownedListings = Sets.newHashSet(new FoodListing(lat + .2f, lng), new FoodListing(lat - .1f,
				lng + .1f), new FoodListing(lat - .1f, lng + .1f));

		Objectify objectify = ObjectifyUtil.get();
		objectify.put(ownedListings);
		objectify.put(unownedListings);

		Set<FoodListing> listingsForCurrentUser = Sets.newHashSet(FoodListing.getListingsFor(FoodMoverUser
				.getCurrentUser()));

		assertTrue(listingsForCurrentUser.equals(ownedListings));
		assertEquals(0, Sets.intersection(listingsForCurrentUser, unownedListings).size());
	}

	@Test
	public void testListingsSortedByCreationDate() {
		int lat = 3;
		int lng = 4;
		FoodListing firstListing = new FoodListing(lat, lng);
		
		// it's necessary to explicitly set the creation date
		// or the test may fail because all three constructors
		// get called so quickly.
		firstListing.setCreationDate(new Date(2009, 1, 1));
		FoodListing secondListing = new FoodListing(lat + .1f, lng + .1f);
		secondListing.setCreationDate(new Date(2010, 1, 1));
		FoodListing thirdListing = new FoodListing(lat - .1f, lng - .1f);
		thirdListing.setCreationDate(new Date(2011, 1, 1));

		List<FoodListing> sortedByCreationDate = Lists.newArrayList(firstListing, secondListing, thirdListing);

		for (FoodListing listing : sortedByCreationDate) {
			listing.setOwner(FoodMoverUser.getCurrentUser());
		}
		
		List<FoodListing> shuffled = Lists.newArrayList(sortedByCreationDate);
		Collections.shuffle(shuffled);

		Objectify objectify = ObjectifyUtil.get();
		objectify.put(shuffled);

		List<FoodListing> listingsForCurrentUser = FoodListing.getListingsFor(FoodMoverUser.getCurrentUser());

		assertEquals(sortedByCreationDate, listingsForCurrentUser);
	}

	@Test
	public void testIsProducer() {
		FoodMoverUser currentUser = FoodMoverUser.getCurrentUser();
		currentUser.setIsProducer(true);
		Key<FoodMoverUser> foodMoverUser = ObjectifyUtil.get().put(currentUser);

		FoodMoverUser restored = ObjectifyUtil.get().find(foodMoverUser);
		assertTrue(restored.isProducer());
	}

	@Test
	public void testIsConsumer() {
		FoodMoverUser currentUser = FoodMoverUser.getCurrentUser();
		currentUser.setIsProducer(false);
		Key<FoodMoverUser> foodMoverUser = ObjectifyUtil.get().put(currentUser);

		FoodMoverUser restored = ObjectifyUtil.get().find(foodMoverUser);
		assertFalse(restored.isProducer());
	}

	// TODO: add test asserting that *_VAR_NAME fields actually correspond to a field on that method
	// For example, in FoodMoverUser, we have OWNER_VAR_NAME = "userKey", and a field `private Key<User> userKey`.
	// If `userKey` was renamed, but OWNER_VAR_NAME was the same, we'd have a silent failure leading to invalid queries.
	// It would be nice to verify these fields are correctly named programmatically.
}