package com.book.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.dto.BookDTO;
import com.book.dto.BookSearchCriteria;
import com.book.dto.MessageDTO;
import com.book.model.BookConfig;
import com.book.repository.UserRepository;
import com.book.service.BookSearchService;

@RestController
public class BookSearchController {

	UserRepository userRepository;
	BookSearchService bookSearchService;

	public BookSearchController(UserRepository userRepository, BookSearchService bookSearchService) {
		this.userRepository = userRepository;
		this.bookSearchService = bookSearchService;
	}

	@GetMapping("/getSearchingCriteria")
	public ResponseEntity<?> getSearchingCriteria() {
		try {
			Map<String, List<String>> searchingCriteria = new HashMap<>();
			List<String> searchList = List.of("username", "title", "genre", "location", "author", "availability");
			searchingCriteria.put("Searching Criteria", searchList);
			return ResponseEntity.ok(searchingCriteria);
		} catch (Exception getSearchingCriteriaException) {
			getSearchingCriteriaException.getStackTrace();
			return ResponseEntity.badRequest().body("Error while getting searching criteria");
		}
	}

	@PostMapping("/searchBookByCriteria")
	public ResponseEntity<?> searchBookByCriteria(@RequestBody BookSearchCriteria searchCriteria,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sortBy, 
			@RequestParam(defaultValue = "ASC") String sortDir) {
		try {
			Sort sort = sortDir.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
			Pageable pageable = PageRequest.of(page, size, sort);

			Page<BookConfig> bookPage = bookSearchService.searchBookByCriteria(searchCriteria, pageable);

			if (bookPage.isEmpty()) {
				MessageDTO messageDTO = new MessageDTO();
				messageDTO.setMessage("No Books available for criteria "
						+ searchCriteria.getCriteria() + " and value " + searchCriteria.getValue());
				return ResponseEntity.badRequest().body(messageDTO);
			}

			return ResponseEntity.ok(BookDTO.convertToDTO(bookPage.getContent()));
		} catch (Exception searchBookByTitleException) {
			searchBookByTitleException.printStackTrace();
			return ResponseEntity.badRequest().body("Error while searching book by title");
		}
	}

}
