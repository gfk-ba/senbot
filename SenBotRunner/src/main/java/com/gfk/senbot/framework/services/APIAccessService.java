package com.gfk.senbot.framework.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gfk.senbot.framework.BaseServiceHub;
import com.gfk.senbot.framework.context.SenBotContext;
import com.gfk.senbot.framework.cucumber.stepdefinitions.ScenarioGlobals;
import com.gfk.senbot.framework.data.GenericUser;

import cucumber.api.Scenario;

/**
 * A generic service for accessing a remote WebService API
 * @author joostschouten
 *
 */
public class APIAccessService extends BaseServiceHub{
	
	/**
	 * setup the default coockie handeling so all API requests will function as expected
	 */
//	static {
//		CookieHandler.setDefault(new CookieManager());
//		((CookieManager)CookieHandler.getDefault()).setCookiePolicy(CookiePolicy.ACCEPT_ALL);
//	}
	
	private static Logger log = LoggerFactory.getLogger(APIAccessService.class);
	
	/**
	 * Static keys used to store Cucumber {@link Scenario} session scoped variables
	 */
	public static final String LAST_SCENARIO_HTTP_RESPONSE_CODE_KEY = "LAST_SCENARIO_HTTP_RESPONSE_CODE_KEY";
	public static final String API_AUTHENTICATION_USER = "API_AUTHENTICATION_USER";
	public static final String API_ACCESS_MODE_KEY = "API_ACCESS_MODE_KEY";
	/**
	 * Static keys used to store Scenarion scoped values in the {@link ScenarioGlobals}
	 */
	public static final String LAST_SCENARIO_JSON_RESPONSE_KEY = "LAST_SCENARIO_JSON_RESPONSE_KEY";

	/**
	 * Constants indicating if the API should be accessed directly or though the browser using selenium
	 */
	public static final String BROWSER_API_ACCESS_MODE = "browser";
	public static final String HTTP_API_ACCESS_MODE = "http";

	
	public JSONObject getLastJSONResponse() {
		return (JSONObject) getScenarioGlobals().getAttribute(APIAccessService.LAST_SCENARIO_JSON_RESPONSE_KEY);
	}

	/**
	 * When the api is accessed though the browser selenium is used, though straight HTTP calls we can choose to POST or GET the api
	 * 
	 * @return the api access mode being either {@link DriveCoreAPIUtil#HTTP_API_ACCESS_MODE} (default) or {@link DriveCoreAPIUtil#BROWSER_API_ACCESS_MODE}
	 */
	public String getAPIAccessMode() {
		String apiAccessMode = (String) getScenarioGlobals().getAttribute(API_ACCESS_MODE_KEY);
		return apiAccessMode == null ? HTTP_API_ACCESS_MODE : apiAccessMode;
	}

	public Integer getLastHTTPResponseCode() {
		return (Integer) getScenarioGlobals().getAttribute(LAST_SCENARIO_HTTP_RESPONSE_CODE_KEY);
	}
	
	/**
	 * assert that the {@link JSONObject} response is not null and does not hold any error messages
	 * @param response
	 * @throws JSONException
	 */
	public void assertCorrectResonse(JSONObject response) throws JSONException {
		Integer lastHTTPResponseCode = getLastHTTPResponseCode();
		if(lastHTTPResponseCode != null) {
			//if a response code is set, check it. Selenium requests do not let you query this which is why we can't check this in case of a selenium API call
			assertEquals("The https response code should be 200 (OK)", new Integer(HttpURLConnection.HTTP_OK), lastHTTPResponseCode);
		}
		assertNotNull("The request should result in a response", response);	
	}

	public void assertNoError(JSONObject response) throws JSONException {
		if(response.has("error") ) {
			Object errorObj = response.get("error");
			if(!JSONObject.NULL.equals(errorObj)) {				
				fail("The request resulted in a response with error: " + errorObj.toString());
			}
		}		
	}

