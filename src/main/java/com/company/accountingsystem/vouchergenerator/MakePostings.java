package com.company.accountingsystem.vouchergenerator;

import com.company.accountingsystem.account.Account;
import com.company.accountingsystem.voucher.Voucher;
import com.company.accountingsystem.voucher.posting.Posting;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MakePostings {
    public List<Posting> makePostingsSet(Voucher voucher, Integer numberOfPostings) {
        Faker faker = new Faker();

        // Generate a random date for the postingSet
        LocalDate localDate = GeneratorHelper.createRandomDateToLocalDate(voucher.getYear().getId(), 1, 1,
                voucher.getYear().getId(), 12, 31);

        List<Posting> postings = new ArrayList<>();
        BigDecimal counter = BigDecimal.ZERO;
        for (int i = 0; i < numberOfPostings; i++) {
            List<Integer> accountIds = List.of(2400, 1920, 1900, 3000, 7790, 1500);
            // Pick a random accountId
            Integer accountId = accountIds.get(ThreadLocalRandom.current().nextInt(accountIds.size()));
            BigDecimal amount = BigDecimal.valueOf(faker.number().numberBetween(-1000, 1000));
            Posting posting = new Posting(
                    voucher,
                    localDate,
                    new Account(accountId, null),
                    amount
            );
            postings.add(posting);
            counter = counter.add(amount);
            if ((i + 1 == numberOfPostings) && counter.compareTo(BigDecimal.ZERO) != 0) { // If not null, add posting to make sum 0
                BigDecimal amountLast = counter.negate();
                List<Integer> accountIds2 = List.of(2400, 1920, 1900, 3000, 7790, 1500);
                // Pick a random accountId
                Integer accountId2 = accountIds2.get(ThreadLocalRandom.current().nextInt(accountIds2.size()));
                Posting posting2 = new Posting(
                        voucher,
                        localDate,
                        new Account(accountId2, null),
                        amountLast
                );
                postings.add(posting2);
            }

        }
        BigDecimal postingSUM = BigDecimal.ZERO;
        for (Posting posting : postings) {
            postingSUM = postingSUM.add(posting.getAmount());
        }
        return postings;
    }
}
