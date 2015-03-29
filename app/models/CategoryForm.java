package models;

import play.data.validation.Constraints;

/**
 * Created by avrj on 27.3.2015.
 */
public class CategoryForm {
    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String title;
}
