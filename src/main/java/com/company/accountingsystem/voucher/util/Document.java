package com.company.accountingsystem.voucher.util;

import com.company.accountingsystem.exception.ConflictException;
import com.company.accountingsystem.fms.config.Helper;
import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.dto.*;
import com.company.accountingsystem.voucher.tempvoucher.TempVoucherDTO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class Document {
    public static DocumentDTO getDocumentDTO(String filePath) {
        if (filePath == null) {
            return null;
        }
        // Convert to Path
        Path path = Paths.get(filePath);
        // Get the file name
        String[] split = path.getFileName().toString().split("(?<=\\b[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\b)-", 2);

        String uuid = split[0];
        String fileName = split.length > 1 ? split[1] : "";

        String fileData;
        try {
            File file = path.toFile();
            // Check if the file exists
            if (!file.exists()) {
                throw new IllegalStateException("File not found");
            }
            // Read the file as bytes
            byte[] fileBytes = Files.readAllBytes(path);
            // Encode to Base64
            fileData = Base64.getEncoder().encodeToString(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to read the file");
        }
        return new DocumentDTO(fileData, fileName);
    }

    public static String uploadDocument(String fileData, String fileName) {
        UUID uuid = UUID.randomUUID();
        String fileId = uuid + "-" + fileName;
        String currentPath = Paths.get("").toAbsolutePath().toString();
        String fmsPath = currentPath + File.separator + "src" + File.separator + "main" + File.separator + "java" +
                File.separator + "com" + File.separator + "company" + File.separator + "accountingsystem" +
                File.separator + "fms" + File.separator;


        String filePath;
        try {
            // Decode the Base64-encoded PDF
            byte[] decodedBytes = Base64.getDecoder().decode(fileData);

            // Convert the first few bytes to a String
            String header = new String(decodedBytes, 0, Math.min(5, decodedBytes.length));

            // If header does not start with "%PDF-" --> throw not pdf error
            if (!header.startsWith("%PDF-")) {
                throw new ConflictException("Filedata is not a Base64 encoded pdf");
            }

            // Save the decoded PDF to a file
            filePath = fmsPath + fileId;
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(decodedBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConflictException("Failed to save PDF");
        }
        return filePath;
    }

    public static String saveDocumentIfAttached(VoucherRequestDTO voucher) {
        String filePath = null;
        if (voucher.getDocumentDTO() != null && voucher.getDocumentDTO().getFileData() != null) {
            filePath = Document.uploadDocument(voucher.getDocumentDTO().getFileData(), voucher.getDocumentDTO().getFileName());
        }
        return filePath;
    }

    public static String saveDocumentIfAttached(TempVoucherDTO voucher) {
        String filePath = null;
        if (voucher.getDocumentDTO() != null && voucher.getDocumentDTO().getFileData() != null) {
            filePath = Document.uploadDocument(voucher.getDocumentDTO().getFileData(), voucher.getDocumentDTO().getFileName());
        }
        return filePath;
    }

    public static void deleteDocumentIfAttached(Voucher voucher) {
        if (voucher.getFilePath() != null) {
            Helper.deleteFile(voucher.getFilePath());
        }
    }
}
