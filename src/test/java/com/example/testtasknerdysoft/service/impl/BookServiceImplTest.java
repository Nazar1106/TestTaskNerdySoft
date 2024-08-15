package com.example.testtasknerdysoft.service.impl;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.dto.BorrowedBookDTO;
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
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private Book book1;
    private Book book2;
    private Book book3;
    private Book bookDuplicate;
    private Book existingBook;
    private BookDTO bookDTO;
    private Member member;
    private List<Book> books;
    private List<BookDTO> bookDTOs;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");

        book1 = new Book();
        book1.setTitle("Book One");

        book2 = new Book();
        book2.setTitle("Book Two");

        book3 = new Book();
        book3.setTitle("Book One");

        bookDuplicate = new Book();
        bookDuplicate.setTitle("Book One");
        bookDuplicate.setBookId(3L);

        existingBook = new Book();
        existingBook.setBookId(1L);
        existingBook.setAmount(2);

        member = new Member();
        member.setName("Test Member");
        member.setBorrowedBooks(Set.of(book));

        bookDTO = new BookDTO();
        bookDTO.setBookId(1L);
        bookDTO.setTitle("Test Book");
        bookDTO.setAuthor("Test Author");
        bookDTO.setAmount(1);

        books = new ArrayList<>();
        books.add(book);

        bookDTOs = new ArrayList<>();
        bookDTOs.add(bookDTO);
    }

    @Test
    void getBookById_success() throws ApiRequestException {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.bookDTO(book)).thenReturn(bookDTO);

        BookDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(bookDTO.getBookId(), result.getBookId());
        assertEquals(bookDTO.getTitle(), result.getTitle());

        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).bookDTO(book);
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found with id: 1", exception.getMessage());

        verify(bookMapper, never()).bookDTO(any());
    }

    @Test
    void getAllBooks_success() throws ApiRequestException {

        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.ListToMemberList(books)).thenReturn(bookDTOs);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDTO.getBookId(), result.get(0).getBookId());
        assertEquals(bookDTO.getTitle(), result.get(0).getTitle());

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).ListToMemberList(books);
    }

    @Test
    void getAllBooks_emptyList() throws ApiRequestException {

        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        when(bookMapper.ListToMemberList(anyList())).thenReturn(new ArrayList<>());

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).ListToMemberList(anyList());
    }

    @Test
    void getAllByMemberName_success() throws BusinessException {

        when(memberRepository.findAllByName("Test Member")).thenReturn(List.of(member));
        when(bookMapper.bookDTO(book)).thenReturn(bookDTO);

        List<BookDTO> result = bookService.getAllByMemberName("Test Member");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bookDTO.getBookId(), result.get(0).getBookId());
        assertEquals(bookDTO.getTitle(), result.get(0).getTitle());

        verify(memberRepository, times(1)).findAllByName("Test Member");
        verify(bookMapper, times(1)).bookDTO(book);
    }

    @Test
    void getAllByMemberName_noBooks() throws BusinessException {

        Member memberWithoutBooks = new Member();
        memberWithoutBooks.setName("Test Member");
        memberWithoutBooks.setBorrowedBooks(new HashSet<>());

        when(memberRepository.findAllByName("Test Member")).thenReturn(List.of(memberWithoutBooks));


        List<BookDTO> result = bookService.getAllByMemberName("Test Member");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(memberRepository, times(1)).findAllByName("Test Member");
        verify(bookMapper, never()).bookDTO(any());
    }

    @Test
    void getAllByMemberName_memberNotFound() throws BusinessException {

        when(memberRepository.findAllByName("Unknown Member")).thenReturn(new ArrayList<>());

        List<BookDTO> result = bookService.getAllByMemberName("Unknown Member");

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(memberRepository, times(1)).findAllByName("Unknown Member");
        verify(bookMapper, never()).bookDTO(any());
    }

    @Test
    void getAllBorrowedDistinct_success() throws ApiRequestException, BusinessException {

        List<Book> books = List.of(book1, book2, book3);

        when(bookRepository.findAllByBorrowersNotEmpty()).thenReturn(books);

        Set<String> result = bookService.getAllBorrowedDistinct();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Book One"));
        assertTrue(result.contains("Book Two"));

        verify(bookRepository, times(1)).findAllByBorrowersNotEmpty();
    }

    @Test
    void getAllBorrowedDistinct_emptyList() throws ApiRequestException, BusinessException {

        when(bookRepository.findAllByBorrowersNotEmpty()).thenReturn(new ArrayList<>());

        Set<String> result = bookService.getAllBorrowedDistinct();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository, times(1)).findAllByBorrowersNotEmpty();
    }

    @Test
    void getAllBorrowedDistinct_withDuplicates() throws ApiRequestException, BusinessException {

        List<Book> booksWithDuplicates = new ArrayList<>();
        booksWithDuplicates.add(book1);
        booksWithDuplicates.add(bookDuplicate);

        when(bookRepository.findAllByBorrowersNotEmpty()).thenReturn(booksWithDuplicates);

        Set<String> result = bookService.getAllBorrowedDistinct();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains("Book One"));

        verify(bookRepository, times(1)).findAllByBorrowersNotEmpty();
    }

    @Test
    void getDistinctBorrowedBooksWithAmounts_success() throws ApiRequestException, BusinessException {

        book1.getBorrowers().add(new Member());
        book1.getBorrowers().add(new Member());
        book2.getBorrowers().add(new Member());

        Set<Book> books = new HashSet<>();
        books.add(book1);
        books.add(book2);

        when(bookRepository.findAllByBorrowersNotEmpty()).thenReturn(new ArrayList<>(books));

        List<BorrowedBookDTO> result = bookService.getDistinctBorrowedBooksWithAmounts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> "Book One".equals(dto.getName()) && dto.getBorrowedQuantity() == 2));
        assertTrue(result.stream().anyMatch(dto -> "Book Two".equals(dto.getName()) && dto.getBorrowedQuantity() == 1));

        verify(bookRepository, times(1)).findAllByBorrowersNotEmpty();
    }

    @Test
    public void testCreateBook_NewBook() throws ApiRequestException {

        Book newBook = new Book();
        newBook.setBookId(1L);
        newBook.setTitle(bookDTO.getTitle());
        newBook.setAuthor(bookDTO.getAuthor());
        newBook.setAmount(bookDTO.getAmount());

        when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())).thenReturn(null);
        when(bookMapper.book(bookDTO)).thenReturn(newBook);
        when(bookRepository.save(newBook)).thenReturn(newBook);
        when(bookMapper.bookDTO(newBook)).thenReturn(bookDTO);

        BookDTO result = bookService.createBook(bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(bookDTO.getAmount(), result.getAmount());

        verify(bookRepository, times(1)).save(newBook);
    }

    @Test
    public void testCreateBook_ExistingBook() throws ApiRequestException {

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(bookDTO.getAuthor());

        Book updatedBook = new Book();
        updatedBook.setBookId(1L);
        updatedBook.setTitle(bookDTO.getTitle());
        updatedBook.setAuthor(bookDTO.getAuthor());
        updatedBook.setAmount(3);

        when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())).thenReturn(existingBook);
        when(bookMapper.bookDTO(updatedBook)).thenReturn(bookDTO);
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);

        BookDTO result = bookService.createBook(bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
        assertEquals(bookDTO.getAuthor(), result.getAuthor());
        assertEquals(1, result.getAmount());

        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    public void testCreateBook_Exception() throws ApiRequestException {
        when(bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor())).thenThrow(new ApiRequestException("Database error"));

        Exception exception = assertThrows(ApiRequestException.class, () -> {
            bookService.createBook(bookDTO);
        });

        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void updateBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookMapper.bookDTO(any(Book.class))).thenReturn(bookDTO);

        BookDTO updatedBookDTO = bookService.updateBook(1L, bookDTO);

        assertNotNull(updatedBookDTO);
        assertEquals("Test Book", updatedBookDTO.getTitle());
        assertEquals("Test Author", updatedBookDTO.getAuthor());
        assertEquals(1, updatedBookDTO.getAmount());

        verify(bookRepository).findById(1L);
        verify(bookRepository).save(existingBook);
    }

    @Test
    void updateBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            bookService.updateBook(1L, bookDTO);
        });

        assertEquals("Book not found with id: 1", thrown.getMessage());
        verify(bookRepository).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Book not found with id: 1", thrown.getMessage());
        verify(bookRepository, never()).delete(any(Book.class));
    }

    @Test
    void deleteBook_BookHasBorrowers() {
        book.setBorrowers(Set.of(new Member()));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        ApiRequestException thrown = assertThrows(ApiRequestException.class, () -> {
            bookService.deleteBook(1L);
        });

        assertEquals("Cannot delete a book that is currently borrowed", thrown.getMessage());
        verify(bookRepository, never()).delete(any(Book.class));
    }
}