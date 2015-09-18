package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lumian on 18.9.2015.
 */

@Entity
public class Website extends Model {
	@Id
	public long id;

	public String url;

	public int latestStatusCode;

	public boolean isLatestContentStatusOk;

	@OneToMany(mappedBy = "website")
	public List<ContentRequirement> contentRequirements = new ArrayList<>();

	public static List<Website> findAll() {
		return Ebean.find(Website.class).findList();
	}

	public List<String> getContentRequirements() {
		List<String> contentRequirementStrings = new ArrayList<>();

		for (ContentRequirement requirement : contentRequirements) {
			contentRequirementStrings.add(requirement.requiredText);
		}

		return contentRequirementStrings;
	}
}
