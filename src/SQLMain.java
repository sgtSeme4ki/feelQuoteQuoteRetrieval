import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class SQLMain {

	public static void main(String[] args) throws SQLException {

		Connection conn = null;

		try {

			String url = "jdbc:sqlite:C:\\Users\\MADLo\\OneDrive\\Dokumente\\Eclipse Workspaces\\OOP\\FeelQuoteFlutterQuotes\\SQLite\\testMot.db";

			conn = DriverManager.getConnection(url);

			System.out.println("Connected \n");
			SQLMethods test = new SQLMethods(conn);
			//test.createSchema();
			

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}
class SQLMethods{
	
	Connection c;
	
	public SQLMethods(Connection c) {
		this.c = c;
	}
	
	void createSchema() throws SQLException {
		Statement create = c.createStatement();
		try {
			
			create.execute("create table if not exists quote( "
					+ "quote_id integer primary key autoincrement, "
					+ "quote_class varchar(15), "
					+ "quote_text text, "
					+ "author string, "
					+ "ratingLike integer, "
					+ "ratingTime integer, "
					+ "initial_date text);");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	void insertQuote(String quote_class, String quote, String author) {
		try {
			PreparedStatement insert = c.prepareStatement("insert into quote(default, ?, ?, ?, 0, 0, select date('now'));");
			insert.setString(1, quote_class);
			insert.setString(2, quote);
			insert.setString(3, author);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void createView(String quote_class) {
		try {
			//create View here
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}