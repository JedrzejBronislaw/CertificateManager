package certificateManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebParserSzukajwarchiwach implements WebParser {

	@AllArgsConstructor
	class Pagination {
		String prevScanURL = null;
		String nextScanURL = null;
	}

	private final static String domain = "https://szukajwarchiwach.pl/";
	private final static String nextLink = " Nastêpny > >";
	private final static String prevLink = " < < Poprzedni";

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
			certificateURL = addDomain(certificateURL);

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

	private Pagination extractPaginationLinks(Document doc) {
		String prevScanURL = null;
		String nextScanURL = null;
		
		Elements links = doc.select(".pagination").first().select("a");
		System.out.println("liks -> " + links.html());
		int paginationSize = links.size();
		System.out.println("paginationSize: " + paginationSize);
		if(paginationSize == 2) {
			prevScanURL = addDomain(links.first().attr("href"));
			nextScanURL = addDomain(links.last().attr("href"));
		}
		if(paginationSize == 1) {
			String textLink = links.first().text();
			String hrefLink = links.first().attr("href");

			if (textLink.equals(nextLink))
				nextScanURL = addDomain(hrefLink);
			if (textLink.equals(prevLink))
				prevScanURL = addDomain(hrefLink);
		}
		
		System.out.println("prev: " + prevScanURL);
		System.out.println("next: " + nextScanURL);
		return new Pagination(prevScanURL, nextScanURL);
	}
	
	private String addDomain(String url) {
		if (url.startsWith("/"))
			return domain + url.substring(1);
		else
			return domain + url;
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

	
	public List<String> getAllScansInCatallog() {
		if (!validateUrl()) return null;
		
		List<String> certificates = new LinkedList<>();
		try {
			Document doc = createConnection(url).get();
			
			Pagination p = extractPaginationLinks(doc);
			certificates.add(url);
			
			
			if (p.prevScanURL != null) {
				Pagination pagin;
				Document docu;
				
				pagin = p;
				do {
					certificates.add(0, pagin.prevScanURL);
					docu = createConnection(pagin.prevScanURL).get();
					pagin = extractPaginationLinks(docu);
				} while(pagin.prevScanURL != null);
			}
			
			if (p.nextScanURL != null) {
				Pagination pagin;
				Document docu;
				
				pagin = p;
				do {
					certificates.add(pagin.nextScanURL);
					docu = createConnection(pagin.nextScanURL).get();
					pagin = extractPaginationLinks(docu);
				} while(pagin.nextScanURL != null);				
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return certificates;
	}
	
	private Connection createConnection(String collectionURL) {
		return Jsoup.connect(collectionURL).validateTLSCertificates(false).header("Accept-Language", "pl");
	}

}
