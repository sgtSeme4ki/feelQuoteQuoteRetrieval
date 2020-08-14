import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuoteAlgorithm {

	// the id is the index of the tuple at the same time
	// every ArrayList is one column
	ArrayList<String> text = new ArrayList<String>();
	ArrayList<String> author = new ArrayList<String>();

	public static void main(String[] args) {
		String generate_URL = "https://www.oberlo.com/blog/motivational-quotes";
		String inputLine;
		String extractedQuote = "";
		int id = 1;
		try {
			URL data = new URL(generate_URL);
			HttpURLConnection con = (HttpURLConnection) data.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			int countLine = 0;
			Pattern pattern = Pattern.compile(
					"<li style=\"font-weight: 400;\"><span style=\"font-weight: 400;\">&#x201C;(.+?)</span></li>");

			while ((inputLine = in.readLine()) != null) {
				Matcher matcher = pattern.matcher(inputLine);
				if (matcher.find()) {
					extractedQuote += matcher.group(1) + "\n";
					countLine++;

				}
			}
			extractedQuote = extractedQuote.replaceAll("&#x2013;", "|");
			extractedQuote = extractedQuote.replaceAll("&#x2019;", "'");
			extractedQuote = extractedQuote.replaceAll("&#x201C;", "");
			extractedQuote = extractedQuote.replaceAll("&#x201D;", "");
			extractedQuote = extractedQuote.replaceAll("&#x2015;", "|");
			extractedQuote = extractedQuote.replaceAll("&#x2013;", "|");
			extractedQuote = extractedQuote.replaceAll("&#x2013;", "|");
			extractedQuote = extractedQuote.replaceAll("&#x2018;", "'");
			extractedQuote = extractedQuote.replaceAll("&#x2026;", "...");
			extractedQuote = extractedQuote.replaceAll("&#xA0;", "");
			extractedQuote = extractedQuote.replaceAll("</span><span style=\"font-weight: 400;\">", "");
			extractedQuote = extractedQuote.replaceAll("<span><span style=\"font-weight: 400;\">", "");
			extractedQuote = extractedQuote.replaceAll("</span><i><span style=\"font-weight: 400;\">", "");
			extractedQuote = extractedQuote.replaceAll("</span> <span style=\"font-weight: 400;\">", "");
			extractedQuote = extractedQuote.replaceAll("</span></i><span style=\"font-weight: 400;\">", "");
			extractedQuote = extractedQuote.replaceAll("&#x2014;", " - ");
			extractedQuote = extractedQuote.replaceAll("&#x2014;", " - ");
			extractedQuote = extractedQuote.replaceAll("&#xE9;", "é");
			extractedQuote = extractedQuote.replaceAll("motivator.'", "motivator.");
			System.out.println(extractedQuote);
			in.close();
			SQLMethods test = new SQLMethods(null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
