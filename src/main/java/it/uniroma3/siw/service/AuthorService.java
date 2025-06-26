package it.uniroma3.siw.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.repository.AuthorRepository;
import jakarta.transaction.Transactional;

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
		return authorRepository.existsByNameAndSurnameAndDayOfBirth(name, surname, dayOfBirth);
	}

	@Transactional
	public void save(Author author, MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			author.setPhoto(file.getBytes());
		}
		this.authorRepository.save(author);
	}

	public Iterable<Author> findAuthorsNotInBook(Long bookId) {
		return authorRepository.findAuthorsNotInBook(bookId);
	}

	public void delete(Author author) {
		authorRepository.delete(author);
	}

	public byte[] getPhoto(Long id) {
		return this.authorRepository.findById(id).map(Author::getPhoto).orElse(null);
	}

}
