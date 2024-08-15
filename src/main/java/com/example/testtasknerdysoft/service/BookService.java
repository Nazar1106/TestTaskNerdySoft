package com.example.testtasknerdysoft.service;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.dto.BorrowedBookDTO;

import java.util.List;
import java.util.Set;

public interface BookService {

    BookDTO createBook(BookDTO book);

    BookDTO getBookById(Long id);

    List<BookDTO> getAllBooks();

    BookDTO updateBook(Long id, BookDTO book);

    void deleteBook(Long id);

    List<BookDTO> getAllByMemberName(String name);

    Set<String> getAllBorrowedDistinct();

    List<BorrowedBookDTO> getDistinctBorrowedBooksWithAmounts();


}
