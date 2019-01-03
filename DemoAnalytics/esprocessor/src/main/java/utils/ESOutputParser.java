package utils;

import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ESOutputParser {

	static JSONParser parser = new JSONParser();

	public static String parseESOutput(String esOutput) {

		return parseCountryCustomerOutput(esOutput);
	}

	public static String parseCountryCustomerOutput(String esOutput) {

		JSONArray outputArray = new JSONArray();

		try {
			JSONObject outputJSON = (JSONObject) parser.parse(esOutput);
			JSONObject aggregationsJSON = (JSONObject) outputJSON.get("aggregations");

			JSONObject countryJSON = (JSONObject) aggregationsJSON.get("country");

			JSONArray countriesArray = (JSONArray) countryJSON.get("buckets");

			countriesArray.forEach(jsonObject -> {
				JSONObject countries = (JSONObject) jsonObject;
				String country = (String) countries.get("key");
				JSONObject customersAggregateJSON = (JSONObject) countries.get("customers");

				JSONObject customersOutputJSON = new JSONObject();

				JSONArray customersArray = (JSONArray) customersAggregateJSON.get("buckets");

				customersArray.forEach(customer -> {
					JSONObject customerObject = (JSONObject) customer;
					long customerID = (long) customerObject.get("key");

					JSONObject totalValueJSON = (JSONObject) customerObject.get("total");
					double purchase_amount = (double) totalValueJSON.get("value");

					customersOutputJSON.put("country", country);
					customersOutputJSON.put("customer_id", customerID);
					customersOutputJSON.put("purchase_amount", purchase_amount);

					outputArray.add(customersOutputJSON);
				});

			});

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return outputArray.toJSONString();

	}

	public static HashMap<String, Double> parseCountryAggregeJSON(String esOutput) {

		HashMap<String,Double> outputMap = new HashMap<String,Double>();

		try {
			JSONObject outputJSON = (JSONObject) parser.parse(esOutput);
			JSONObject aggregationsJSON = (JSONObject) outputJSON.get("aggregations");

			JSONObject countryJSON = (JSONObject) aggregationsJSON.get("country");

			JSONArray countriesArray = (JSONArray) countryJSON.get("buckets");

			countriesArray.forEach(jsonObject -> {
				JSONObject countriesJSON = (JSONObject) jsonObject;
				String country = (String) countriesJSON.get("key");
				JSONObject totalJSON = (JSONObject) countriesJSON.get("total");
				
				outputMap.put(country, (Double) totalJSON.get("value"));
				
			});
		} catch (Exception e) {

		}
		return outputMap;
	}
	
	public static double parseTotalAmoutJSON(String esOutput) {
		JSONArray outputArray = new JSONArray();
		double amount = 0;
		try {
			JSONObject outputJSON = (JSONObject) parser.parse(esOutput);
			JSONObject aggregationsJSON = (JSONObject) outputJSON.get("aggregations");
			JSONObject totalJSON = (JSONObject) aggregationsJSON.get("total");
			amount = (double) totalJSON.get("value");
		}
		catch(Exception e) {
			
		}
		
		return amount;
	
	}

}
