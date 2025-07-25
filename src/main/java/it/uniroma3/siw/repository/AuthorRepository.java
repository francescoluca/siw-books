package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

	boolean existsByNameAndSurnameAndDayOfBirth(String name, String surname, LocalDate dayOfBirth);

	@Query(value = "select * " + "from author a " + "where a.id not in " + "(select authors_id " + "from book_authors "
			+ "where book_authors.books_id = :bookId)", nativeQuery = true)
	Iterable<Author> findAuthorsNotInBook(@Param("bookId") Long bookId);

	@Query(value = "SELECT * FROM author a " + "WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR a.surname LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	Page<Author> searchAuthorsByKeyword(@Param(value = "keyword") String keyword, Pageable pageable);

	@Query("SELECT a FROM Author a LEFT JOIN a.books b GROUP BY a.id ORDER BY COUNT(b) DESC")
	List<Author> findTop3ByBooksCount(Pageable pageable);

}
