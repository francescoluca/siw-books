package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	@Query("""
			    SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END
			    FROM Book b JOIN b.authors a
			    WHERE b.title = :title AND a IN :authors
			""")
	boolean existsByTitleAndAnyAuthors(@Param("title") String title, @Param("authors") List<Author> authors);

	@Query("""
			    SELECT AVG(r.stars)
			    FROM Review r
			    WHERE r.book = :book
			""")
	Double findAverageRatingForBook(@Param("book") Book book);

	@Query(value = "SELECT * FROM book b " + "WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) "
			+ "OR b.isbn_code LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
	Page<Book> searchBooksByKeyword(@Param("keyword") String keyword, Pageable pageable);

	@Query("""
			    SELECT b
			    FROM Book b
			    JOIN b.authors a
			    WHERE a = :author
			""")
	Iterable<Book> findBooksByAuthor(@Param("author") Author author);

	@Query("SELECT b FROM Book b LEFT JOIN b.reviews r GROUP BY b.id ORDER BY COUNT(r) DESC")
	List<Book> findTop3ByReviewsCount(Pageable pageable);

	@Query("SELECT b FROM Book b JOIN b.reviews r GROUP BY b.id ORDER BY AVG(r.stars) DESC")
	List<Book> findTop3ByAverageRating(Pageable pageable);

}
