package com.rkc.zds.service;

import com.rkc.zds.dto.BookDto;
import com.rkc.zds.service.impl.PaginationPage;
import com.rkc.zds.utils.Pagination;

public interface BookService {
	
	PaginationPage<BookDto> findBooks(Pagination pageable);
	
	BookDto findOne(int bookId);

	void updateBook(BookDto book);

	void saveBook(BookDto book);
	
}
