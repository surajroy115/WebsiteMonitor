package models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.persistence.Column;
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
