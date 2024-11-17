package com.book.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookSearchCriteria {
	private String criteria;
	private String value;

}
