package models.customer;

import play.data.validation.Constraints;

/**
 * Created by avrj on 22.3.2015.
 */
public class LoginForm {
    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String username;

    @Constraints.Required
    @Constraints.MinLength(value = 8)
    public String password;
}