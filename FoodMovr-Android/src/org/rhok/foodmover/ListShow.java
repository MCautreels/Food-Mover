package org.rhok.foodmover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;


public class ListShow extends ListActivity {
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String [] [] mjsonValues = null;
        int resultCount = 0;
        List<Map<String, String>> groupData = null;
        
        // Create an array of Strings, that will be put to our ListActivity
        String[] names = new String[] { "Sandwiches    info@rhok.org", "Pasta        merko@gmail.com", "Pizza         getpizza@gmail.com", "Chinese    zacks@yahoo.com",
                "Cookies     givea@gmail.com"};
        // Create an ArrayAdapter, that will actually make the Strings above
        // appear in the ListView
        
        // Todo uncomment these 2 lines
        //this.setListAdapter(new ArrayAdapter<String>(this,
        //       android.R.layout.simple_list_item_1, names));
   
       
/*		Toast.makeText(getApplicationContext(),
				"list activity ",
				Toast.LENGTH_SHORT).show();
		
        final ContentValues initialValues = new ContentValues();
		String[] cret = null;
		String[] descarr = null;
		
     //   initialValues.put("url", "http://foodmovr.appspot.com/api/v1/listings?lat=37.3861111&lng=-122.0827778");
		
		   initialValues.put("url", "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query=umbrella&results=10");
		try {
			cret = (String[]) new NWTasks().execute(NWTasks.GET_LISTING,initialValues).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		this.setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, cret));
		
		for (int i = 0; i < cret.length; i++) {					

		System.out.print("Description " + cret[i]);
		}*/
        
        
        /*  Lous yahoo code  */
		//String request = "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=YahooDemo&query=umbrella&results=10&output=json";
        // Get title, link, category
        // select title,link, category from rss where url="http://rss.news.yahoo.com/rss/topstories"
        //String request = "http://query.yahooapis.com/v1/public/yql?q=select%20title%2Clink%2C%20category%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json&diagnostics=true&callback=cbfunc";
        String request = "http://query.yahooapis.com/v1/public/yql?q=select%20title%2Clink%2C%20category%20from%20rss%20where%20url%3D%22http%3A%2F%2Frss.news.yahoo.com%2Frss%2Ftopstories%22&format=json";
                        
        HttpClient httpclient = new DefaultHttpClient();
		
		// Prepare a request object
		HttpGet httpget = new HttpGet(request);
		
