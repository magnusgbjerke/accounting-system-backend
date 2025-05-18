package com.company.accountingsystem.vouchergenerator.pdfgenerator;

import com.company.accountingsystem.voucher.Voucher;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class Generator {

    public static void PDFGenerator(Voucher voucher, String fmsPath, String fileId) {
        String currentPath = Paths.get("").toAbsolutePath().toString();
        String pdfPath = currentPath + File.separator + "src" + File.separator + "main" + File.separator + "java" +
                File.separator + "com" + File.separator + "company" + File.separator + "accountingsystem" +
                File.separator + "vouchergenerator" +
                File.separator + "pdfgenerator" + File.separator + "sample.pdf";
        try {
            PDDocument document = Loader.loadPDF(new File(pdfPath));

            PDPageContentStream contentStream = new PDPageContentStream(
                    document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, false, true);

            contentStream.beginText();

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText(voucher.getYear().getId().toString());

            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 20);
            contentStream.newLineAtOffset(0, -40);
            contentStream.showText("Bilag " + voucher.getId());

            contentStream.endText();
            contentStream.close();

            document.save(fmsPath + fileId + ".pdf");

            document.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
