package controllers;

import models.Categories;
import models.CategoryForm;
import models.Item;
import models.Items;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * Created by avrj on 22.3.2015.
 */
public class CategoryController extends Controller {
    private static Categories categories = new Categories();

    public static Result all() {
        List<Item> items = new Items().getOpenItems();

        return ok(views.html.categories.show.render(items, categories, null));
    }

    public static Result show(Long id) {
        List<Item> items = new Items().getOpenItemsByCategoryId(id);

        if (items.size() == 0)
            return redirect(routes.ItemController.all());

        return ok(views.html.categories.show.render(items, categories, categories.getCategoryById(id)));
    }

    @Security.Authenticated(Secured.class)
    public static Result new_category() {
        Form<CategoryForm> newCategoryForm = Form.form(CategoryForm.class);

        return ok(views.html.categories.add.render(newCategoryForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result create() {
        Form<CategoryForm> newCategoryForm = Form.form(CategoryForm.class).bindFromRequest();

        if (newCategoryForm.hasErrors()) {
            return badRequest(views.html.categories.add.render(newCategoryForm));
        } else {
            CategoryForm category = newCategoryForm.get();

            int categoryStatus = categories.createCategory(category.title);

            if (categoryStatus > 0) {
                flash("success", "Kategoria lisätty!");

                return redirect(routes.CategoryController.all());
            } else {
                flash("error", "Kategorian lisääminen epäonnistui.");

                return badRequest(views.html.categories.add.render(newCategoryForm));
            }
        }
    }
}
