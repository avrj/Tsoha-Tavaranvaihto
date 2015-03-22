package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import static play.mvc.Controller.flash;

/**
 * Created by avrj on 22.3.2015.
 */
public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        flash("error", "Pääsy estetty. Kirjaudu ensin sisään.");

        return redirect(routes.SessionController.index());
    }
}