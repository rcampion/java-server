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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.rkc.zds.dto.Book;
/*
import com.rkc.zds.web.spring.wiki.model.Category;
import com.rkc.zds.web.spring.wiki.model.GroupMap;
import com.rkc.zds.web.spring.wiki.model.ImageData;
import com.rkc.zds.web.spring.wiki.model.Interwiki;
import com.rkc.zds.web.spring.wiki.model.LogItem;
import com.rkc.zds.web.spring.wiki.model.Namespace;
import com.rkc.zds.web.spring.wiki.model.RecentChange;
import com.rkc.zds.web.spring.wiki.model.Role;
import com.rkc.zds.web.spring.wiki.model.RoleMap;
import com.rkc.zds.web.spring.wiki.model.Topic;
import com.rkc.zds.web.spring.wiki.model.TopicType;
import com.rkc.zds.web.spring.wiki.model.TopicVersion;
import com.rkc.zds.web.spring.wiki.model.UserBlock;
import com.rkc.zds.web.spring.wiki.model.VirtualWiki;
import com.rkc.zds.web.spring.wiki.model.WikiFile;
import com.rkc.zds.web.spring.wiki.model.WikiFileVersion;
import com.rkc.zds.web.spring.wiki.model.WikiGroup;
import com.rkc.zds.web.spring.wiki.model.WikiUser;
import com.rkc.zds.web.spring.wiki.model.WikiUserDetails;
*/
import com.rkc.zds.utils.Pagination;

/**
 * This interface provides all methods needed for retrieving, inserting, or updating
 * data from the database.
 */
public interface QueryHandler {

	/** Ansi query handler class */
	public static final String QUERY_HANDLER_ANSI = "com.rkc.zds.db.AnsiQueryHandler";
	/** MySql query handler class */
	public static final String QUERY_HANDLER_MYSQL = "com.rkc.zds.db.MySqlQueryHandler";

	/**
	 * Retrieve a result set containing all user information for a given WikiUser.
	 *
	 * @param login The login of the user record being retrieved.
	 * @param encryptedPassword The encrypted password for the user record being
	 *  retrieved.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return <code>true</code> if the login and password matches an existing
	 *  user, <code>false</code> otherwise.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	boolean authenticateUser(String login, String encryptedPassword, Connection conn) throws SQLException;

	/**
	 * Some databases support automatically incrementing primary key values without the
	 * need to explicitly specify a value, thus improving performance.  This method provides
	 * a way for the a query handler to specify whether or not auto-incrementing is supported.
	 *
	 * @return <code>true</code> if the query handler supports auto-incrementing primary keys.
	 */
	boolean autoIncrementPrimaryKeys();

	/**
	 * Returns the simplest possible query that can be used to validate
	 * whether or not a database connection is valid.  Note that the query
	 * returned MUST NOT query any Application tables since it will be used prior
	 * to setting up the Application tables.
	 *
	 * @return Returns a simple query that can be used to validate a database
	 *  connection.
	 */
	String connectionValidationQuery();

	/**
	 * Return a simple query, that if successfully run indicates that Application
	 * tables have been initialized in the database.
	 *
	 * @return Returns a simple query that, if successfully run, indicates
	 *  that Application tables have been set up in the database.
	 */
	
	String existenceValidationQuery();

	/**
	 * Method called to set up all Application system tables, indexes, and other
	 * required database objects.  If a failure occurs during object creation
	 * then this method will not attempt to clean up any objects that were
	 * created prior to the failure.
	 *
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 * /
	void createTables(Connection conn) throws SQLException;
	
	*/
	
	/**
	 * Delete all authorities for a specific user.
	 *
	 * @param username The username for which authorities are being deleted.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteUserAuthorities(String username, Connection conn) throws SQLException;

	/**
	 * Retrieve a list of all books.  The
	 * list may be limited by specifying the number of results to retrieve in a Pagination
	 * object.
	 *
	 * @param pagination A Pagination object that specifies the number of results
	 *  and starting result offset for the result set to be retrieved.
	 * @param b 
	 * @return A list of all books
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	//List<Book> getBooks(Pagination pagination, boolean b) throws SQLException;
	String getBooks(Pagination pagination, boolean b) throws SQLException;

	/**
	 * Create a book record in the database.
	 *
	 * @param book The Book record that is to be created in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void createBook(Book book) throws SQLException;

	/**
	 * Read a book record in the database.
	 *
	 * @param bookId The Book ID that is to be read from the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @return A  book
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	//
	//Book readBookById(int bookId) throws SQLException;
	String readBookById(int bookId) throws SQLException;
	
	/**
	 * Update a book record in the database.
	 *
	 * @param book The Book record that is to be updated in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void updateBook(Book book) throws SQLException;

	/**
	 * Delete a book record in the database.
	 *
	 * @param book The Book record that is to be deleted in the database.
	 * @param conn A database connection to use when connecting to the database
	 *  from this method.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	void deleteBook(Book book) throws SQLException;


}
