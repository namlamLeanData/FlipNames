package SFRest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.io.IOException;
 
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ClientProtocolException;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import org.json.JSONException;
/**
 * Class logs into Salesforce with given USERNAME and PASSWORD.
 * It is then directed to the URI located at baseUri + ENDPOINT.
 * 
 * It is able to create new leads, get existing leads, and update 
 * existing leads so that the First Name and Last Name of given lead 
 * is swapped on Salesforce using its REST API.
 */
public class FlipNames {
	
	/**
	 * Logs into the Salesforce platform.
	 * 
	 * @return 200 if login is successful
	 */
    
	public int doLogin() {
		return login();
	}    
	
	/**
	 * Posts a new Lead.
	 * 
	 * @param firstName The lead's first name
	 * @param lastName The lead's last name
	 * @param companyName The lead's company
	 * @return 200 if a new lead is created
	 */
    public int postLead(String firstName, String lastName, String companyName) {
		Lead lead = new Lead();
		lead.setFirstName(firstName);
		lead.setLastName(lastName);
		lead.setCompanyName(companyName);

    	return createLead(lead);
    }
    
    /**
     * Gets all available leads.
     * @return a List containing all leads
     */
    public List<Lead> getLeads() {
    	return getAllLeads();
    }
    
    /**
     * Updates an existing lead.
     * 
     * @param lead The lead to be updated
     * @return 200 if the lead was updated
     */
    public int putLead(Lead lead) {
    	return updateLead(lead);
    }
    
    /**
     * Finds a lead on Salesforce by their Id
     * 
     * @param id The Id number of the Lead requested
     * @return The lead requested or an empty lead if not found
     */
    public Lead getLeadById(String id) {
    	return getLead(id);
    }
    
    /**
     * Helper function of doLogin().
     * Connects to Salesforce using the given credentials
     * 
     * @return 200 if login is successful
     */
    private int login() {
        HttpClient httpclient = HttpClientBuilder.create().build();
 
        // Assemble the login request URL
        String loginURL = LOGINURL +
                          GRANTSERVICE +
                          "&client_id=" + CLIENTID +
                          "&client_secret=" + CLIENTSECRET +
                          "&username=" + USERNAME +
                          "&password=" + PASSWORD;
 
        HttpPost httpPost = new HttpPost(loginURL);
        HttpResponse response = null;
 
        try {
            response = httpclient.execute(httpPost);
        } catch (ClientProtocolException cpe) {
            cpe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            System.out.println("Error authenticating: " + statusCode);
        }
 
        String getResult = null;
        try {
            getResult = EntityUtils.toString(response.getEntity());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
 
        JSONObject jsonObject = null;
        String loginAccessToken = null;
        String loginInstanceUrl = null;
 
        try {
            jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            loginAccessToken = jsonObject.getString("access_token");
            loginInstanceUrl = jsonObject.getString("instance_url");
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
 
        baseUri = loginInstanceUrl + ENDPOINT;
        oauthHeader = new BasicHeader("Authorization", "OAuth " + loginAccessToken);
        
        System.out.println("baseUri: "+ baseUri);        
 
        httpPost.releaseConnection();
        
        return statusCode;
    }
    
    /**
     * Helper function of postLead().
     * Creates a JSON Object representing the lead to be created and
     * then uses POST on the Salesforce REST API.
     * 
     * @param lead The lead to be created
     * @return 200 If the lead was created
     */
    private int createLead(Lead lead) {
    	int statusCode = 0;
    	
    	HttpPost httpPost = new HttpPost(baseUri);
        httpPost.addHeader(oauthHeader);
        httpPost.addHeader(stylizedHeader);
    	try {	
    		HttpClient httpClient = HttpClientBuilder.create().build();
            
    		JSONObject j = new JSONObject();
            j.put("firstName", lead.getFirstName());
            j.put("lastName", lead.getLastName());
            j.put("companyName", lead.getCompanyName());
            StringEntity body = new StringEntity(j.toString(1));
            body.setContentType("application/json");
            httpPost.setEntity(body);
 
            HttpResponse response = httpClient.execute(httpPost);

            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != 201) {
                System.out.println("Insertion unsuccessful. Status code: " + statusCode);
            }            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } finally{
    		httpPost.releaseConnection();
    	}
    	
    	return statusCode;
    }
    
    /**
     * Helper function of getLeads().
     * Sends a GET request to Salesforce requesting all leads
     * 
     * @return A List of all leads
     */
    private List<Lead> getAllLeads() {
    	List<Lead> leads = new ArrayList<>();
    	HttpGet httpGet = new HttpGet(baseUri);
        httpGet.addHeader(oauthHeader);
        httpGet.addHeader(stylizedHeader);
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(httpGet);
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseString = EntityUtils.toString(response.getEntity());
                JSONArray jsa = new JSONArray(responseString);

                for(int i = 0; i < jsa.length(); i++) {
                	Lead currentLead = new Lead();
                	JSONObject current = jsa.optJSONObject(i);
                	Iterator<String> itr = current.keys();
                	while(itr.hasNext()) {
                		String currentKey = itr.next();
                		if(currentKey.equals("FirstName")) { currentLead.setFirstName(current.getString(currentKey)); }
                		else if(currentKey.equals("LastName")) { currentLead.setLastName(current.getString(currentKey)); }
                		else if(currentKey.equals("Id")) { currentLead.setId(current.getString(currentKey)); }
                	}
                	leads.add(currentLead);
                }
            }
            else {
                System.out.println("Query unsuccessful. Status code: " + statusCode);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } finally {
        	httpGet.releaseConnection();
        }
        
        return leads;
    }
    
