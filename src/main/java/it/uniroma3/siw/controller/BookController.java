package it.uniroma3.siw.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.uniroma3.siw.controller.validator.BookValidator;
import it.uniroma3.siw.model.Author;
import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AuthorService;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private AuthorService authorService;

	@Autowired
	private BookValidator bookValidator;

	@Autowired
	private UserService userService;

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private ReviewService reviewService;

	@GetMapping("/book/{id}")
	public String getBook(@PathVariable("id") Long bookId, Model model,
			@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails != null) {
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			User currentUser = userService.getUser(credentials.getId());
			model.addAttribute("userReview",
					reviewService.findByBookIdAndWriterId(bookId, currentUser.getId()).orElse(null));
			model.addAttribute("otherReviews", reviewService.findByBookIdAndWriterIdNot(bookId, currentUser.getId()));
		} else {
			model.addAttribute("userReview", null);
			model.addAttribute("otherReviews", reviewService.findByBookId(bookId));
		}
		model.addAttribute("book", this.bookService.findById(bookId));
		return "book.html";
	}

	@GetMapping("/books")
	public String getBooks(Model model) {
		model.addAttribute("books", this.bookService.findAll());
		return "books.html";
	}

	@GetMapping("/admin/formNewBook")
	public String formNewBook(Model model) {
		model.addAttribute("book", new Book());
		model.addAttribute("allAuthors", this.authorService.findAll());
		return "admin/formNewBook.html";
	}

	@PostMapping("/admin/book")
	public String newBook(@ModelAttribute("book") Book book, BindingResult bindingResult,
			@RequestParam("cover") MultipartFile cover, Model model) throws IOException {
		this.bookValidator.validate(book, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.bookService.save(book, cover);
			model.addAttribute("book", book);
			return "redirect:/book/" + book.getId();
		} else {
			return "admin/formNewBook";
		}
	}

	@GetMapping("/book/{id}/cover")
	public ResponseEntity<byte[]> cover(@PathVariable Long id) {
		byte[] image = bookService.getCover(id);
		if (image == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}

	@GetMapping("/admin/manageBooks")
	public String manageBooks(Model model) {
		model.addAttribute("books", this.bookService.findAll());
		return "/admin/manageBooks";
	}

	@GetMapping("/admin/formUpdateBook/{id}")
	public String formUpdateBook(@PathVariable("id") Long id, Model model) {
		model.addAttribute("book", this.bookService.findById(id));
		return "/admin/formUpdateBook";
	}

	@GetMapping("/admin/updateAuthors/{id}")
	public String updateAuthors(@PathVariable("id") Long bookId, Model model) {

		List<Author> authorsToAdd = this.authorsToAdd(bookId);
		model.addAttribute("authorsToAdd", authorsToAdd);
		model.addAttribute("book", this.bookService.findById(bookId));

		return "admin/authorsToAdd.html";
	}

	@GetMapping("/admin/addAuthorToBook/{authorId}/{bookId}")
	public String addAuthorToBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId,
			@RequestParam("cover") MultipartFile cover, Model model) throws IOException {
		Book book = this.bookService.findById(bookId);
		Author author = this.authorService.findById(authorId);
		List<Author> authors = book.getAuthors();
		authors.add(author);
		this.bookService.save(book, cover);
		List<Author> authorsToAdd = authorsToAdd(bookId);
		model.addAttribute("book", book);
		model.addAttribute("authorsToAdd", authorsToAdd);
		return "admin/authorsToAdd.html";
	}

	@GetMapping("/admin/removeAuthorFromBook/{authorId}/{bookId}")
	public String removeAuthorFromBook(@PathVariable("authorId") Long authorId, @PathVariable("bookId") Long bookId,
			@RequestParam("cover") MultipartFile cover, Model model) throws IOException {
		Book book = this.bookService.findById(bookId);
		Author author = this.authorService.findById(authorId);
		List<Author> authors = book.getAuthors();
		authors.remove(author);
		this.bookService.save(book, cover);
		List<Author> authorsToAdd = authorsToAdd(bookId);
		model.addAttribute("book", book);
		model.addAttribute("authorsToAdd", authorsToAdd);
		return "admin/authorsToAdd.html";
	}

	@GetMapping("/admin/deleteBook/{bookId}")
	public String deleteBook(@PathVariable("bookId") Long bookId, Model model) {
		Book book = this.bookService.findById(bookId);
		this.bookService.delete(book);
		model.addAttribute("books", this.bookService.findAll());
		return "admin/manageBooks";
	}

	private List<Author> authorsToAdd(Long bookId) {
		List<Author> authorsToAdd = new ArrayList<>();

		for (Author a : authorService.findAuthorsNotInBook(bookId)) {
			authorsToAdd.add(a);
		}
		return authorsToAdd;
	}
}
