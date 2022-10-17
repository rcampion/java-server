/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, version 2.1, dated February 1999.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the latest version of the GNU Lesser General
 * Public License as published by the Free Software Foundation;
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program (LICENSE.txt); if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.rkc.zds.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.rkc.zds.Environment;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.utils.AppLogger;
import com.rkc.zds.utils.Pagination;

/**
 * Default implementation of the QueryHandler implementation for retrieving,
 * inserting, and updating data in the database. This method uses ANSI SQL and
 * should therefore work with any fully ANSI-compliant database.
 */
public class AnsiQueryHandler implements QueryHandler {

	private static final AppLogger logger = AppLogger.getLogger(AnsiQueryHandler.class.getName());
	protected static final String SQL_PROPERTY_FILE_NAME = "sql/sql.ansi.properties";

	protected static String STATEMENT_CONNECTION_VALIDATION_QUERY = null;

	protected static String STATEMENT_CREATE_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_CREATE_BOOKS_TABLE = null;
	protected static String STATEMENT_CREATE_CATEGORY_TABLE = null;
	protected static String STATEMENT_CREATE_CATEGORY_INDEX = null;
	
	protected static String STATEMENT_DELETE_BOOKS = null;
	protected static String STATEMENT_DELETE_AUTHORITIES = null;
	
	protected static String STATEMENT_DROP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_DROP_BOOKS_TABLE = null;
	protected static String STATEMENT_DROP_CATEGORY_TABLE = null;
	
	protected static String STATEMENT_INSERT_BOOK = null;

	protected static String STATEMENT_INSERT_AUTHORITY = null;
	protected static String STATEMENT_INSERT_CATEGORY = null;
	
	

	protected static String STATEMENT_SELECT_BOOKS = null;
	protected static String STATEMENT_SELECT_BOOK_BY_ID = null;

	protected static String STATEMENT_SELECT_AUTHORITIES_AUTHORITY = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_LOGIN = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_USER = null;

	protected static String STATEMENT_SELECT_CATEGORIES = null;

	protected static String STATEMENT_SELECT_USERS_AUTHENTICATION = null;

	protected static String STATEMENT_UPDATE_BOOK = null;

	private Properties props = null;

	/**
	 *
	 */
	protected AnsiQueryHandler() {
		props = Environment.loadProperties(SQL_PROPERTY_FILE_NAME);
		this.init(props);
	}

