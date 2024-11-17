package com.book.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.dto.MessageDTO;
import com.book.model.BookConfig;
import com.book.model.User;
import com.book.repository.BookConfigRepository;
import com.book.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/bookConfig")
public class BookConfigController {

	UserRepository userRepository;
	BookConfigRepository bookConfigRepository;

	public BookConfigController(UserRepository userRepository, BookConfigRepository bookConfigRepository) {
		this.userRepository = userRepository;
		this.bookConfigRepository = bookConfigRepository;
	}

	@PostMapping("/addBook")
	public MessageDTO addBook(@RequestBody BookConfig bookConfig) {
		MessageDTO messageDTO = new MessageDTO();
		try {

			User user = userRepository.findByUsername(bookConfig.getUsername());
			bookConfig.setUser(user);
			
			Long ifExistis = bookConfigRepository.checkIfBookExists(user.getUser_id(), bookConfig.getTitle());
			if (ifExistis > 0) {
				messageDTO.setMessage("Book already exists");
				return messageDTO;
			}
			
			bookConfigRepository.save(bookConfig);
			messageDTO.setMessage("Book added successfully");
		} catch (Exception bookConfigException) {
			bookConfigException.getStackTrace();
			messageDTO.setMessage("Error while adding book");
			return messageDTO;
		}
		return messageDTO;
	}
	
	@PostMapping("/editBookInfo")
	public MessageDTO editBookInfo(@RequestBody BookConfig bookConfig) {
		MessageDTO messageDTO = new MessageDTO();
		try {
			
			User user = userRepository.findByUsername(bookConfig.getUsername());
			bookConfig.setUser(user);
			
			bookConfigRepository.save(bookConfig);
			messageDTO.setMessage("Book updated successfully");
		} catch (Exception bookConfigException) {
			bookConfigException.getStackTrace();
			messageDTO.setMessage("Error while updating book");
			return messageDTO;
		}
		return messageDTO;
	}
	
	@GetMapping("/deleteBookRecord")
	public MessageDTO deleteBookRecord(@RequestParam int bookId) {
		MessageDTO messageDTO = new MessageDTO();
        try {
            bookConfigRepository.deleteById((long) bookId);
            messageDTO.setMessage("Book deleted successfully");
        } catch (Exception bookConfigException) {
            bookConfigException.getStackTrace();
            messageDTO.setMessage("Error while deleting record");
            return messageDTO;
        }
        return messageDTO;
	}


}
