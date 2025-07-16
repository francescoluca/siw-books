package it.uniroma3.siw.controller;

import java.time.LocalDateTime;

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
		Review review = new Review();
		review.setCreatedAt(LocalDateTime.now());
		model.addAttribute("review", new Review());
		return "formNewReview.html";
	}

	@GetMapping("/formUpdateReview/{bookId}/{reviewId}")
	public String formUpdateReview(@PathVariable Long bookId, @PathVariable Long reviewId, Model model) {
		Review review = reviewService.finById(reviewId);
		Book book = bookService.findById(bookId);
		model.addAttribute("book", book);
		model.addAttribute("review", review);
		return "formUpdateReview.html";
	}

	@PostMapping("/updateReview/{bookId}/{reviewId}")
	public String updateReview(@PathVariable Long bookId, @PathVariable Long reviewId,
			@ModelAttribute("review") Review updatedReview) {
		Review review = reviewService.finById(reviewId);
		review.setTitle(updatedReview.getTitle());
		review.setText(updatedReview.getText());
		review.setStars(updatedReview.getStars());
		reviewService.save(review);
		return "redirect:/book/" + bookId;
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

	@GetMapping("deleteReview/{bookId}/{reviewId}")
	public String deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId) {
		this.reviewService.delete(reviewService.finById(reviewId));
		return "redirect:/book/" + bookId;
	}

}