		// Execute the request
        HttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        // Examine the response status
        System.out.println("Login form get: " + response.getStatusLine());
        
        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        
        // If the response does not enclose an entity, there is no need
        // to worry about connection release
        if (entity != null) {
            InputStream instream = null;
			try {
				instream = entity.getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try {

            	// Process the response from Yahoo! Web Services
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                String jsonString = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString += line;
                }
                reader.close();
                
                // Construct a JSONObject from a source JSON text string.
                // A JSONObject is an unordered collection of name/value pairs. Its external 
                // form is a string wrapped in curly braces with colons between the names 
                // and values, and commas between the values and names. Example:              
                // {"ResultSet":{"type":"web","totalResultsAvailable":149000000,"totalResultsReturned":10,
                // "firstResultPosition":1,"moreSearch":"\/WebSearchService\/V1\/webSearch?query=umbrella&amp;
                // appid=YahooDemo&amp;region=us","Result":[{"Title":"Umbrella - Wikipedia, the free 
                // encyclopedia","Summary":"An umbrella or parasol (also called a brolly, rainshade, sunshade, 
                // gamp or bumbershoot) is a canopy designed to protect against rain or sunlight. ...",
                // "Url":"http:\/\/en.wikipedia.org\/wiki\/Umbrella",
                
                JSONObject jo = null;
				try {
					jo = new JSONObject(jsonString);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
                // A JSONArray is an ordered sequence of values. Its external form is a 
                // string wrapped in square brackets with commas between the values.
                JSONArray ja = null;
                
                // Get the JSONObject value associated with the search result key.
                // First url jo = jo.getJSONObject("ResultSet");
                
                // Get the JSONArray value associated with the Result key
                // first url ja = jo.getJSONArray("Result");

                jo = jo.getJSONObject("query");
                jo = jo.getJSONObject("results");
                ja = jo.getJSONArray("item");
                
                // Get the number of search results in this set
                resultCount = ja.length();

                
                mjsonValues = new String [resultCount][3];

                // Loop over each result and print the title, summary, and URL
/*                for (int i = 0; i < resultCount; i++)
                {
                	JSONObject resultObject = ja.getJSONObject(i);
                	System.out.println(resultObject.get("Title"));
                	System.out.println(resultObject.get("Summary"));
                	System.out.println(resultObject.get("Url"));
                	System.out.println("--");
                	
                	 mjsonValues[i][0] = (String) resultObject.get("Title");
                     mjsonValues[i][1] = (String) resultObject.get("Summary");
                     mjsonValues[i][2] = (String) resultObject.get("Url");
             		
                }*/

    			groupData = new ArrayList<Map<String, String>>();

    			// -- list item hash re-used
    			Map<String, String> group;

    			// -- create record
    			group = new HashMap<String, String>();
    			
    			for (int i = 0; i < resultCount; i++)
                {
    				//titlearr[i] =(String) mjsonValues[i][0];
    				//summarryarr = (String) mjsonValues[i][1];
    				//urlarr = (String) mjsonValues[i][2];

    				JSONObject resultObject = ja.getJSONObject(i);
    				
    				group.put( "TITLE",(String) resultObject.get("title") );
    				group.put( "LINK",  (String) resultObject.get("link") );
    				group.put( "CATEGORY", (String) resultObject.get("category") );

    				groupData.add(group);				
                }	
    			                

            } catch (IOException ex) {

                // In case of an IOException the connection will be released
                // back to the connection manager automatically
                try {
					throw ex;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

            } catch (RuntimeException ex) {

                // In case of an unexpected exception you may want to abort
                // the HTTP request in order to shut down the underlying
                // connection and release it back to the connection manager.
                httpget.abort();
                throw ex;

            } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

                // Closing the input stream will trigger connection release
                try {
					instream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
            }

			String [] titlearr = new String[resultCount];
			String [] summarryarr = new String[resultCount];
			String [] urlarr = new String[resultCount];


			// -- create an adapter, takes care of binding hash objects in our list to actual row views
		    SimpleAdapter adapter = new SimpleAdapter( this, groupData, android.R.layout.simple_list_item_2, 
			                                                   new String[] { "TITLE", "CATEGORY" },
			                                                   new int[]{ android.R.id.text1, android.R.id.text2 } );
			setListAdapter( adapter );

			
     	//	this.setListAdapter(new ArrayAdapter<String>(this,
        //            android.R.layout.simple_list_item_1, titlearr));
     		
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();	
        }	// End if
        
        
        
    }
   
    /**
     * @return
     * @throws Exception
     */
    public static String[] getListings() throws Exception {
        String request = "http://foodmovr.appspot.com/api/v1/listings?lat=37&lng=-122";

        String[] values = null;
       
        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(request);

        // Execute the request
        HttpResponse response = httpclient.execute(httpget);

        // Examine the response status
        System.out.println("Login form get: " + response.getStatusLine());

        // Get hold of the response entity
        HttpEntity entity = response.getEntity();

        // If the response does not enclose an entity, there is no need
        // to worry about connection release
        if (entity != null) {
            InputStream instream = entity.getContent();
            try {

                // Process the response from Yahoo! Web Services
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(instream));
                String jsonString = "";
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonString += line;
                }
                reader.close();

                // A JSONArray is an ordered sequence of values. Its external form is
                // a string wrapped in square brackets with commas between the values.
                JSONArray ja = new JSONArray(jsonString);
                 
                // Get the number of search results in this set
                int resultCount = ja.length();
                values = new String[resultCount];

                // Loop over each result and print the title, summary, and URL
                for (int i = 0; i < resultCount; i++) {
                    JSONObject resultObject = ja.getJSONObject(i);
                    values[i] = (String) resultObject.get("description");
                   
                    /*System.out.println(resultObject.get("description"));
                    System.out.println(resultObject.get("quantity"));
                    System.out.println(resultObject.get("lng"));
                    System.out.println(resultObject.get("lat"));
                    System.out.println("--");*/
                }

            } catch (IOException ex) {

                // In case of an IOException the connection will be released
                // back to the connection manager automatically
                throw ex;

            } catch (RuntimeException ex) {

                // In case of an unexpected exception you may want to abort
                // the HTTP request in order to shut down the underlying
                // connection and release it back to the connection manager.
                httpget.abort();
                throw ex;

            } finally {

                // Closing the input stream will trigger connection release
                instream.close();

            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
                       
        } // End if
        return values;

    }
}