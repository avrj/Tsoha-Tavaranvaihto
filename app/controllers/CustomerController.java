package controllers;

import models.RegisterForm;
import models.Customers;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register;

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

    public static Result register() {
        Form<RegisterForm> customerForm = Form.form(RegisterForm.class).bindFromRequest();

        if (customerForm.hasErrors()) {
            return badRequest(register.render("", null, null, customerForm));
        } else {
            RegisterForm customer = customerForm.get();

            int registerStatus = customers.createCustomer(customer.username, customer.email, customer.password);

            return ok(customers.getCustomers().size() + " " + Integer.toString(registerStatus));
            /*
            if(registerStatus > 0) {
                flash("success", "Käyttäjätilisi on nyt luotu. Voit nyt kirjautua sisään alla olevalla lomakkeella.");

                return redirect("/login");
            } else {
                return ok(register.render("Käyttäjätilin luominen epäonnistui", customer.email, customer.username, customerForm));
            }*/
        }
    }
}
