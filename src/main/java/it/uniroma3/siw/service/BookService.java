package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.repository.BookRepository;
import jakarta.transaction.Transactional;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public boolean existsByTitleAndAuthors(String title, List<Author> authors) {
		return bookRepository.existsByTitleAndAuthors(title, authors);
	}

	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}

	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	@Transactional
	public void save(Book book, MultipartFile file) throws IOException {
		if (!file.isEmpty()) {
			book.setCoverImage(file.getBytes());
		}
		this.bookRepository.save(book);
	}

	public byte[] getCover(Long id) {
		return this.bookRepository.findById(id).map(Book::getCoverImage).orElse(null);
	}

	public void delete(Book book) {
		bookRepository.delete(book);
	}
}
