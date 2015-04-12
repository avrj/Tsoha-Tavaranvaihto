package controllers;

import models.*;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.my_items;
import views.html.edit_customer;
import views.html.register;
import views.html.show_customer;
import views.html.my_counteroffers;

/**
 * Created by avrj on 22.3.2015.
 */
public class CustomerController extends Controller {
    private static Customers customers = new Customers();

    public static Result index() {
        Form<RegisterForm> customerForm = Form.form(RegisterForm.class);


        return ok(register.render("", null, null, customerForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result all() {
        return ok(Integer.toString(customers.getCustomers().size()));
    }

    public static Result show(Long id) {
        Customer customer = customers.getCustomerById(id);

        if(customer == null)
            return redirect(routes.ItemController.all());

        Items items = new Items();
        Categories categories = new Categories();
        return ok(show_customer.render(items.getItemsByCustomerId(id), customer, categories));
    }

    @Security.Authenticated(Secured.class)
    public static Result items() {
        Items items = new Items();
        Categories categories = new Categories();

        return ok(my_items.render(items.getItemsByCustomerId(Long.parseLong(session().get("customer_id"))), categories));
    }

    @Security.Authenticated(Secured.class)
    public static Result edit() {
        Customer customer = customers.getCustomerById(Long.parseLong(session().get("customer_id")));

        Form<ChangePasswordForm> changePasswordForm = Form.form(ChangePasswordForm.class);

        return ok(edit_customer.render(customer, changePasswordForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result update() {
        Form<ChangePasswordForm> changePasswordForm = Form.form(ChangePasswordForm.class).bindFromRequest();

        Customer customer = customers.getCustomerById(Long.parseLong(session().get("customer_id")));

        if (changePasswordForm.hasErrors()) {
            return badRequest(edit_customer.render(customer, changePasswordForm));
        } else {
            ChangePasswordForm changePassword = changePasswordForm.get();
            int customer_id = customers.authenticate(customer.getUsername(), changePassword.current_password);

            if(customer_id > 0) {
                int customerStatus = customers.changePassword(Long.parseLong(session().get("customer_id")), changePassword.new_password);

                if (customerStatus > 0) {
                    flash("success", "Salasana on nyt vaihdettu.");

                    return redirect(routes.CustomerController.edit());
                } else {
                    flash("error", "Salasanan vaihtaminen epäonnistui.");

                    return badRequest(edit_customer.render(customer, changePasswordForm));
                }
            } else {
                flash("error", "Salasanan vaihtaminen epäonnistui: Väärä salasana syötetty.");

                return badRequest(edit_customer.render(customer, changePasswordForm));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result delete() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String password = requestData.get("password");

        Customer customer = customers.getCustomerById(Long.parseLong(session().get("customer_id")));

        int customer_id = customers.authenticate(customer.getUsername(), password);
        if(customer_id > 0) {
            int customerStatus = customers.deleteCustomer(Long.parseLong(session().get("customer_id")));

            if(customerStatus > 0) {
                session().remove("customer_id");

                flash("info", "Käyttäjätilisi on nyt poistettu.");

                return redirect(routes.SessionController.index());
            } else {
                flash("error", "Käyttäjätilin poistaminen epäonnistui.");
                return redirect(routes.CustomerController.edit());
            }
        } else {
            flash("error", "Käyttäjätilin poistaminen epäonnistui: Väärä salasana syötetty.");

            Form<ChangePasswordForm> changePasswordForm = Form.form(ChangePasswordForm.class).bindFromRequest();
            return badRequest(edit_customer.render(customer, changePasswordForm));
        }
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
        Items items = new Items();

        return ok(my_counteroffers.render(items.getItemsForCustomerCounterOffersByCustomerId(Long.parseLong(session().get("customer_id")))));
    }
}
