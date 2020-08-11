import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

public class SQL {

	public static void main(String[] args) throws SQLException {

		Connection conn = null;

		try {

			String url = "C:\\Users\\MADLo\\OneDrive\\Dokumente\\Eclipse Workspaces\\OOP\\FeelQuoteFlutterQuotes\\SQLite\\testMot.db";

			conn = DriverManager.getConnection(url);

			System.out.println("Connected \n");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}