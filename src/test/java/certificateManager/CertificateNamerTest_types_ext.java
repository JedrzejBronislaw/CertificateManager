package certificateManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import certificateManager.CertificateNamer.CertificateType;

class CertificateNamerTest_types_ext {

	private CertificateNamer namer;

	@BeforeEach
	void before() {
		namer = new CertificateNamer();
	}

	@Test
	void birthDeath_withoutTypeNames_withoutType() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	5	1809");
		assertEquals("1809_5_Andrzej Kowalski", namer.generateName());
	}

	@Test
	void birthDeath_withTypeNames_withoutType() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		namer.setCertificateTypeNames("birth", "marriage", "death");
		assertEquals("1809_Andrzej Kowalski", namer.generateName());
	}

	@Test
	void birthDeath_withoutTypeNames_withType() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		namer.setType(CertificateType.Birth);
		assertEquals("1809_Andrzej Kowalski", namer.generateName());
	}

	@Test
	void birthDeath_withTypeNames_withType_birth() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		namer.setCertificateTypeNames("birth", "marriage", "death");
		namer.setType(CertificateType.Birth);
		assertEquals("1809_Andrzej Kowalski - birth", namer.generateName());
	}

	@Test
	void birthDeath_withTypeNames_withType_marriage() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		namer.setCertificateTypeNames("birth", "marriage", "death");
		namer.setType(CertificateType.Marriage);
		assertEquals("1809_Andrzej Kowalski - marriage", namer.generateName());
	}

	@Test
	void birthDeath_withTypeNames_withType_death() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		namer.setCertificateTypeNames("birth", "marriage", "death");
		namer.setType(CertificateType.Death);
		assertEquals("1809_Andrzej Kowalski - death", namer.generateName());
	}

//------
	
	@Test
	void marriage_withTypeNames_autoMarriageType() {
		namer.setRecord("Kowalski	Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		namer.setCertificateTypeNames("birth", "marriage", "death");
		assertEquals("1883_17_Wladyslaw Kowalski i Rozalia Ciechanska - marriage", namer.generateName());
	}
	
	@Test
	void marriage_withTypeNames_autoMarriageType_changeType() {
		namer.setRecord("Kowalski	Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		namer.setCertificateTypeNames("birth", "marriage", "death");
		namer.setType(CertificateType.Birth);
		assertEquals("1883_17_Wladyslaw Kowalski i Rozalia Ciechanska - birth", namer.generateName());
	}

	//------

	@Test
	void birthDeath_withExtension() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	5	1809");
		assertEquals("1809_5_Andrzej Kowalski.jpg", namer.generateName("jpg"));
	}

}
