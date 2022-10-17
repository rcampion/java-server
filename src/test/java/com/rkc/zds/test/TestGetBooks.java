package com.rkc.zds.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.rkc.zds.db.AnsiDataHandler;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.exceptions.DataAccessException;
import com.rkc.zds.utils.Pagination;

public class TestGetBooks {
	
	static public AnsiDataHandler dataHandler;
	
    private List<String> list;

    @Before
    public void init() {
        //LOG.info("startup");
        //list = new ArrayList<>(Arrays.asList("test1", "test2"));
    }

    @BeforeClass
    public static void setup() {
        //LOG.info("startup - creating DB connection");
    	dataHandler = new AnsiDataHandler();
    }

    @AfterClass
    public static void tearDown() {
        //LOG.info("closing DB connection");
    }
    
	@Test
	public void testGetBooks() {
		
		Pagination pagination = new Pagination(6, 0);
		
		List<BookDto> bookList = new ArrayList<BookDto>();
		try {
			bookList = dataHandler.getBooks(pagination, false);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int size = bookList.size();
		
		assertTrue(size > 0);
		
	}

}
