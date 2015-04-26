package controllers;

import models.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * Created by avrj on 22.3.2015.
 */
public class ItemController extends Controller {

    public static Result all() {
        return ok(views.html.items.all.render(Item.getOpenItems()));
    }

    public static Result show(Long id) {
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

        Item item = Item.getItemById(id);
        if (item == null)
            return redirect(routes.ItemController.all());

        Logger.error(Long.toString(item.getCustomerId()));
        Customer customer = Customer.getCustomerById(item.getCustomerId());
        Category category = Category.getCategoryById(item.getCategoryId());

        CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
        List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);
        return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
    }

    @Security.Authenticated(Secured.class)
    public static Result edit(Long id) {
        Item item = Item.getItemById(id);

        if (item.getCustomerId() != Long.parseLong(session().get("customer_id"))) {
            flash("error", "Voit muokata vain omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        if (item == null)
            return redirect(routes.ItemController.all());

        Form<Item> newItemForm = Form.form(Item.class);

        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

        /* TODO: NewItemForm <-> Item */
        Form<Item> filledItemForm = newItemForm.fill(new Item(item.getTitle(), item.getDescription(), item.getVaihdossa(), item.getCategoryId()));

        return ok(views.html.items.edit.render(id, play.libs.Scala.toSeq(Category.getCategoriesAsScalaTupleList()), filledItemForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result update(Long id) {
        Item cur_item = Item.getItemById(id);

        if (cur_item == null)
            return redirect(routes.ItemController.all());

        if (cur_item.getCustomerId() != Long.parseLong(session().get("customer_id"))) {
            flash("error", "Voit muokata vain omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        Form<Item> itemForm = Form.form(Item.class).bindFromRequest();

        if (itemForm.hasErrors()) {
            return badRequest(views.html.items.edit.render(id, play.libs.Scala.toSeq(Category.getCategoriesAsScalaTupleList()), itemForm));
        } else {
            Item item = itemForm.get();
            if (item == null)
                return redirect(routes.ItemController.all());

            if (Item.updateItem(id, item.category_id, item.title, item.description, item.vaihdossa)) {
                flash("success", "Ilmoituksen tiedot on nyt päivitetty.");

                return redirect(routes.ItemController.show(id));
            } else {
                flash("error", "Ilmoituksen tietojen päivittäminen epäonnistui.");

                return badRequest(views.html.items.edit.render(id, play.libs.Scala.toSeq(Category.getCategoriesAsScalaTupleList()), itemForm));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result delete(Long id) {
        Item item = Item.getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        if (item.getCustomerId() != Long.parseLong(session().get("customer_id"))) {
            flash("error", "Voit poistaa vain omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        /*
            TODO: must belong to current user
         */
        if (Item.deleteItem(id)) {
            flash("success", "Ilmoitus poistettu.");

            return redirect(routes.ItemController.all());
        } else {
            flash("error", "Ilmoituksen poistaminen epäonnistui.");

            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);
            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result new_item() {
        Form<Item> newItemForm = Form.form(Item.class);

        return ok(views.html.items.add.render(play.libs.Scala.toSeq(Category.getCategoriesAsScalaTupleList()), newItemForm));
    }

    @Security.Authenticated(Secured.class)
    public static Result create() {
        Form<Item> newItemForm = Form.form(Item.class).bindFromRequest();

        if (newItemForm.hasErrors()) {
            return badRequest(views.html.items.add.render(play.libs.Scala.toSeq(Category.getCategoriesAsScalaTupleList()), newItemForm));
        } else {
            Item item = newItemForm.get();

            Item newItem = new Item(Long.parseLong(session().get("customer_id")), item.category_id, item.title, item.description, item.vaihdossa);

            if (newItem.save()) {
                flash("success", "Ilmoitus lisätty!");

                return redirect(routes.ItemController.all());
            } else {
                flash("error", "Ilmoituksen lisääminen epäonnistui.");

                return badRequest(views.html.items.add.render(play.libs.Scala.toSeq(Category.getCategoriesAsScalaTupleList()), newItemForm));
            }
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result lock(Long id) {
        Item item = Item.getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        if (item.getCustomerId() == Long.parseLong(session().get("customer_id"))) {
            flash("error", "Et voi lukita omia ilmoituksiasi.");
            return redirect(routes.ItemController.show(id));
        }

        /*
            TODO: customer cannot lock item that is already locked
         */
        if (Item.lockItem(id, Long.parseLong(session().get("customer_id")))) {
            flash("success", "Ilmoitus lukittu.");

            return redirect(routes.ItemController.show(id));
        } else {
            flash("error", "Ilmoituksen lukitseminen epäonnistui.");

            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);
            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result unlock(Long id) {
        Item item = Item.getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        /*
            TODO: customer cannot unlock item that belongs to another customer
         */
        if (Item.unlockItem(id)) {
            flash("success", "Ilmoituksen lukitus poistettu.");

            return redirect(routes.ItemController.show(id));
        } else {
            flash("error", "Ilmoituksen lukituksen poistaminen epäonnistui.");

            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);
            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result acceptOffer(Long id) {
        Item item = Item.getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        if (Item.acceptOffer(id)) {
            flash("success", "Tarjous hyväksytty.");

            return redirect(routes.ItemController.show(id));
        } else {
            flash("error", "Tarjouksen hyväksyminen epäonnistui.");

            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);
            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    public static void deleteExpiredOffers() {
        Item.deleteExpiredOffers();
    }
}
