package org.rhok.foodmover.entities;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;

public abstract class BaseEntity {
	
	protected Entity entity;

	public void put() {
		DatastoreServiceFactory.getDatastoreService().put(entity);
	}
	
	public void delete(){
		DatastoreServiceFactory.getDatastoreService().delete(entity.getKey());
	}
	
	public Key getKey() {
		return entity.getKey();
	}
	
	protected abstract String getEntityName();
	
	public Query getQuery() {
		return new Query(getEntityName());
	}
}