	/**
	 *
	 */
	public boolean authenticateUser(String username, String encryptedPassword, Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(STATEMENT_SELECT_USERS_AUTHENTICATION);
			stmt.setString(1, username);
			stmt.setString(2, encryptedPassword);
			return (stmt.executeQuery().next());
		} finally {
			DatabaseConnection.closeStatement(stmt);
		}
	}

	/**
	 *
	 */
	public boolean autoIncrementPrimaryKeys() {
		return false;
	}

	/**
	 *
	 */
	public String connectionValidationQuery() {
		return STATEMENT_CONNECTION_VALIDATION_QUERY;
	}

	/**
	 */
	public void createTables(Connection conn) throws SQLException {
		DatabaseConnection.executeUpdate(STATEMENT_CREATE_BOOKS_TABLE, conn);
	}

	/**
	 * @param sql    The SQL statement in MessageFormat format ("date is {0} null").
	 * @param params An array of objects (which should be strings) to use when
	 *               formatting the message.
	 * @return A formatted SQL string.
	 */
	protected String formatStatement(String sql, Object[] params) {
		if (params == null || params.length == 0) {
			return sql;
		}
		try {
			// replace all single quotes with '' since otherwise MessageFormat
			// will treat the content is a quoted string
			return MessageFormat.format(sql.replaceAll("'", "''"), params);
		} catch (IllegalArgumentException e) {
			String msg = "Unable to format " + sql + " with values: ";
			for (int i = 0; i < params.length; i++) {
				msg += (i > 0) ? " | " + params[i] : params[i];
			}
			logger.warn(msg);
			return null;
		}
	}


	protected void init(Properties properties) {
		this.props = properties;


		STATEMENT_CONNECTION_VALIDATION_QUERY = props.getProperty("STATEMENT_CONNECTION_VALIDATION_QUERY");

		STATEMENT_CREATE_AUTHORITIES_TABLE = props.getProperty("STATEMENT_CREATE_AUTHORITIES_TABLE");
		STATEMENT_CREATE_CATEGORY_TABLE = props.getProperty("STATEMENT_CREATE_CATEGORY_TABLE");
		STATEMENT_CREATE_CATEGORY_INDEX = props.getProperty("STATEMENT_CREATE_CATEGORY_INDEX");
		STATEMENT_DELETE_AUTHORITIES = props.getProperty("STATEMENT_DELETE_AUTHORITIES");

		STATEMENT_DELETE_AUTHORITIES = props.getProperty("STATEMENT_DELETE_AUTHORITIES");
		
		
		STATEMENT_DROP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_DROP_AUTHORITIES_TABLE");
		STATEMENT_DROP_CATEGORY_TABLE = props.getProperty("STATEMENT_DROP_CATEGORY_TABLE");
		STATEMENT_INSERT_AUTHORITY = props.getProperty("STATEMENT_INSERT_AUTHORITY");
		
		STATEMENT_INSERT_BOOK = props.getProperty("STATEMENT_INSERT_BOOK");
		
		STATEMENT_INSERT_CATEGORY = props.getProperty("STATEMENT_INSERT_CATEGORY");
		STATEMENT_SELECT_AUTHORITIES_AUTHORITY = props.getProperty("STATEMENT_SELECT_AUTHORITIES_AUTHORITY");
		STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL = props.getProperty("STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL");
		STATEMENT_SELECT_AUTHORITIES_LOGIN = props.getProperty("STATEMENT_SELECT_AUTHORITIES_LOGIN");
		STATEMENT_SELECT_AUTHORITIES_USER = props.getProperty("STATEMENT_SELECT_AUTHORITIES_USER");
		
		STATEMENT_SELECT_BOOKS = props.getProperty("STATEMENT_SELECT_BOOKS");
		STATEMENT_SELECT_BOOK_BY_ID = props.getProperty("STATEMENT_SELECT_BOOK_BY_ID");
		
		STATEMENT_SELECT_CATEGORIES = props.getProperty("STATEMENT_SELECT_CATEGORIES");

		STATEMENT_UPDATE_BOOK = props.getProperty("STATEMENT_UPDATE_BOOK");
		
		
		
	}

	@Override
	public String existenceValidationQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUserAuthorities(String username, Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(STATEMENT_DELETE_AUTHORITIES);
			stmt.setString(1, username);
			stmt.executeUpdate();
		} finally {
			DatabaseConnection.closeStatement(stmt);
		}
	}

	@Override
	public List<BookDto> getBooks(Pagination pagination, boolean b) throws SQLException {
//	public String getBooks(Pagination pagination, boolean b) throws SQLException {
		
		List<BookDto> bookList = new ArrayList<BookDto>();
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DatabaseConnection.getConnection();
			stmt = this.getBooksStatement(conn, pagination);
			rs = stmt.executeQuery();

			while (rs.next()) {
				BookDto book = new BookDto();
				book.setId(rs.getString("id"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
//				BookCategoryEnum e = new BookCategoryEnum();
				book.setCategory(rs.getInt("category"));
				bookList.add(book);
			}

		} finally {
			DatabaseConnection.closeConnection(conn, stmt, rs);
		}
		
		return bookList;
	}

	/**
	 *
	 */
	protected PreparedStatement getBooksStatement(Connection conn, Pagination pagination) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement(STATEMENT_SELECT_BOOKS);
		stmt.setInt(1, pagination.getNumResults());
		stmt.setInt(2, pagination.getOffset());
		return stmt;
	}

	/**
	 *
	 */
	private BookDto initBook(ResultSet rs) throws SQLException {
		BookDto book = new BookDto();
		book.setId(rs.getString("id"));
		book.setTitle(rs.getString("Title"));
		book.setAuthor(rs.getString("Author"));
		book.setCategory(rs.getInt("Category"));
		return book;
	}

	@Override
	public void createBook(BookDto book) throws SQLException {
		PreparedStatement stmt = null;
		try {
			Connection conn = DatabaseConnection.getConnection();

			stmt = conn.prepareStatement(STATEMENT_INSERT_BOOK);
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setInt(3, book.getCategory());

			stmt.executeUpdate();
		} finally {
			DatabaseConnection.closeStatement(stmt);
		}

	}

	@Override
	public BookDto readBookById(int bookId) throws SQLException {

		Connection conn = DatabaseConnection.getConnection();

		boolean closeConnection = (conn == null);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			if (conn == null) {
				conn = DatabaseConnection.getConnection();
			}
			stmt = conn.prepareStatement(STATEMENT_SELECT_BOOK_BY_ID);
			stmt.setInt(1, bookId);
			rs = stmt.executeQuery();
			// return (rs.next()) ? this.initBook(rs) : null;

			rs.next();
			
			BookDto book = this.initBook(rs);
			
			return book;
			
		} finally {
			if (closeConnection) {
				DatabaseConnection.closeConnection(conn, stmt, rs);
			} else {
				// close only the statement and result set - leave the connection open for
				// further use
				DatabaseConnection.closeConnection(null, stmt, rs);
			}
		}

	}

	@Override
	public void updateBook(BookDto book) throws SQLException {
		PreparedStatement stmt = null;

		try {
			Connection conn = DatabaseConnection.getConnection();
			stmt = conn.prepareStatement(STATEMENT_UPDATE_BOOK);
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setInt(3, book.getCategory());
			stmt.setString(4, book.getId());
			stmt.executeUpdate();
		} finally {
			DatabaseConnection.closeStatement(stmt);
		}

	}
	
	/**
	 *
	 */
	public void updateBook(BookDto book, Connection conn) throws SQLException {
		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(STATEMENT_UPDATE_BOOK);
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setInt(3, book.getCategory());
			stmt.setString(4, book.getId());
			stmt.executeUpdate();
		} finally {
			DatabaseConnection.closeStatement(stmt);
		}
	}
	
	@Override
	public void deleteBook(BookDto book) throws SQLException {
		// TODO Auto-generated method stub

	}

}
