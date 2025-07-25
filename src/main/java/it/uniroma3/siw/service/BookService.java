package it.uniroma3.siw.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
		return bookRepository.existsByTitleAndAnyAuthors(title, authors);
	}

	public Book findById(Long id) {
		return bookRepository.findById(id).get();
	}

	public Iterable<Book> findAll() {
		return bookRepository.findAll();
	}

	public Page<Book> findAll(Pageable pageable) {
		return bookRepository.findAll(pageable);
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

	public Double findAverageRatingForBook(Book book) {
		return this.bookRepository.findAverageRatingForBook(book);
	}

	public Page<Book> searchBooksByKeyword(String keyword, Pageable pageable) {
		return this.bookRepository.searchBooksByKeyword(keyword, pageable);
	}

	public Iterable<Book> findBooksWithSorting(String field) {
		return this.bookRepository.findAll(Sort.by(Sort.Direction.ASC, field));
	}

	public Iterable<Book> findBooksByAuthor(Author author) {
		return this.bookRepository.findBooksByAuthor(author);
	}

	public long count() {
		return bookRepository.count();
	}

	public List<Book> findTop3ByReviewsCount() {
		Pageable topThree = PageRequest.of(0, 3);
		return bookRepository.findTop3ByReviewsCount(topThree);
	}

	public List<Book> findTop3ByAverageRating() {
		Pageable topThree = PageRequest.of(0, 3);
		return bookRepository.findTop3ByAverageRating(topThree);
	}

}
