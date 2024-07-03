package com.example.minorproject1.controller;

import com.example.minorproject1.model.SecuredUser;
import com.example.minorproject1.model.Transaction;
import com.example.minorproject1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {


    @Autowired
    TransactionService transactionService;

    @PostMapping("/issue")
//    @PreAuthorize("hasAuthority('student')")
    public String issueTxn(@RequestParam("name") String name) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return transactionService.issueTxn(name, studentId);
    }

    @PostMapping("/return")
    @PreAuthorize("hasAuthority('student')")
    public String returnTxn(@RequestParam("bookId") int bookId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return transactionService.returnTxn(bookId, studentId);
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAuthority('student')")
    public List<Transaction> getAllTxn(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecuredUser securedUser = (SecuredUser) authentication.getPrincipal();
        int studentId = securedUser.getStudent().getId();
        return transactionService.getAllTxn(studentId);
    }

}
