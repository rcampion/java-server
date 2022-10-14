package com.rkc.zds.dto;

import com.rkc.zds.enums.BookCategoryEnum;

public class Book implements Comparable<Book> {

	private String id;
	private String title;
	private String author;
	private int category;
	
	public Book() {
	}
	
	public Book(String id, String title, String author) {
		this.id = id;
		this.title = title;
		this.author = author;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	@Override
	public String toString() {

		return this.id + " " + this.title + " " + this.author + " " + this.category;
	}

	@Override
	public int compareTo(Book other) {
		if(!this.title.equals(other.title))
			return this.title.compareTo(other.title);
		return 0;
	}

}
