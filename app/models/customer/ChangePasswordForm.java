package models.customer;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avrj on 29.3.2015.
 */
public class ChangePasswordForm {
    @Constraints.Required
    @Constraints.MinLength(value = 8)
    public String current_password, new_password, new_password_again;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<ValidationError>();

        if (!new_password.equals(new_password_again))
            errors.add(new ValidationError("new_password", "Salasanat eivät täsmää."));

        return errors.isEmpty() ? null : errors;
    }
}
