package controllers;

import models.customer.Customer;
import models.Item;
import play.mvc.*;

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
