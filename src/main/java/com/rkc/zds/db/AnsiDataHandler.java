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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.rkc.zds.exceptions.DataAccessException;
import com.rkc.zds.exceptions.AppException;

import org.apache.commons.lang3.StringUtils;
import com.rkc.zds.exceptions.DataAccessException;

import com.rkc.zds.Environment;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.exceptions.AppException;
import com.rkc.zds.exceptions.AppMessage;
import com.rkc.zds.utils.AppLogger;
import com.rkc.zds.utils.Encryption;
import com.rkc.zds.utils.Pagination;

/**
 * Default handler for ANSI SQL compatible databases.
 */
public class AnsiDataHandler {

	/**
	 * Any topic lookup that takes longer than the specified time (in ms) will
	 * trigger a log message.
	 */
	private static final int TIME_LIMIT_TOPIC_LOOKUP = 20;
	/*
	 * private static final WikiCache<String, List<Interwiki>> CACHE_INTERWIKI_LIST
	 * = new WikiCache<String, List<Interwiki>>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_INTERWIKI_LIST");
	 * private static final WikiCache<String, List<Namespace>> CACHE_NAMESPACE_LIST
	 * = new WikiCache<String, List<Namespace>>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_NAMESPACE_LIST");
	 * private static final WikiCache<String, List<RoleMap>> CACHE_ROLE_MAP_GROUP =
	 * new WikiCache<String, List<RoleMap>>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_ROLE_MAP_GROUP");
	 * private static final WikiCache<String, String> CACHE_TOPIC_NAMES_BY_NAME =
	 * new WikiCache<String, String>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_TOPIC_NAMES_BY_NAME");
	 * private static final WikiCache<Integer, Topic> CACHE_TOPICS_BY_ID = new
	 * WikiCache<Integer,
	 * Topic>("com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_TOPICS_BY_ID");
	 * private static final WikiCache<String, Integer> CACHE_TOPIC_IDS_BY_NAME = new
	 * WikiCache<String, Integer>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_TOPIC_IDS_BY_NAME");
	 * private static final WikiCache<Integer, TopicVersion> CACHE_TOPIC_VERSIONS =
	 * new WikiCache<Integer, TopicVersion>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_TOPIC_VERSIONS");
	 * private static final WikiCache<String, Map<Object, UserBlock>>
	 * CACHE_USER_BLOCKS_ACTIVE = new WikiCache<String, Map<Object, UserBlock>>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_USER_BLOCKS_ACTIVE");
	 * private static final WikiCache<Integer, WikiUser> CACHE_USER_BY_USER_ID = new
	 * WikiCache<Integer, WikiUser>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_USER_BY_USER_ID");
	 * private static final WikiCache<String, WikiUser> CACHE_USER_BY_USER_NAME =
	 * new WikiCache<String, WikiUser>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_USER_BY_USER_NAME");
	 * private static final WikiCache<String, List<VirtualWiki>>
	 * CACHE_VIRTUAL_WIKI_LIST = new WikiCache<String, List<VirtualWiki>>(
	 * "com.rkc.zds.web.spring.wiki.db.AnsiDataHandler.CACHE_VIRTUAL_WIKI_LIST");
	 */
	private static final AppLogger logger = AppLogger.getLogger(AnsiDataHandler.class.getName());

	// TODO - remove when the ability to upgrade to 1.3 is deprecated
	private static final Map<String, String> LEGACY_DATA_HANDLER_MAP = new HashMap<String, String>();
	static {
		LEGACY_DATA_HANDLER_MAP.put("com.rkc.zds.db.AnsiDataHandler", QueryHandler.QUERY_HANDLER_ANSI);

		LEGACY_DATA_HANDLER_MAP.put("com.rkc.zds.db.MySqlDataHandler", QueryHandler.QUERY_HANDLER_MYSQL);

	}

	protected final QueryHandler queryHandler;
	protected AnsiDataValidator dataValidator = new AnsiDataValidator();

	/**
	 * 
	 */
	public AnsiDataHandler() {
		this.queryHandler = this.queryHandlerInstance();
	}

