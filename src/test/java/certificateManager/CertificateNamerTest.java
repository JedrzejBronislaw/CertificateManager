package certificateManager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CertificateNamerTest {

	private CertificateNamer namer;

	@BeforeEach
	void before() {
		namer = new CertificateNamer();
	}

	@Test
	void birthDeath_withoutOrdinal() {
		namer.setRecord("Kowalski	Andrzej	Belzyce	liber	1809");
		assertEquals("1809_Andrzej Kowalski", namer.generateName());
	}

	@Test
	void birthDeath_withOrdinal() {
		namer.setRecord("Kowalski	Stanislaw	Abramowice	41	1813");
		assertEquals("1813_41_Stanislaw Kowalski", namer.generateName());
	}

	@Test
	void marriage_endDate() {
		namer.setRecord("Kowalski	Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals("1883_17_Wladyslaw Kowalski i Rozalia Ciechanska", namer.generateName());
	}

	@Test
	void birthDeath_beginDate() {
		namer.setRecord("1637	1	Marcin 	Kowal");
		assertEquals("1637_1_Marcin Kowal", namer.generateName());
	}

	@Test
	void marriage_beginDate() {
		namer.setRecord("1653	2	Jan 	Kowalski		Elzbieta 	Michalowska");
		assertEquals("1653_2_Jan Kowalski i Elzbieta Michalowska", namer.generateName());
	}

//----

	@Test
	void marriage_beginDate_noSurnameBride_2additionalTab() {
		namer.setRecord("1639	3	Piotr 	Kowal		Zofia 		");
		assertEquals("1639_3_Piotr Kowal i Zofia", namer.generateName());
	}

	@Test
	void marriage_beginDate_noSurnameBride_1additionalTab() {
		namer.setRecord("1639	3	Piotr 	Kowal		Zofia 	");
		assertEquals("1639_3_Piotr Kowal i Zofia", namer.generateName());
	}

	@Test
	void marriage_beginDate_noSurnameBride() {
		namer.setRecord("1639	3	Piotr 	Kowal		Zofia ");
		assertEquals("1639_3_Piotr Kowal i Zofia", namer.generateName());
	}

	@Test
	void marriage_beginDate_noSurnameBridegroom() {
		namer.setRecord("1639	3	Piotr 			Zofia 	Kowal");
		assertEquals("1639_3_Piotr i Zofia Kowal", namer.generateName());
	}

//----

	@Test
	void marriage_beginDate_noNameBride() {
		namer.setRecord("1639	3	Piotr 	Kowal			Kowalska");
		assertEquals("1639_3_Piotr Kowal i Kowalska", namer.generateName());
	}

	@Test
	void marriage_beginDate_noNameBridegroom() {
		namer.setRecord("1639	3	 	Grocholowski		Zofia 	Kowal");
		assertEquals("1639_3_Grocholowski i Zofia Kowal", namer.generateName());
	}

//----

	@Test
	void marriage_beginDate_noOrdinal() {
		namer.setRecord("1694		Jan 	Grocholowski		Regina 	Kowalska");
		assertEquals("1694_Jan Grocholowski i Regina Kowalska", namer.generateName());
	}

// ----

	@Test
	void birthDeath_withoutName() {
		namer.setRecord("Kowalski		Abramowice	41	1813");
		assertEquals("1813_41_Kowalski", namer.generateName());
	}

	@Test
	void birthDeath_withoutSurname() {
		namer.setRecord("Stanislaw	Abramowice	41	1813");
		assertEquals("1813_41_Stanislaw", namer.generateName());
	}

	@Test
	void birthDeath_withoutSurname_additionalTab() {
		namer.setRecord("	Stanislaw	Abramowice	41	1813");
		assertEquals("1813_41_Stanislaw", namer.generateName());
	}

	@Test
	void birthDeath_noName() {
		namer.setRecord("Abramowice	41	1813");
		assertEquals("1813_41", namer.generateName());
	}

	@Test
	void birthDeath_noName_1additionalTab() {
		namer.setRecord("	Abramowice	41	1813");
		assertEquals("1813_41", namer.generateName());
	}

	@Test
	void birthDeath_noName_2additionalTab() {
		namer.setRecord("		Abramowice	41	1813");
		assertEquals("1813_41", namer.generateName());
	}

	@Test
	void marriage_endDate_noNameBridegroom() {
		namer.setRecord("Kowalski		Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals("1883_17_Kowalski i Rozalia Ciechanska", namer.generateName());
	}

	@Test
	void marriage_endDate_noSurnameBridegroom() {
		namer.setRecord("Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals("1883_17_Wladyslaw i Rozalia Ciechanska", namer.generateName());
	}

	@Test
	void marriage_endDate_noSurnameBridegroom_additionalTab() {
		namer.setRecord("	Wladyslaw	Ciechanska 	Rozalia	Chelm 	17 	1883");
		assertEquals("1883_17_Wladyslaw i Rozalia Ciechanska", namer.generateName());
	}

	@Test
	void marriage_endDate_noNameBride() {
		namer.setRecord("Kowalski	Wladyslaw	Ciechanska 		Chelm 	17 	1883");
		assertEquals("1883_17_Wladyslaw Kowalski i Ciechanska", namer.generateName());
	}

	@Test
	void marriage_endDate_noSurnameBride() {
		namer.setRecord("Kowalski	Wladyslaw	 	Rozalia	Chelm 	17 	1883");
		assertEquals("1883_17_Wladyslaw Kowalski i Rozalia", namer.generateName());
	}

	// ------

	@Test
	void testParse_empty() {
		namer.setRecord("");
		assertEquals("", namer.generateName());
	}

}
