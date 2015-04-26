package controllers;

import models.*;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by avrj on 22.3.2015.
 */
public class CustomerController extends Controller {
    public static Result new_customer() {
        Form<RegisterForm> customerForm = Form.form(RegisterForm.class);


        return ok(views.html.customers.add.render("", null, null, customerForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result all() {
        return ok(Integer.toString(Customer.getCustomers().size()));
    }

    public static Result show(Long id) {
        Customer customer = Customer.getCustomerById(id);

        if (customer == null)
            return redirect(routes.ItemController.all());

        Items items = new Items();
        return ok(views.html.customers.show.render(items.getOpenItemsByCustomerId(id), customer));
    }

    @Security.Authenticated(Secured.class)
    public static Result items() {
        Items items = new Items();

        return ok(views.html.customers.items.render(items.getItemsOpenByCustomerId(Long.parseLong(session().get("customer_id"))), items.getItemsAcceptedByCustomerId(Long.parseLong(session().get("customer_id")))));
    }

    @Security.Authenticated(Secured.class)
    public static Result edit() {
        Customer customer = Customer.getCustomerById(Long.parseLong(session().get("customer_id")));

        Form<ChangePasswordForm> changePasswordForm = Form.form(ChangePasswordForm.class);

        return ok(views.html.customers.edit.render(customer, changePasswordForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result update() {
        Form<ChangePasswordForm> changePasswordForm = Form.form(ChangePasswordForm.class).bindFromRequest();

        Customer customer = Customer.getCustomerById(Long.parseLong(session().get("customer_id")));

        if (changePasswordForm.hasErrors()) {
            return badRequest(views.html.customers.edit.render(customer, changePasswordForm));
        } else {
            ChangePasswordForm changePassword = changePasswordForm.get();
            int customer_id = Customer.authenticate(customer.getUsername(), changePassword.current_password);

            if (customer_id > 0) {
                if (Customer.changePassword(Long.parseLong(session().get("customer_id")), changePassword.new_password)) {
                    flash("success", "Salasana on nyt vaihdettu.");

                    return redirect(routes.CustomerController.edit());
                } else {
                    flash("error", "Salasanan vaihtaminen epäonnistui.");

                    return badRequest(views.html.customers.edit.render(customer, changePasswordForm));
                }
            } else {
                flash("error", "Salasanan vaihtaminen epäonnistui: Väärä salasana syötetty.");

                return badRequest(views.html.customers.edit.render(customer, changePasswordForm));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result delete() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String password = requestData.get("password");

        Customer customer = Customer.getCustomerById(Long.parseLong(session().get("customer_id")));

        int customer_id = Customer.authenticate(customer.getUsername(), password);
        if (customer_id > 0) {
            if (Customer.deleteCustomer(Long.parseLong(session().get("customer_id")))) {
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
            return badRequest(views.html.customers.edit.render(customer, changePasswordForm));
        }
    }

    public static Result create() {
        Form<RegisterForm> customerForm = Form.form(RegisterForm.class).bindFromRequest();

        if (customerForm.hasErrors()) {
            return badRequest(views.html.customers.add.render("", null, null, customerForm));
        } else {
            RegisterForm customer = customerForm.get();

            Customer new_customer = new Customer(customer.username, customer.email, customer.password);
            if (new_customer.save()) {
                flash("success", "Käyttäjätilisi on nyt luotu. Voit nyt kirjautua sisään alla olevalla lomakkeella.");
                flash("username", customer.username);

                return redirect(routes.SessionController.index());
            } else {
                /*
                    TODO: flash
                 */
                return badRequest(views.html.customers.add.render("Käyttäjätilin luominen epäonnistui", customer.email, customer.username, customerForm));
            }
        }
    }

    public static Result counterOffers() {
        return ok(views.html.customers.counteroffers.render(new Items().
                getItemsOpenForCustomerCounterOffersByCustomerId(Long.parseLong(session().get("customer_id"))),
                new Items().
                        getItemsAcceptedForCustomerCounterOffersByCustomerId(Long.parseLong(session().get("customer_id"))),
                new Items().
                        getItemsClosedForCustomerCounterOffersByCustomerId(Long.parseLong(session().get("customer_id")))));
    }

    public static Long getLockedItemsCount() {
        return Customer.getLockedItemsCountByCustomerId(Long.parseLong(session().get("customer_id")));
    }

    public static Result offers() {
        return ok(views.html.customers.offers.render(new Items().getItemsOpenForCustomerOffersByCustomerId(Long.parseLong(session().get("customer_id"))), new Items().getItemsAcceptedForCustomerOffersByCustomerId(Long.parseLong(session().get("customer_id")))));
    }
}
