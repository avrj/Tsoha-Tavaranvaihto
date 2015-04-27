package controllers;

import models.*;
import models.customer.Customer;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * Created by avrj on 9.4.2015.
 */
public class CounterOfferController extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result add(Long id) {
        Item item = Item.getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        if (item.getCustomerId() == Long.parseLong(session().get("customer_id"))) {
            flash("error", "Et voi tehdä vastatarjouksia omiin ilmoituksiisi.");
            return redirect(routes.ItemController.show(id));
        }

        DynamicForm requestData = Form.form().bindFromRequest();

        if(requestData.get("counteroffer_description") == "") {
            flash("error", "Vastatarjous ei voi olla tyhjä.");
            return redirect(routes.ItemController.show(id));
        }

        CounterOffer counterOffer = new CounterOffer(Long.parseLong(session().get("customer_id")), id, requestData.get("counteroffer_description"));

        if (counterOffer.save()) {
            flash("success", "Vastatarjous lisätty onnistuneesti.");
            return redirect(routes.ItemController.show(id));
        } else {
            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);

            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result delete(Long id) {
        Item item = Item.getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        if (item.getCustomerId() == Long.parseLong(session().get("customer_id"))) {
            flash("error", "Et voi tehdä vastatarjouksia omiin ilmoituksiisi.");
            return redirect(routes.ItemController.show(id));
        }

        if (CounterOffer.deleteCounterOffer(id, Long.parseLong(session().get("customer_id")))) {
            flash("success", "Vastatarjous poistettu onnistuneesti.");
            return redirect(routes.ItemController.show(id));
        } else {
            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(id);

            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result accept(Long item_id) {
        Item item = Item.getItemById(item_id);

        if (item == null)
            return redirect(routes.ItemController.all());

        DynamicForm requestData = Form.form().bindFromRequest();
        Long customer_id = Long.parseLong(requestData.get("customer_id"));

        if (CounterOffer.acceptCounterOffer(item_id, customer_id)) {
            flash("success", "Vastatarjous hyväksytty.");

            return redirect(routes.ItemController.show(item_id));
        } else {
            flash("error", "Vastatarjouksen hyväksyminen epäonnistui.");

            Customer customer = Customer.getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = CounterOffer.getCounterOfferForItemByCustomerId(item_id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = CounterOffer.getCounterOffersForItem(item_id);
            return ok(views.html.items.show.render(item_id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }
}
