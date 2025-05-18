package com.company.accountingsystem.vouchergenerator;

import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.posting.Posting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Print {
    public static String printVoucher(Voucher voucher) {
        Integer id = voucher.getId();
        Integer Year = voucher.getYear().getId();
        Boolean reverse_voucher = voucher.getReverseVoucher();
        Boolean update_voucher = voucher.getUpdateVoucher();
        String full_path = voucher.getFilePath();
        String full_path_forwardSlash = full_path.replace("\\", "/");

        //Todo Use full_path_forwardSlash for local dev

        String trimmedPath = full_path_forwardSlash;
        int index = full_path_forwardSlash.indexOf("src/");
        if (index != -1) {
            trimmedPath = full_path_forwardSlash.substring(index);
        }
        LocalDateTime created_on = voucher.getCreatedOn();
        LocalDateTime temp_created_on = voucher.getTempCreatedOn();
        return "(" +
                id + ", " +
                Year + ", " +
                reverse_voucher + ", " +
                update_voucher + ", " +
                "'" + trimmedPath + "'" + ", " +
                "'" + created_on + "'" + ", " +
                temp_created_on +
                ")";
    }

    public static String printPosting(Posting posting) {
        Integer account_id = posting.getAccount().getId();
        Integer voucher_year_fk = posting.getVoucher().getYear().getId();
        Integer voucher_id_fk = posting.getVoucher().getId();
        LocalDate date = posting.getDate();
        BigDecimal amount = posting.getAmount();
        return "(" +
                account_id + ", " +
                voucher_year_fk + ", " +
                voucher_id_fk + ", " +
                "'" + date + "'" + ", " +
                amount +
                ")";
    }
}