package org.rhok.foodmover.entities;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@Entity
public class FoodMoverUser {
	
	public enum UserType {CONSUMER, PRODUCER};
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

	@Basic
	private User user;
	
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
