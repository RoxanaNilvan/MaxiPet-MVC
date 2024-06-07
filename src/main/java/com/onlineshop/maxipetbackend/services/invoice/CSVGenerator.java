package com.onlineshop.maxipetbackend.services.invoice;

import com.onlineshop.maxipetbackend.entities.LocalOrder;
import com.onlineshop.maxipetbackend.entities.OrderItems;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class CSVGenerator {
    public static void generateCSVReport(List<LocalOrder> orders) {
        String csvFileName = "orders_report.csv";

        try (FileWriter writer = new FileWriter(csvFileName)) {
            writer.append("OrderID,UserID,TotalAmount,Date,ProductName,Quantity,Price\n");

            for (LocalOrder order : orders) {
                // Scrie informațiile comune pentru comanda curentă
                writer.append(String.format("%s,%s,%s,%s\n",
                        order.getId(), order.getUser().getId(), order.getTotalAmount(), order.getDate()));

                // Iterează prin elementele comenzii și scrie detaliile acestora
                for (OrderItems orderItems : order.getOrderItems()) {
                    writer.append(String.format(",,,,%s,%s,%s\n",
                            orderItems.getProduct().getName(),
                            orderItems.getQuantity(),
                            orderItems.getPrice()));
                }
            }

            System.out.println("Raportul CSV a fost generat cu succes în fișierul " + csvFileName);
        } catch (IOException e) {
            System.err.println("Eroare la generarea raportului CSV: " + e.getMessage());
        }
    }
}
