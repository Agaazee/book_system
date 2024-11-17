package com.book.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.book.dto.BookSearchCriteria;
import com.book.model.BookConfig;
import com.book.repository.BookConfigRepository;

@Service
public class BookSearchService {

	BookConfigRepository bookConfigRepository;

	public BookSearchService(BookConfigRepository bookConfigRepository) {
		this.bookConfigRepository = bookConfigRepository;
	}

	public Page<BookConfig> searchBookByCriteria(BookSearchCriteria bookSearchCriteria, Pageable pageable) {
		switch (bookSearchCriteria.getCriteria()) {
		case "title":
			return bookConfigRepository.findByTitle(bookSearchCriteria.getValue(), pageable);
		case "genre":
			return bookConfigRepository.findByGenre(bookSearchCriteria.getValue(), pageable);
		case "location":
			return bookConfigRepository.findByLocation(bookSearchCriteria.getValue(), pageable);
		case "author":
			return bookConfigRepository.findByAuthor(bookSearchCriteria.getValue(), pageable);
		case "availability":
			return bookConfigRepository.findByAvailability(Boolean.parseBoolean(bookSearchCriteria.getValue()),
					pageable);
		default:
			return null;
		}

	}

}
