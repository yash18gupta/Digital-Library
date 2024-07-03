package com.example.minorproject1.repository;

import com.example.minorproject1.model.Author;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Transactional
public interface AuthorRepository extends JpaRepository<Author,Integer> {

    Author findByEmail(String email);
}
