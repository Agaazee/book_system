package com.book.dto;

import java.util.ArrayList;
import java.util.List;

import com.book.model.BookConfig;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookDTO {

	private int id;
	private String username;
	private String title;
	private String author;
	private String genre;
	private int quantity;
	private String condition;
	private boolean availability;
	private String location;

	public static List<BookDTO> convertToDTO(List<BookConfig> bookList) {
		List<BookDTO> bookDTOList = new ArrayList<>();
		for (BookConfig book : bookList) {
			BookDTO bookDTO = new BookDTO();
			bookDTO.setId(book.getId());
			bookDTO.setUsername(book.getUser().getUsername());
			bookDTO.setTitle(book.getTitle());
			bookDTO.setAuthor(book.getAuthor());
			bookDTO.setGenre(book.getGenre());
			bookDTO.setQuantity(book.getQuantity());
			bookDTO.setCondition(book.getCondition());
			bookDTO.setAvailability(book.isAvailability());
			bookDTO.setLocation(book.getLocation());
			bookDTOList.add(bookDTO);
		}
		return bookDTOList;
	}

}
