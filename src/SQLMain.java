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
	
	void createSchema() {
		Prepared
	}
	
}