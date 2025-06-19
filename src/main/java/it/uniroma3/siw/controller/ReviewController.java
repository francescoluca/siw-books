package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Book;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.BookService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ReviewService;
import it.uniroma3.siw.service.UserService;

@Controller
public class ReviewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private BookService bookService;
	@Autowired
	private UserService userService;
	@Autowired
	private CredentialsService credentialsService;

	@GetMapping("/formNewReview/{bookId}")
	public String formNewReview(@PathVariable Long bookId, Model model) {
		Book book = bookService.findById(bookId);
		model.addAttribute("book", book);
		model.addAttribute("review", new Review());
		return "formNewReview.html";
	}

	@PostMapping("/saveReview/{bookId}")
	public String saveReview(@PathVariable Long bookId, @ModelAttribute("review") Review review,
			@AuthenticationPrincipal UserDetails userDetails) {
		Book book = bookService.findById(bookId);
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User currentUser = userService.getUser(credentials.getId());
		review.setBook(book);
		review.setWriter(currentUser);
		reviewService.save(review);
		return "redirect:/book/" + bookId;
	}
}
