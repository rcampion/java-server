package com.rkc.zds.enums;

public enum BookCategoryEnum {
	FICTION,
	NONFICTION;
	
	//int category;
	
	public static BookCategoryEnum fromInt(int category){
		switch(category) {
		case 1:
			return FICTION;
		case 2:
			return NONFICTION;
		}
		return null;
	}
}
