package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@NotBlank
	private String title;
	@NotBlank
	private String isbnCode;
	@NotNull
	@Min(0)
	@Max(2025)
	private Integer year;
	private List<String> images;
	@ManyToMany
	private List<Author> authors;
	@OneToMany (mappedBy = "book")
	private List<Review> reviews;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> autors) {
		this.authors = autors;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public String getIsbnCode() {
		return isbnCode;
	}

	public void setIsbnCode(String isbnCode) {
		this.isbnCode = isbnCode;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
