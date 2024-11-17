package com.book.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.book.model.BookConfig;

public interface BookConfigRepository extends JpaRepository<BookConfig, Long> {
	
	@Query(value = "select count(1) from book_config where user_id = :userId and title = :title", nativeQuery = true)
	Long checkIfBookExists(@Param("userId") int userId,@Param("title") String title);
	
	@Query(value = "select * from book_config where user_id = :userId", nativeQuery = true)
	List<BookConfig> findByUserId(@Param("userId") int userId);
	
	Page<BookConfig> findByTitle(String title, Pageable pageable);

	Page<BookConfig> findByGenre(String bookGenre, Pageable pageable);

	Page<BookConfig> findByLocation(String bookLocation, Pageable pageable);

	Page<BookConfig> findByAuthor(String bookAuthor, Pageable pageable);

	Page<BookConfig> findByAvailability(Boolean bookAvailability, Pageable pageable);
	
}
