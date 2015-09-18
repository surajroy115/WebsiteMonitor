package controllers.helpers;

import models.Website;
import play.libs.ws.*;
import play.libs.F.Promise;

import java.util.Date;

/**
 * Created by Lumian on 18.9.2015.
 */

public class WebsiteMonitorHelper {
	private static final WSClient ws = WS.client();

	public static void monitorWebsite(Website website) {
		WSRequest request = ws.url(website.url);
		Promise<WSResponse> responsePromise = request.get();

		responsePromise.map(response ->
		{
			System.err.println(new Date() + " : " + website.url + " : Response status " + response.getStatus());

			for (String contentRequirement : website.getContentRequirements()) {
				System.err.println("requirement: " + contentRequirement);
			}
			return null;
		});
	}
}
