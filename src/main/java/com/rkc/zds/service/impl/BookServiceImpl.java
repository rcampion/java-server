package com.rkc.zds.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rkc.zds.JavaServerApp;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.exceptions.DataAccessException;
import com.rkc.zds.service.BookService;
import com.rkc.zds.utils.Pagination;

public class BookServiceImpl implements BookService {

	@Override
	public PaginationPage<BookDto> findBooks(Pagination pagination) {

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

		// PageRequest pageRequest = PageRequest.of(0, size);

		// PageImpl<BookDto> page = new PageImpl<BookDto>(bookList, pageRequest, size);
		PaginationPage page = new PaginationPage(bookList, size);
		
		int count = 0;
		// Registering the Driver
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());

			// Getting the connection
			String mysqlUrl = "jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&serverTimezone=UTC&useLegacyDatetimeCode=false";
			Connection con = DriverManager.getConnection(mysqlUrl, "auth_user", "ChangeIt");
			System.out.println("Connection established......");
			// Creating the Statement object
			Statement stmt = con.createStatement();
			// Query to get the number of rows in a table
			String query = "SELECT COUNT(*) from Book";
			// Executing the query
			ResultSet rs = stmt.executeQuery(query);
			// Retrieving the result
			rs.next();
			count = rs.getInt(1);
			System.out.println("Number of records in the Book table: " + count);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		page.totalElements  = count;
		return page;
	}

}
