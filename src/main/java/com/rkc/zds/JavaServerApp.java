package com.rkc.zds;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.rkc.zds.linkedlist.SinglyLinkedList;
//import com.rkc.zds.linkedlist.impl.SinglyLinkedListImpl;
//import com.rkc.zds.web.spring.wiki.WikiBase;
import com.rkc.zds.db.AnsiDataHandler;
import com.rkc.zds.web.controller.BookController;

/**
 * @author rcampion
 */
public class JavaServerApp {

	//private static final Logger logger = LoggerFactory.getLogger(JavaServerApp.class);

	private static JavaServerApp instance = null;

	int port = 4245;
	String strResponse = "";
	boolean testOK = true; // Set to true if want to test positive response.

	HttpServer server = null;

	/** Data stored using an external database */
	public static final String PERSISTENCE_EXTERNAL = "DATABASE";
	/** Data stored using an internal copy of the HSQL database */
	public static final String PERSISTENCE_INTERNAL = "INTERNAL";

	/** The data handler that looks after read/write operations. */
	private static AnsiDataHandler dataHandler = null;
	
	private BookController bookController = null;

	private JavaServerApp() {

		try {
						
			dataHandler = new AnsiDataHandler();
			
			bookController = BookController.getInstance();

			server = HttpServer.create(new InetSocketAddress(port), 10);

			server.setExecutor(Executors.newFixedThreadPool(10));
			
			server.createContext("/auth-server/api/books", bookController.new BookHandler());

			server.createContext("/auth-server/api/greeting/", (exchange -> {

				if ("GET".equals(exchange.getRequestMethod())) {
					String responseText = "Hello World! from our framework-less REST API\n";
					exchange.sendResponseHeaders(200, responseText.getBytes().length);
					OutputStream output = exchange.getResponseBody();
					output.write(responseText.getBytes());
					output.flush();
				} else {
					exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
				}
				exchange.close();
			}));

			server.start();

			System.out.println("The server is running");

		} catch (Exception e) {
			//logger.error("Failure while initializing JavaServerApp", e);
		}
	}

	/**
	 * Get an instance of the current data handler.
	 *
	 * @return The current data handler instance, or <code>null</code> if the
	 *         handler has not yet been initialized.
	 */
	public static AnsiDataHandler getDataHandler() {
		if (JavaServerApp.dataHandler == null) {
			JavaServerApp.dataHandler = new AnsiDataHandler();
		}
		return JavaServerApp.dataHandler;
	}

	public static void main(String[] args) {

		JavaServerApp.instance = new JavaServerApp();

	}
	
