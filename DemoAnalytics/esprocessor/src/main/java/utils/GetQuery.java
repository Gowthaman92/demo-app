package utils;

public class GetQuery {

	public static String getQuery(String queryName) {
		
		String customerAndCountryAggregate = "{\"size\":0,\"aggs\":{\"country\":{\"terms\":{\"field\":\"country.country\"},\"aggs\":{\"customers\":{\"terms\":{\"field\":\"customer_id\",\"size\":5},\"aggs\":{\"total\":{\"sum\":{\"field\":\"amount\"}}}}}}}}";
		String countryAggregate = "{\"size\":0,\"aggs\":{\"country\":{\"terms\":{\"field\":\"country.country\"},\"aggs\":{\"total\":{\"sum\":{\"field\":\"amount\"}}}}}}";
		String totalAmount = "{\"size\":0,\"aggs\":{\"total\":{\"sum\":{\"field\":\"amount\"}}}}";
		
		if(queryName.equalsIgnoreCase("customerAndCountryAggregate")) {
			return customerAndCountryAggregate;
		}else if(queryName.equalsIgnoreCase("countryAggregate")) {
			return countryAggregate;
		}else if(queryName.equalsIgnoreCase("totalAmount")){
			return totalAmount;
		}
		
		return "";
	}
	
	
	
}
