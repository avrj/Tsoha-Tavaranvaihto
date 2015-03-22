package controllers;

import models.Customer;
import play.data.validation.Constraints;
import play.db.DB;
import play.mvc.*;

import views.html.*;

import java.sql.*;

public class ApplicationController extends Controller {
    public static Result index() {
        return ok(index.render(""));
    }
}
