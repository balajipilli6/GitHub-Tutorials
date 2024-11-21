package com.example.mavenapplication;

import jakarta.annotation.Nonnull;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.*;

@Entity
@Data
@Table(name = "Book_Record")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Book {

	public Book() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bookid;
	
	@Nonnull
	private String name;
   @Nonnull
	private String summary;
	
	private String rating;

	

	
	public Book(Long bookid, String name, String summary, String rating) {
		super();
		this.bookid = bookid;
		this.name = name;
		this.summary = summary;
		this.rating = rating;
	}

	public Long getBookid() {
		return bookid;
	}

	public void setBookid(Long bookid) {
		this.bookid = bookid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	
}
