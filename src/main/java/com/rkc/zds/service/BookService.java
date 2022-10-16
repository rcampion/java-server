package com.rkc.zds.service;

import com.rkc.zds.dto.BookDto;
import com.rkc.zds.service.impl.Page;
import com.rkc.zds.utils.Pagination;

public interface BookService {
	Page<BookDto> findBooks(Pagination pageable);
}
