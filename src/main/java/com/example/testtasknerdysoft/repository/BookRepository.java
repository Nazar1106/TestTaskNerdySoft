package com.example.testtasknerdysoft.repository;

import com.example.testtasknerdysoft.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitleAndAuthor(String title, String author);

    List<Book> findAllByBorrowersNotEmpty();

}
