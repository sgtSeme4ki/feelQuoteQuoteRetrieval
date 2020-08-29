import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class SQLMain {

	public static void main(String[] args) throws SQLException, IOException {

		Connection conn = null;

		try {

			String url = "jdbc:sqlite:C:\\Users\\MADLo\\OneDrive\\Dokumente\\Eclipse Workspaces\\OOP\\FeelQuoteFlutterQuotes\\SQLite\\testMot.db";

			conn = DriverManager.getConnection(url);
			System.out.println("Connected \n");
			SQLMethods test = new SQLMethods(conn);
			conn.setAutoCommit(false);
			//System.out.println(test.toString());
			test.right(1); // Make your fear of losing your greatest motivator.
			test.right(1);
			test.right(1);
			test.right(1);
			test.right(1);//+150
			test.sameKeywords();
			System.out.println(test.toString());
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}

class SQLMethods {

	private Connection c;

	public SQLMethods(Connection c) {
		this.c = c;
	}

	void createSchema() throws SQLException {
		Statement create = c.createStatement();
		try {

			create.execute("create table if not exists quote( " + "quote_id integer primary key autoincrement, "
					+ "quote_class varchar(15), " + "quote_text text, " + "author string, " + "ratingLike integer, "
					+ "initial_date text, showed integer default 0);");
			// create.execute(""); creating Trigger for view
			create.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	void insertQuote(String quote_class, String quote, String author) {
		try {
			PreparedStatement insert = c.prepareStatement(
					"insert into quote(quote_class, quote_text, author, ratingLike, initial_date) values ( ?, ?, ?, 0, date('now'));");
			insert.setString(1, quote_class);
			insert.setString(2, quote);
			insert.setString(3, author);
			insert.execute();
			insert.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void sameKeywords() {
		/*
		 * PreparedStatement key = c.prepareStatement("create temp view if not e");
		 */
		try (Statement notShown = c.createStatement();
				PreparedStatement updateRatingKeyword = c.prepareStatement(
						"update quote set ratingLike = ratingLike + ? where lower(quote_text) like lower(?) and quote_id <> ? and ratingLike > 150")) {
			ResultSet RsAll = notShown.executeQuery("select * from quote"); // get all results
			while (RsAll.next()) {

				int id = RsAll.getInt("quote_id");
				String quote_class = RsAll.getString("quote_class");
				int factor = RsAll.getInt("RatingLike");
				factor = factor / 10;

				String quote = RsAll.getString("quote_text");		//TODO all != usually -> fix
				quote = quote.replace(".", "");
				quote = quote.replace(",", "");
				quote = quote.replace("\"", "");
				quote = quote.replace("\'", "");
				String word[] = quote.split(" ");
				for (int i = 0; i < word.length; i++) {

					if ((word[i].equals("and") || word[i].equals("of") || word[i].equals("a") || word[i].equals("to")
							|| word[i].equals("it") || word[i].equals("that") || word[i].equals("you")
							|| word[i].equals("the"))) {
						i++;
						//skip most common english words
					}
					if (i == word.length)
						break;

					updateRatingKeyword.setInt(1, factor);
					updateRatingKeyword.setString(2, "%" + word[i] + "%");
					updateRatingKeyword.setInt(3, id);
					updateRatingKeyword.execute();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void sameAuthor() {

		// TODO handle unknown authors

		try (Statement StmtAll = c.createStatement();
				PreparedStatement updateRatingAuthor = c.prepareStatement(
						"update quote set ratingLike = ratingLike + ? where author like ? and quote_id <> ?")) {
			ResultSet RsAll = StmtAll.executeQuery("Select * from quote where ratingLike <> 0;");
			while (RsAll.next()) { // one iteration = one tuple

				int factor = RsAll.getInt("RatingLike");
				factor = factor / 10;
				updateRatingAuthor.setInt(1, factor); // increase ratingLike by factor

				String author = RsAll.getString("author");
				updateRatingAuthor.setString(2, author); // where author equals tuple from RsAll

				int id = RsAll.getInt("quote_id"); // and is not tuple from RsAll
				updateRatingAuthor.setInt(3, id);

				updateRatingAuthor.executeUpdate();
			}

		} catch (SQLException e) {

		}
	}

	void createView(String quote_class) {
		try {
			PreparedStatement view = c.prepareStatement(
					"drop view if exists quote_view; create temp quote_view(quote_id, quote_class, quote_text, author, rating) as"
							+ " select quote_id, quote_class, quote_text, author, ratingLike");
			view.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void favorite(int quote_id) {
		try {
			PreparedStatement fav = c
					.prepareStatement("update quote set ratingLike = ratingLike +100, showed = 1 where quote_id = ?;");
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
					.prepareStatement("update quote set ratingLike = ratingLike - 30, showed = 1 where quote_id = ?;");
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
					.prepareStatement("update quote set ratingLike = ratingLike + 30, showed = 1 where quote_id = ?;");
			right.setInt(1, quote_id);
			right.execute();
			right.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void dateRating() {
		try {
			PreparedStatement date = c.prepareStatement(
					"update quote set ratingLike = ratingLike + (select cast(julianday('now') - julianday(initial_date) as int) * 10) where ratingLike < 0;");
			date.execute();
			date = c.prepareStatement("update quote set initial_date = date('now');");
			date.execute();
			date.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void readFile(String fileName, String quote_class) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line = "";
		while ((line = reader.readLine()) != null) {
			String quote = line.substring(0, line.indexOf('|') - 1);
			String author = line.substring(line.indexOf('|') + 2);
			insertQuote(quote_class, quote, author);
		}
		reader.close();
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
		if (out.length() == 0) {
			return "no Tuples";
		}

		return out.substring(0, out.length() - 1); // cut off last newline

	}

	public String toString(int quote_id) {
		String out = "";
		try (PreparedStatement psAll = c.prepareStatement("select * from quote where quote_id  = ?")) {
			psAll.setInt(1, quote_id);
			ResultSet RsAll = psAll.executeQuery();
			while (RsAll.next())
				out += RsAll.getObject(1) + "|" + RsAll.getObject(2) + "|" + RsAll.getObject(3) + "|"
						+ RsAll.getObject(4) + "|" + RsAll.getObject(5) + "|" + RsAll.getObject(6) + "|"
						+ RsAll.getObject(7) + "\n";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (out.length() == 0) {
			return "no Tuples";
		}

		return out.substring(0, out.length() - 1); // cut off last newline

	}

	public String toString(String author) {
		String out = "";
		try (PreparedStatement psAll = c.prepareStatement("select * from quote where lower(author) like lower(?)")) { // search
																														// similiar
																														// author
			psAll.setString(1, "%" + author + "%");
			ResultSet RsAll = psAll.executeQuery();
			while (RsAll.next())
				out += RsAll.getObject(1) + "|" + RsAll.getObject(2) + "|" + RsAll.getObject(3) + "|"
						+ RsAll.getObject(4) + "|" + RsAll.getObject(5) + "|" + RsAll.getObject(6) + "|"
						+ RsAll.getObject(7) + "\n";

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (out.length() == 0) {
			return "no Tuples";
		}

		return out.substring(0, out.length() - 1); // cut off last newline

	}
}