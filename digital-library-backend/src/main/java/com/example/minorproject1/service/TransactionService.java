package com.example.minorproject1.service;

import com.example.minorproject1.controller.BookController;
import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.model.Transaction;
import com.example.minorproject1.model.enums.TransactionStatus;
import com.example.minorproject1.model.enums.TransactionType;
import com.example.minorproject1.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionService {

    @Autowired
    StudentService studentService;

    @Autowired
    BookService bookService;

    private static final int maxBooksForIssuance=3;

    @Value("${student.issue.number_of_days}")
    private int numberOfDaysForIssuance;

    @Autowired
    TransactionRepository transactionRepository;

    public String issueTxn(String bookName,int studentId) throws Exception{

        List<Book> bookList;

        try {
            bookList = bookService.getAllAvailable(Optional.ofNullable(bookName), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        }
        catch (Exception e){
            throw new Exception("Book not found");
        }

        Student student = studentService.getStudent(studentId);

        if(student.getPrimeTransactionDetails()==null){
            throw new Exception("Not eligible - Become a prime member");
        }

        if(student.getBookList() != null && student.getBookList().size()>=maxBooksForIssuance){
           throw new Exception("Book limit reached");
        }

        if(bookList.isEmpty()){
            throw new Exception("Not able to find any book in the library");
        }

        Book book = bookList.get(0);

        Transaction transaction = Transaction.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .transactionType(TransactionType.ISSUE)
                .student(student)
                .book(book)
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        transaction = transactionRepository.save(transaction);


        try {
            book.setStudent(student);
            bookService.assignBookToStudent(book, student);

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            transaction.setTransactionStatus(TransactionStatus.FAILED);
        }finally {
            return transactionRepository.save(transaction).getExternalTxnId();
        }

    }

    public String returnTxn(int bookId, int studentId) throws Exception {
        Book book;
        try {
            book = this.bookService.getBookById(bookId);
        }catch (Exception e){
            throw new Exception("not able to fetch book details");
        }

        // Validation
        if(book.getStudent() == null || book.getStudent().getId() != studentId){
            throw new Exception("Book is not assigned to this student");
        }

        Student student;
        try{
            student = this.studentService.getStudent(studentId);
        }
        catch (Exception e){
            throw new Exception("Error in student authentication");
        }

        if(student==null){
            throw  new Exception("Student not found");
        }

        Transaction transaction = Transaction.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .transactionType(TransactionType.RETURN)
                .student(student)
                .book(book)
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        transaction = transactionRepository.save(transaction);

        // Get the corresponding issue Txn

        Transaction issueTransaction = this.transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByTransactionTimeDesc(student, book, TransactionType.ISSUE, TransactionStatus.SUCCESS);

        // Fine calculation
        long issueTxnInMillis = issueTransaction.getTransactionTime().getTime();

        long currentTimeMillis = System.currentTimeMillis();

        long timeDifferenceInMillis = currentTimeMillis - issueTxnInMillis;

        long timeDifferenceInDays = TimeUnit.DAYS.convert(timeDifferenceInMillis, TimeUnit.MILLISECONDS);

        Double fine = 0.0;
        if(timeDifferenceInDays > numberOfDaysForIssuance){
            fine = (timeDifferenceInDays - numberOfDaysForIssuance) * 1.0;
        }

        try {

            book.setStudent(null);
            this.bookService.unassignBookFromStudent(book);

            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            transaction.setTransactionStatus(TransactionStatus.FAILED);
        }finally {
            transaction.setFine(fine);
            return transactionRepository.save(transaction).getExternalTxnId();
        }
    }

    public List<Transaction> getAllTxn(int id) {
        return transactionRepository.getAllTxn(id);
    }
}
