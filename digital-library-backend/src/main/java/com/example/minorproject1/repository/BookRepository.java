package com.example.minorproject1.repository;

import com.example.minorproject1.model.Book;
import com.example.minorproject1.model.Student;
import com.example.minorproject1.model.enums.Genre;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Transactional
public interface BookRepository extends JpaRepository<Book,Integer> {

    @Query("SELECT b FROM Book b  " +
            "WHERE (:name IS NULL OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:author IS NULL OR LOWER(b.author.name) LIKE LOWER(CONCAT('%', :author, '%'))) "+
            "AND (:genre IS NULL OR b.genre=:genre) "+
            "AND (:minPages IS NULL OR b.pages>=:minPages)"+
            "AND (:maxPages IS NULL OR b.pages<=:maxPages)"+
            "AND (b.student IS NULL)"
    )

    List<Book> getAllAvailable(@Param("name")String name,
                               @Param("author") String author,
                               @Param("genre") Genre genre,
                               @Param("minPages")Integer minPages,
                               @Param("maxPages")Integer maxPages
    );

    @Modifying // for DML support
    @Transactional // for updating any data
    @Query("update Book b set b.student = ?2 where b.id = ?1 and b.student is null")
    void assignBookToStudent(int bookId, Student student);

    @Modifying // for DML support
    @Transactional // for updating any data
    @Query("update Book b set b.student = null where b.id = ?1 ")
    void unassignBook(int bookId);

    @Query("SELECT b FROM Book b where b.student IS NULL")
    List<Book> getAvailableBook();

}
