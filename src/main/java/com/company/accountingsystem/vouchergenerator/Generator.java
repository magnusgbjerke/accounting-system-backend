package com.company.accountingsystem.vouchergenerator;

import com.company.accountingsystem.voucher.dto.VoucherWithPostingsDTO;
import com.github.javafaker.Faker;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.company.accountingsystem.vouchergenerator.Print.printPosting;

public class Generator {

    public static void VoucherGenerator() {
        Faker faker = new Faker();
        // Path to save folder
        String currentPath = Paths.get("").toAbsolutePath().toString();
        String fmsPath = currentPath + File.separator + "src" + File.separator + "main" + File.separator + "java" +
                File.separator + "com" + File.separator + "company" + File.separator + "accountingsystem" +
                File.separator + "fms" + File.separator;

        // Make vouchers for years
        List<Integer> listOfYears = List.of(2020, 2021, 2022, 2023);

        StringBuilder vouchers = new StringBuilder();
        StringBuilder postings = new StringBuilder();
        StringBuilder skipFiles = new StringBuilder();
        for (int i = 0; i < listOfYears.size(); i++) {

            // How many vouchers in year
            int numberOfVouchers = faker.number().numberBetween(50, 50);

            for (int y = 0; y < numberOfVouchers; y++) {
                // Faker config
                // Make fileName and fileId.
                UUID uuid = UUID.randomUUID();
                String fileName = faker.lorem().sentence();
                String fileId = uuid + "-" + fileName;
                String filePath = fmsPath + fileId + ".pdf";
                skipFiles.append("(fmsPath + \"" + fileId + ".pdf\")");
                skipFiles.append(",");
                skipFiles.append(System.lineSeparator());

                // Number of postingSets in voucher
                int numberOfPostingSets = faker.number().numberBetween(1, 6);
                // Number of postings in postingSet
                int numberOfPostings = faker.number().numberBetween(1, 6);

                // Generate a random date for voucher createdOn date
                LocalDateTime localDateTime = GeneratorHelper.createRandomDateToLocalDateTime(listOfYears.get(i), 1, 1,
                        listOfYears.get(i), 12, 31);

                VoucherWithPostingsDTO voucherWithPostingsDTO = new MakeVoucher().
                        makeVoucher(y + 1, listOfYears.get(i), numberOfPostingSets, numberOfPostings, filePath, localDateTime);

                String voucher = Print.printVoucher(voucherWithPostingsDTO.getVoucher());
                vouchers.append(voucher);
                vouchers.append(",");
                vouchers.append(System.lineSeparator());

                voucherWithPostingsDTO.getPostings().forEach(posting -> {
                    String string = printPosting(posting);
                    postings.append(string);
                    postings.append(",");
                    postings.append(System.lineSeparator());
                });
                //PDF generation
                com.company.accountingsystem.vouchergenerator.pdfgenerator.Generator.PDFGenerator(voucherWithPostingsDTO.getVoucher(), fmsPath, fileId);
            }
        }
        System.out.println("skipFiles(in AccountingSystemApplication): ");
        System.out.println(skipFiles);
        System.out.println("Vouchers(in data.sql): ");
        System.out.println(vouchers);
        System.out.println("Postings(in data.sql): ");
        System.out.println(postings);
    }
}
