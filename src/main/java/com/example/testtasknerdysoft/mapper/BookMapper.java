package com.example.testtasknerdysoft.mapper;

import com.example.testtasknerdysoft.dto.BookDTO;
import com.example.testtasknerdysoft.entity.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book book(BookDTO bookDTO);

    BookDTO bookDTO(Book book);

    List<BookDTO> ListToMemberList(List<Book> bookList);


}
