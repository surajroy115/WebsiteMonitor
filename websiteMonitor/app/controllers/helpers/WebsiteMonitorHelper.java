package controllers.helpers;

import play.libs.ws.*;
import play.libs.F.Promise;

import java.util.Date;

/**
 * Created by Lumian on 18.9.2015.
 */

public class WebsiteMonitorHelper {
	private static final WSClient ws = WS.client();

	public static void monitorWebsite(String url) {
		WSRequest request = ws.url(url);
		Promise<WSResponse> responsePromise = request.get();

		responsePromise.map(response ->
		{
			System.err.println(new Date() + " : " + url + " : Response status " + response.getStatus());
			return null;
		});
	}
}