	/**
	 * 
	 * @param pagination
	 * @param descending
	 * @return
	 * @throws DataAccessException
	 */
	public List<BookDto> getBooks(Pagination pagination, boolean descending) throws DataAccessException {
		try {
			return this.queryHandler().getBooks(pagination, true);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * 
	 * @param book
	 * @throws DataAccessException
	 */
	public void addBook(BookDto book) throws DataAccessException {
		try {
			this.dataValidator.validateBook(book);
			this.queryHandler().createBook(book);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Read a book record from the Book table.
	 *
	 * @param book The Book record to be deleted.
	 * @throws DataAccessException Thrown if any error occurs during method
	 *                             execution.
	 */
	public BookDto lookupBookById(int bookId) throws DataAccessException {
		// public String lookupBookById(int bookId) throws DataAccessException {

		BookDto result = null;
		try {
			result = this.queryHandler().readBookById(bookId);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}

		return result;

	}

	/**
	 * Update a book record from the Book table.
	 * 
	 * @param book The Book record to be deleted
	 */
	public void updateBook(BookDto book) throws DataAccessException {
		this.dataValidator.validateBook(book);
		try {
			this.queryHandler().updateBook(book);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}



	/**
	 * Delete an book record from the Book table.
	 *
	 * @param book The Book record to be deleted.
	 * @throws DataAccessException Thrown if any error occurs during method
	 *                             execution.
	 */
	public void deleteBook(int bookId) throws DataAccessException {
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			this.queryHandler().deleteBook(bookId);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
		// CACHE_BOOK_LIST.removeAllFromCache();
	}

	/**
	 *
	 * / private void addWikiUser(WikiUser user, Connection conn) throws
	 * DataAccessException, WikiException { try {
	 * this.dataValidator.validateWikiUser(user);
	 * this.queryHandler().insertWikiUser(user, conn); } catch (SQLException e) {
	 * throw new DataAccessException(e); } }
	 * 
	 * /** Determine if a value matching the given username and password exists in
	 * the data store.
	 *
	 * @param username The username that is being validated against.
	 * @param password The password that is being validated against.
	 * @return <code>true</code> if the username / password combination matches an
	 *         existing record in the data store, <code>false</code> otherwise.
	 * @throws DataAccessException Thrown if an error occurs while accessing the
	 *                             data store.
	 */
	public boolean authenticate(String username, String password) throws DataAccessException {
		if (StringUtils.isBlank(password)) {
			return false;
		}
		Connection conn = null;
		try {
			conn = DatabaseConnection.getConnection();
			// password is stored encrypted, so encrypt password
			String encryptedPassword = Encryption.encrypt(password);
			return this.queryHandler().authenticateUser(username, encryptedPassword, conn);
		} catch (SQLException e) {
			throw new DataAccessException(e);
		} finally {
			DatabaseConnection.closeConnection(conn);
		}
	}

	/**
	 * Delete an interwiki record from the interwiki table.
	 *
	 * @param interwiki The Interwiki record to be deleted.
	 * @throws DataAccessException Thrown if any error occurs during method
	 *                             execution. / public void
	 *                             deleteInterwiki(Interwiki interwiki) throws
	 *                             DataAccessException { Connection conn = null; try
	 *                             { conn = DatabaseConnection.getConnection();
	 *                             this.queryHandler().deleteInterwiki(interwiki,
	 *                             conn); } catch (SQLException e) { throw new
	 *                             DataAccessException(e); } finally {
	 *                             DatabaseConnection.closeConnection(conn); }
	 *                             CACHE_INTERWIKI_LIST.removeAllFromCache(); }
	 * 
	 * 
	 *                             /** Determine the largest namespace ID for all
	 *                             current defined namespaces. / private int
	 *                             findMaxNamespaceId() throws DataAccessException {
	 *                             int namespaceEnd = 0; for (Namespace namespace :
	 *                             this.lookupNamespaces()) { namespaceEnd =
	 *                             (namespace.getId() > namespaceEnd) ?
	 *                             namespace.getId() : namespaceEnd; } return
	 *                             namespaceEnd; }
	 * 
	 * 
	 *                             /**
	 *
	 */
	protected final QueryHandler queryHandler() {
		return this.queryHandler;
	}

	/**
	 * Utility method to retrieve an instance of the current query handler.
	 *
	 * @return An instance of the current query handler.
	 * @throws IllegalStateException Thrown if a data handler instance can not be
	 *                               instantiated.
	 */
	private QueryHandler queryHandlerInstance() {
		if (StringUtils.isBlank(Environment.getValue(Environment.PROP_DB_TYPE))) {
			// this is a problem, but it should never occur
			logger.warn("AnsiDataHandler.queryHandlerInstance called without a valid PROP_DB_TYPE value");
		}
		String queryHandlerClass = Environment.getValue(Environment.PROP_DB_TYPE);
		// TODO - remove when the ability to upgrade to 1.3 is removed
		String dataHandlerClass = LEGACY_DATA_HANDLER_MAP.get(queryHandlerClass);
		if (dataHandlerClass != null) {
			queryHandlerClass = dataHandlerClass;
			Environment.setValue(Environment.PROP_DB_TYPE, queryHandlerClass);
			try {
				Environment.saveConfiguration();
			} catch (AppException e) {
				throw new IllegalStateException("Failure while updating properties", e);
			}
		}
		try {
			// return (QueryHandler)ResourceUtil.instantiateClass(queryHandlerClass);
			return new AnsiQueryHandler();
		} catch (ClassCastException e) {
			throw new IllegalStateException(
					"Query handler specified in application.properties does not implement com.rkc.zds.web.spring.wiki.db.QueryHandler: "
							+ dataHandlerClass);
		}
	}

	public void writeConfiguration(Map<String, String> propertiesToMap) throws DataAccessException, AppException {
		// TODO Auto-generated method stub

	}
}
