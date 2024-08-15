package com.example.testtasknerdysoft.service.impl;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.entity.Book;
import com.example.testtasknerdysoft.entity.Member;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.mapper.BookMapper;
import com.example.testtasknerdysoft.repository.BookRepository;
import com.example.testtasknerdysoft.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookLoanServiceImplTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookLoanServiceImpl bookLoanService;

    private Member member;
    private Book book;
    private final Long memberId = 1L;
    private final Long bookId = 1L;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setMemberId(memberId);
        member.setBorrowedBooks(new HashSet<>());

        book = new Book();
        book.setBookId(bookId);
        book.setAmount(5);
        book.setBorrowers(new HashSet<>());
    }

    @Test
    void testLendBookToMember_BookNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> bookLoanService.lendBookToMember(memberId, bookId));
    }

    @Test
    void testLendBookToMember_MemberNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> bookLoanService.lendBookToMember(memberId, bookId));
    }

    @Test
    void testLendBookToMember_BookOutOfStock() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(ApiRequestException.class, () -> bookLoanService.lendBookToMember(memberId, bookId));
    }

    @Test
    void testLendBookToMember_BorrowLimitExceeded() {
        Set<Book> borrowedBooks = new HashSet<>();
        int borrowLimit = 5;
        for (int i = 0; i < borrowLimit + 1; i++) {
            borrowedBooks.add(new Book());
        }
        member.setBorrowedBooks(borrowedBooks);

        Book book = new Book();
        book.setBookId(bookId);
        book.setAmount(1);
        book.setBorrowers(new HashSet<>());

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(ApiRequestException.class, () -> bookLoanService.lendBookToMember(memberId, bookId));
    }

    @Test
    void testProcessBookReturn_Success() {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(bookId);
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setAmount(book.getAmount());

        member.getBorrowedBooks().add(book);
        book.setAmount(1);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.bookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookLoanService.processBookReturn(memberId, bookId);

        assertEquals(bookDTO, result);
        assertEquals(2, book.getAmount());
        verify(memberRepository).save(member);
        verify(bookRepository).save(book);
    }

    @Test
    void testProcessBookReturn_BookNotBorrowed() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        assertThrows(ApiRequestException.class, () -> bookLoanService.processBookReturn(memberId, bookId));
    }

    @Test
    void testProcessBookReturn_MemberNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> bookLoanService.processBookReturn(memberId, bookId));
    }


    @Test
    void testProcessBookReturn_BookNotFound() {
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> bookLoanService.processBookReturn(memberId, bookId));
    }
}