	public void assertErrorMessage(String expectedMessage) throws JSONException {
		
		JSONObject response = getLastJSONResponse();
			
		if(!response.has("error")) {
			fail("The request holds no error while it is expected");
		}	
		else {			
			Object errorObj = response.get("error");
			String foundErrorMessage = null;
			if(errorObj instanceof JSONObject) {
				JSONObject errorJSON = (JSONObject) errorObj;
				foundErrorMessage = errorJSON.getString("message");
			}
			else if (errorObj instanceof String) {
				foundErrorMessage = (String) errorObj;
			}
			assertEquals(expectedMessage, foundErrorMessage);
		}
	}
	
	/**
	 * Process the {@link JSONObject} request
	 * 
	 * @param urlString
	 * @param request
	 * @param requestType
	 * @param requestHedaers
	 * @return
	 * @throws CertificateException
	 * @throws IOException
	 * @throws JSONException
	 */
	public JSONObject processAPIRequest(
			String urlString,
			JSONObject request,
			String requestType, 
			String[] requestParameters,
			String... requestHedaers) throws CertificateException, IOException, JSONException {
		resetRequestScenarioVariables();
		
		String response = null;
		
		GenericUser authenticationUser = (GenericUser) getScenarioGlobals().getAttribute(API_AUTHENTICATION_USER);
		
		if(BROWSER_API_ACCESS_MODE.equals(getAPIAccessMode())) {
			response = processBrowserAPIRequest(urlString, request, requestType, requestParameters);
		}
		else {
			response = processHTTPAPIRequest(urlString, request, authenticationUser, requestType, requestParameters, requestHedaers);			
		}
		
		JSONObject responseJSON = null;
		
		try{
			if(!StringUtils.isBlank(response)) {
				responseJSON = new JSONObject(response);
				log.debug("API JSON response: " + responseJSON.toString(4));
			}				
		}
		catch(JSONException ex) {
			fail("Contructing the JSON respose failed with: " + ex.getLocalizedMessage() + "\n\nResponse: " + response);
		}
		getScenarioGlobals().setAttribute(APIAccessService.LAST_SCENARIO_JSON_RESPONSE_KEY, responseJSON);
		return responseJSON;
	}
	
