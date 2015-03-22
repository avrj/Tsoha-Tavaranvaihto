package models;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avrj on 22.3.2015.
 */
public class RegisterForm {
    @Constraints.Required
    @Constraints.MinLength(value = 3)
    public String username;

    @Constraints.Required
    @Constraints.MinLength(value = 5)
    @Constraints.Email
    public String email;

    @Constraints.Required
    @Constraints.MinLength(value = 8)
    public String password, password_again;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (!password.equals(password_again))
            errors.add(new ValidationError("password", "Salasanat eivät täsmää."));

        return errors.isEmpty() ? null : errors;
    }
}
