package certificateManager;

import certificateManager.CertificateRecord.PersonType;
import lombok.Setter;

public class CertificateNamer {

	public enum CertificateType {Birth, Marriage, Death, Other};
	
	private final static String separator = "_";
	private String birth;
	private String marriage;
	private String death;
	
	private CertificateRecord record;
	@Setter
	private CertificateType type = CertificateType.Other;
	
	public void setRecord(String recordText) {
		record = new CertificateRecord(recordText);
		if(record.getPersonType() == PersonType.MARRIAGE) type = CertificateType.Marriage; 
	}
	
	public void setCertificateTypeNames(String birth, String marriage, String death) {
		this.birth = birth;
		this.marriage = marriage;
		this.death = death;
	}
	
	public String generateName(){
		return generateName(null);
	}
	
	public String generateName(String extension){
		StringBuilder sb = new StringBuilder();

		if(record.getYear() != null) {
			sb.append(record.getYear());
		}
		
		if(record.getOrdinal() != null) {
			sb.append(separator);
			sb.append(record.getOrdinal());
		}
		
		if(record.getPerson() != null && !record.getPerson().isEmpty()) {
			sb.append(separator);
			sb.append(record.getPerson());
		}

		addCertificateType(sb);			
		
		if(extension != null) {
			sb.append(".");
			sb.append(extension);
		}
				
		
		String name = sb.toString();
		return (name == null) ? "" : name;
	}

	private void addCertificateType(StringBuilder sb) {
		if (type == CertificateType.Other) return;
		
		if (type == CertificateType.Birth && birth != null)
			sb.append(" - " + birth);
		if (type == CertificateType.Marriage && marriage != null)
			sb.append(" - " + marriage);
		if (type == CertificateType.Death && death != null)
			sb.append(" - " + death);
	}
}
