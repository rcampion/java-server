package com.rkc.zds.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rkc.zds.JavaServerApp;
import com.rkc.zds.dto.BookDto;
import com.rkc.zds.exceptions.DataAccessException;
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

	/**
	 * 
	 * @param httpExchange There are any number of ways to create an error in the
	 *                     response. This method sends back a 404 and also
	 *                     mismatched the content length and the content, to create
	 *                     an unexpected EOF error.
	 * 
	 */
	private void replyError(HttpExchange httpExchange) {
		String replyString = "Error";
		try {
			Headers responseHeaders = httpExchange.getResponseHeaders();
			responseHeaders.add("Content-Type", ("application/json"));
			httpExchange.sendResponseHeaders(404, 0);

			try (OutputStream os = httpExchange.getResponseBody()) {
				os.write(replyString.getBytes());
			}

		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
	}

	private static void sendAndGetResponse(HttpExchange httpExchange, byte[] response) throws IOException {
		if (response.length > 0) {
			httpExchange.getResponseHeaders().add("Content-type", "application/json");
			httpExchange.getResponseHeaders().add("Content-length", Integer.toString(response.length));
			httpExchange.sendResponseHeaders(200, response.length);
			httpExchange.getResponseBody().write(response);
			httpExchange.close();
		}
	}

//--------------

	public class InfoHandler implements HttpHandler {

		public void handle(HttpExchange httpExchange) throws IOException {

			String response = "Use /get?hello=word&foo=bar to see how to handle url parameters";

			JavaServerApp.writeResponse(httpExchange, response.toString());

		}

	}

	public class GetHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange httpExchange) throws IOException {

			URI uri = httpExchange.getRequestURI();
			String test = "Hello From handleRequest: " + uri;

			// URI uri = new URI("http://example.com/foo/bar/42?param=true");
			String[] segments = uri.getPath().split("/");
			String lastSegment = segments[segments.length - 1];
			if (!lastSegment.equalsIgnoreCase("books")) {
				int id = Integer.parseInt(lastSegment);

				Headers r = httpExchange.getResponseHeaders();
				r.clear();

				if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {

					httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers",
							"Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, apiKey");

					httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:4200");

					httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

					httpExchange.sendResponseHeaders(204, -1);
					return;
				} else {

					Headers headers = httpExchange.getResponseHeaders();
								
					httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

					httpExchange.getResponseHeaders().add("Content-type", "application/json");
				
					BookDto response = null;
					try {
						response = JavaServerApp.getDataHandler().lookupBookById(id);
					} catch (DataAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String json = null;
					try {
						json = ow.writeValueAsString(response);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					JavaServerApp.writeResponse(httpExchange, json.toString());
											
				}				

			} else {

				Headers r = httpExchange.getResponseHeaders();
				r.clear();

				if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {

					httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers",
							"Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Access-Control-Allow-Origin, apiKey");

					httpExchange.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:4200");

					httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

					httpExchange.sendResponseHeaders(204, -1);
					return;
				} else {

					Headers headers = httpExchange.getResponseHeaders();
					
					httpExchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true");

					httpExchange.getResponseHeaders().add("Content-type", "application/json");
				
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
		}

	}

	public class GetHandlerOne implements HttpHandler {
		public void handle(HttpExchange httpExchange) throws IOException {

			byte[] response = getResponse().getBytes();
			sendAndGetResponse(httpExchange, response);

		}
	}

	// Handler for '/test' context
	public class PostHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange httpExchange) throws IOException {
			System.out.println("Serving the request");

			// Serve for POST requests only
			if (httpExchange.getRequestMethod().equalsIgnoreCase("POST")) {

				try {

					// REQUEST Headers
					Headers requestHeaders = httpExchange.getRequestHeaders();
					Set<Map.Entry<String, List<String>>> entries = requestHeaders.entrySet();

					int contentLength = Integer.parseInt(requestHeaders.getFirst("Content-length"));

					// REQUEST Body
					InputStream is = httpExchange.getRequestBody();

					byte[] data = new byte[contentLength];
					int length = is.read(data);

					// RESPONSE Headers
					Headers responseHeaders = httpExchange.getResponseHeaders();

					// Send RESPONSE Headers
					httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentLength);

					// RESPONSE Body
					OutputStream os = httpExchange.getResponseBody();

					os.write(data);

					httpExchange.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 
	 * @param httpExchange
	 * 
	 *                     Sends a 200 OK back
	 */
	private void replyOK(HttpExchange httpExchange) {

		try {
			Headers responseHeaders = httpExchange.getResponseHeaders();
			responseHeaders.add("Content-Type", ("application/json"));
			strResponse = getResponse();
			if (strResponse != null) {
				httpExchange.sendResponseHeaders(200, strResponse.length());

				try (OutputStream os = httpExchange.getResponseBody()) {
					os.write(strResponse.getBytes());
				}
			} else {
				httpExchange.sendResponseHeaders(200, -1);
			}

		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}
	}

	static String getResponse() {
		return "HttpServer Works!\n";
	}

}
