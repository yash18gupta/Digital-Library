package com.example.minorproject1.service;

import com.example.minorproject1.controller.BookController;
import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.model.Transaction;
import com.example.minorproject1.model.enums.TransactionStatus;
import com.example.minorproject1.model.enums.TransactionType;
import com.example.minorproject1.repository.TransactionRepository;
import jakarta.transaction.Transactional;
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

    private static final int maxBooksForIssuance = 3;

    @Value("${student.issue.number_of_days}")
    private int numberOfDaysForIssuance;

    @Autowired
    TransactionRepository transactionRepository;

    public String issueTxn(String bookName, int studentId) throws Exception {
        List<Book> bookList;
        try {
            bookList = bookService.getAllBooksAvailableForIssue(bookName);
        } catch (Exception e) {
            throw new Exception("Not able to fetch the book at this moment!", e);
        }

        if (bookList.isEmpty()) {
            throw new Exception("Requested Book is not available!");
        }

        Student student;
        try {
            student = studentService.getStudent(studentId);
        } catch (Exception e) {
            throw new Exception("Unable to fetch the student!", e);
        }

        if (student.getPrimeTransactionDetails() == null) {
            throw new Exception("Not eligible - Become a prime member");
        }

        if (student.getBookList() != null && student.getBookList().size() >= maxBooksForIssuance) {
            throw new Exception("Book limit reached");
        }

        Book book = bookList.get(0);

        Transaction transaction = Transaction.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .transactionType(TransactionType.ISSUE)
                .student(student)
                .book(book)
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        try {
            transaction = transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new Exception("Error in saving transaction!", e);
        }

        try {
            issueBook(book, student, transaction);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new Exception("Failed to issue the book", e);
        } finally {
            transactionRepository.save(transaction);
        }

        return transaction.getExternalTxnId();
    }

    @Transactional
    public void issueBook(Book book, Student student, Transaction transaction) throws Exception {
        book.setStudent(student);
        bookService.assignBookToStudent(book, student);
    }


    public String returnTxn(int bookId, int studentId) throws Exception {
        Book book;
        try {
            book = this.bookService.getBookById(bookId);
        } catch (Exception e) {
            throw new Exception("Not able to fetch book details", e);
        }

        if (book == null) {
            throw new Exception("Error in fetching book!");
        }

        if (book.getStudent() == null || book.getStudent().getId() != studentId) {
            throw new Exception("Book is not assigned to this student");
        }

        Student student;
        try {
            student = this.studentService.getStudent(studentId);
        } catch (Exception e) {
            throw new Exception("Error in fetching student!", e);
        }

        if (student == null) {
            throw new Exception("Student not found!");
        }

        Transaction transaction = Transaction.builder()
                .externalTxnId(UUID.randomUUID().toString())
                .transactionType(TransactionType.RETURN)
                .student(student)
                .book(book)
                .transactionStatus(TransactionStatus.PENDING)
                .build();

        try {
            transaction = transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new Exception("Error in saving return book transaction", e);
        }

        Transaction issueTransaction;
        try {
            issueTransaction = this.transactionRepository.findTopByStudentAndBookAndTransactionTypeAndTransactionStatusOrderByTransactionTimeDesc(
                    student, book, TransactionType.ISSUE, TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new Exception("Error in fetching issue transaction", e);
        }

        // Fine calculation
        long issueTxnInMillis = issueTransaction.getTransactionTime().getTime();
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceInMillis = currentTimeMillis - issueTxnInMillis;
        long timeDifferenceInDays = TimeUnit.DAYS.convert(timeDifferenceInMillis, TimeUnit.MILLISECONDS);

        Double fine = 0.0;
        if (timeDifferenceInDays > numberOfDaysForIssuance) {
            fine = (timeDifferenceInDays - numberOfDaysForIssuance) * 1.0;
        }

        try {
            unassignBook(book);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            throw new Exception("Failed to return the book", e);
        } finally {
            transaction.setFine(fine);
            transactionRepository.save(transaction);
        }

        return transaction.getExternalTxnId();
    }

    @Transactional
    public void unassignBook(Book book) {
        book.setStudent(null);
        this.bookService.unassignBookFromStudent(book);
    }


    public List<Transaction> getAllTxn(int id) {
        return transactionRepository.getAllTxn(id);
    }
}
