package com.example.mavenapplication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BookControllerTest {

	@Mock
	private BookRepository bookRepository;

	@InjectMocks
	private BookController bookController;
	Book Record_1=new Book(1L,"Zero to One","Zero to One presents at once an optimistic view of the future of progress in America and a new way of thinking about innovation: it starts by learning to ask the questions that lead you to find value in unexpected places.","4.5");

	Book Record_2=new Book(2L,"atomic habits","How to build better habits","4.6");
	Book Record_3=new Book(3L,"Rich Dad Poor Dad"," Shows parents why they can't rely on the school system to teach their kids about money","4.7");

	private MockMvc mockMvc;



	@BeforeEach
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		this.mockMvc=MockMvcBuilders.standaloneSetup(bookController).build();
	}


	@Test
	void testGetAlltheDetails() throws Exception{
		List<Book> records=new ArrayList<>(Arrays.asList(Record_1,Record_2,Record_3));
		//		records.add(Record_1);
		//		records.add(Record_2);
		//		records.add(Record_3);

		Mockito.when(bookRepository.findAll()).thenReturn(records);



		mockMvc.perform(MockMvcRequestBuilders.
				get("/book/allRecords").
				contentType(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk()).
		andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3))).
		andExpect(jsonPath("$[0].name", is("Zero to One"))).
		andExpect(jsonPath("$[1].name", is("atomic habits"))).
		andExpect(jsonPath("$[2].name", is("Rich Dad Poor Dad")));
	}

	@Test
	void TestFindById_success() throws Exception
	{
		Mockito.when(bookRepository.findById(Record_1.getBookid())).thenReturn(Optional.of(Record_1));
		mockMvc.perform(MockMvcRequestBuilders.
				get("/book/{bookid}",1L).
				contentType(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk()).
		andExpect(jsonPath("$.bookid", is(1))).
		andExpect(jsonPath("$.name", is("Zero to One"))).
		andExpect(jsonPath("$.summary", is("Zero to One presents at once an optimistic view of the future of progress in America and a new way of thinking about innovation: it starts by learning to ask the questions that lead you to find value in unexpected places."))).
		andExpect(jsonPath("$.rating", is("4.5")));

		Mockito.when(bookRepository.findById(Record_2.getBookid())).thenReturn(Optional.of(Record_2));
		mockMvc.perform(MockMvcRequestBuilders.
				get("/book/{bookid}",2L).
				contentType(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk()).
		andExpect(jsonPath("$.bookid", is(2))).
		andExpect(jsonPath("$.name", is("atomic habits"))).
		andExpect(jsonPath("$.rating", is("4.6")));

	}

	Book ExistingBook=new Book(1L,"atomic habits","How to build better habits","4.6");
	Book updatedBook=new Book(1L,"atomic habits updated","How to build better habits","4.8 in amazon");
	@Test
	void TestupdateBook_success() throws Exception
	{

		Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(ExistingBook));
		Mockito.when(bookRepository.save(ExistingBook)).thenReturn(updatedBook);


		mockMvc.perform(MockMvcRequestBuilders.
				put("/book/update").
				content("{\"name\":\"atomic habits updated\", \"summary\":\"How to build better habits\", \"rating\":\"4.8 in amazon\"}"))
		.andExpect(status().isOk()).
		andExpect(jsonPath("$.bookid", is(1))).
		andExpect(jsonPath("$.name", is("atomic habits updated"))).
		andExpect(jsonPath("$.summary", is("How to build better habits"))).
		andExpect(jsonPath("$.rating", is("4.8 in amazon")));

		//verify(bookRepository).save(ExistingBook);
		 verify(bookRepository, times(1)).save(ExistingBook);
	}

	
	
	@Test
	public void getBookId_success() throws Exception
	{
		Mockito.when(bookRepository.findById(Record_2.getBookid())).thenReturn(Optional.of(Record_2));
		
		mockMvc.perform(MockMvcRequestBuilders.
				get("/book/2").
				contentType(MediaType.APPLICATION_JSON)).
		andExpect(status().isOk()).
		andExpect(jsonPath("$", notNullValue())).
		andExpect(jsonPath("$.name", is("atomic habits")));
		
	}
	
//	@Test
//	void TestcreateBook_success() throws Exception
//	{
//Book recordd=Book.builder()
//		Book record=Book.class.
//				builder().
//				bookid(4L).
//				name("quality Thinking").
//				summary("good Thinking").
//				rating("5").
//				build();
//
//}
	
	
	
	
	@Test
	void TestDeleteById_success() throws Exception
	{
		Long bookId = 1L;

	    // Set up mock behavior
	    Mockito.when(bookRepository.existsById(bookId)).thenReturn(true);
	    doNothing().when(bookRepository).deleteById(bookId);

	    // Perform the DELETE request
	    mockMvc.perform(MockMvcRequestBuilders.delete("/book/delete/{id}", bookId))
	            .andExpect(status().isOk());

	    // Verify that deleteById was called once with the specified ID
	    verify(bookRepository, times(1)).deleteById(bookId);
}
}