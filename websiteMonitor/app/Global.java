import com.fasterxml.jackson.databind.JsonNode;
import controllers.helpers.WebsiteMonitorHelper;
import models.Website;
import play.Application;
import play.GlobalSettings;
import play.api.Play;
import play.libs.Akka;
import play.libs.Json;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lumian on 18.9.2015.
 */
public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		FiniteDuration delay = FiniteDuration.create(0, TimeUnit.SECONDS);
		FiniteDuration frequency = FiniteDuration.create(10, TimeUnit.SECONDS);

		parseWebsites();
		List<Website> websites = Website.findAll();

		Runnable showTime = new Runnable() {
			@Override
			public void run() {
				for (Website website : websites) {
					WebsiteMonitorHelper.monitorWebsite(website.url);
				}
			}
		};

		Akka.system().scheduler().schedule(delay, frequency, showTime, Akka.system().dispatcher());
	}

	private static void parseWebsites() {
		String filePath = "data/websites.json";

		String fileContents = readFile(filePath, Charset.defaultCharset());
		JsonNode json = Json.parse(fileContents);

		for (JsonNode jsonNode : json) {
			Website website = new Website();
			website.url = jsonNode.findPath("url").textValue();
			website.save();
		}
	}

	private static String readFile(String filePath, Charset encoding) {
		try {
			return new String(Files.readAllBytes(Paths.get(filePath)), encoding);
		}
		catch (IOException e) {
			System.err.println("Global.java: IOException: " + e.getMessage());
		}

		return null;
	}

}
