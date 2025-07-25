package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Review;

public interface ReviewRepository extends CrudRepository<Review, Long> {

	List<Review> findByBookId(Long bookId);

	Optional<Review> findByBookIdAndWriterId(Long bookId, Long writerId);

	List<Review> findByBookIdAndWriterIdNot(Long bookId, Long writerId);

	@Query("SELECT r FROM Review r ORDER BY r.createdAt DESC")
	List<Review> findTop2ByCreatedAt(Pageable pageable);

}
