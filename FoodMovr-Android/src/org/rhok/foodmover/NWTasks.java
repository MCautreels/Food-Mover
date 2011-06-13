package org.rhok.foodmover;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

public class NWTasks extends AsyncTask<Object, Void, Object> {
									//<Params, Progress, Result>
	
	public static final int GET_PRODUCER = 9;
	public static final int POST_PRODUCER = 99;
	public static final int GET_CONSUMER = 999;
	public static final int POST_CONSUMER = 9999;
	public static final int GET_LISTING = 99999;
	public static final int POST_LISTING = 999999;	
	

	
	private static final String TAG = "NWTasks";
	
	@Override
	protected Object doInBackground(final Object... args) {
		// This class will run network tasks on a background thread
		long result = -1;
		String[] listingsValues = null;
		Log.d(TAG, "In async doInbackgnd");	

		
		final ContentValues initialValues = (ContentValues)args[1];
        
		createConnection();

		switch ((Integer) args[0]) {
		case GET_PRODUCER:
			
		case POST_PRODUCER:
			
		case GET_CONSUMER:
			
		case POST_CONSUMER:

		case GET_LISTING:
			try {
				listingsValues = getListings();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return listingsValues;
			
		case POST_LISTING:			
			try {
				createListing(initialValues);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		default:
			result = -1;
			return result;
	} // End switch

	}
	
	void createConnection() {
	
	        

	}
	
	public void createListing(ContentValues initialValues)throws IOException {

        // Create a new HttpClient and Post Header
        HttpClient client = new DefaultHttpClient();
        JSONObject json = new JSONObject();
        String url = null;
        try {
        	/*initialValues.get("gunit",
            HttpPost post = new HttpPost(
                    "http://foodmovr.appspot.com/api/v1/listings");
            json.put("lat", "37.3861111");
            json.put("lng", "-122.0827778");
            json.put("description", "mytext");
            json.put("quantity", "10"); */
            
        	url = (String)initialValues.get("url");
            HttpPost post = new HttpPost(url);
            json.put("lat", initialValues.get("lat"));
            json.put("lng", initialValues.get("lng"));
            json.put("description", initialValues.get("description"));
            json.put("quantity", initialValues.get("quantity"));            
            
            StringEntity se = new StringEntity("JSON: " + json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                    "application/json"));
            post.setEntity(se);
            HttpResponse response = client.execute(post);
            /* Checking response */
            if (response != null) {
                System.out.println(response.toString());
                InputStream in = response.getEntity().getContent(); // Get the
                                                                    // data in
                                                                    // the
                                                                    // entity
                System.out.println(in.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            // "Cannot Establish Connection"
        }        
	
		}		
					
	public String[] getListings() throws IOException {
		final ContentValues getValues = null;
        String[] descarr = null;   
        
		String request = "http://foodmovr.appspot.com/api/v1/listings?lat=37.386111&lng=-122.0827778";
					
    	//HttpClient client = new HttpClient();
        //GetMethod method = new GetMethod(request);
        
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
        System.out.println("Listing form get: " + response.getStatusLine());
        
        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        
        // If the response does not enclose an entity, there is no need
        // to worry about connection release
        if (entity != null) {
            InputStream instream = null;
			try {
				instream = entity.getContent();
			
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
                
              //  JSONObject jo = new JSONObject(jsonString);
                
                // A JSONArray is an ordered sequence of values. Its external form is a 
                // string wrapped in square brackets with commas between the values.
                
                JSONArray ja = new JSONArray(jsonString);
                
                // Get the JSONObject value associated with the search result key.
                //jo = jo.getJSONObject("listings");
                
                //System.out.println(jo.toString());
                
                // Get the JSONArray value associated with the Result key
                //ja = jo.getJSONArray("listings");
                
                // Get the number of search results in this set
                int resultCount = ja.length();
            
                
                // Loop over each result and print the title, summary, and URL
                for (int i = 0; i < resultCount; i++)
                {
                	JSONObject resultObject = ja.getJSONObject(i);
                	descarr[i] = (String) resultObject.get("description");
                	 
                	//System.out.println(resultObject.get("lat"));
                	//System.out.println(resultObject.get("lng"));
                	//System.out.println(resultObject.get("quantity"));                	
                	//System.out.println(resultObject.get("description"));
                	//System.out.println("--");
                	
                	//getValues.put("lat", (String)resultObject.get("lat"));
                	//getValues.put("lng", (String)resultObject.get("lng"));
                	//getValues.put("quantity", (String)resultObject.get("quantity"));
                	//getValues.put("description", (String)resultObject.get("description"));
                	
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

            } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

                // Closing the input stream will trigger connection release
                instream.close();

            }

            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();	
        }	// End if
        return descarr;
	}
	
}
