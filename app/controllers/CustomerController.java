package controllers;

import models.RegisterForm;
import models.Customers;
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
            return ok("Got user " + customer);
        }
/*
        DynamicForm requestData = Form.form().bindFromRequest();

        String username = requestData.get("username");
        String email = requestData.get("email");
        String password = requestData.get("password");
        String password_again = requestData.get("password_again");

        if(email.length() < 5) {
            return ok(register.render("Sähköpostiosoitteen tulee olla vähintään 5 merkkiä pitkä.", email, username));
        } else if(username.length() < 3) {
            return ok(register.render("Käyttäjätunnuksen tulee olla vähintään 3 merkkiä pitkä.", email, username));
        }  else if(!password.equals(password_again)) {
            return ok(register.render("Salasanat eivät täsmää.", email, username));
        } else if(password.length() < 8) {
            return ok(register.render("Salasanan tulee olla vähintään 8 merkkiä pitkä.", email, username));
        }

        if(customers.createCustomer(username, email, password) > 0) {
            flash("success", "Käyttäjätilisi on nyt luotu. Voit nyt kirjautua sisään alla olevalla lomakkeella.");

            return redirect("/login");
        } else {
            return ok(register.render("Käyttäjätilin luominen epäonnistui", email, username));
        }*/
    }
}
