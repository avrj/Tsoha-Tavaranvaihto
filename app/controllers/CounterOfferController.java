package controllers;

import models.*;
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
        Item item = new Items().getItemById(id);

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

        int counterOfferStatus = new CounterOffers().createCounterOffer(Long.parseLong(session().get("customer_id")), id, requestData.get("counteroffer_description"));

        if (counterOfferStatus > 0) {
            flash("success", "Vastatarjous lisätty onnistuneesti.");
            return redirect(routes.ItemController.show(id));
        } else {
            Customer customer = new Customers().getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = new CounterOffers().getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = new CounterOffers().getCounterOffersForItem(id);

            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result delete(Long id) {
        Item item = new Items().getItemById(id);

        if (item == null)
            return redirect(routes.ItemController.all());

        if (item.getCustomerId() == Long.parseLong(session().get("customer_id"))) {
            flash("error", "Et voi tehdä vastatarjouksia omiin ilmoituksiisi.");
            return redirect(routes.ItemController.show(id));
        }

        int counterOfferStatus = new CounterOffers().deleteCounterOffer(id, Long.parseLong(session().get("customer_id")));

        if (counterOfferStatus > 0) {
            flash("success", "Vastatarjous poistettu onnistuneesti.");
            return redirect(routes.ItemController.show(id));
        } else {
            Customer customer = new Customers().getCustomerById(item.getCustomerId());
            Category category = Category.getCategoryById(item.getCategoryId());
            CounterOffer currentCustomerCounterOffer = new CounterOffers().getCounterOfferForItemByCustomerId(id, Long.parseLong(session().get("customer_id")));
            List<CounterOffer> counterOffers = new CounterOffers().getCounterOffersForItem(id);

            return ok(views.html.items.show.render(id, item, customer, category, currentCustomerCounterOffer, counterOffers));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result accept(Long item_id) {
        DynamicForm requestData = Form.form().bindFromRequest();
        Long customer_id = Long.parseLong(requestData.get("customer_id"));

        return ok();
    }
}
