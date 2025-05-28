package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public boolean existsByTitleAndAuthors(String title, List<Author> authors) {
		return bookRepository.existsByTitleAndAuthors(title,authors);
	}

	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}

	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	public void save(Book book) {
		this.bookRepository.save(book);
	}
}
