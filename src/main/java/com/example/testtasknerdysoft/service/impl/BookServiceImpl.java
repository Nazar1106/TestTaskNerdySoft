package com.example.testtasknerdysoft.service.impl;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.dto.BorrowedBookDTO;
import com.example.testtasknerdysoft.entity.Book;
import com.example.testtasknerdysoft.entity.Member;
import com.example.testtasknerdysoft.exception.ApiRequestException;
import com.example.testtasknerdysoft.exception.BusinessException;
import com.example.testtasknerdysoft.mapper.BookMapper;
import com.example.testtasknerdysoft.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.testtasknerdysoft.repository.BookRepository;
import com.example.testtasknerdysoft.service.BookService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final MemberRepository memberRepository;

    private final BookMapper bookMapper;


    @Autowired
    public BookServiceImpl(BookRepository bookRepository, MemberRepository memberRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public BookDTO getBookById(Long id) throws BusinessException{
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Book not found with id: " + id));
        return bookMapper.bookDTO(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDTO> getAllBooks() throws ApiRequestException {
        List<Book> books = bookRepository.findAll();
        return bookMapper.ListToMemberList(books);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookDTO> getAllByMemberName(String name) throws BusinessException {
        return memberRepository.findAllByName(name).stream().map(Member::getBorrowedBooks).flatMap(Collection::stream).map(bookMapper::bookDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Set<String> getAllBorrowedDistinct() throws ApiRequestException, BusinessException {
        return bookRepository.findAllByBorrowersNotEmpty().stream().map(Book::getTitle).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    @Override
    public List<BorrowedBookDTO> getDistinctBorrowedBooksWithAmounts() throws ApiRequestException, BusinessException {
        return bookRepository.findAllByBorrowersNotEmpty().stream().map(book -> new BorrowedBookDTO(book.getTitle(), book.getBorrowers().size())).toList();
    }

    @Transactional
    @Override
    public BookDTO createBook(BookDTO book) throws ApiRequestException {
        try {
            Book existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor());

            if (existingBook != null) {
                existingBook.setAmount(existingBook.getAmount() + 1);
                Book updatedBook = bookRepository.save(existingBook);
                return bookMapper.bookDTO(updatedBook);
            } else {
                Book newBook = bookMapper.book(book);
                Book savedBook = bookRepository.save(newBook);
                return bookMapper.bookDTO(savedBook);
            }
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public BookDTO updateBook(Long id, BookDTO book) {

        Book existingBook = bookRepository.findById(id).orElseThrow(() -> new BusinessException("Book not found with id: " + id));


        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setAmount(book.getAmount());
        bookRepository.save(existingBook);

        return bookMapper.bookDTO(existingBook);
    }

    @Transactional
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BusinessException("Book not found with id: " + id));

        if (book.getBorrowers().isEmpty()) {
            bookRepository.delete(book);
        } else {
            throw new ApiRequestException("Cannot delete a book that is currently borrowed");
        }
    }
}

