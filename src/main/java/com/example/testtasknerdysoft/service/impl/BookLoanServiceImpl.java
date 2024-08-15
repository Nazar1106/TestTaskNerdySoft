package com.example.testtasknerdysoft.service.impl;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.entity.Book;
import com.example.testtasknerdysoft.entity.Member;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.mapper.BookMapper;
import com.example.testtasknerdysoft.repository.BookRepository;
import com.example.testtasknerdysoft.repository.MemberRepository;
import com.example.testtasknerdysoft.service.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookLoanServiceImpl implements BookLoanService {

    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Autowired
    public BookLoanServiceImpl(MemberRepository memberRepository, BookRepository bookRepository, BookMapper bookMapper) {
        this.memberRepository = memberRepository;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Value("${member.borrow.limit}")
    private int borrowLimit;

    @Transactional
    @Override
    public BookDTO lendBookToMember(Long memberId, Long bookId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException("Member not found with id: " + memberId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException("Book not found with id: " + bookId));

        if (book.getAmount() == 0) {
            throw new ApiRequestException("Cannot borrow this book, it is out of stock");
        } else if (member.getBorrowedBooks().size() >= borrowLimit) {
            throw new ApiRequestException("You cannot borrow more books");
        }

        member.getBorrowedBooks().add(book);
        book.getBorrowers().add(member);

        book.setAmount(book.getAmount() - 1);

        memberRepository.save(member);
        bookRepository.save(book);

        return bookMapper.bookDTO(book);
    }

    @Transactional
    @Override
    public BookDTO processBookReturn(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException("Member not found with id: " + memberId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BusinessException("Book not found with id:" + bookId));

        if (!member.getBorrowedBooks().contains(book)) {
            throw new ApiRequestException("This book was not borrowed by this member");
        }

        book.getBorrowers().remove(member);
        member.getBorrowedBooks().remove(book);

        book.setAmount(book.getAmount() + 1);

        memberRepository.save(member);
        bookRepository.save(book);

        return bookMapper.bookDTO(book);
    }
}

