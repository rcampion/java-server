package com.rkc.zds.service.impl;


import java.util.ArrayList;
import java.util.List;

import com.rkc.zds.JavaServerApp;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.exceptions.DataAccessException;
import com.rkc.zds.service.BookService;
import com.rkc.zds.utils.Pagination;

public class BookServiceImpl implements BookService {

	@Override
	public Page<BookDto> findBooks(Pagination pagination) {

		List<BookDto> bookList = new ArrayList<BookDto>();		
		try {
			bookList = JavaServerApp.getDataHandler().getBooks(pagination, false);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int size = bookList.size();
		if (size == 0) {
			size = 1;
		}

		//PageRequest pageRequest = PageRequest.of(0, size);

		//PageImpl<BookDto> page = new PageImpl<BookDto>(bookList, pageRequest, size);
		Page page = new Page(bookList, size);
		return page;
	}

}
