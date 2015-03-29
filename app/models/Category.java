package models;

/**
 * Created by avrj on 27.3.2015.
 */
public class Category {
    private String title;
    private Long id;

    public Category(Long id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }
}
