package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by avrj on 27.3.2015.
 */
public class Item {
    private String title, description, vaihdossa;
    private Long item_id, category_id, customer_id, locked_customer_id, accepted_customer_id;
    private java.sql.Timestamp timestamp, locked_at_timestamp, accepted_offer_at;

    public Item(Long item_id, String title, String description, String vaihdossa, Long category_id, Long customer_id, java.sql.Timestamp created_at_timestamp, java.sql.Timestamp locked_at_timestamp, Long locked_customer_id, java.sql.Timestamp accepted_offer_at, Long accepted_customer_id) {
        this.title = title;
        this.description = description;
        this.vaihdossa = vaihdossa;
        this.category_id = category_id;
        this.customer_id = customer_id;
        this.timestamp = created_at_timestamp;
        this.item_id = item_id;
        this.locked_at_timestamp = locked_at_timestamp;
        this.locked_customer_id = locked_customer_id;
        this.accepted_offer_at = accepted_offer_at;
        this.accepted_customer_id = accepted_customer_id;
    }

    public Long getId() {
        return item_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVaihdossa() {
        return vaihdossa;
    }

    public Long getCategoryId() {
        return category_id;
    }

    public Long getCustomerId() {
        return customer_id;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public Customer getOwner() {
        return new Customers().getCustomerById(customer_id);
    }

    public Long getCounterOffersCount() {
        return CounterOffer.getCounterOffersCountForItem(item_id);
    }

    public CounterOffer getCounterOfferByCustomerId(Long customer_id) {
        return CounterOffer.getCounterOfferForItemByCustomerId(item_id, customer_id);
    }

    public java.sql.Timestamp getLockedAtTimestamp() {
        return locked_at_timestamp;
    }

    public java.sql.Timestamp getLockExpiresAtTimestamp() {
        Calendar cal = Calendar.getInstance();

        cal.setTime(locked_at_timestamp);
        cal.add(Calendar.DAY_OF_WEEK, 1);

        return new java.sql.Timestamp(cal.getTime().getTime());
    }

    public Long getLockedCustomerId() {
        return locked_customer_id;
    }

    public Customer getLockedCustomer() {
        return new Customers().getCustomerById(locked_customer_id);
    }

    public java.sql.Timestamp getAcceptedOfferAtTimestamp() {
        return accepted_offer_at;
    }

    public Long getAcceptedOfferCustomerId() {
        return accepted_customer_id;
    }

    public java.sql.Timestamp getAcceptedOfferAt() {
        return accepted_offer_at;
    }

    public Customer getAcceptedOfferCustomer() {
        return new Customers().getCustomerById(accepted_customer_id);
    }
}