    /**
     * Helper function of putLead().
     * Swaps the lead's first name with their last name.
     * 
     * @param lead The lead to be swapped
     * @return 200 if the swap was successful
     */
    private int updateLead(Lead lead) {
    	int statusCode = 0;
    	String uri = baseUri + "/" + lead.getId();
    	HttpPut httpPut = new HttpPut(uri);
        httpPut.addHeader(oauthHeader);
        httpPut.addHeader(stylizedHeader);
    	try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            
            JSONObject jso = new JSONObject();
            StringEntity body = new StringEntity(jso.toString(1));
            body.setContentType("application/json");
            httpPut.setEntity(body);

            HttpResponse response = httpClient.execute(httpPut);

            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != 204) {
                System.out.println("Lead update NOT successful. Status code: " + statusCode);
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } finally {
        	httpPut.releaseConnection();
        }
    	
    	return statusCode;
    }
    
    /**
     * Helper function of getLeadById().
     * Sends a GET request for the lead with this Id number
     * 
     * @param id The Id of the Lead
     * @return The lead of this Id
     */
    private Lead getLead(String id) {
    	Lead lead = new Lead();
    	HttpGet httpGet = new HttpGet(baseUri + '/' + id);
        httpGet.addHeader(oauthHeader);
        httpGet.addHeader(stylizedHeader);
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse response = httpClient.execute(httpGet);
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseString = EntityUtils.toString(response.getEntity());
                JSONArray jsa = new JSONArray(responseString);

                for(int i = 0; i < jsa.length(); i++) {
                	JSONObject current = jsa.optJSONObject(i);
                	Iterator<String> itr = current.keys();
                	while(itr.hasNext()) {
                		String currentKey = itr.next();
                		if(currentKey.equals("FirstName")) { lead.setFirstName(current.getString(currentKey)); }
                		else if(currentKey.equals("LastName")) { lead.setLastName(current.getString(currentKey)); }
                		else if(currentKey.equals("Id")) { lead.setId(current.getString(currentKey)); }
                	}
                }
            }
            else {
                System.out.println("Query unsuccessful. Status code: " + statusCode);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        } finally {
        	httpGet.releaseConnection();
        }
        
        return lead;
    }
    
    private final String USERNAME        = "dev%2bci%40leandatainc.com";
    private final String PASSWORD        = "namlam123Ecx3lISY1G3KhpY58CsZBh18";
    private final String LOGINURL        = "https://continuous-integration.my.salesforce.com";
    private final String GRANTSERVICE    = "/services/oauth2/token?grant_type=password";
    private final String CLIENTID        = "3MVG9zlTNB8o8BA0YWXbimYqNTEDuZXJroHZ0TIFyuuHDFzjvHgLf3oJwPDWGiR_X5U6GFCDuR_pQ.8wop7qs";
    private final String CLIENTSECRET    = "8334322503885021746";
    private final String ENDPOINT        = "/services/apexrest/QATestLeads" ;
    private String baseUri;
    private Header oauthHeader;
    private final Header stylizedHeader  = new BasicHeader("X-PrettyPrint", "1");
    public static int count				 = 0;
}