package com.onlineshop.maxipetbackend.validators;
import com.onlineshop.maxipetbackend.entities.LocalUser;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class UserValidators {
    public static boolean validateUser(LocalUser user) {
        return validateFirstName(user.getFirstName()) &&
                validateLastName(user.getLastName()) &&
                validateUserName(user.getUserName()) &&
                validateEmail(user.getEmail()) &&
                validatePassword(user.getPassword()) &&
                validateRole(user.getRole());
    }

    private static boolean validateFirstName(String firstName) {
        return firstName != null && firstName.matches("[A-Za-z]+");
    }

    private static boolean validateLastName(String lastName) {
        return lastName != null && lastName.matches("[A-Za-z]+");
    }

    private static boolean validateUserName(String userName) {
        return userName != null && userName.length() >= 5 && userName.length() <= 50;
    }

    private static boolean validateEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private static boolean validatePassword(String password) {
        return password != null && !password.isEmpty();
    }

    private static boolean validateRole(String role) {
        return role != null && !role.isEmpty();
    }
}
