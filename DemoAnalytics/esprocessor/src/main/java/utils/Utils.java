package utils;

import java.util.HashMap;

import org.json.simple.JSONObject;

public class Utils {

	public static String calculatePercentage(HashMap map,double totalAmount) {
		map.forEach((country,amount) -> {
			
			double percentage = (double ) amount/totalAmount *100.0;
			double roundOffPercent = Math.round(percentage*100.0)/100.0;
			map.put(country, roundOffPercent);
		});
		
		JSONObject outputJSON = new JSONObject(map);
		return outputJSON.toJSONString();
	}
	
	
}
