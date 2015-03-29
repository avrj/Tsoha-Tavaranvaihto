package controllers;

import models.*;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.customer_items;
import views.html.register;
import views.html.show_customer;

/**
 * Created by avrj on 22.3.2015.
 */
public class CustomerController extends Controller {
    private static Customers customers = new Customers();

    public static Result index() {
        Form<RegisterForm> customerForm = Form.form(RegisterForm.class);


        return ok(register.render("", null, null, customerForm));
    }

    public static Result all() {
        return ok(Integer.toString(customers.getCustomers().size()));
    }

    public static Result show(Long id) {
        Customer customer = customers.getCustomerById(id);

        if(customer == null)
            return redirect(routes.ItemController.all());

        Items items = new Items();

        return ok(show_customer.render(items.getItemsByCustomerId(Integer.parseInt(session().get("customer_id"))), customer));
    }

    public static Result items() {
        Items items = new Items();
        Categories categories = new Categories();

        return ok(customer_items.render(items.getItemsByCustomerId(Integer.parseInt(session().get("customer_id"))), categories.getCategories()));
    }

    public static Result register() {
        Form<RegisterForm> customerForm = Form.form(RegisterForm.class).bindFromRequest();

        if (customerForm.hasErrors()) {
            return badRequest(register.render("", null, null, customerForm));
        } else {
            RegisterForm customer = customerForm.get();

            int registerStatus = customers.createCustomer(customer.username, customer.email, customer.password);

            if(registerStatus > 0) {
                flash("success", "Käyttäjätilisi on nyt luotu. Voit nyt kirjautua sisään alla olevalla lomakkeella.");
                flash("username", customer.username);

                return redirect(routes.SessionController.index());
            } else {
                /*
                    TODO: flash
                 */
                return badRequest(register.render("Käyttäjätilin luominen epäonnistui", customer.email, customer.username, customerForm));
            }
        }
    }

    public static Result counterOffers() {
        return ok("TBA");
    }
}
