package com.rkc.zds.utils;


import java.util.Collections;
import java.util.function.Function;

import com.rkc.zds.utils.PageImpl;
import com.rkc.zds.utils.Pageable;

/**
 * A page is a sublist of a list of objects. It allows gain information about the position of it in the containing
 * entire list.
 *
 * @param <T>
 * @author Oliver Gierke
 */
public interface PaginationPage<T> {

	/**
	 * Creates a new empty {@link PaginationPage}.
	 *
	 * @return
	 * @since 2.0
	 */
	static <T> PaginationPage<T> empty() {
		return empty(Pageable.unpaged());
	}

	/**
	 * Creates a new empty {@link PaginationPage} for the given {@link Pageable}.
	 *
	 * @param pageable must not be {@literal null}.
	 * @return
	 * @since 2.0
	 */
	static <T> PaginationPage<T> empty(Pageable pageable) {
		return new PageImpl<>(Collections.emptyList(), pageable, 0);
	}

	/**
	 * Returns the number of total pages.
	 *
	 * @return the number of total pages
	 */
	int getTotalPages();

	/**
	 * Returns the total amount of elements.
	 *
	 * @return the total amount of elements
	 */
	long getTotalElements();

	/**
	 * Returns a new {@link PaginationPage} with the content of the current one mapped by the given {@link Function}.
	 *
	 * @param converter must not be {@literal null}.
	 * @return a new {@link PaginationPage} with the content of the current one mapped by the given {@link Function}.
	 * @since 1.10
	 */
	<U> PaginationPage<U> map(Function<? super T, ? extends U> converter);
}

