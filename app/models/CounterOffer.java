package models;

/**
 * Created by avrj on 9.4.2015.
 */
public class CounterOffer {
    private Long customer_id, item_id;
    private String description;

    public CounterOffer(Long customer_id, Long item_id, String description) {
        this.customer_id = customer_id;
        this.item_id = item_id;
        this.description = description;
    }

    public Long getCustomerId() {
        return customer_id;
    }

    public Customer getCustomer() {
        return new Customers().getCustomerById(customer_id);
    }

    public Long getItemId() {
        return item_id;
    }

    public String getDescription() {
        return description;
    }

}
