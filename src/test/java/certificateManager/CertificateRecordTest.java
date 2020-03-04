package certificateManager;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CertificateRecordTest {

	public CertificateRecord record;

	@Test
	void testParse_birthDeath_withoutOrdinal() {
		record = new CertificateRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		assertEquals(1809, record.getYear());
		assertEquals(null, record.getOrdinal());
		assertEquals("Andrzej Kowalski", record.getPerson());
	}

	@Test
	void testParse_birthDeath_withOrdinal() {
		record = new CertificateRecord("Kowalski	Stanislaw	Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("Stanislaw Kowalski", record.getPerson());
	}

	@Test
	void testParse_marriage_endDate() {
		record = new CertificateRecord("Kowalski	Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals(1883, record.getYear());
		assertEquals(17, record.getOrdinal());
		assertEquals("Wladyslaw Kowalski " + CertificateRecord.conjunction + " Rozalia Ciechanska", record.getPerson());
	}

	@Test
	void testParse_birthDeath_beginDate() {
		record = new CertificateRecord("1637	1	Marcin 	Kowal");
		assertEquals(1637, record.getYear());
		assertEquals(1, record.getOrdinal());
		assertEquals("Marcin Kowal", record.getPerson());
	}

	@Test
	void testParse_marriage_beginDate() {
		record = new CertificateRecord("1653	2	Jan 	Kowalski		Elzbieta 	Michalowska");
		assertEquals(1653, record.getYear());
		assertEquals(2, record.getOrdinal());
		assertEquals("Jan Kowalski " + CertificateRecord.conjunction + " Elzbieta Michalowska", record.getPerson());
	}

//----

	@Test
	void testParse_marriage_beginDate_noSurnameBride_2additionalTab() {
		record = new CertificateRecord("1639	3	Piotr 	Kowal		Zofia 		");
		assertEquals(1639, record.getYear());
		assertEquals(3, record.getOrdinal());
		assertEquals("Piotr Kowal " + CertificateRecord.conjunction + " Zofia", record.getPerson());
	}

	@Test
	void testParse_marriage_beginDate_noSurnameBride_1additionalTab() {
		record = new CertificateRecord("1639	3	Piotr 	Kowal		Zofia 	");
		assertEquals(1639, record.getYear());
		assertEquals(3, record.getOrdinal());
		assertEquals("Piotr Kowal " + CertificateRecord.conjunction + " Zofia", record.getPerson());
	}

	@Test
	void testParse_marriage_beginDate_noSurnameBride() {
		record = new CertificateRecord("1639	3	Piotr 	Kowal		Zofia ");
		assertEquals(1639, record.getYear());
		assertEquals(3, record.getOrdinal());
		assertEquals("Piotr Kowal " + CertificateRecord.conjunction + " Zofia", record.getPerson());
	}

	@Test
	void testParse_marriage_beginDate_noSurnameBridegroom() {
		record = new CertificateRecord("1639	3	Piotr 			Zofia 	Kowal");
		assertEquals(1639, record.getYear());
		assertEquals(3, record.getOrdinal());
		assertEquals("Piotr " + CertificateRecord.conjunction + " Zofia Kowal", record.getPerson());
	}

//----

	@Test
	void testParse_marriage_beginDate_noNameBride() {
		record = new CertificateRecord("1639	3	Piotr 	Kowal			Kowalska");
		assertEquals(1639, record.getYear());
		assertEquals(3, record.getOrdinal());
		assertEquals("Piotr Kowal " + CertificateRecord.conjunction + " Kowalska", record.getPerson());
	}

	@Test
	void testParse_marriage_beginDate_noNameBridegroom() {
		record = new CertificateRecord("1639	3	 	Grocholowski		Zofia 	Kowal");
		assertEquals(1639, record.getYear());
		assertEquals(3, record.getOrdinal());
		assertEquals("Grocholowski " + CertificateRecord.conjunction + " Zofia Kowal", record.getPerson());
	}

//----

	@Test
	void testParse_marriage_beginDate_noOrdinal() {
		record = new CertificateRecord("1694		Jan 	Grocholowski		Regina 	Kowalska");
		assertEquals(1694, record.getYear());
		assertEquals(null, record.getOrdinal());
		assertEquals("Jan Grocholowski " + CertificateRecord.conjunction + " Regina Kowalska", record.getPerson());
	}

// ----

	@Test
	void testParse_birthDeath_withoutName() {
		record = new CertificateRecord("Kowalski		Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("Kowalski", record.getPerson());
	}

	@Test
	void testParse_birthDeath_withoutSurname() {
		record = new CertificateRecord("Stanislaw	Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("Stanislaw", record.getPerson());
	}

	@Test
	void testParse_birthDeath_withoutSurname_additionalTab() {
		record = new CertificateRecord("	Stanislaw	Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("Stanislaw", record.getPerson());
	}

	@Test
	void testParse_birthDeath_noName() {
		record = new CertificateRecord("Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("", record.getPerson());
	}

	@Test
	void testParse_birthDeath_noName_1additionalTab() {
		record = new CertificateRecord("	Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("", record.getPerson());
	}

	@Test
	void testParse_birthDeath_noName_2additionalTab() {
		record = new CertificateRecord("		Abramowice	41	1813");
		assertEquals(1813, record.getYear());
		assertEquals(41, record.getOrdinal());
		assertEquals("", record.getPerson());
	}


	@Test
	void testParse_marriage_endDate_noNameBridegroom() {
		record = new CertificateRecord("Kowalski		Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals(1883, record.getYear());
		assertEquals(17, record.getOrdinal());
		assertEquals("Kowalski " + CertificateRecord.conjunction + " Rozalia Ciechanska", record.getPerson());
	}
	
	@Test
	void testParse_marriage_endDate_noSurnameBridegroom() {
		record = new CertificateRecord("Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals(1883, record.getYear());
		assertEquals(17, record.getOrdinal());
		assertEquals("Wladyslaw " + CertificateRecord.conjunction + " Rozalia Ciechanska", record.getPerson());
	}
	
	@Test
	void testParse_marriage_endDate_noSurnameBridegroom_additionalTab() {
		record = new CertificateRecord("	Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals(1883, record.getYear());
		assertEquals(17, record.getOrdinal());
		assertEquals("Wladyslaw " + CertificateRecord.conjunction + " Rozalia Ciechanska", record.getPerson());
	}
	
	@Test
	void testParse_marriage_endDate_noNameBride() {
		record = new CertificateRecord("Kowalski	Wladyslaw	Ciechanska 		Chelm 	17 	1883");
		assertEquals(1883, record.getYear());
		assertEquals(17, record.getOrdinal());
		assertEquals("Wladyslaw Kowalski " + CertificateRecord.conjunction + " Ciechanska", record.getPerson());
	}
	
	@Test
	void testParse_marriage_endDate_noSurnameBride() {
		record = new CertificateRecord("Kowalski	Wladyslaw	 	Rozalia	Chelm 	17 	1883");
		assertEquals(1883, record.getYear());
		assertEquals(17, record.getOrdinal());
		assertEquals("Wladyslaw Kowalski " + CertificateRecord.conjunction + " Rozalia", record.getPerson());
	}

}
