package org.rhok.foodmover.test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;

import static org.junit.Assert.*;

public class LocalDatastoreTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    // run this test twice to prove we're not leaking any state across tests
    private void doTest() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        assertEquals(0, ds.prepare(new Query("yam")).countEntities(Builder.withLimit(10)));
        ds.put(new Entity("yam"));
        ds.put(new Entity("yam"));
        assertEquals(2, ds.prepare(new Query("yam")).countEntities(Builder.withLimit(10)));
    }
    
    @Test
    public void testNotificationDistance() {
    	float lat = 3;
    	float lng = 4;
    	FoodListingNotification notification = new FoodListingNotification();
    	
    	notification.setLat(lat);
    	notification.setLongitude(lng);
    	notification.setRadiusKm(200);
    	
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
    	notification.setRadiusKm(2);
    	
    	FoodListing listing = new FoodListing();
    	listing.setLat(lat + 100);
    	listing.setLongitude(lng + 100);
    	
    	assertFalse(notification.closeEnoughTo(listing));
    }

    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }
}