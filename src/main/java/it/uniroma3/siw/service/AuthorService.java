package it.uniroma3.siw.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.repository.AuthorRepository;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	public Iterable<Author> findAll() {
		return authorRepository.findAll();
	}
	
	public Author findById(Long id) {
		return authorRepository.findById(id).get();
	}

	public boolean existsByNameAndSurnameAndDayOfBirth(String name, String surname, LocalDate dayOfBirth) {
		return authorRepository.existsByNameAndSurnameAndDayOfBirth(name,surname,dayOfBirth);
	}

	public void save(Author author) {
		authorRepository.save(author);
	}

	public Iterable<Author> findAuthorsNotInBook(Long bookId) {
		return authorRepository.findAuthorsNotInBook(bookId);
	}
	
	
}
