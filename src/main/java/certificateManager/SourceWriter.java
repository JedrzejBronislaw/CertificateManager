package certificateManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class SourceWriter {
	
	private final String fileName;
	private final WebParser webParser;
	@Setter
	private String certificateName;
	@Setter
	private String url;
	
	private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public boolean write() {
		boolean newFile = !new File(fileName).exists();
		
		try(FileWriter writer = new FileWriter(fileName, true)){

			if(!newFile)
				writer.write("\n\n");
			
			if (certificateName != null)
				writer.write(certificateName);
			else
				writer.write("[no name]");
			writer.write("\n");
			writer.write("\tdownload time: "+LocalDateTime.now().format(dateTimeFormatter));
			writer.write("\n");
			writer.write("\t"+webParser.getCollection());
			writer.write("\n");
			writer.write("\t"+webParser.getDescription());
			writer.write("\n");
			writer.write("\t"+url);
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
}
