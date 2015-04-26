package controllers;

import models.Customers;
import models.LoginForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by avrj on 22.3.2015.
 */
public class SessionController extends Controller {
    private static Customers customers = new Customers();

    public static Result index() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class);

        return ok(views.html.sessions.login.render(loginForm));
    }

    public static Result login() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(views.html.sessions.login.render(loginForm));
        } else {
            LoginForm login_data = loginForm.get();

            int customer_id = customers.authenticate(login_data.username, login_data.password);
            if (customer_id > 0) {
                session("customer_id", Integer.toString(customer_id));

                flash("success", "Tervetuloa " + login_data.username);

                return redirect(routes.ApplicationController.index());
            } else {
                flash("error", "Kirjautuminen epäonnistui: Väärä käyttäjätunnus ja/tai salasana.");

                return badRequest(views.html.sessions.login.render(loginForm));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().remove("customer_id");

        flash("info", "Olet nyt kirjautunut ulos järjestelmästä.");

        return redirect(routes.SessionController.index());
    }
}
