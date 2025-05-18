package com.company.accountingsystem;

import com.company.accountingsystem.fms.config.Fms;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountingSystemApplication.class, args);

        // Delete contents of folder "fms"(file-management-system) upon start of app.
        // Deletes everything except config folder and sample files.
        Fms.deleteFmsDirectory();
    }
}