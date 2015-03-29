package controllers;

import models.*;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

/**
 * Created by avrj on 22.3.2015.
 */
public class ItemController extends Controller {
    private static Items items = new Items();
    private static Categories categories = new Categories();

    public static Result all() {
        return ok(items_list.render(items.getItems(), categories.getCategories()));
    }

    public static Result show(Long id) {
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

        Item item = items.getItemById(id);
        if(item == null)
            return redirect(routes.ItemController.all());

        Logger.error(Long.toString(item.getCustomerId()));
        Customer customer = new Customers().getCustomerById(item.getCustomerId());
        Category category = categories.getCategoryById(item.getCategoryId());

        return ok(show_item.render(item, customer, category));
    }

    @Security.Authenticated(Secured.class)
    public static Result edit(Long id) {
        /*
        TODO: must belong to current user
        */

        Item item = items.getItemById(id);
        if(item == null)
            return redirect(routes.ItemController.all());

        Form<ItemForm> newItemForm = Form.form(ItemForm.class);

        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

        /* TODO: NewItemForm <-> Item */
        Form<ItemForm> filledItemForm = newItemForm.fill(new ItemForm(item.getTitle(), item.getDescription(), item.getVaihdossa()));

        return ok(edit_item.render(id, play.libs.Scala.toSeq(categories.getCategoriesAsScalaTupleList()), filledItemForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result update(Long id) {
        /*
            TODO: must belong to current user
         */
        Form<ItemForm> itemForm = Form.form(ItemForm.class).bindFromRequest();

        if (itemForm.hasErrors()) {
            return badRequest(edit_item.render(id, play.libs.Scala.toSeq(categories.getCategoriesAsScalaTupleList()), itemForm));
        } else {
            ItemForm item = itemForm.get();
            if(item == null)
                return redirect(routes.ItemController.all());

            int itemStatus = items.updateItem(id, item.category, item.title, item.description, item.vaihdossa);

            if(itemStatus > 0) {
                flash("success", "Ilmoituksen tiedot on nyt päivitetty.");

                return redirect(routes.ItemController.show(id));
            } else {
                flash("error", "Ilmoituksen tietojen päivittäminen epäonnistui.");

                return badRequest(edit_item.render(id, play.libs.Scala.toSeq(categories.getCategoriesAsScalaTupleList()), itemForm));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result delete(Long id) {
        /*
            TODO: must belong to current user
         */
        if(items.deleteItem(id) > 0) {
            flash("success", "Ilmoitus poistettu.");

            return redirect(routes.ItemController.all());
        } else {
            flash("error", "Ilmoituksen poistaminen epäonnistui.");

            Item item = items.getItemById(id);
            if(item == null)
                return redirect(routes.ItemController.all());

            Customer customer = new Customers().getCustomerById(item.getCustomerId());
            Category category = categories.getCategoryById(item.getCategoryId());

            return badRequest(show_item.render(item, customer, category));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result new_item() {
        Form<ItemForm> newItemForm = Form.form(ItemForm.class);

        return ok(new_item.render(play.libs.Scala.toSeq(categories.getCategoriesAsScalaTupleList()), newItemForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result create() {
        Form<ItemForm> newItemForm = Form.form(ItemForm.class).bindFromRequest();

        if (newItemForm.hasErrors()) {
            return badRequest(new_item.render(play.libs.Scala.toSeq(categories.getCategoriesAsScalaTupleList()), newItemForm));
        } else {
            ItemForm item = newItemForm.get();

            int itemStatus = items.createItem(Integer.parseInt(session().get("customer_id")), item.category, item.title, item.description, item.vaihdossa);

            if(itemStatus > 0) {
                flash("success", "Ilmoitus lisätty!");

                return redirect(routes.ItemController.all());
            } else {
                flash("error", "Ilmoituksen lisääminen epäonnistui.");

                return badRequest(new_item.render(play.libs.Scala.toSeq(categories.getCategoriesAsScalaTupleList()), newItemForm));
            }
        }
    }
}
