package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.UserBook;
import it.uniroma3.siw.repository.UserBookRepository;

@Service
public class UserBookService {

	@Autowired
	private UserBookRepository userBookRepository;

	public Optional<UserBook> findByUserAndBookId(User currentUser, Long bookId) {
		return userBookRepository.findByUserAndBookId(currentUser, bookId);
	}

	public void save(UserBook userBook) {
		userBookRepository.save(userBook);
	}

	public Iterable<UserBook> findAll() {
		return userBookRepository.findAll();
	}

	public Iterable<UserBook> findAllReadBooksByUser(User currentUser) {
		return userBookRepository.findAllReadBooksByUser(currentUser.getId());
	}

	public Iterable<UserBook> findAllWantToReadBooksByUser(User currentUser) {
		return userBookRepository.findAllWantToReadBooksByUser(currentUser.getId());

	}

	public Iterable<UserBook> findAllCurrentlyReadingBooksByUser(User currentUser) {
		return userBookRepository.findAllCurrentlyReadingBooksByUser(currentUser.getId());
	}

}
