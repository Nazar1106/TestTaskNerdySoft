package com.example.testtasknerdysoft.service;

import com.example.testtasknerdysoft.dto.BookDTO;

public interface BookLoanService {

    BookDTO lendBookToMember(Long memberId, Long bookId);

    BookDTO processBookReturn(Long memberId, Long bookId);
}
