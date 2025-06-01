package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.UserBook;

public interface UserBookRepository extends CrudRepository<UserBook, Long> {

	Optional<UserBook> findByUserAndBook(User currentUser, Book book);

	@Query(value = "SELECT * FROM user_book WHERE user_id = ?1 AND status = 'READ'", nativeQuery = true)
	public Iterable<UserBook> findAllReadBooksByUser(Long userId);

	@Query(value = "SELECT * FROM user_book WHERE user_id = ?1 AND status = 'WANT_TO_READ'", nativeQuery = true)
	public Iterable<UserBook> findAllWantToReadBooksByUser(Long userId);

	@Query(value = "SELECT * FROM user_book WHERE user_id = ?1 AND status = 'CURRENTLY_READING'", nativeQuery = true)
	public Iterable<UserBook> findAllCurrentlyReadingBooksByUser(Long userId);

}