	public String processBrowserAPIRequest(String urlString, JSONObject request, String requestType, String[] requestParameters) throws UnsupportedEncodingException {
		
		StringBuilder builder = new StringBuilder();
		builder.append(urlString + "?json=" + URLEncoder.encode(request.toString(), "UTF-8"));
		for(int i = 0 ; i < requestParameters.length; i = i + 2) {
			builder.append("&" + requestParameters[i] + "=" + requestParameters[i+1]);
		}
		
		SenBotContext.getSeleniumDriver().get(builder.toString());
		String response = null;
		try{	
			//some browsers add some markup to the returned json. If so, clean it up by getText() on the root element
			WebElement rootElement = SenBotContext.getSeleniumDriver().findElement(By.xpath("*"));
			if(rootElement != null) {
				response = rootElement.getText();
			}
			else {				
				response = SenBotContext.getSeleniumDriver().getPageSource();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return response;
	}
	
	private void resetRequestScenarioVariables() {
		//clear the request variables
		getScenarioGlobals().setAttribute(LAST_SCENARIO_HTTP_RESPONSE_CODE_KEY, null);
		getScenarioGlobals().setAttribute(APIAccessService.LAST_SCENARIO_JSON_RESPONSE_KEY, null);
	}
	
	/**
	 * Process the json API request
	 * 
	 * @param request
	 * @param requestType POST or GET
	 * @param requestHedaers2 
	 * @return the {@link JSONObject} representation of the http response
	 * @throws CertificateException
	 * @throws IOException
	 * @throws JSONException
	 */
	public String processHTTPAPIRequest(
			String urlString,
			JSONObject request,
			GenericUser authenticationUser,
			String requestType, 
			String[] requestParameters,
			String... requestHedaers) throws CertificateException, IOException, JSONException {
		
		log.debug("API request: " + request.toString(4));
		String jsonRequest = request.toString();
		
		HttpURLConnection ucon = null;
		
		if("POST".equals(requestType)) {
			
			URL url = new URL(urlString);		
			log.debug("API request: " + url);
			
			ucon = (HttpURLConnection) url.openConnection();

			ucon.setRequestMethod("POST");
			ucon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						
			setRequestHeaders(ucon, authenticationUser, requestHedaers);
				
			for(int i = 0 ; i < requestParameters.length; i = i + 2) {
				ucon.setRequestProperty(getReferenceService().namespaceString(requestParameters[i]), getReferenceService().namespaceString(requestParameters[i+1]));
			}
			
			ucon.setUseCaches (false);
			ucon.setDoInput(true);
			ucon.setDoOutput(true);

		    //Send request
			String encodedPostRequest = "json=" + URLEncoder.encode(jsonRequest, "UTF-8");
			ucon.setRequestProperty("Content-Length", Integer.toString(encodedPostRequest.getBytes().length));
		    DataOutputStream wr = new DataOutputStream (ucon.getOutputStream ());
			wr.writeBytes(encodedPostRequest);
		    wr.flush ();
		    wr.close ();
		}
		else if ("GET".equals(requestType)) {
			log.debug("API request: " + request.toString());
			
			
			StringBuilder builder = new StringBuilder();
			builder.append(urlString + "?json=" + URLEncoder.encode(request.toString(), "UTF-8").replaceAll("\"", "%22"));
			for(int i = 0 ; i < requestParameters.length; i = i + 2) {
				builder.append("&" + getReferenceService().namespaceString(requestParameters[i]) + "=" + getReferenceService().namespaceString(requestParameters[i+1]));
			}
			
			URL url = new URL(builder.toString());		
			log.debug("API request: " + url);

			ucon = (HttpURLConnection) url.openConnection();
			setRequestHeaders(ucon, authenticationUser, requestHedaers);
			
			ucon.setConnectTimeout(5000);
			ucon.setFollowRedirects(true);
			ucon.setDoOutput(true);
		}
		else {
			throw new IllegalArgumentException("Only POST and GET API requests are supported at this stage");
		}

		
		//Obtain the response
		StringBuilder responseBuilder = new StringBuilder();
		try{			
			InputStream inputStream = ucon.getInputStream();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
			String pre = null;                       
			while ((pre = in.readLine()) != null){
				responseBuilder.append(pre);
				responseBuilder.append("\n");
			}
			in.close();
		}
		catch (IOException e) {
			//on error codes we should not fail but just capture the response code
			log.debug("Genarating the API call response caused: ", e);
		}
		String response = responseBuilder.toString();
		
		log.debug("The generated API call response: " + response);
		
		getScenarioGlobals().setAttribute(LAST_SCENARIO_HTTP_RESPONSE_CODE_KEY, ucon.getResponseCode());
		
		return response;
	}

	/**
	 * Add the request headers to the request
	 * 
	 * @param ucon the connection to append the http headers to
	 * @param authenticationUser The username:password combination to pass along as basic http authentication
	 * @param requestHedaers the https header name:value pairs. This means the headers should always come in an even number (name + value) * n
	 */
	private void setRequestHeaders(HttpURLConnection ucon, 
			GenericUser authenticationUser, 
			String[] requestHedaers) {
		if(authenticationUser != null) {
			String authenticationString = authenticationUser.getUserName() + ":" + authenticationUser.getPassword();
			byte[] encodedAuthentication = Base64.encodeBase64(authenticationString.getBytes());
			String authenticationHeaderValue = "Basic " + new String(encodedAuthentication);
			ucon.setRequestProperty("Authorization", authenticationHeaderValue);
		}
		
		for(int i = 0;i<requestHedaers.length;i = i+2) {
			ucon.setRequestProperty(getReferenceService().namespaceString(requestHedaers[i]), getReferenceService().namespaceString(requestHedaers[i+1]));
		}
	}

}
