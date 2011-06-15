package org.rhok.foodmover;

import java.util.Map;
import java.util.Map.Entry;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import com.google.appengine.repackaged.com.google.common.collect.Maps;

public class FoodMovrRestApi extends Application {

	private static final Map<String, Class<? extends BaseResource>> resourceMap = Maps.newHashMap();
	
	static {
		resourceMap.put("/listing", FoodListingResource.class);
		resourceMap.put("/setUserKind", FoodMovrUserResource.class);
		resourceMap.put("/notification", FoodListingNotificationResource.class);
	}
	
	@Override
	public Restlet createInboundRoot() {
		Router router = new Router(getContext());

		router.attachDefault(new Directory(getContext(), "war:///"));
		
		for (Entry<String, Class<? extends BaseResource>> pathResourceMapping : resourceMap.entrySet()) {
			router.attach(pathResourceMapping.getKey(), pathResourceMapping.getValue());
		}

		return router;
	}

}