	/*
	 * public static void main(String[] args) {
	 * 
	 * JavaServerApp app = new JavaServerApp();
	 * 
	 * Connection connect = null; Statement statement = null; ResultSet resultSet =
	 * null;
	 * 
	 * List<Book> list = new ArrayList<Book>(); SinglyLinkedList linkedList = new
	 * SinglyLinkedListImpl();
	 * 
	 * try { Class.forName("com.mysql.cj.jdbc.Driver"); connect =
	 * DriverManager.getConnection(
	 * "jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&serverTimezone=UTC&useLegacyDatetimeCode=false",
	 * "auth_user", "ChangeIt"); statement = connect.createStatement();
	 * 
	 * String sql = "SELECT * FROM Book";
	 * 
	 * resultSet = statement.executeQuery(sql); while (resultSet.next()) { Book dto
	 * = new Book(); dto.setId(resultSet.getString("id"));
	 * dto.setTitle(resultSet.getString("Title"));
	 * dto.setAuthor(resultSet.getString("Author")); int category =
	 * resultSet.getInt("Category");
	 * 
	 * BookCategoryEnum catEnum = BookCategoryEnum.fromInt(category);
	 * 
	 * dto.setCategory(catEnum);
	 * 
	 * list.add(dto); linkedList.append(dto); }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { try { if (resultSet != null)
	 * resultSet.close(); if (statement != null) statement.close(); if (connect !=
	 * null) connect.close(); } catch (SQLException e) { e.printStackTrace(); } }
	 * System.out.println(list); linkedList.display();
	 * 
	 * Map<BookCategoryEnum, List<Book>> newMap = new HashMap<BookCategoryEnum,
	 * List<Book>>(); newMap =
	 * list.stream().collect(Collectors.groupingBy(Book::getCategory)); //
	 * .collect(Collectors.toList()); System.out.println("original list");
	 * System.out.println(list); System.out.println("streamed map");
	 * System.out.println(newMap);
	 * 
	 * for (Entry<BookCategoryEnum, List<Book>> entry : newMap.entrySet()) {
	 * System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
	 * }
	 * 
	 * BookCategoryEnum key = BookCategoryEnum.fromInt(2);
	 * 
	 * List<Book> entry = newMap.get(key);
	 * 
	 * System.out.println(entry);
	 * 
	 * try { Class.forName("com.mysql.cj.jdbc.Driver"); connect =
	 * DriverManager.getConnection(
	 * "jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&serverTimezone=UTC&useLegacyDatetimeCode=false",
	 * "auth_user", "ChangeIt"); statement = connect.createStatement();
	 * list.clear();
	 * 
	 * String search = "efactor"; PreparedStatement ps = null;
	 * 
	 * String sql = "SELECT * FROM Book WHERE Title LIKE ?";
	 * 
	 * ps = connect.prepareStatement(sql); ps.setString(1, "%" + search + "%");
	 * resultSet = ps.executeQuery(); while (resultSet.next()) { Book dto = new
	 * Book(); dto.setId(resultSet.getString("id"));
	 * dto.setTitle(resultSet.getString("Title"));
	 * dto.setAuthor(resultSet.getString("Author")); int category =
	 * resultSet.getInt("Category");
	 * 
	 * BookCategoryEnum catEnum = BookCategoryEnum.fromInt(category);
	 * 
	 * dto.setCategory(catEnum);
	 * 
	 * list.add(dto); //linkedList.append(dto); }
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { try { if (resultSet != null)
	 * resultSet.close(); if (statement != null) statement.close(); if (connect !=
	 * null) connect.close(); } catch (SQLException e) { e.printStackTrace(); } }
	 * System.out.println(list);
	 * 
	 * //app.updateTest(list);
	 * 
	 * }
	 * 
	 * private void updateTest(List<Book> list) { // String sql =
	 * "UPDATE Registration " + // "SET age = 30 WHERE id in (100, 101)"; for(Book
	 * entry:list) {
	 * 
	 * Connection connect = null; PreparedStatement ps = null;
	 * 
	 * int result = 0;
	 * 
	 * try { Class.forName("com.mysql.cj.jdbc.Driver"); connect =
	 * DriverManager.getConnection(
	 * "jdbc:mysql://localhost:3306/auth?createDatabaseIfNotExist=true&serverTimezone=UTC&useLegacyDatetimeCode=false",
	 * "auth_user", "ChangeIt"); //statement = connect.createStatement();
	 * 
	 * int i = 0; String title = entry.getTitle() + i;
	 * 
	 * 
	 * String sql = "UPDATE Book SET Title = ? WHERE id = ?";
	 * 
	 * ps = connect.prepareStatement(sql); ps.setString(1, title); ps.setString(2,
	 * entry.getId()); result = ps.executeUpdate();
	 * 
	 * } catch (Exception e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } finally { try { if (ps != null) ps.close(); if
	 * (connect != null) connect.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } }
	 * 
	 * }
	 * 
	 * }
	 */

	public static void writeResponse(HttpExchange httpExchange, String response) throws IOException {
		
		httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
		
		httpExchange.sendResponseHeaders(200, response.length());

		OutputStream os = httpExchange.getResponseBody();

		os.write(response.getBytes());

		os.close();

	}
	
	/**
	 * 
	 * returns the url parameters in a map
	 * 
	 * @param query
	 * 
	 * @return map
	 * 
	 */

	public static Map<String, String> queryToMap(String query) {

		Map<String, String> result = new HashMap<String, String>();

		for (String param : query.split("&")) {

			String pair[] = param.split("=");

			if (pair.length > 1) {

				result.put(pair[0], pair[1]);

			} else {

				result.put(pair[0], "");

			}

		}

		return result;

	}	
}
