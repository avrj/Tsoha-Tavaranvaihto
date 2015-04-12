package controllers;

import models.Categories;
import models.Customer;
import models.Customers;
import models.Items;
import play.data.validation.Constraints;
import play.db.DB;
import play.mvc.*;

import views.html.*;

import java.sql.*;

public class ApplicationController extends Controller {
    private static Items items = new Items();
    public static Result index() {
        Categories categories = new Categories();

        // 2nd attribute should be items.getMostPopularItems(5)
        return ok(index.render(items.getLatestItems(5), items.getLatestItems(5), categories));
    }

    public static String getCurrentCustomer() {
        if(session().get("customer_id").isEmpty())
            return null;

        Customer customer = new Customers().getCustomerById(Long.parseLong(session().get("customer_id")));

        if(customer == null)
            return null;

        return customer.getUsername();
    }
}
