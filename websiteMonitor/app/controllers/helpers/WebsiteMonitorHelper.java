package controllers.helpers;

import models.Website;
import play.libs.ws.*;
import play.libs.F.Promise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lumian on 18.9.2015.
 */

public class WebsiteMonitorHelper {
	private static final WSClient ws = WS.client();

	public static void monitorWebsite(Website website) {
		WSRequest request = ws.url(website.url);
		Promise<WSResponse> responsePromise = request.get();
		Date requestSentDate = new Date();

		responsePromise.map(response ->
		{
			Date responseReceivedDate = new Date();
			long milliSecondsFromRequestToResponse = (responseReceivedDate.getTime() - requestSentDate.getTime());

			System.err.println("---- MONITORING RESULTS: " + website.url + " ----");

			System.err.println("OK: Request sent " + requestSentDate);
			System.err.println("OK: Response received " + responseReceivedDate);
			System.err.println("OK: Duration " + milliSecondsFromRequestToResponse + " milliseconds.");

			int status = response.getStatus();
			if (status == 200) {
				System.err.println("OK: Response status " + response.getStatus());
			}
			else {
				System.err.println("ERROR: Response status " + response.getStatus());
			}

			String pageBody = response.getBody();

			for (String contentRequirement : website.getContentRequirements()) {
				if (pageBody.contains(contentRequirement)) {
					System.err.println("OK: Webpage contains string '" + contentRequirement + "'.");
				}
				else {
					System.err.println("ERROR: The webpage does not contain string '" + contentRequirement +"'.");
				}
			}

			return null;
		});
	}
}
