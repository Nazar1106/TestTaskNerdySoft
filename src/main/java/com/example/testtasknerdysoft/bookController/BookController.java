package com.example.testtasknerdysoft.bookController;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.dto.BorrowedBookDTO;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.testtasknerdysoft.service.impl.BookServiceImpl;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookServiceImpl bookService;

    @Autowired
    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }


    @Operation(summary = "Get a book by its ID", description = "Retrieves detailed information about a book based on its unique ID")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) throws ApiRequestException, BusinessException {
        return bookService.getBookById(id);
    }

    @Operation(summary = "Retrieve all books", description = "Returns a list of all available books")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<BookDTO> getAllBooks() throws ApiRequestException {
        return bookService.getAllBooks();
    }

    @Operation(summary = "Retrieve all borrowed books by a specific member", description = "Retrieves a list of all books that have been borrowed by the member specified by their name")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/by-member/{memberName}")
    public List<BookDTO> getAllBooksByMemberName(@PathVariable String memberName) throws BusinessException, ApiRequestException {
        return bookService.getAllByMemberName(memberName);
    }

    @Operation(summary = "Retrieve all distinct borrowed book names", description = "Retrieves a set of unique names of all books that have been borrowed.")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/distinct-borrowed")
    public Set<String> getUniqueBorrowedBookNames() throws ApiRequestException {

        return bookService.getAllBorrowedDistinct();
    }


    @Operation(summary = "Retrieve all distinct borrowed books with the amount of each borrowed", description = "Retrieves a list of all distinct borrowed books along with the amount of each book that has been borrowed")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/borrowed-books")
    List<BorrowedBookDTO> getDistinctBorrowedBooksWithAmounts() throws ApiRequestException {
        return bookService.getDistinctBorrowedBooksWithAmounts();
    }

    @Operation(summary = "Create a new book", description = "Creates a new book based on the provided book details")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDTO createBook(@RequestBody @Valid BookDTO book) throws ApiRequestException {

        return bookService.createBook(book);
    }

    @Operation(summary = "Update a book by its ID", description = "Updates the details of an existing book based on the provided ID")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody @Valid BookDTO book) throws ApiRequestException, BusinessException {
        return bookService.updateBook(id, book);
    }

    @Operation(summary = "Delete a book by its ID", description = "Deletes the book identified by the provided ID")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) throws ApiRequestException, BusinessException {
        bookService.deleteBook(id);
    }
}

