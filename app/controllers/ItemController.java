package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.list;

/**
 * Created by avrj on 22.3.2015.
 */
public class ItemController extends Controller {
    @Security.Authenticated(Secured.class)
    public static Result all() {
        return ok(list.render(""));
    }

    public static Result show(Long id) {
        return ok(list.render(""));
    }
}
