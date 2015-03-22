package controllers;

import models.LoginForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.login;

/**
 * Created by avrj on 22.3.2015.
 */
public class SessionController extends Controller {
    public static Result index() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class);

        return ok(login.render(loginForm));
    }

    public static Result login() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            LoginForm login = loginForm.get();

            session("username", login.username);
            return redirect(routes.ApplicationController.index());
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result logout() {
        session().remove("username");

        flash("info", "Olet nyt kirjautunut ulos järjestelmästä.");

        return redirect(routes.SessionController.index());
    }
}
