package controllers;

import models.Website;
import play.mvc.*;
import views.html.*;

import java.util.List;

public class Application extends Controller {
	public Result index() {
		List<Website> websites = Website.findAll();
		return ok(index.render(websites));
	}
}
