package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * Created by avrj on 22.3.2015.
 */
public class CategoryController extends Controller {
    public static Result all() {
        List<Item> items = Item.getOpenItems();

        return ok(views.html.categories.show.render(items, null));
    }

    public static Result show(Long id) {
        List<Item> items = Item.getOpenItemsByCategoryId(id);

        if (items.size() == 0)
            return redirect(routes.ItemController.all());

        return ok(views.html.categories.show.render(items, Category.getCategoryById(id)));
    }

    @Security.Authenticated(Secured.class)
    public static Result new_category() {
        Form<Category> newCategoryForm = Form.form(Category.class);

        return ok(views.html.categories.add.render(newCategoryForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result create() {
        Form<Category> newCategoryForm = Form.form(Category.class).bindFromRequest();

        if (newCategoryForm.hasErrors()) {
            return badRequest(views.html.categories.add.render(newCategoryForm));
        } else {
            Category category = newCategoryForm.get();

            if (category.save()) {
                flash("success", "Kategoria lisätty!");

                return redirect(routes.CategoryController.all());
            } else {
                flash("error", "Kategorian lisääminen epäonnistui.");

                return badRequest(views.html.categories.add.render(newCategoryForm));
            }
        }
    }
}
