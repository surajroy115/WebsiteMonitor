package controllers.helpers;

import models.Website;
import play.Logger;
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

			Logger.info("---- MONITORING RESULTS: " + website.url + " ----");

			Logger.info("Request sent " + requestSentDate);
			Logger.info("Response received " + responseReceivedDate);
			Logger.info("Duration " + milliSecondsFromRequestToResponse + " milliseconds.");

			int status = response.getStatus();
			website.latestStatusCode = status;
			if (status == 200) {
				Logger.info("Response status: " + response.getStatus() + " " + response.getStatusText());
			}
			else {
				Logger.error("Response status: " + response.getStatus() + " " + response.getStatusText());
			}

			String pageBody = response.getBody();
			boolean isContentStatusOK = true;

			for (String contentRequirement : website.getContentRequirements()) {
				if (pageBody.contains(contentRequirement)) {
					String message = "OK: Webpage contains string '" + contentRequirement + "'.";
					Logger.info(message);
				}
				else {
					String message = "ERROR: The webpage does not contain string '" + contentRequirement +"'.";
					Logger.error(message);
					isContentStatusOK = false;
				}
			}

			website.isLatestContentStatusOk = isContentStatusOK;
			website.save();

			return null;
		});
	}
}
