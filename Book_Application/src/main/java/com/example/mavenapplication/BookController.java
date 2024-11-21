package com.example.mavenapplication;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/book")
public class BookController {

	@Autowired
	public BookRepository bookRepository;


	@GetMapping("/allRecords")
	public List<Book> GetAlltheDetails()
	{
		return bookRepository.findAll();
	}

	@GetMapping(value = "{bookid}")
	public Optional<Book> getBookById(@PathVariable(value = "bookid") Long bookId)
	{

		return bookRepository.findById(bookId);
	}

	@DeleteMapping(value = "{bookid}")
	public String deleteBookById(@PathVariable(value = "bookid") Long bookId)
	{

		bookRepository.deleteById(bookId);
		
		return bookId+" record is deleted";
	}

	@PostMapping("/create")
	public Book createBook(@RequestBody Book bookRecord)
	{
		return bookRepository.save(bookRecord);
	}

	
	
	@PutMapping("/update")
	public Book updateBook(@RequestBody Book BookRecord) throws NotFoundException
	{
		if(BookRecord==null || BookRecord.getBookid()==null)
		{
			throw new NotFoundException();
		}
		
		Optional<Book> optional=bookRepository.findById(BookRecord.getBookid());
		if(!optional.isPresent()) {
			throw new NotFoundException();
		}
		Book existingBookRecord=optional.get();
		existingBookRecord.setName(BookRecord.getName());
		existingBookRecord.setSummary(BookRecord.getSummary());
		existingBookRecord.setRating(BookRecord.getRating());
		
			return bookRepository.save(existingBookRecord);
		
	}

}
