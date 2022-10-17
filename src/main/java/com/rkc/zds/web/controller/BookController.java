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

	private String strResponse = "";
	private boolean testOK = true; // Set to true if want to test positive response.

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

			URI uri = httpExchange.getRequestURI();
			String test = "Hello From handleRequest: " + uri;

			// URI uri = new URI("http://example.com/foo/bar/42?param=true");
			String[] segments = uri.getPath().split("/");
			String lastSegment = segments[segments.length - 1];

			if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {

				httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers",
						"Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, apiKey");

				httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:4200");

				httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

				httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS");

				httpExchange.sendResponseHeaders(204, -1);

				return;

			}

			Headers r = httpExchange.getResponseHeaders();
			r.clear();

			Headers headers = httpExchange.getResponseHeaders();

			httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

			httpExchange.getResponseHeaders().add("Content-type", "application/json");

			if (httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {

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

			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("PUT")) {
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
			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {

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
			} else if (httpExchange.getRequestMethod().equalsIgnoreCase("DELETE")) {
				
				int bookId = Integer.parseInt(lastSegment);
				
				bookService.deleteBook(bookId)	;	
			
			}
		}
	}
}
