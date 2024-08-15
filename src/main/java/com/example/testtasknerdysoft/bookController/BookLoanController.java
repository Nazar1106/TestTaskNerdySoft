package com.example.testtasknerdysoft.bookController;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.service.impl.BookLoanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrow")
public class BookLoanController {

    private final BookLoanServiceImpl libraryService;

    public BookLoanController(BookLoanServiceImpl libraryService) {
        this.libraryService = libraryService;
    }


    @Operation(summary = "Lend a book to a member", description = "Assigns a book to a member, marking it as borrowed")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{memberId}/{bookId}")
    public BookDTO lendBookToMember(@PathVariable Long memberId, @PathVariable Long bookId) throws BusinessException, ApiRequestException {
        return libraryService.lendBookToMember(memberId, bookId);
    }

    @Operation(summary = "Process the return of a book by a member", description = "Handles the return of a book by a member. Updates the book's availability and removes the book from the member's borrowed list")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{memberId}/return/{bookId}")
    public BookDTO processBookReturn(@PathVariable Long memberId, @PathVariable Long bookId) throws BusinessException, ApiRequestException {
        return libraryService.processBookReturn(memberId, bookId);
    }
}
