package controllers;

import models.Customer;
import models.Item;
import play.data.validation.Constraints;
import play.db.DB;
import play.mvc.*;

import java.sql.*;

public class ApplicationController extends Controller {
    public static Result index() {
        return ok(views.html.index.render(Item.getLatestItems(5), Item.getMostPopularItems(5)));
    }

    public static String getCurrentCustomer() {
        if (session().get("customer_id").isEmpty())
            return null;

        Customer customer = Customer.getCustomerById(Long.parseLong(session().get("customer_id")));

        if (customer == null)
            return null;

        return customer.getUsername();
    }
}
