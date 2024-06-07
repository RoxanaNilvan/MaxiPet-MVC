package com.onlineshop.maxipetbackend.services.invoice;

import com.onlineshop.maxipetbackend.entities.OrderItems;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class TextInvoiceGenerator  implements InvoiceGenerator {
    @Override
    public String generateInvoice(Invoice invoice) {
        StringBuilder invoiceText = new StringBuilder();
        invoiceText.append("Factura pentru ").append(invoice.getLastName()).append(" ").append(invoice.getFirstName()).append(":\n");
        invoiceText.append("-----------------------------------------------------------\n");
        invoiceText.append(String.format("%-20s %-10s %-10s %-10s\n", "Produs", "Preț", "Cantitate", "Total"));
        invoiceText.append("-----------------------------------------------------------\n");

        for (OrderItems orderItems :invoice.getOrderItemsList()) {
            String productName = orderItems.getProduct().getName();
            double productPrice = orderItems.getProduct().getPrice();
            int quantity = orderItems.getQuantity();
            double totalPrice = orderItems.getPrice();

            invoiceText.append(String.format("%-20s %-10.2f %-10d %-10.2f\n", productName, productPrice, quantity, totalPrice));
        }

        invoiceText.append("-----------------------------------------------------------\n");
        invoiceText.append(String.format("%-40s %.2f\n", "Total:", invoice.getTotalPrice()));
        saveInvoiceToFile(invoiceText.toString());
        return invoiceText.toString();
    }

    private void saveInvoiceToFile(String invoiceText) {
        String fileName = "invoice.txt"; // Numele fișierului în care se va salva factura
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(invoiceText.getBytes(StandardCharsets.UTF_8));
            System.out.println("Factura salvată cu succes în fișierul " + fileName);
        } catch (IOException e) {
            System.err.println("Eroare la salvarea facturii în fișierul " + fileName + ": " + e.getMessage());
        }
    }
}
