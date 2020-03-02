package certificateManager;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebParserSzukajwarchiwach implements WebParser{

	private final static String domain = "https://szukajwarchiwach.pl/";
	
	private final String url;
	
	
	@Getter
	private String certificateURL;
	@Getter
	private String description;
		
	private boolean validateUrl() {
		return url.startsWith(domain);
	}
	
	@Override
	public void parse() {
		if (!validateUrl()) return;
		
		try {
			Document doc = Jsoup.connect(url).validateTLSCertificates(false).header("Accept-Language", "pl").get();
			
			Elements contentElems = doc.select("#contentBox p");
			
			certificateURL = contentElems.last().select("a").first().attr("href");
			certificateURL = domain + certificateURL;
			
			description = contentElems.first().text();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Certificate: " + certificateURL);
		System.out.println("Description: " + description);
	}
	
}
