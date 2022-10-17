package com.rkc.zds.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rkc.zds.JavaServerApp;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.service.BookService;
import com.rkc.zds.service.impl.BookServiceImpl;
import com.rkc.zds.service.impl.PaginationPage;
import com.rkc.zds.utils.Pagination;
import com.rkc.zds.utils.SystemUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class BookController {

	private static BookController instance = null;
	
	private BookService bookService;

	private BookController() {
		this.bookService = new BookServiceImpl();
	}

	public static BookController getInstance() {

		if (instance == null) {
			instance = new BookController();
		}

		return instance;
	}

	public class BookHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
					
			Headers r = httpExchange.getResponseHeaders();
			r.clear();

			Headers headers = httpExchange.getResponseHeaders();

			httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

			httpExchange.getResponseHeaders().add("Content-type", "application/json");

			if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
				
				handleOptions(httpExchange);

			}

			if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
				
				handleGet(httpExchange);

			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("PUT")) {
				
				handlePut(httpExchange);
				
			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {

				handlePost(httpExchange);
				
			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
				
				handleDelete(httpExchange);
				
			}
		}
		
		/**
		 * 
		 * @param httpExchange
		 * @throws IOException
		 */
		private void handleOptions(HttpExchange httpExchange) throws IOException{
			httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers",
					"Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, apiKey");

			httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:4200");

			httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

			httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");

			httpExchange.sendResponseHeaders(204, -1);

			return;
			
		}
		
		/**
		 * 
		 * @param httpExchange
		 * @throws IOException
		 */
		private void handleGet(HttpExchange httpExchange) throws IOException {
			
			URI uri = httpExchange.getRequestURI();

			String[] segments = uri.getPath().split("/");
			String lastSegment = segments[segments.length - 1];
			
			if (!lastSegment.equalsIgnoreCase("books")) {

				int id = Integer.parseInt(lastSegment);

				BookDto book = bookService.findOne(id);
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = null;
				try {
					json = ow.writeValueAsString(book);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JavaServerApp.writeResponse(httpExchange, json.toString());

			} else {

				Map<String, String> params = SystemUtils.queryToMap(httpExchange.getRequestURI().getQuery());

				System.out.println(params);

				String searchParam = params.get("search");
				String sizeParam = params.get("size");
				String sortParam = params.get("sort");
				String pageParam = params.get("page");

				int page = Integer.parseInt(pageParam);
				int itemsPerPage = Integer.parseInt(sizeParam);

				int offset = (page) * itemsPerPage;

				Pagination pagination = new Pagination(itemsPerPage, offset);

				PaginationPage<BookDto> paginationPage = bookService.findBooks(pagination);

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = null;
				try {
					json = ow.writeValueAsString(paginationPage);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JavaServerApp.writeResponse(httpExchange, json.toString());

			}			
		}
		
		/**
		 * 
		 * @param httpExchange
		 */
		private void handlePost(HttpExchange httpExchange) {
			
			try {

				// REQUEST Headers
				Headers requestHeaders = httpExchange.getRequestHeaders();
				Set<Map.Entry<String, List<String>>> entries = requestHeaders.entrySet();

				int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));

				// REQUEST Body
				InputStream is = httpExchange.getRequestBody();

				byte[] data = new byte[contentLength];
				int length = is.read(data);

				ObjectMapper mapper = new ObjectMapper();

				BookDto book = new BookDto();
				try {
					book = mapper.readValue(data, BookDto.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				bookService.saveBook(book);

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = null;
				try {
					json = ow.writeValueAsString(book);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JavaServerApp.writeResponse(httpExchange, json.toString());

				httpExchange.close();

				httpExchange.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		/**
		 * 
		 * @param httpExchange
		 */
		private void handlePut(HttpExchange httpExchange) {
			
			try {

				// REQUEST Headers
				Headers requestHeaders = httpExchange.getRequestHeaders();
				Set<Map.Entry<String, List<String>>> entries = requestHeaders.entrySet();

				int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));

				// REQUEST Body
				InputStream is = httpExchange.getRequestBody();

				byte[] data = new byte[contentLength];
				int length = is.read(data);

				ObjectMapper mapper = new ObjectMapper();

				BookDto book = new BookDto();
				try {
					book = mapper.readValue(data, BookDto.class);
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				bookService.updateBook(book);

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = null;
				try {
					json = ow.writeValueAsString(book);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				JavaServerApp.writeResponse(httpExchange, json.toString());

				httpExchange.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		
		/**
		 * 
		 * @param httpExchange
		 * @throws IOException
		 */
		private void handleDelete(HttpExchange httpExchange) throws IOException {
			
			URI uri = httpExchange.getRequestURI();

			String[] segments = uri.getPath().split("/");
			String lastSegment = segments[segments.length - 1];
			
			int bookId = Integer.parseInt(lastSegment);
			
			BookDto book = bookService.findOne(bookId);
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = null;
			try {
				json = ow.writeValueAsString(book);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			bookService.deleteBook(bookId);	

			JavaServerApp.writeResponse(httpExchange, json.toString());
				
		}
	}
}
