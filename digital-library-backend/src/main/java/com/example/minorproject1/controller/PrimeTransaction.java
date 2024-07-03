package com.example.minorproject1.controller;

import com.example.minorproject1.model.PrimeTransactionDetails;
import com.example.minorproject1.model.SecuredUser;
import com.example.minorproject1.service.PrimeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrimeTransaction {

    @Autowired
    PrimeTransactionService primeTransactionService;

    @GetMapping("/prime/{amount}")
    @PreAuthorize("hasAuthority('student')")
    public PrimeTransactionDetails primeTransaction(@PathVariable Double amount) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return primeTransactionService.createTransaction(amount,studentId);
    }
}
