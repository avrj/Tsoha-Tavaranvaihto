package controllers;

import models.*;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.List;

/**
 * Created by avrj on 22.3.2015.
 */
public class ItemController extends Controller {
    private static Items items = new Items();
    private static Categories categories = new Categories();

    public static Result all() {
        return ok(items_list.render(items.getItems(), categories));
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

        CounterOffer currentCustomerCounterOffer = new CounterOffers().getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
        List<CounterOffer> counterOffers = new CounterOffers().getCounterOffersForItem(id);
        return ok(show_item.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
    }

    @Security.Authenticated(Secured.class)
    public static Result edit(Long id) {
        Item item = items.getItemById(id);

        if(item.getCustomerId() != Long.parseLong(session().get("customer_id"))) {
            flash("error", "Voit muokata vain omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

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
        Item cur_item = items.getItemById(id);

        if(cur_item == null)
            return redirect(routes.ItemController.all());

        if(cur_item.getCustomerId() != Long.parseLong(session().get("customer_id"))) {
            flash("error", "Voit muokata vain omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

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
        Item item = items.getItemById(id);

        if(item == null)
            return redirect(routes.ItemController.all());

        if(item.getCustomerId() != Long.parseLong(session().get("customer_id"))) {
            flash("error", "Voit poistaa vain omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        /*
            TODO: must belong to current user
         */
        if(items.deleteItem(id) > 0) {
            flash("success", "Ilmoitus poistettu.");

            return redirect(routes.ItemController.all());
        } else {
            flash("error", "Ilmoituksen poistaminen epäonnistui.");

            Customer customer = new Customers().getCustomerById(item.getCustomerId());
            Category category = categories.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = new CounterOffers().getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = new CounterOffers().getCounterOffersForItem(id);
            return ok(show_item.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
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

    @Security.Authenticated(Secured.class)
    public static Result lock(Long id) {
        Item item = items.getItemById(id);

        if(item == null)
            return redirect(routes.ItemController.all());

        if(item.getCustomerId() == Long.parseLong(session().get("customer_id"))) {
            flash("error", "Et voi lukita omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        /*
            TODO: customer cannot lock item that is already locked
         */
        if(items.lockItem(id, Long.parseLong(session().get("customer_id"))) > 0) {
            flash("success", "Ilmoitus lukittu.");

            return redirect(routes.ItemController.show(id));
        } else {
            flash("error", "Ilmoituksen lukitseminen epäonnistui.");

            Customer customer = new Customers().getCustomerById(item.getCustomerId());
            Category category = categories.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = new CounterOffers().getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = new CounterOffers().getCounterOffersForItem(id);
            return ok(show_item.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result unlock(Long id) {
        Item item = items.getItemById(id);

        if(item == null)
            return redirect(routes.ItemController.all());

        if(item.getCustomerId() == Long.parseLong(session().get("customer_id"))) {
            flash("error", "Et voi lukita omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        /*
            TODO: customer cannot unlock item that belongs to another customer
         */
        if(items.unlockItem(id) > 0) {
            flash("success", "Ilmoituksen lukitus poistettu.");

            return redirect(routes.ItemController.show(id));
        } else {
            flash("error", "Ilmoituksen lukituksen poistaminen epäonnistui.");

            Customer customer = new Customers().getCustomerById(item.getCustomerId());
            Category category = categories.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = new CounterOffers().getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = new CounterOffers().getCounterOffersForItem(id);
            return ok(show_item.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }
}
