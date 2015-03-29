package controllers;

import models.Categories;
import models.CategoryForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.categories_list;
import views.html.new_category;

/**
 * Created by avrj on 22.3.2015.
 */
public class CategoryController extends Controller {
    private static Categories categories = new Categories();

    public static Result all() {
        return ok(categories_list.render(categories.getCategories()));
    }

    public static Result show(Long id) {
        return ok(categories_list.render(categories.getCategories()));
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
