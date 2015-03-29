package models;

import play.data.validation.Constraints;

/**
 * Created by avrj on 27.3.2015.
 */
public class ItemForm {
    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String title;

    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String description;

    @Constraints.Required
    public int category;

    public String vaihdossa;

    public ItemForm() {

    }

    public ItemForm(String title, String description, String vaihdossa) {
        this.title = title;
        this.description = description;
        this.vaihdossa = vaihdossa;
    }
}
