package certificateManager;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebParserSzukajwarchiwach implements WebParser {

	private final static String domain = "https://szukajwarchiwach.pl/";

	private final String url;

	@Getter
	private String certificateURL;
	@Getter
	private String description;
	@Getter
	private String referenceCode;
	@Getter
	private String collectionURL;
	@Getter
	private String collection;

	private boolean validateUrl() {
		return url.startsWith(domain);
	}

	@Override
	public void parse() {
		if (!validateUrl()) return;

		try {
			Document doc = createConnection(url).get();

			Elements contentElems = doc.select("#contentBox p");

			certificateURL = contentElems.last().select("a").first().attr("href");
			certificateURL = domain + certificateURL;

			description = contentElems.first().text();

			referenceCode = extractReferenceCode(description);
			collectionURL = getCollectionUrl(url, referenceCode);
			collection = parseCollectionName(collectionURL);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Certificate: " + certificateURL);
		System.out.println("Description: " + description);
		System.out.println("Reference code: " + referenceCode);
		System.out.println("Collection URL: " + collectionURL);
		System.out.println("Collection: " + collection);
	}

	private String extractReferenceCode(String description) {
		int colonPos = description.indexOf(":");
		String referenceCode = description.substring(0, colonPos);

		return referenceCode;
	}

	private String getCollectionUrl(String url, String referenceCode) {
		return url.substring(0, url.indexOf(referenceCode) + referenceCode.length());
	}

	private String parseCollectionName(String collectionURL) {
		String collection;

		try {
			Document doc = createConnection(collectionURL).get();
			collection = doc.select(".details").select("a").first().text();

		} catch (IOException e) {
			e.printStackTrace();
			collection = "";
		}

		return collection;
	}

	private Connection createConnection(String collectionURL) {
		return Jsoup.connect(collectionURL).validateTLSCertificates(false).header("Accept-Language", "pl");
	}

}
