package it.uniroma3.siw.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {

	boolean existsByNameAndSurnameAndDayOfBirth(String name, String surname, LocalDate dayOfBirth);

	@Query(value = "select * " + "from author a " + "where a.id not in " + "(select authors_id " + "from book_authors "
			+ "where book_authors.books_id = :bookId)", nativeQuery = true)
	Iterable<Author> findAuthorsNotInBook(@Param("bookId") Long bookId);

}
