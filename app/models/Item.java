package models;

/**
 * Created by avrj on 27.3.2015.
 */
public class Item {
    private String title, description, vaihdossa;
    private Long id, category_id, customer_id;
    private java.sql.Timestamp timestamp;

    public Item(Long id, String title, String description, String vaihdossa, Long category_id, Long customer_id, java.sql.Timestamp created_at_timestamp) {
        this.title = title;
        this.description = description;
        this.vaihdossa = vaihdossa;
        this.category_id = category_id;
        this.customer_id = customer_id;
        this.timestamp = created_at_timestamp;
        this.id = id;
    }

    public Long getId() {
        return id;
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
}
