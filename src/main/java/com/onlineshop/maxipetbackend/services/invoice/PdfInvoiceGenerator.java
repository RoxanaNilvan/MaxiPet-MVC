package com.onlineshop.maxipetbackend.services.invoice;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.onlineshop.maxipetbackend.entities.OrderItems;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Component
public class PdfInvoiceGenerator implements InvoiceGenerator {

    @Override
    public String generateInvoice(Invoice invoice) {
        String fileName = "invoice.pdf";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.RED);
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);


            Paragraph title = new Paragraph("Factura pentru " + invoice.getFirstName() + " " + invoice.getLastName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);

            PdfPCell cell = new PdfPCell(new Phrase("Produs", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pret produs", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Cantitate", headerFont));
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Pret final", headerFont));
            table.addCell(cell);

            for (OrderItems orderItems : invoice.getOrderItemsList()) {
                table.addCell(new Phrase(orderItems.getProduct().getName(), normalFont));
                table.addCell(new Phrase(String.valueOf(orderItems.getProduct().getPrice()), normalFont));
                table.addCell(new Phrase(String.valueOf(orderItems.getQuantity()), normalFont));
                table.addCell(new Phrase(String.valueOf(orderItems.getPrice()), normalFont));
            }
            document.add(table);

            // Adăugarea totalului
            Paragraph totalParagraph = new Paragraph("Total: " + invoice.getTotalPrice(), headerFont);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }


}
