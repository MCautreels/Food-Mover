package org.rhok.foodmover.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.rhok.foodmover.database.EMF;
import org.rhok.foodmover.entities.FoodListing;

public class FoodListingService {
	
	public static void addFoodListing(FoodListing foodlisting){
		EntityManagerFactory emf = EMF.get();
		EntityManager em = emf.createEntityManager();
		
		EntityTransaction tx =  em.getTransaction();
		try
		{
		    tx.begin();		    
		    em.persist(foodlisting);
		    tx.commit();
		}
		finally
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }
		    em.close();
		}
	}
	
	public static List<FoodListing> findFoodListings(Float longitude, Float latitude, Float distance)
	{
		List<FoodListing> result = new ArrayList<FoodListing>();
		
		FoodListing fl1 = new FoodListing();
		fl1.setLatitude(1);
		fl1.setLongitude(1);
		fl1.setDescription("Food listing 1");
		
		FoodListing fl2 = new FoodListing();
		fl2.setLatitude(1);
		fl2.setLongitude(1);
		fl2.setDescription("Food listing 2");
		
		result.add(fl1);
		result.add(fl2);
		
		return result;
		
		//throw new RuntimeException("Not implemented");
	}
	
	public static List<FoodListing> getFoodListingByName(String name)
	{
		List<FoodListing> resultList = new ArrayList<FoodListing>();
		EntityManagerFactory emf = EMF.get();
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		try
		{
		    tx.begin();

		    Query q = em.createQuery("SELECT fl FROM FoodListing fl WHERE fl.name = '" + name + "'");
		    List results = q.getResultList();
		    Iterator iter = results.iterator();
		    
		    while (iter.hasNext())
		    {
		        FoodListing fl = (FoodListing) iter.next();
		        resultList.add(fl);
		    }
		    		    
		    tx.commit();
		}
		finally
		{
		    if (tx.isActive())
		    {
		        tx.rollback();
		    }

		    em.close();
		}
		return resultList;
	}
}
