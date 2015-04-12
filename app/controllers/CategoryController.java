package controllers;

import models.Categories;
import models.CategoryForm;
import models.Item;
import models.Items;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.categories_list;
import views.html.new_category;
import views.html.show_category;

import java.util.List;

/**
 * Created by avrj on 22.3.2015.
 */
public class CategoryController extends Controller {
    private static Categories categories = new Categories();

    public static Result all() {
        List<Item> items = new Items().getItems();

        return ok(show_category.render(items, categories));
    }

    public static Result show(Long id) {
        List<Item> items = new Items().getItemsByCategoryId(id);

        if(items.size() == 0)
            return redirect(routes.ItemController.all());

        return ok(show_category.render(items, categories));
    }

    @Security.Authenticated(Secured.class)
    public static Result new_category() {
        Form<CategoryForm> newCategoryForm = Form.form(CategoryForm.class);

        return ok(new_category.render(newCategoryForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result create() {
        Form<CategoryForm> newCategoryForm = Form.form(CategoryForm.class).bindFromRequest();

        if (newCategoryForm.hasErrors()) {
            return badRequest(new_category.render(newCategoryForm));
        } else {
            CategoryForm category = newCategoryForm.get();

            int categoryStatus = categories.createCategory(category.title);

            if(categoryStatus > 0) {
                flash("success", "Kategoria lisätty!");

                return redirect(routes.CategoryController.all());
            } else {
                flash("error", "Kategorian lisääminen epäonnistui.");

                return badRequest(new_category.render(newCategoryForm));
            }
        }
    }
}
