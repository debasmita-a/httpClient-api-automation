package httpClientAPIs;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class PostUserAPI {

	@Test
	public void createUserAPITest() {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		
		//1. create post request with URL:
		HttpPost postRequest = new HttpPost("https://gorest.co.in/public/v2/users");
		
		//2. add or set headers:
		postRequest.addHeader("Authorization", "Bearer 5e874cd5ea5d4dcf22fd42eb9dc03bd89c67e7c2483b4c2a24d8e8e25814857f");
		postRequest.setHeader("Content-Type", "application/json");
		
		//3. convert pojo to json string:
		User user = new User("Debasmita", "debasmitatest8@gmail.com", "female", "inactive");
		
		//convert Java POJO to JSON --Serialization - Jackson API:
		ObjectMapper mapper = new ObjectMapper();
		String userJson = null;
		try {
			userJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println(userJson);
		
		//4. add the request body to POST request
		StringEntity userStringEntity = null;
		try {
			userStringEntity = new StringEntity(userJson);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		postRequest.setEntity(userStringEntity);
		
		//5. execute the API:
		try {
			response = httpClient.execute(postRequest);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//6. print the status code
		int statusCode = response.getStatusLine().getStatusCode();
		System.out.println(statusCode);
		Assert.assertEquals(statusCode, 201);
		
		//7. get response body:
		HttpEntity responseEntity = response.getEntity();
		String responseBody = null;
		try {
			responseBody = EntityUtils.toString(responseEntity);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(responseBody);
		
		//8. get the id from the response
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(responseBody);
		List<Object> result = JsonPath.read(document, "$..id");
		System.out.println("user id is : "+ result.get(0));
	}
}
