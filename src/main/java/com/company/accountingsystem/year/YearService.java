package com.company.accountingsystem.year;

import com.company.accountingsystem.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class YearService {

    private final YearRepository yearRepository;

    public YearService(YearRepository yearRepository) {
        this.yearRepository = yearRepository;
    }

    public Year getYear(Integer id) {
        return yearRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Year " + id + " does not exist."));
    }
}
