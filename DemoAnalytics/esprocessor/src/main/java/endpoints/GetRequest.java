package endpoints;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.json.simple.JSONObject;

import utils.ESOutputParser;
import utils.GetQuery;
import utils.Utils;

@Path("/v1/getQuery")
public class GetRequest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public static String getRequest(@QueryParam("index") String es_index, @QueryParam("query_name") String queryName) {

		return routeQuery(es_index, queryName);

	}

	public static String routeQuery(String index, String queryName) {
		String result = "";
		if (queryName.equalsIgnoreCase("customerAndCountryAggregate")) {

			String query = GetQuery.getQuery(queryName);
			String esOutput = queryES(index, query);
			result = ESOutputParser.parseCountryCustomerOutput(esOutput);

		} else if (queryName.equalsIgnoreCase("percentageForCountries")) {
			result = createPercentageResult();
		}

		return result;

	}
	
	public static String createPercentageResult() {
		String query1 = GetQuery.getQuery("countryAggregate");
		String query2 = GetQuery.getQuery("totalAmount");
		
		String aggregateESOutput = queryES("sales",query1);
		String amountESOutput = queryES("sales",query2);
		
		HashMap map = ESOutputParser.parseCountryAggregeJSON(aggregateESOutput);
		double totalAmount = ESOutputParser.parseTotalAmoutJSON(amountESOutput);
		
		return Utils.calculatePercentage(map, totalAmount);
	}

	public static String queryES(String index, String query) {
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget target = client.target("http://localhost:9200/sales/_search");
		String responseJSON = target.request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(query, MediaType.APPLICATION_JSON), String.class);

		return responseJSON;
	}
}
