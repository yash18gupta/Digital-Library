//package com.example.minorproject1.configs;
//
//import com.example.minorproject1.model.Student;
//import com.example.minorproject1.repository.StudentRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class PrimeStatusUpdateTask {
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    // Run this task at midnight every day
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void updatePrimeStatus() {
//        Date currentDate = new Date(System.currentTimeMillis());
//        List<Student> students = studentRepository.findAll();
//
//        for (Student student : students) {
//            if (student.getPrimeTransactionDetails() != null) {
//                Date validity = student.getPrimeValidity();
//                if (validity != null && validity.before(currentDate)) {
//                    student.setPrime(false);
//                    studentRepository.save(student);
//                }
//            }
//        }
//    }
//}
