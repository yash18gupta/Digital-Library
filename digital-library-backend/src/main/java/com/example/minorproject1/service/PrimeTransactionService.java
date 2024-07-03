package com.example.minorproject1.service;

import com.example.minorproject1.model.PrimeTransactionDetails;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.repository.PrimeRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PrimeTransactionService {

    private static final String KEY = "rzp_test_Np1bKPqhESiAqc";
    private static final String KEY_SECRET = "o5kQBNdZhiBqcKBscwKlmGMP";

    private static final String CURRENCY = "INR";


    @Autowired
    StudentService studentService;

    @Autowired
    PrimeRepository primeRepository;

    public PrimeTransactionDetails createTransaction(Double amount, int studentId) throws Exception {
        try{
            Student student = studentService.getStudent(studentId);

            if(student.isPrime()){
                throw new Exception("User is already prime member!");
            }

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("currency", CURRENCY);
            jsonObject.put("amount",(amount*100));

            RazorpayClient razorpayClient = new RazorpayClient(KEY,KEY_SECRET);

            Order order = razorpayClient.orders.create(jsonObject);

            PrimeTransactionDetails details=null;
            if(student.getPrimeTransactionDetails()!=null){
                details = student.getPrimeTransactionDetails();
            }
            else {
                details = preparePrimeTransactionDetails(order, studentId);
            }

            details.setStudent(student);
            primeRepository.save(details);

            student.setPrimeTransactionDetails(details);
            student.setPrime(true);
            student.setPrimeValidity((new Date(System.currentTimeMillis() + 31536000000l)));
            studentService.save(student);

            return details;

        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return null;
    }

    private PrimeTransactionDetails preparePrimeTransactionDetails(Order order, int studentId){
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");


        return PrimeTransactionDetails.builder()
                .orderId(orderId)
                .currency(currency)
                .amount(amount)
                .build();
    }
}
