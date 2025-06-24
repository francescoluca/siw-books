package it.uniroma3.siw.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Review;
import it.uniroma3.siw.repository.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;

	public void save(Review review) {
		review.setCreatedAt(LocalDateTime.now());
		reviewRepository.save(review);
	}

	public List<Review> findByBookId(Long bookId) {
		return reviewRepository.findByBookId(bookId);
	}

	public Optional<Review> findByBookIdAndWriterId(Long bookId, Long writerId) {
		return reviewRepository.findByBookIdAndWriterId(bookId, writerId);
	}

	public List<Review> findByBookIdAndWriterIdNot(Long bookId, Long writerId) {
		return reviewRepository.findByBookIdAndWriterIdNot(bookId, writerId);
	}

	public Review finById(Long reviewId) {
		return reviewRepository.findById(reviewId).get();
	}

	public void delete(Review review) {
		reviewRepository.delete(review);
	}

}
