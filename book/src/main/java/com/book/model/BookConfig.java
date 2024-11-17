package com.book.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "book_config")
public class BookConfig {
	@Id
	@GeneratedValue
	@PrimaryKeyJoinColumn
	private int id;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	private String title;
	private String author;
	private String genre;
	private int quantity;
	private String condition;
	private boolean availability;
	@Transient
	private String username;
	private String location;
}
