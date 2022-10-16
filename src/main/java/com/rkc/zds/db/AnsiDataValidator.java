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

import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.rkc.zds.dto.BookDto;
import com.rkc.zds.exceptions.AppException;
import com.rkc.zds.exceptions.AppMessage;

/**
 * Utility methods for validating data prior to committing it to the
 * database.
 */
public class AnsiDataValidator {

	/**
	 * Verify that a string does not exceed a specified maximum, throwing
	 * an exception if it does.
	 */
	private void checkLength(String value, int maxLength) throws AppException {
		if (value != null && value.length() > maxLength) {
			throw new AppException(new AppMessage("error.fieldlength", value, Integer.valueOf(maxLength).toString()));
		}
	}

	/**
	 * Validate that all fields of a role object are valid for the
	 * database.
	 */
	protected void validateAuthority(String role) throws AppException {
		checkLength(role, 30);
	}

	/**
	 * Validate that all fields of a Category object are valid for the
	 * database.
	 * /
	protected void validateCategory(Category category) throws WikiException {
		checkLength(category.getName(), 200);
		checkLength(category.getSortKey(), 200);
	}
	*/
	
	public void validateBook(BookDto book) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Validate that all fields of a configuration object are valid for the
	 * database.
	 */
	protected void validateConfiguration(Map<String, String> configuration) throws AppException {
		for (Map.Entry<String, String> entry : configuration.entrySet()) {
			checkLength(entry.getKey(), 50);
			checkLength(entry.getValue(), 500);
		}
	}

	/**
	 * Validate that all fields of a Role object are valid for the
	 * database.
	 * /
	protected void validateRole(Role role) throws WikiException {
		checkLength(role.getAuthority(), 30);
		role.setDescription(StringUtils.substring(role.getDescription(), 0, 200));
	}

	/**
	 * Validate that all fields of a WikiUserDetails object are valid for
	 * the database.
	 * /
	protected void validateUserDetails(WikiUserDetails userDetails) throws WikiException {
		checkLength(userDetails.getUsername(), 100);
		// do not throw exception containing password info
		if (userDetails.getPassword() != null && userDetails.getPassword().length() > 100) {
			throw new WikiException(new WikiMessage("error.fieldlength", "-", "100"));
		}
	}

*/
}
