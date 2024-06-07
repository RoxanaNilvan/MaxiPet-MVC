package com.onlineshop.maxipetbackend.validators;
import com.onlineshop.maxipetbackend.entities.Product;

public class ProductValidators {
    public static boolean validateProduct(Product product) {
        return validatePrice(product.getPrice()) && validateStock(product.getStock());
    }

    private static boolean validatePrice(double price) {
        return price >= 0;
    }

    private static boolean validateStock(int stock) {
        return stock >= 0;
    }
}
