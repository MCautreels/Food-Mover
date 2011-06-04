package org.rhok.foodmover.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class FoodListing {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	
	@Basic
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}