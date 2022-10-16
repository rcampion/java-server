package com.rkc.zds.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.rkc.zds.dto.BookDto;
import com.rkc.zds.service.Sort;

public class PaginationPage <T> {
	
	public PaginationPage(List<T> bookList, int inputsize) {
		content = bookList;
		size = inputsize;
	}
	
	public List<T> content;	
	public boolean last;
	public boolean first;
	public int number;
	public int size;
	public int totalPages;
	public int itemsPerPage;
    // sort?: Array<PaginationPropertySort>;
	public Sort sort;
}
