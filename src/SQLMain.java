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
			conn.setAutoCommit(false);
			System.out.println("Connected \n");
			SQLMethods test = new SQLMethods(conn);
			test.createSchema();
			test.insertQuote("motivational", "Rosen sind rot, Veillchen sind blau", "Bruce Lee");
			test.insertQuote("motivational", "Be like water", "Bruce Lee");
			test.insertQuote("motivational", "It's important to drink water", "Ghandi");
			System.out.println(test.toString());

			// test.createSchema();

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}

class SQLMethods {

	Connection c;

	public SQLMethods(Connection c) {
		this.c = c;
	}

	void createSchema() throws SQLException {
		Statement create = c.createStatement();
		try {

			create.execute("create table if not exists quote( " + "quote_id integer primary key autoincrement, "
					+ "quote_class varchar(15), " + "quote_text text, " + "author string, " + "ratingLike integer, "
					+ "ratingTime integer, " + "initial_date text);");
			create.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	void insertQuote(String quote_class, String quote, String author) {
		try {
			PreparedStatement insert = c.prepareStatement(
					"insert into quote(quote_class, quote_text, author, ratingLike, ratingTime, initial_date) values ( ?, ?, ?, 0, 0, date('now'));");
			insert.setString(1, quote_class);
			insert.setString(2, quote);
			insert.setString(3, author);
			insert.execute();
			insert.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void createView(String quote_class) {
		try {
			PreparedStatement view = c.prepareStatement(
					"drop view if exists quote_view; create quote_view(quote_id, quote_class, quote_text, author, rating) as"
							+ " select quote_id, quote_class, quote_text, author, ratingLike");
			view.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void favorite(int quote_id) {
		try {
			PreparedStatement fav = c
					.prepareStatement("update quote set ratingLike = ratingLike +100 where quote_id = ?;");
			fav.setInt(1, quote_id);
			fav.execute();
			fav.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void left(int quote_id) {
		try {
			PreparedStatement left = c
					.prepareStatement("update quote set ratingLike = ratingLike - 30 where quote_id = ?;");
			left.setInt(1, quote_id);
			left.execute();
			left.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void right(int quote_id) {
		try {
			PreparedStatement right = c
					.prepareStatement("update quote set ratingLike = ratingLike + 30 where quote_id = ?;");
			right.setInt(1, quote_id);
			right.execute();
			right.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		Statement StmtAll;
		String out = "";
		try {
			StmtAll = c.createStatement();
			ResultSet RsAll = StmtAll.executeQuery("Select * from quote;");
			while (RsAll.next())
				out += RsAll.getObject(1) + "|" + RsAll.getObject(2) + "|" + RsAll.getObject(3) + "|"
						+ RsAll.getObject(4) + "|" + RsAll.getObject(5) + "|" + RsAll.getObject(6) + "|"
						+ RsAll.getObject(7) + "\n";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out.substring(0, out.length() - 1);		//cut off last newline

	}
}