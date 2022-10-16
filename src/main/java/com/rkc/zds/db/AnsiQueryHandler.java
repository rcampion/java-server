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
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Properties;

// import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
import com.rkc.zds.db.DatabaseConnection;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.enums.BookCategoryEnum;
import com.rkc.zds.utils.AppLogger;
import com.rkc.zds.Environment;

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
	protected static String STATEMENT_CREATE_CONFIGURATION_TABLE = null;
	protected static String STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_CREATE_GROUP_MEMBERS_TABLE = null;
	protected static String STATEMENT_CREATE_GROUP_TABLE = null;
	protected static String STATEMENT_CREATE_INTERWIKI_TABLE = null;
	protected static String STATEMENT_CREATE_LOG_TABLE = null;
	protected static String STATEMENT_CREATE_NAMESPACE_TABLE = null;
	protected static String STATEMENT_CREATE_NAMESPACE_TRANSLATION_TABLE = null;
	protected static String STATEMENT_CREATE_RECENT_CHANGE_TABLE = null;
	protected static String STATEMENT_CREATE_ROLE_TABLE = null;
	protected static String STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT = null;
	protected static String STATEMENT_CREATE_TOPIC_TABLE = null;
	protected static String STATEMENT_CREATE_TOPIC_LINKS_TABLE = null;
	protected static String STATEMENT_CREATE_TOPIC_LINKS_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_PAGE_NAME_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_PAGE_NAME_LOWER_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_NAMESPACE_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_VIRTUAL_WIKI_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_CURRENT_VERSION_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_VERSION_TABLE = null;
	protected static String STATEMENT_CREATE_TOPIC_VERSION_TOPIC_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_VERSION_PREVIOUS_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_VERSION_USER_DISPLAY_INDEX = null;
	protected static String STATEMENT_CREATE_TOPIC_VERSION_USER_ID_INDEX = null;
	protected static String STATEMENT_CREATE_USER_BLOCK_TABLE = null;
	protected static String STATEMENT_CREATE_USERS_TABLE = null;
	protected static String STATEMENT_CREATE_VIRTUAL_WIKI_TABLE = null;
	protected static String STATEMENT_CREATE_WATCHLIST_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_FILE_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_USER_TABLE = null;
	protected static String STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX = null;
	protected static String STATEMENT_CREATE_USER_PREFERENCES_DEFAULTS_TABLE = null;
	protected static String STATEMENT_CREATE_USER_PREFERENCES_TABLE = null;
	protected static String STATEMENT_CREATE_USER_PREFERENCES_WIKI_USER_INDEX = null;
	protected static String STATEMENT_DELETE_BOOKS = null;
	protected static String STATEMENT_DELETE_AUTHORITIES = null;
	protected static String STATEMENT_DELETE_CONFIGURATION = null;
	protected static String STATEMENT_DELETE_GROUP_AUTHORITIES = null;
	protected static String STATEMENT_DELETE_GROUP_MAP_GROUP = null;
	protected static String STATEMENT_DELETE_GROUP_MAP_USER = null;
	protected static String STATEMENT_DELETE_INTERWIKI = null;
	protected static String STATEMENT_DELETE_LOG_ITEMS = null;
	protected static String STATEMENT_DELETE_LOG_ITEMS_BY_TOPIC_VERSION = null;
	protected static String STATEMENT_DELETE_NAMESPACE_TRANSLATIONS = null;
	protected static String STATEMENT_DELETE_RECENT_CHANGES = null;
	protected static String STATEMENT_DELETE_RECENT_CHANGES_TOPIC = null;
	protected static String STATEMENT_DELETE_RECENT_CHANGES_TOPIC_VERSION = null;
	protected static String STATEMENT_DELETE_TOPIC_CATEGORIES = null;
	protected static String STATEMENT_DELETE_TOPIC_LINKS = null;
	protected static String STATEMENT_DELETE_TOPIC_VERSION = null;
	protected static String STATEMENT_DELETE_WATCHLIST_ENTRY = null;
	protected static String STATEMENT_DELETE_USER_PREFERENCES = null;
	protected static String STATEMENT_DROP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_DROP_CATEGORY_TABLE = null;
	protected static String STATEMENT_DROP_CONFIGURATION_TABLE = null;
	protected static String STATEMENT_DROP_GROUP_AUTHORITIES_TABLE = null;
	protected static String STATEMENT_DROP_GROUP_MEMBERS_TABLE = null;
	protected static String STATEMENT_DROP_GROUP_TABLE = null;
	protected static String STATEMENT_DROP_INTERWIKI_TABLE = null;
	protected static String STATEMENT_DROP_LOG_TABLE = null;
	protected static String STATEMENT_DROP_NAMESPACE_TABLE = null;
	protected static String STATEMENT_DROP_NAMESPACE_TRANSLATION_TABLE = null;
	protected static String STATEMENT_DROP_RECENT_CHANGE_TABLE = null;
	protected static String STATEMENT_DROP_ROLE_TABLE = null;
	protected static String STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT = null;
	protected static String STATEMENT_DROP_TOPIC_TABLE = null;
	protected static String STATEMENT_DROP_TOPIC_LINKS_TABLE = null;
	protected static String STATEMENT_DROP_TOPIC_VERSION_TABLE = null;
	protected static String STATEMENT_DROP_USER_BLOCK_TABLE = null;
	protected static String STATEMENT_DROP_USERS_TABLE = null;
	protected static String STATEMENT_DROP_VIRTUAL_WIKI_TABLE = null;
	protected static String STATEMENT_DROP_WATCHLIST_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_FILE_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_FILE_VERSION_TABLE = null;
	protected static String STATEMENT_DROP_WIKI_USER_TABLE = null;

	protected static String STATEMENT_INSERT_BOOK = null;

	protected static String STATEMENT_INSERT_AUTHORITY = null;
	protected static String STATEMENT_INSERT_CATEGORY = null;
	protected static String STATEMENT_INSERT_CONFIGURATION = null;
	protected static String STATEMENT_INSERT_GROUP = null;
	protected static String STATEMENT_INSERT_GROUP_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_GROUP_AUTHORITY = null;
	protected static String STATEMENT_INSERT_GROUP_MEMBER = null;
	protected static String STATEMENT_INSERT_GROUP_MEMBER_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_INTERWIKI = null;
	protected static String STATEMENT_INSERT_LOG_ITEM = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_BLOCK = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_BY_TOPIC_VERSION_TYPE = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_IMPORT = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_MOVE = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_UNBLOCK = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_UPLOAD = null;
	protected static String STATEMENT_INSERT_LOG_ITEMS_USER = null;
	protected static String STATEMENT_INSERT_NAMESPACE = null;
	protected static String STATEMENT_INSERT_NAMESPACE_TRANSLATION = null;
	protected static String STATEMENT_INSERT_RECENT_CHANGE = null;
	protected static String STATEMENT_INSERT_RECENT_CHANGES_LOGS = null;
	protected static String STATEMENT_INSERT_RECENT_CHANGES_VERSIONS = null;
	protected static String STATEMENT_INSERT_ROLE = null;
	protected static String STATEMENT_INSERT_TOPIC = null;
	protected static String STATEMENT_INSERT_TOPIC_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_TOPIC_LINKS = null;
	protected static String STATEMENT_INSERT_TOPIC_VERSION = null;
	protected static String STATEMENT_INSERT_TOPIC_VERSION_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_USER = null;
	protected static String STATEMENT_INSERT_USER_BLOCK = null;
	protected static String STATEMENT_INSERT_USER_BLOCK_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_VIRTUAL_WIKI = null;
	protected static String STATEMENT_INSERT_VIRTUAL_WIKI_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_WATCHLIST_ENTRY = null;
	protected static String STATEMENT_INSERT_WIKI_FILE = null;
	protected static String STATEMENT_INSERT_WIKI_FILE_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_WIKI_FILE_VERSION = null;
	protected static String STATEMENT_INSERT_WIKI_FILE_VERSION_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_WIKI_USER = null;
	protected static String STATEMENT_INSERT_WIKI_USER_AUTO_INCREMENT = null;
	protected static String STATEMENT_INSERT_USER_PREFERENCE = null;
	protected static String STATEMENT_INSERT_USER_PREFERENCE_DEFAULTS = null;

	protected static String STATEMENT_SELECT_BOOKS = null;
	protected static String STATEMENT_SELECT_BOOK_BY_ID = null;

	protected static String STATEMENT_SELECT_AUTHORITIES_AUTHORITY = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_LOGIN = null;
	protected static String STATEMENT_SELECT_AUTHORITIES_USER = null;

	protected static String STATEMENT_SELECT_CATEGORIES = null;
	protected static String STATEMENT_SELECT_CATEGORY_TOPICS = null;
	protected static String STATEMENT_SELECT_CONFIGURATION = null;
	protected static String STATEMENT_SELECT_GROUP_MAP_GROUP = null;
	protected static String STATEMENT_SELECT_GROUP_MAP_USER = null;
	protected static String STATEMENT_SELECT_GROUP_MAP_AUTHORITIES = null;
	protected static String STATEMENT_SELECT_GROUPS = null;
	protected static String STATEMENT_SELECT_GROUP = null;
	protected static String STATEMENT_SELECT_GROUP_BY_ID = null;
	protected static String STATEMENT_SELECT_GROUP_AUTHORITIES = null;
	protected static String STATEMENT_SELECT_GROUPS_AUTHORITIES = null;
	protected static String STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE = null;
	protected static String STATEMENT_SELECT_GROUP_SEQUENCE = null;
	protected static String STATEMENT_SELECT_INTERWIKIS = null;
	protected static String STATEMENT_SELECT_LOG_ITEMS = null;
	protected static String STATEMENT_SELECT_LOG_ITEMS_BY_TYPE = null;
	protected static String STATEMENT_SELECT_NAMESPACE_SEQUENCE = null;
	protected static String STATEMENT_SELECT_NAMESPACES = null;
	protected static String STATEMENT_SELECT_PW_RESET_CHALLENGE_DATA = null;
	protected static String STATEMENT_SELECT_RECENT_CHANGES = null;
	protected static String STATEMENT_SELECT_ROLES = null;
	protected static String STATEMENT_SELECT_TOPIC_BY_ID = null;
	protected static String STATEMENT_SELECT_TOPIC_BY_TYPE = null;
	protected static String STATEMENT_SELECT_TOPIC_COUNT = null;
	protected static String STATEMENT_SELECT_TOPIC = null;
	protected static String STATEMENT_SELECT_TOPIC_HISTORY = null;
	protected static String STATEMENT_SELECT_TOPIC_LINK_ORPHANS = null;
	protected static String STATEMENT_SELECT_TOPIC_LINKS = null;
	protected static String STATEMENT_SELECT_TOPIC_LOWER = null;
	protected static String STATEMENT_SELECT_TOPIC_NAME = null;
	protected static String STATEMENT_SELECT_TOPIC_NAME_LOWER = null;
	protected static String STATEMENT_SELECT_TOPIC_NAMES = null;
	protected static String STATEMENT_SELECT_TOPICS_ADMIN = null;
	protected static String STATEMENT_SELECT_TOPIC_SEQUENCE = null;
	protected static String STATEMENT_SELECT_TOPIC_VERSION = null;
	protected static String STATEMENT_SELECT_TOPIC_VERSION_NEXT_ID = null;
	protected static String STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE = null;
	protected static String STATEMENT_SELECT_USER_BLOCKS = null;
	protected static String STATEMENT_SELECT_USER_BLOCK_SEQUENCE = null;

	protected static String STATEMENT_SELECT_USERS_AUTHENTICATION = null;

	protected static String STATEMENT_SELECT_VIRTUAL_WIKIS = null;
	protected static String STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WATCHLIST = null;
	protected static String STATEMENT_SELECT_WATCHLIST_CHANGES = null;
	protected static String STATEMENT_SELECT_WIKI_FILE = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_COUNT = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WIKI_FILE_VERSIONS = null;
	protected static String STATEMENT_SELECT_WIKI_USER = null;
	protected static String STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS = null;
	protected static String STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN = null;
	protected static String STATEMENT_SELECT_WIKI_USER_COUNT = null;
	protected static String STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD = null;
	protected static String STATEMENT_SELECT_WIKI_USER_LOGIN = null;
	protected static String STATEMENT_SELECT_WIKI_USER_SEQUENCE = null;
	protected static String STATEMENT_SELECT_WIKI_USERS = null;
	protected static String STATEMENT_SELECT_USER_PREFERENCES_DEFAULTS = null;
	protected static String STATEMENT_SELECT_USER_PREFERENCES = null;
	protected static String STATEMENT_UPDATE_GROUP = null;
	protected static String STATEMENT_UPDATE_ROLE = null;
	protected static String STATEMENT_UPDATE_NAMESPACE = null;
	protected static String STATEMENT_UPDATE_PW_RESET_CHALLENGE_DATA = null;
	protected static String STATEMENT_UPDATE_RECENT_CHANGES_PREVIOUS_VERSION_ID = null;
	protected static String STATEMENT_UPDATE_TOPIC = null;
	protected static String STATEMENT_UPDATE_TOPIC_NAMESPACE = null;
	protected static String STATEMENT_UPDATE_TOPIC_VERSION = null;
	protected static String STATEMENT_UPDATE_TOPIC_VERSION_PREVIOUS_VERSION_ID = null;
	protected static String STATEMENT_UPDATE_USER = null;
	protected static String STATEMENT_UPDATE_USER_BLOCK = null;
	protected static String STATEMENT_UPDATE_VIRTUAL_WIKI = null;
	protected static String STATEMENT_UPDATE_WIKI_FILE = null;
	protected static String STATEMENT_UPDATE_WIKI_USER = null;
	protected static String STATEMENT_UPDATE_USER_PREFERENCE_DEFAULTS = null;
	protected static String STATEMENT_CREATE_FILE_DATA_TABLE = null;
	protected static String STATEMENT_DROP_FILE_DATA_TABLE = null;
	protected static String STATEMENT_INSERT_FILE_DATA = null;
	protected static String STATEMENT_DELETE_RESIZED_IMAGES = null;
	protected static String STATEMENT_SELECT_FILE_INFO = null;
	protected static String STATEMENT_SELECT_FILE_DATA = null;
	protected static String STATEMENT_SELECT_FILE_VERSION_DATA = null;
	protected static String STATEMENT_CREATE_SEQUENCES = null;
	protected static String STATEMENT_DROP_SEQUENCES = null;

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

	/**
	 *
	 * / public List<WikiFileVersion> getAllWikiFileVersions(WikiFile wikiFile,
	 * boolean descending) throws SQLException { Connection conn = null;
	 * PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_WIKI_FILE_VERSIONS); // FIXME - sort
	 * order ignored stmt.setInt(1, wikiFile.getFileId()); rs = stmt.executeQuery();
	 * List<WikiFileVersion> fileVersions = new ArrayList<WikiFileVersion>(); while
	 * (rs.next()) { fileVersions.add(this.initWikiFileVersion(rs)); } return
	 * fileVersions; } finally { DatabaseConnection.closeConnection(conn, stmt, rs);
	 * } }
	 * 
	 * /**
	 *
	 * / public List<Category> getCategories(int virtualWikiId, String
	 * virtualWikiName, Pagination pagination) throws SQLException { Connection conn
	 * = null; PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt = this.getCategoriesStatement(conn,
	 * virtualWikiId, virtualWikiName, pagination); rs = stmt.executeQuery();
	 * List<Category> results = new ArrayList<Category>(); while (rs.next()) {
	 * Category category = new Category();
	 * category.setName(rs.getString("category_name")); // child topic name not
	 * initialized since it is not needed category.setVirtualWiki(virtualWikiName);
	 * category.setSortKey(rs.getString("sort_key")); // topic type not initialized
	 * since it is not needed results.add(category); } return results; } finally {
	 * DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / protected PreparedStatement getCategoriesStatement(Connection conn, int
	 * virtualWikiId, String virtualWikiName, Pagination pagination) throws
	 * SQLException { PreparedStatement stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_CATEGORIES); stmt.setInt(1,
	 * virtualWikiId); stmt.setInt(2, pagination.getNumResults()); stmt.setInt(3,
	 * pagination.getOffset()); return stmt; }
	 * 
	 * /**
	 *
	 * / public List<LogItem> getLogItems(int virtualWikiId, String virtualWikiName,
	 * int logType, Pagination pagination, boolean descending) throws SQLException {
	 * Connection conn = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * List<LogItem> logItems = new ArrayList<LogItem>(); try { conn =
	 * DatabaseConnection.getConnection(); stmt = this.getLogItemsStatement(conn,
	 * virtualWikiId, virtualWikiName, logType, pagination, descending); // FIXME -
	 * sort order ignored rs = stmt.executeQuery(); while (rs.next()) {
	 * logItems.add(this.initLogItem(rs, virtualWikiName)); } return logItems; }
	 * finally { DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / protected PreparedStatement getLogItemsStatement(Connection conn, int
	 * virtualWikiId, String virtualWikiName, int logType, Pagination pagination,
	 * boolean descending) throws SQLException { int index = 1; PreparedStatement
	 * stmt = null; if (logType == -1) { stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_LOG_ITEMS); } else { stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_LOG_ITEMS_BY_TYPE);
	 * stmt.setInt(index++, logType); } stmt.setInt(index++, virtualWikiId);
	 * stmt.setInt(index++, pagination.getNumResults()); stmt.setInt(index++,
	 * pagination.getOffset()); return stmt; }
	 * 
	 * /**
	 *
	 * / public List<RecentChange> getRecentChanges(String virtualWiki, Pagination
	 * pagination, boolean descending) throws SQLException { Connection conn = null;
	 * PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt =
	 * this.getRecentChangesStatement(conn, virtualWiki, pagination, descending); //
	 * FIXME - sort order ignored rs = stmt.executeQuery(); List<RecentChange>
	 * recentChanges = new ArrayList<RecentChange>(); while (rs.next()) {
	 * recentChanges.add(this.initRecentChange(rs)); } return recentChanges; }
	 * finally { DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / protected PreparedStatement getRecentChangesStatement(Connection conn,
	 * String virtualWiki, Pagination pagination, boolean descending) throws
	 * SQLException { PreparedStatement stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_RECENT_CHANGES); stmt.setString(1,
	 * virtualWiki); stmt.setInt(2, pagination.getNumResults()); stmt.setInt(3,
	 * pagination.getOffset()); return stmt; }
	 * 
	 * /**
	 *
	 * / public List<RoleMap> getRoleMapByLogin(String loginFragment) throws
	 * SQLException { if (StringUtils.isBlank(loginFragment)) { return new
	 * ArrayList<RoleMap>(); } Connection conn = null; PreparedStatement stmt =
	 * null; ResultSet rs = null; try { conn = DatabaseConnection.getConnection();
	 * stmt = conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_LOGIN);
	 * loginFragment = '%' + loginFragment.toLowerCase() + '%'; stmt.setString(1,
	 * loginFragment); rs = stmt.executeQuery(); LinkedHashMap<Integer, RoleMap>
	 * roleMaps = new LinkedHashMap<Integer, RoleMap>(); while (rs.next()) { Integer
	 * userId = rs.getInt("wiki_user_id"); RoleMap roleMap = new RoleMap(); if
	 * (roleMaps.containsKey(userId)) { roleMap = roleMaps.get(userId); } else {
	 * roleMap.setUserId(userId); roleMap.setUserLogin(rs.getString("username")); }
	 * roleMap.addRole(rs.getString("authority")); roleMaps.put(userId, roleMap); }
	 * return new ArrayList<RoleMap>(roleMaps.values()); } finally {
	 * DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / public List<RoleMap> getRoleMapByRole(String authority,boolean
	 * includeInheritedRoles) throws SQLException { Connection conn = null;
	 * PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); if (includeInheritedRoles) { stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL);
	 * stmt.setString(1, authority); stmt.setString(2, authority); stmt.setString(3,
	 * authority); stmt.setString(4, authority); } else { stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_AUTHORITY);
	 * stmt.setString(1, authority); stmt.setString(2, authority); } rs =
	 * stmt.executeQuery(); LinkedHashMap<String, RoleMap> roleMaps = new
	 * LinkedHashMap<String, RoleMap>(); while (rs.next()) { int userId =
	 * rs.getInt("wiki_user_id"); int groupId = rs.getInt("group_id"); RoleMap
	 * roleMap = new RoleMap(); String key = userId + "|" + groupId; if
	 * (roleMaps.containsKey(key)) { roleMap = roleMaps.get(key); } else { if
	 * (userId > 0) { roleMap.setUserId(userId);
	 * roleMap.setUserLogin(rs.getString("username")); } if (groupId > 0) {
	 * roleMap.setGroupId(groupId);
	 * roleMap.setGroupName(rs.getString("group_name")); } } String roleName =
	 * rs.getString("authority"); if (roleName != null) { roleMap.addRole(roleName);
	 * } // roleMap.addRole(rs.getString("authority")); roleMaps.put(key, roleMap);
	 * } return new ArrayList<RoleMap>(roleMaps.values()); } finally {
	 * DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / public List<Role> getRoleMapGroup(String groupName) throws SQLException {
	 * Connection conn = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * try { conn = DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_GROUP_AUTHORITIES); stmt.setString(1,
	 * groupName); rs = stmt.executeQuery(); List<Role> roles = new
	 * ArrayList<Role>(); while (rs.next()) { roles.add(this.initRole(rs)); } return
	 * roles; } finally { DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / public List<RoleMap> getRoleMapGroups() throws SQLException { Connection
	 * conn = null; PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_GROUPS_AUTHORITIES); rs =
	 * stmt.executeQuery(); LinkedHashMap<Integer, RoleMap> roleMaps = new
	 * LinkedHashMap<Integer, RoleMap>(); while (rs.next()) { Integer groupId =
	 * rs.getInt("group_id"); RoleMap roleMap = new RoleMap(); if
	 * (roleMaps.containsKey(groupId)) { roleMap = roleMaps.get(groupId); } else {
	 * roleMap.setGroupId(groupId);
	 * roleMap.setGroupName(rs.getString("group_name")); }
	 * roleMap.addRole(rs.getString("authority")); roleMaps.put(groupId, roleMap); }
	 * return new ArrayList<RoleMap>(roleMaps.values()); } finally {
	 * DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / public List<Role> getRoleMapUser(String login) throws SQLException {
	 * Connection conn = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * try { conn = DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_AUTHORITIES_USER); stmt.setString(1,
	 * login); stmt.setString(2, login); rs = stmt.executeQuery(); List<Role> roles
	 * = new ArrayList<Role>(); while (rs.next()) { roles.add(this.initRole(rs)); }
	 * return roles; } finally { DatabaseConnection.closeConnection(conn, stmt, rs);
	 * } }
	 * 
	 * /**
	 *
	 * / public List<Role> getRoles() throws SQLException { Connection conn = null;
	 * PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_ROLES); rs = stmt.executeQuery();
	 * List<Role> roles = new ArrayList<Role>(); while (rs.next()) {
	 * roles.add(this.initRole(rs)); } return roles; } finally {
	 * DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / public List<WikiGroup> getGroups() throws SQLException { Connection conn =
	 * null; PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_GROUPS); rs = stmt.executeQuery();
	 * List<WikiGroup> groups = new ArrayList<WikiGroup>(); while (rs.next()) {
	 * groups.add(this.initWikiGroup(rs)); } return groups; } finally {
	 * DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 * 
	 * / public LinkedHashMap<String, Map<String, String>>
	 * getUserPreferencesDefaults() throws SQLException { Connection conn = null;
	 * PreparedStatement stmt = null; ResultSet rs = null; try { conn =
	 * DatabaseConnection.getConnection(); stmt =
	 * conn.prepareStatement(STATEMENT_SELECT_USER_PREFERENCES_DEFAULTS); rs =
	 * stmt.executeQuery(); // the map of groups containing the maps to their
	 * preferences LinkedHashMap<String, Map<String, String>> groups = new
	 * LinkedHashMap<String, Map<String, String>>(); LinkedHashMap<String, String>
	 * defaultPreferences = null; String lastGroup = null; while (rs.next()) { //
	 * get the group name String group = rs.getString(3); // test if we need a new
	 * list of items for a new group if (group != null && (lastGroup == null ||
	 * !lastGroup.equals(group))) { lastGroup = group; defaultPreferences = new
	 * LinkedHashMap<String, String>(); } defaultPreferences.put(rs.getString(1),
	 * rs.getString(2)); groups.put(group, defaultPreferences); } return groups; }
	 * finally { DatabaseConnection.closeConnection(conn, stmt, rs); } }
	 * 
	 * /**
	 *
	 * / public List<RecentChange> getTopicHistory(int topicId, Pagination
	 * pagination, boolean descending, boolean selectDeleted) throws SQLException {
	 * Connection conn = null; PreparedStatement stmt = null; ResultSet rs = null;
	 * try { conn = DatabaseConnection.getConnection(); stmt =
	 * getTopicHistoryStatement(conn, topicId, pagination, descending,
	 * selectDeleted); // FIXME - sort order ignored rs = stmt.executeQuery();
	 * List<RecentChange> recentChanges = new ArrayList<RecentChange>(); while
	 * (rs.next()) { recentChanges.add(this.initRecentChange(rs)); } return
	 * recentChanges; } finally { DatabaseConnection.closeConnection(conn, stmt,
	 * rs); } }
	 * 
	 * /**
	 *
	 */
	protected void init(Properties properties) {
		this.props = properties;

		STATEMENT_SELECT_BOOKS = props.getProperty("STATEMENT_SELECT_BOOKS");
		STATEMENT_SELECT_BOOK_BY_ID = props.getProperty("STATEMENT_SELECT_BOOK_BY_ID");

		STATEMENT_CONNECTION_VALIDATION_QUERY = props.getProperty("STATEMENT_CONNECTION_VALIDATION_QUERY");
		STATEMENT_CREATE_CONFIGURATION_TABLE = props.getProperty("STATEMENT_CREATE_CONFIGURATION_TABLE");
		STATEMENT_CREATE_GROUP_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_TABLE");
		STATEMENT_CREATE_INTERWIKI_TABLE = props.getProperty("STATEMENT_CREATE_INTERWIKI_TABLE");
		STATEMENT_CREATE_NAMESPACE_TABLE = props.getProperty("STATEMENT_CREATE_NAMESPACE_TABLE");
		STATEMENT_CREATE_NAMESPACE_TRANSLATION_TABLE = props
				.getProperty("STATEMENT_CREATE_NAMESPACE_TRANSLATION_TABLE");
		STATEMENT_CREATE_ROLE_TABLE = props.getProperty("STATEMENT_CREATE_ROLE_TABLE");
		STATEMENT_CREATE_VIRTUAL_WIKI_TABLE = props.getProperty("STATEMENT_CREATE_VIRTUAL_WIKI_TABLE");
		STATEMENT_CREATE_WIKI_USER_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_USER_TABLE");
		STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX = props.getProperty("STATEMENT_CREATE_WIKI_USER_LOGIN_INDEX");
		STATEMENT_CREATE_USER_PREFERENCES_DEFAULTS_TABLE = props
				.getProperty("STATEMENT_CREATE_USER_PREFERENCES_DEFAULTS_TABLE");
		STATEMENT_CREATE_USER_PREFERENCES_TABLE = props.getProperty("STATEMENT_CREATE_USER_PREFERENCES_TABLE");
		STATEMENT_CREATE_USER_PREFERENCES_WIKI_USER_INDEX = props
				.getProperty("STATEMENT_CREATE_USER_PREFERENCES_WIKI_USER_INDEX");
		STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT = props
				.getProperty("STATEMENT_CREATE_TOPIC_CURRENT_VERSION_CONSTRAINT");
		STATEMENT_CREATE_TOPIC_TABLE = props.getProperty("STATEMENT_CREATE_TOPIC_TABLE");
		STATEMENT_CREATE_TOPIC_LINKS_TABLE = props.getProperty("STATEMENT_CREATE_TOPIC_LINKS_TABLE");
		STATEMENT_CREATE_TOPIC_LINKS_INDEX = props.getProperty("STATEMENT_CREATE_TOPIC_LINKS_INDEX");
		STATEMENT_CREATE_TOPIC_PAGE_NAME_INDEX = props.getProperty("STATEMENT_CREATE_TOPIC_PAGE_NAME_INDEX");
		STATEMENT_CREATE_TOPIC_PAGE_NAME_LOWER_INDEX = props
				.getProperty("STATEMENT_CREATE_TOPIC_PAGE_NAME_LOWER_INDEX");
		STATEMENT_CREATE_TOPIC_NAMESPACE_INDEX = props.getProperty("STATEMENT_CREATE_TOPIC_NAMESPACE_INDEX");
		STATEMENT_CREATE_TOPIC_VIRTUAL_WIKI_INDEX = props.getProperty("STATEMENT_CREATE_TOPIC_VIRTUAL_WIKI_INDEX");
		STATEMENT_CREATE_TOPIC_CURRENT_VERSION_INDEX = props
				.getProperty("STATEMENT_CREATE_TOPIC_CURRENT_VERSION_INDEX");
		STATEMENT_CREATE_TOPIC_VERSION_TABLE = props.getProperty("STATEMENT_CREATE_TOPIC_VERSION_TABLE");
		STATEMENT_CREATE_TOPIC_VERSION_TOPIC_INDEX = props.getProperty("STATEMENT_CREATE_TOPIC_VERSION_TOPIC_INDEX");
		STATEMENT_CREATE_TOPIC_VERSION_PREVIOUS_INDEX = props
				.getProperty("STATEMENT_CREATE_TOPIC_VERSION_PREVIOUS_INDEX");
		STATEMENT_CREATE_TOPIC_VERSION_USER_DISPLAY_INDEX = props
				.getProperty("STATEMENT_CREATE_TOPIC_VERSION_USER_DISPLAY_INDEX");
		STATEMENT_CREATE_TOPIC_VERSION_USER_ID_INDEX = props
				.getProperty("STATEMENT_CREATE_TOPIC_VERSION_USER_ID_INDEX");
		STATEMENT_CREATE_USER_BLOCK_TABLE = props.getProperty("STATEMENT_CREATE_USER_BLOCK_TABLE");
		STATEMENT_CREATE_USERS_TABLE = props.getProperty("STATEMENT_CREATE_USERS_TABLE");
		STATEMENT_CREATE_WIKI_FILE_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_FILE_TABLE");
		STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE = props.getProperty("STATEMENT_CREATE_WIKI_FILE_VERSION_TABLE");
		STATEMENT_CREATE_AUTHORITIES_TABLE = props.getProperty("STATEMENT_CREATE_AUTHORITIES_TABLE");
		STATEMENT_CREATE_CATEGORY_TABLE = props.getProperty("STATEMENT_CREATE_CATEGORY_TABLE");
		STATEMENT_CREATE_CATEGORY_INDEX = props.getProperty("STATEMENT_CREATE_CATEGORY_INDEX");
		STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_AUTHORITIES_TABLE");
		STATEMENT_CREATE_GROUP_MEMBERS_TABLE = props.getProperty("STATEMENT_CREATE_GROUP_MEMBERS_TABLE");
		STATEMENT_CREATE_LOG_TABLE = props.getProperty("STATEMENT_CREATE_LOG_TABLE");
		STATEMENT_CREATE_RECENT_CHANGE_TABLE = props.getProperty("STATEMENT_CREATE_RECENT_CHANGE_TABLE");
		STATEMENT_CREATE_WATCHLIST_TABLE = props.getProperty("STATEMENT_CREATE_WATCHLIST_TABLE");
		STATEMENT_DELETE_AUTHORITIES = props.getProperty("STATEMENT_DELETE_AUTHORITIES");
		STATEMENT_DELETE_CONFIGURATION = props.getProperty("STATEMENT_DELETE_CONFIGURATION");
		STATEMENT_DELETE_GROUP_AUTHORITIES = props.getProperty("STATEMENT_DELETE_GROUP_AUTHORITIES");
		STATEMENT_DELETE_GROUP_MAP_GROUP = props.getProperty("STATEMENT_DELETE_GROUP_MAP_GROUP");
		STATEMENT_DELETE_GROUP_MAP_USER = props.getProperty("STATEMENT_DELETE_GROUP_MAP_USER");
		STATEMENT_DELETE_INTERWIKI = props.getProperty("STATEMENT_DELETE_INTERWIKI");
		STATEMENT_DELETE_LOG_ITEMS = props.getProperty("STATEMENT_DELETE_LOG_ITEMS");
		STATEMENT_DELETE_LOG_ITEMS_BY_TOPIC_VERSION = props.getProperty("STATEMENT_DELETE_LOG_ITEMS_BY_TOPIC_VERSION");
		STATEMENT_DELETE_NAMESPACE_TRANSLATIONS = props.getProperty("STATEMENT_DELETE_NAMESPACE_TRANSLATIONS");
		STATEMENT_DELETE_RECENT_CHANGES = props.getProperty("STATEMENT_DELETE_RECENT_CHANGES");
		STATEMENT_DELETE_RECENT_CHANGES_TOPIC = props.getProperty("STATEMENT_DELETE_RECENT_CHANGES_TOPIC");
		STATEMENT_DELETE_RECENT_CHANGES_TOPIC_VERSION = props
				.getProperty("STATEMENT_DELETE_RECENT_CHANGES_TOPIC_VERSION");
		STATEMENT_DELETE_TOPIC_CATEGORIES = props.getProperty("STATEMENT_DELETE_TOPIC_CATEGORIES");
		STATEMENT_DELETE_TOPIC_LINKS = props.getProperty("STATEMENT_DELETE_TOPIC_LINKS");
		STATEMENT_DELETE_TOPIC_VERSION = props.getProperty("STATEMENT_DELETE_TOPIC_VERSION");
		STATEMENT_DELETE_WATCHLIST_ENTRY = props.getProperty("STATEMENT_DELETE_WATCHLIST_ENTRY");
		STATEMENT_DELETE_USER_PREFERENCES = props.getProperty("STATEMENT_DELETE_USER_PREFERENCES");
		STATEMENT_DROP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_DROP_AUTHORITIES_TABLE");
		STATEMENT_DROP_CATEGORY_TABLE = props.getProperty("STATEMENT_DROP_CATEGORY_TABLE");
		STATEMENT_DROP_CONFIGURATION_TABLE = props.getProperty("STATEMENT_DROP_CONFIGURATION_TABLE");
		STATEMENT_DROP_GROUP_AUTHORITIES_TABLE = props.getProperty("STATEMENT_DROP_GROUP_AUTHORITIES_TABLE");
		STATEMENT_DROP_GROUP_MEMBERS_TABLE = props.getProperty("STATEMENT_DROP_GROUP_MEMBERS_TABLE");
		STATEMENT_DROP_GROUP_TABLE = props.getProperty("STATEMENT_DROP_GROUP_TABLE");
		STATEMENT_DROP_INTERWIKI_TABLE = props.getProperty("STATEMENT_DROP_INTERWIKI_TABLE");
		STATEMENT_DROP_LOG_TABLE = props.getProperty("STATEMENT_DROP_LOG_TABLE");
		STATEMENT_DROP_NAMESPACE_TABLE = props.getProperty("STATEMENT_DROP_NAMESPACE_TABLE");
		STATEMENT_DROP_NAMESPACE_TRANSLATION_TABLE = props.getProperty("STATEMENT_DROP_NAMESPACE_TRANSLATION_TABLE");
		STATEMENT_DROP_RECENT_CHANGE_TABLE = props.getProperty("STATEMENT_DROP_RECENT_CHANGE_TABLE");
		STATEMENT_DROP_ROLE_TABLE = props.getProperty("STATEMENT_DROP_ROLE_TABLE");
		STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT = props
				.getProperty("STATEMENT_DROP_TOPIC_CURRENT_VERSION_CONSTRAINT");
		STATEMENT_DROP_TOPIC_TABLE = props.getProperty("STATEMENT_DROP_TOPIC_TABLE");
		STATEMENT_DROP_TOPIC_LINKS_TABLE = props.getProperty("STATEMENT_DROP_TOPIC_LINKS_TABLE");
		STATEMENT_DROP_TOPIC_VERSION_TABLE = props.getProperty("STATEMENT_DROP_TOPIC_VERSION_TABLE");
		STATEMENT_DROP_USER_BLOCK_TABLE = props.getProperty("STATEMENT_DROP_USER_BLOCK_TABLE");
		STATEMENT_DROP_USERS_TABLE = props.getProperty("STATEMENT_DROP_USERS_TABLE");
		STATEMENT_DROP_VIRTUAL_WIKI_TABLE = props.getProperty("STATEMENT_DROP_VIRTUAL_WIKI_TABLE");
		STATEMENT_DROP_WATCHLIST_TABLE = props.getProperty("STATEMENT_DROP_WATCHLIST_TABLE");
		STATEMENT_DROP_WIKI_USER_TABLE = props.getProperty("STATEMENT_DROP_WIKI_USER_TABLE");
		STATEMENT_DROP_WIKI_FILE_TABLE = props.getProperty("STATEMENT_DROP_WIKI_FILE_TABLE");
		STATEMENT_DROP_WIKI_FILE_VERSION_TABLE = props.getProperty("STATEMENT_DROP_WIKI_FILE_VERSION_TABLE");
		STATEMENT_INSERT_AUTHORITY = props.getProperty("STATEMENT_INSERT_AUTHORITY");
		STATEMENT_INSERT_CATEGORY = props.getProperty("STATEMENT_INSERT_CATEGORY");
		STATEMENT_INSERT_CONFIGURATION = props.getProperty("STATEMENT_INSERT_CONFIGURATION");
		STATEMENT_INSERT_GROUP = props.getProperty("STATEMENT_INSERT_GROUP");
		STATEMENT_INSERT_GROUP_AUTO_INCREMENT = props.getProperty("STATEMENT_INSERT_GROUP_AUTO_INCREMENT");
		STATEMENT_INSERT_GROUP_AUTHORITY = props.getProperty("STATEMENT_INSERT_GROUP_AUTHORITY");
		STATEMENT_INSERT_GROUP_MEMBER = props.getProperty("STATEMENT_INSERT_GROUP_MEMBER");
		STATEMENT_INSERT_GROUP_MEMBER_AUTO_INCREMENT = props
				.getProperty("STATEMENT_INSERT_GROUP_MEMBER_AUTO_INCREMENT");
		STATEMENT_INSERT_INTERWIKI = props.getProperty("STATEMENT_INSERT_INTERWIKI");
		STATEMENT_INSERT_LOG_ITEM = props.getProperty("STATEMENT_INSERT_LOG_ITEM");
		STATEMENT_INSERT_LOG_ITEMS_BLOCK = props.getProperty("STATEMENT_INSERT_LOG_ITEMS_BLOCK");
		STATEMENT_INSERT_LOG_ITEMS_BY_TOPIC_VERSION_TYPE = props
				.getProperty("STATEMENT_INSERT_LOG_ITEMS_BY_TOPIC_VERSION_TYPE");
		STATEMENT_INSERT_LOG_ITEMS_IMPORT = props.getProperty("STATEMENT_INSERT_LOG_ITEMS_IMPORT");
		STATEMENT_INSERT_LOG_ITEMS_MOVE = props.getProperty("STATEMENT_INSERT_LOG_ITEMS_MOVE");
		STATEMENT_INSERT_LOG_ITEMS_UNBLOCK = props.getProperty("STATEMENT_INSERT_LOG_ITEMS_UNBLOCK");
		STATEMENT_INSERT_LOG_ITEMS_UPLOAD = props.getProperty("STATEMENT_INSERT_LOG_ITEMS_UPLOAD");
		STATEMENT_INSERT_LOG_ITEMS_USER = props.getProperty("STATEMENT_INSERT_LOG_ITEMS_USER");
		STATEMENT_INSERT_NAMESPACE = props.getProperty("STATEMENT_INSERT_NAMESPACE");
		STATEMENT_INSERT_NAMESPACE_TRANSLATION = props.getProperty("STATEMENT_INSERT_NAMESPACE_TRANSLATION");
		STATEMENT_INSERT_RECENT_CHANGE = props.getProperty("STATEMENT_INSERT_RECENT_CHANGE");
		STATEMENT_INSERT_RECENT_CHANGES_LOGS = props.getProperty("STATEMENT_INSERT_RECENT_CHANGES_LOGS");
		STATEMENT_INSERT_RECENT_CHANGES_VERSIONS = props.getProperty("STATEMENT_INSERT_RECENT_CHANGES_VERSIONS");
		STATEMENT_INSERT_ROLE = props.getProperty("STATEMENT_INSERT_ROLE");
		STATEMENT_INSERT_TOPIC = props.getProperty("STATEMENT_INSERT_TOPIC");
		STATEMENT_INSERT_TOPIC_AUTO_INCREMENT = props.getProperty("STATEMENT_INSERT_TOPIC_AUTO_INCREMENT");
		STATEMENT_INSERT_TOPIC_LINKS = props.getProperty("STATEMENT_INSERT_TOPIC_LINKS");
		STATEMENT_INSERT_TOPIC_VERSION = props.getProperty("STATEMENT_INSERT_TOPIC_VERSION");
		STATEMENT_INSERT_TOPIC_VERSION_AUTO_INCREMENT = props
				.getProperty("STATEMENT_INSERT_TOPIC_VERSION_AUTO_INCREMENT");
		STATEMENT_INSERT_USER = props.getProperty("STATEMENT_INSERT_USER");
		STATEMENT_INSERT_USER_BLOCK = props.getProperty("STATEMENT_INSERT_USER_BLOCK");
		STATEMENT_INSERT_USER_BLOCK_AUTO_INCREMENT = props.getProperty("STATEMENT_INSERT_USER_BLOCK_AUTO_INCREMENT");
		STATEMENT_INSERT_VIRTUAL_WIKI = props.getProperty("STATEMENT_INSERT_VIRTUAL_WIKI");
		STATEMENT_INSERT_VIRTUAL_WIKI_AUTO_INCREMENT = props
				.getProperty("STATEMENT_INSERT_VIRTUAL_WIKI_AUTO_INCREMENT");
		STATEMENT_INSERT_WATCHLIST_ENTRY = props.getProperty("STATEMENT_INSERT_WATCHLIST_ENTRY");
		STATEMENT_INSERT_WIKI_FILE = props.getProperty("STATEMENT_INSERT_WIKI_FILE");
		STATEMENT_INSERT_WIKI_FILE_AUTO_INCREMENT = props.getProperty("STATEMENT_INSERT_WIKI_FILE_AUTO_INCREMENT");
		STATEMENT_INSERT_WIKI_FILE_VERSION = props.getProperty("STATEMENT_INSERT_WIKI_FILE_VERSION");
		STATEMENT_INSERT_WIKI_FILE_VERSION_AUTO_INCREMENT = props
				.getProperty("STATEMENT_INSERT_WIKI_FILE_VERSION_AUTO_INCREMENT");
		STATEMENT_INSERT_WIKI_USER = props.getProperty("STATEMENT_INSERT_WIKI_USER");
		STATEMENT_INSERT_WIKI_USER_AUTO_INCREMENT = props.getProperty("STATEMENT_INSERT_WIKI_USER_AUTO_INCREMENT");
		STATEMENT_INSERT_USER_PREFERENCE_DEFAULTS = props.getProperty("STATEMENT_INSERT_USER_PREFERENCE_DEFAULTS");
		STATEMENT_INSERT_USER_PREFERENCE = props.getProperty("STATEMENT_INSERT_USER_PREFERENCE");
		STATEMENT_SELECT_AUTHORITIES_AUTHORITY = props.getProperty("STATEMENT_SELECT_AUTHORITIES_AUTHORITY");
		STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL = props.getProperty("STATEMENT_SELECT_AUTHORITIES_AUTHORITY_ALL");
		STATEMENT_SELECT_AUTHORITIES_LOGIN = props.getProperty("STATEMENT_SELECT_AUTHORITIES_LOGIN");
		STATEMENT_SELECT_AUTHORITIES_USER = props.getProperty("STATEMENT_SELECT_AUTHORITIES_USER");
		STATEMENT_SELECT_CATEGORIES = props.getProperty("STATEMENT_SELECT_CATEGORIES");
		STATEMENT_SELECT_CATEGORY_TOPICS = props.getProperty("STATEMENT_SELECT_CATEGORY_TOPICS");
		STATEMENT_SELECT_CONFIGURATION = props.getProperty("STATEMENT_SELECT_CONFIGURATION");
		STATEMENT_SELECT_GROUP_MAP_GROUP = props.getProperty("STATEMENT_SELECT_GROUP_MAP_GROUP");
		STATEMENT_SELECT_GROUP_MAP_USER = props.getProperty("STATEMENT_SELECT_GROUP_MAP_USER");
		STATEMENT_SELECT_GROUP_MAP_AUTHORITIES = props.getProperty("STATEMENT_SELECT_GROUP_MAP_AUTHORITIES");
		STATEMENT_SELECT_GROUP = props.getProperty("STATEMENT_SELECT_GROUP");
		STATEMENT_SELECT_GROUP_BY_ID = props.getProperty("STATEMENT_SELECT_GROUP_BY_ID");
		STATEMENT_SELECT_GROUPS = props.getProperty("STATEMENT_SELECT_GROUPS");
		STATEMENT_SELECT_GROUP_AUTHORITIES = props.getProperty("STATEMENT_SELECT_GROUP_AUTHORITIES");
		STATEMENT_SELECT_GROUPS_AUTHORITIES = props.getProperty("STATEMENT_SELECT_GROUPS_AUTHORITIES");
		STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE = props.getProperty("STATEMENT_SELECT_GROUP_MEMBERS_SEQUENCE");
		STATEMENT_SELECT_GROUP_SEQUENCE = props.getProperty("STATEMENT_SELECT_GROUP_SEQUENCE");
		STATEMENT_SELECT_INTERWIKIS = props.getProperty("STATEMENT_SELECT_INTERWIKIS");
		STATEMENT_SELECT_LOG_ITEMS = props.getProperty("STATEMENT_SELECT_LOG_ITEMS");
		STATEMENT_SELECT_LOG_ITEMS_BY_TYPE = props.getProperty("STATEMENT_SELECT_LOG_ITEMS_BY_TYPE");
		STATEMENT_SELECT_NAMESPACE_SEQUENCE = props.getProperty("STATEMENT_SELECT_NAMESPACE_SEQUENCE");
		STATEMENT_SELECT_NAMESPACES = props.getProperty("STATEMENT_SELECT_NAMESPACES");
		STATEMENT_SELECT_PW_RESET_CHALLENGE_DATA = props.getProperty("STATEMENT_SELECT_PW_RESET_CHALLENGE_DATA");
		STATEMENT_SELECT_RECENT_CHANGES = props.getProperty("STATEMENT_SELECT_RECENT_CHANGES");
		STATEMENT_SELECT_ROLES = props.getProperty("STATEMENT_SELECT_ROLES");
		STATEMENT_SELECT_TOPIC_BY_ID = props.getProperty("STATEMENT_SELECT_TOPIC_BY_ID");
		STATEMENT_SELECT_TOPIC_BY_TYPE = props.getProperty("STATEMENT_SELECT_TOPIC_BY_TYPE");
		STATEMENT_SELECT_TOPIC_COUNT = props.getProperty("STATEMENT_SELECT_TOPIC_COUNT");
		STATEMENT_SELECT_TOPIC = props.getProperty("STATEMENT_SELECT_TOPIC");
		STATEMENT_SELECT_TOPIC_HISTORY = props.getProperty("STATEMENT_SELECT_TOPIC_HISTORY");
		STATEMENT_SELECT_TOPIC_LINK_ORPHANS = props.getProperty("STATEMENT_SELECT_TOPIC_LINK_ORPHANS");
		STATEMENT_SELECT_TOPIC_LINKS = props.getProperty("STATEMENT_SELECT_TOPIC_LINKS");
		STATEMENT_SELECT_TOPIC_LOWER = props.getProperty("STATEMENT_SELECT_TOPIC_LOWER");
		STATEMENT_SELECT_TOPIC_NAME = props.getProperty("STATEMENT_SELECT_TOPIC_NAME");
		STATEMENT_SELECT_TOPIC_NAME_LOWER = props.getProperty("STATEMENT_SELECT_TOPIC_NAME_LOWER");
		STATEMENT_SELECT_TOPIC_NAMES = props.getProperty("STATEMENT_SELECT_TOPIC_NAMES");
		STATEMENT_SELECT_TOPICS_ADMIN = props.getProperty("STATEMENT_SELECT_TOPICS_ADMIN");
		STATEMENT_SELECT_TOPIC_SEQUENCE = props.getProperty("STATEMENT_SELECT_TOPIC_SEQUENCE");
		STATEMENT_SELECT_TOPIC_VERSION = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION");
		STATEMENT_SELECT_TOPIC_VERSION_NEXT_ID = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION_NEXT_ID");
		STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE = props.getProperty("STATEMENT_SELECT_TOPIC_VERSION_SEQUENCE");
		STATEMENT_SELECT_USER_BLOCKS = props.getProperty("STATEMENT_SELECT_USER_BLOCKS");
		STATEMENT_SELECT_USER_BLOCK_SEQUENCE = props.getProperty("STATEMENT_SELECT_USER_BLOCK_SEQUENCE");
		STATEMENT_SELECT_USERS_AUTHENTICATION = props.getProperty("STATEMENT_SELECT_USERS_AUTHENTICATION");
		STATEMENT_SELECT_VIRTUAL_WIKIS = props.getProperty("STATEMENT_SELECT_VIRTUAL_WIKIS");
		STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE = props.getProperty("STATEMENT_SELECT_VIRTUAL_WIKI_SEQUENCE");
		STATEMENT_SELECT_WATCHLIST = props.getProperty("STATEMENT_SELECT_WATCHLIST");
		STATEMENT_SELECT_WATCHLIST_CHANGES = props.getProperty("STATEMENT_SELECT_WATCHLIST_CHANGES");
		STATEMENT_SELECT_WIKI_FILE = props.getProperty("STATEMENT_SELECT_WIKI_FILE");
		STATEMENT_SELECT_WIKI_FILE_COUNT = props.getProperty("STATEMENT_SELECT_WIKI_FILE_COUNT");
		STATEMENT_SELECT_WIKI_FILE_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_FILE_SEQUENCE");
		STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_FILE_VERSION_SEQUENCE");
		STATEMENT_SELECT_WIKI_FILE_VERSIONS = props.getProperty("STATEMENT_SELECT_WIKI_FILE_VERSIONS");
		STATEMENT_SELECT_WIKI_USER = props.getProperty("STATEMENT_SELECT_WIKI_USER");
		STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS = props
				.getProperty("STATEMENT_SELECT_WIKI_USER_CHANGES_ANONYMOUS");
		STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN = props.getProperty("STATEMENT_SELECT_WIKI_USER_CHANGES_LOGIN");
		STATEMENT_SELECT_WIKI_USER_COUNT = props.getProperty("STATEMENT_SELECT_WIKI_USER_COUNT");
		STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD = props.getProperty("STATEMENT_SELECT_WIKI_USER_DETAILS_PASSWORD");
		STATEMENT_SELECT_WIKI_USER_LOGIN = props.getProperty("STATEMENT_SELECT_WIKI_USER_LOGIN");
		STATEMENT_SELECT_WIKI_USER_SEQUENCE = props.getProperty("STATEMENT_SELECT_WIKI_USER_SEQUENCE");
		STATEMENT_SELECT_WIKI_USERS = props.getProperty("STATEMENT_SELECT_WIKI_USERS");
		STATEMENT_SELECT_USER_PREFERENCES_DEFAULTS = props.getProperty("STATEMENT_SELECT_USER_PREFERENCES_DEFAULTS");
		STATEMENT_SELECT_USER_PREFERENCES = props.getProperty("STATEMENT_SELECT_USER_PREFERENCES");
		STATEMENT_UPDATE_GROUP = props.getProperty("STATEMENT_UPDATE_GROUP");
		STATEMENT_UPDATE_NAMESPACE = props.getProperty("STATEMENT_UPDATE_NAMESPACE");
		STATEMENT_UPDATE_PW_RESET_CHALLENGE_DATA = props.getProperty("STATEMENT_UPDATE_PW_RESET_CHALLENGE_DATA");
		STATEMENT_UPDATE_RECENT_CHANGES_PREVIOUS_VERSION_ID = props
				.getProperty("STATEMENT_UPDATE_RECENT_CHANGES_PREVIOUS_VERSION_ID");
		STATEMENT_UPDATE_TOPIC_NAMESPACE = props.getProperty("STATEMENT_UPDATE_TOPIC_NAMESPACE");
		STATEMENT_UPDATE_ROLE = props.getProperty("STATEMENT_UPDATE_ROLE");
		STATEMENT_UPDATE_TOPIC = props.getProperty("STATEMENT_UPDATE_TOPIC");
		STATEMENT_UPDATE_TOPIC_VERSION = props.getProperty("STATEMENT_UPDATE_TOPIC_VERSION");
		STATEMENT_UPDATE_TOPIC_VERSION_PREVIOUS_VERSION_ID = props
				.getProperty("STATEMENT_UPDATE_TOPIC_VERSION_PREVIOUS_VERSION_ID");
		STATEMENT_UPDATE_USER = props.getProperty("STATEMENT_UPDATE_USER");
		STATEMENT_UPDATE_USER_BLOCK = props.getProperty("STATEMENT_UPDATE_USER_BLOCK");
		STATEMENT_UPDATE_VIRTUAL_WIKI = props.getProperty("STATEMENT_UPDATE_VIRTUAL_WIKI");
		STATEMENT_UPDATE_WIKI_FILE = props.getProperty("STATEMENT_UPDATE_WIKI_FILE");
		STATEMENT_UPDATE_WIKI_USER = props.getProperty("STATEMENT_UPDATE_WIKI_USER");
		STATEMENT_UPDATE_USER_PREFERENCE_DEFAULTS = props.getProperty("STATEMENT_UPDATE_USER_PREFERENCE_DEFAULTS");
		STATEMENT_CREATE_FILE_DATA_TABLE = props.getProperty("STATEMENT_CREATE_FILE_DATA_TABLE");
		STATEMENT_DROP_FILE_DATA_TABLE = props.getProperty("STATEMENT_DROP_FILE_DATA_TABLE");
		STATEMENT_INSERT_FILE_DATA = props.getProperty("STATEMENT_INSERT_FILE_DATA");
		STATEMENT_DELETE_RESIZED_IMAGES = props.getProperty("STATEMENT_DELETE_RESIZED_IMAGES");
		STATEMENT_SELECT_FILE_INFO = props.getProperty("STATEMENT_SELECT_FILE_INFO");
		STATEMENT_SELECT_FILE_DATA = props.getProperty("STATEMENT_SELECT_FILE_DATA");
		STATEMENT_SELECT_FILE_VERSION_DATA = props.getProperty("STATEMENT_SELECT_FILE_VERSION_DATA");
		STATEMENT_CREATE_SEQUENCES = props.getProperty("STATEMENT_CREATE_SEQUENCES");
		STATEMENT_DROP_SEQUENCES = props.getProperty("STATEMENT_DROP_SEQUENCES");
	}

	/**
	 *
	 * / public void insertWikiUser(WikiUser user, Connection conn) throws
	 * SQLException { PreparedStatement stmt = null; ResultSet rs = null; try { int
	 * index = 1; if (!this.autoIncrementPrimaryKeys()) { stmt =
	 * conn.prepareStatement(STATEMENT_INSERT_WIKI_USER); int nextUserId =
	 * this.nextWikiUserId(conn); user.setUserId(nextUserId); stmt.setInt(index++,
	 * user.getUserId()); } else { stmt =
	 * conn.prepareStatement(STATEMENT_INSERT_WIKI_USER_AUTO_INCREMENT,
	 * Statement.RETURN_GENERATED_KEYS); } stmt.setString(index++,
	 * user.getUsername()); stmt.setString(index++, user.getDisplayName());
	 * stmt.setTimestamp(index++, user.getCreateDate()); stmt.setTimestamp(index++,
	 * user.getLastLoginDate()); stmt.setString(index++, user.getCreateIpAddress());
	 * stmt.setString(index++, user.getLastLoginIpAddress());
	 * stmt.setString(index++, user.getEmail()); stmt.executeUpdate(); if
	 * (this.autoIncrementPrimaryKeys()) { rs = stmt.getGeneratedKeys(); if
	 * (!rs.next()) { throw new SQLException("Unable to determine auto-generated ID
	 * for database record"); } user.setUserId(rs.getInt(1)); } } finally { // close
	 * only the statement and result set - leave the connection open for further use
	 * DatabaseConnection.closeConnection(null, stmt, rs); } // Store user
	 * preferences Map<String, String> defaults =
	 * this.lookupUserPreferencesDefaults(conn); Map<String, String> preferences =
	 * user.getPreferences(); try { stmt =
	 * conn.prepareStatement(STATEMENT_INSERT_USER_PREFERENCE); // Only store
	 * preferences that are not default for (String key : defaults.keySet()) {
	 * String defVal = defaults.get(key); String cusVal = preferences.get(key); if
	 * (StringUtils.isBlank(cusVal)) { user.setPreference(key, defVal); } else if
	 * (StringUtils.isBlank(defVal) ||
	 * !defaults.get(key).equals(preferences.get(key))) { stmt.setInt(1,
	 * user.getUserId()); stmt.setString(2, key); stmt.setString(3, cusVal);
	 * stmt.executeUpdate(); } } } finally {
	 * DatabaseConnection.closeStatement(stmt); } }
	 * 
	 * /** Retrieve the next available wiki user id from the wiki user table.
	 *
	 * @param conn A database connection to use when connecting to the database from
	 *             this method.
	 * @return The next available wiki user id from the wiki user table.
	 * @throws SQLException Thrown if any error occurs during method execution.
	 */
	private int nextWikiUserId(Connection conn) throws SQLException {
		int nextId = DatabaseConnection.executeSequenceQuery(STATEMENT_SELECT_WIKI_USER_SEQUENCE, "wiki_user_id", conn);
		// note - this returns the last id in the system, so add one
		return nextId + 1;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBook(BookDto book) throws SQLException {
		// TODO Auto-generated method stub

	}

}
