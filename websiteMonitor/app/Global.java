import controllers.helpers.WebsiteMonitorHelper;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.FiniteDuration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lumian on 18.9.2015.
 */
public class Global extends GlobalSettings {
	@Override
	public void onStart(Application application) {

		FiniteDuration delay = FiniteDuration.create(0, TimeUnit.SECONDS);
		FiniteDuration frequency = FiniteDuration.create(10, TimeUnit.SECONDS);

		Runnable showTime = new Runnable() {
			@Override
			public void run() {
				WebsiteMonitorHelper.monitorWebsite("http://www.google.fi/");
			}
		};

		Akka.system().scheduler().schedule(delay, frequency, showTime, Akka.system().dispatcher());
	}

}
