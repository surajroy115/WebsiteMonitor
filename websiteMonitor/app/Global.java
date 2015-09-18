import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.helpers.WebsiteMonitorHelper;
import models.ContentRequirement;
import models.Website;
import play.Application;
import play.GlobalSettings;
import play.Play;
import play.libs.Akka;
import play.libs.Json;
import scala.concurrent.duration.FiniteDuration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lumian on 18.9.2015.
 */
public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {
		// delay 0 seconds = start immediately
		FiniteDuration delay = FiniteDuration.create(0, TimeUnit.SECONDS);
		// monitor webpage frequency: monitor every 10 seconds
		FiniteDuration frequency = FiniteDuration.create(10, TimeUnit.SECONDS);

		// get websites that should be monitored:
		parseWebsites();
		List<Website> websites = Website.findAll();

		Runnable websiteMonitorRunnable = new Runnable() {
			@Override
			public void run() {
				// monitor all websites:
				websites.forEach(WebsiteMonitorHelper::monitorWebsite);
			}
		};

		// run the code inside the above run method of websiteMonitorRunnable every [frequence] seconds:
		Akka.system().scheduler().schedule(delay, frequency, websiteMonitorRunnable, Akka.system().dispatcher());
	}

	private static void parseWebsites() {
		// get website JSON file path from application.conf:
		String filePath = Play.application().configuration().getString("website.json.filepath");

		String fileContents = readFile(filePath, Charset.defaultCharset());
		JsonNode json = Json.parse(fileContents);

		// parse JSON and create Website model objects
		for (JsonNode jsonNode : json) {
			List<String> requirements = new ArrayList<>();

			Website website = new Website();
			website.url = jsonNode.findPath("url").textValue();

			// read content requirements into a list of strings:
			if (jsonNode.findPath("contentRequirements").isArray()) {
				for (JsonNode requirementNode : jsonNode.findPath("contentRequirements")) {
					requirements.add(requirementNode.asText());
				}
			}

			// save website model object
			website.save();

			// requirement model objects can be created now when the website model object exists:
			for (String requirement : requirements) {
				ContentRequirement contentRequirement = new ContentRequirement();
				contentRequirement.requiredText = requirement;
				contentRequirement.website = website;
				contentRequirement.save();
			}
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