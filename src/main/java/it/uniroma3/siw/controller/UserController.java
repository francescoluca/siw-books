package it.uniroma3.siw.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.UserBook;
import it.uniroma3.siw.model.UserBook.ReadingStatus;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserBookService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;
	@Autowired
	private BookService bookService;
	@Autowired
	private UserBookService userBookService;

	@GetMapping("/profile")
	public String profile(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			Model model) {
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = userService.getUser(credentials.getId());
		model.addAttribute("user", currentUser);
		model.addAttribute("readBooks", userBookService.findAllReadBooksByUser(currentUser));
		model.addAttribute("wantToReadBooks", userBookService.findAllWantToReadBooksByUser(currentUser));
		model.addAttribute("currentlyReadingBooks", userBookService.findAllCurrentlyReadingBooksByUser(currentUser));
		return "/profile";
	}

	@GetMapping("/readBook/{bookId}")
	public String readBook(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			@PathVariable("bookId") Long bookId, Model model) {
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = userService.getUser(credentials.getId());
		Book book = bookService.findById(bookId);
		Optional<UserBook> optionalUserBook = userBookService.findByUserAndBookId(currentUser, bookId);
		UserBook userBook;
		if (optionalUserBook.isPresent()) {
			userBook = optionalUserBook.get();
			userBook.setStatus(ReadingStatus.READ);
		} else {
			userBook = new UserBook();
			userBook.setUser(currentUser);
			userBook.setBook(book);
			userBook.setStatus(ReadingStatus.READ);
		}
		userBookService.save(userBook);
		model.addAttribute("user", currentUser);
		model.addAttribute("book", book);
		return "redirect:/book/" + bookId;
	}

	@GetMapping("/wantToReadBook/{bookId}")
	public String wantToReadBook(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			@PathVariable("bookId") Long bookId, Model model) {
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = userService.getUser(credentials.getId());
		Book book = bookService.findById(bookId);
		Optional<UserBook> optionalUserBook = userBookService.findByUserAndBookId(currentUser, bookId);
		UserBook userBook;
		if (optionalUserBook.isPresent()) {
			userBook = optionalUserBook.get();
			userBook.setStatus(ReadingStatus.WANT_TO_READ);
		} else {
			userBook = new UserBook();
			userBook.setUser(currentUser);
			userBook.setBook(book);
			userBook.setStatus(ReadingStatus.WANT_TO_READ);
		}
		userBookService.save(userBook);
		model.addAttribute("user", currentUser);
		model.addAttribute("book", book);
		return "redirect:/book/" + bookId;
	}

	@GetMapping("/currentlyReadingBook/{bookId}")
	public String currentlyReadingBook(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails,
			@PathVariable("bookId") Long bookId, Model model) {
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = userService.getUser(credentials.getId());
		Book book = bookService.findById(bookId);
		Optional<UserBook> optionalUserBook = userBookService.findByUserAndBookId(currentUser, bookId);
		UserBook userBook;
		if (optionalUserBook.isPresent()) {
			userBook = optionalUserBook.get();
			userBook.setStatus(ReadingStatus.CURRENTLY_READING);
		} else {
			userBook = new UserBook();
			userBook.setUser(currentUser);
			userBook.setBook(book);
			userBook.setStatus(ReadingStatus.CURRENTLY_READING);
		}
		userBookService.save(userBook);
		model.addAttribute("user", currentUser);
		model.addAttribute("book", book);
		return "redirect:/book/" + bookId;
	}
}
