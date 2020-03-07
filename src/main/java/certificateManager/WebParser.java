package certificateManager;

public interface WebParser {
	
	String getCertificateURL();
	String getDescription();
	String getReferenceCode();
	String getCollectionURL();
	String getCollection();
	void parse();
}
