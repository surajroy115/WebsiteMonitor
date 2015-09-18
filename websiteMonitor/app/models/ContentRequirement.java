package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by Lumian on 18.9.2015.
 */

@Entity
public class ContentRequirement extends Model {
	@Id
	public long id;

	public String requiredText;

	@ManyToOne
	public Website website;
}
