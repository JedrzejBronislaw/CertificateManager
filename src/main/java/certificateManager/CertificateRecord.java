package certificateManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class CertificateRecord {
	
	private enum RecordType{START_DATE, END_DATE, OTHER};
	private enum Tip{START, END};
	enum PersonType{SINGLE, MARRIAGE, OTHER};
	
	public static final String conjunction = "i";

	@Getter
	private String person;
	@Getter
	private Integer year;
	@Getter
	private Integer ordinal;

	@Getter
	private final String text;
	private final String[] words;

	public CertificateRecord(String text) {
		this.text = text;
		words = prepareWords();
		parse();
	}

	private RecordType recordType = RecordType.OTHER;
	@Getter
	private PersonType personType = PersonType.OTHER;

	private void parse() {

		recordType = specifyRecordType();
		searchNumbers();
		searchNames();
	}

	private String[] prepareWords() {
		String[] words = text.split("\t");
		for (int i = 0; i < words.length; i++)
			words[i] = words[i].trim();
		return words;
	}

	private RecordType specifyRecordType() {
		if (isYear(words[0])) 
			return RecordType.START_DATE;

		if (isYear(words[words.length-1])) 
			return RecordType.END_DATE;
		
		return RecordType.OTHER;
	}

	private void searchNames() {
		if (recordType == RecordType.START_DATE)
			searchNames_startDate();
		else
		if (recordType == RecordType.END_DATE)
			searchNames_endDate();
		else
			person = "";
	}

	private void searchNames_endDate() {
		if (words.length <= 5) {
			List<String> augmentWords = augmentList(words, 5, Tip.START);
			buildSinglePerson(augmentWords, 1, 0);
		} else {	
			List<String> augmentWords = augmentList(words, 7, Tip.START);
			buildMarriagePerson(augmentWords, 1, 0, 3, 2);
		}
	}

	private void searchNames_startDate() {
		if (words.length <= 4) {
			List<String> augmentWords = augmentList(words, 3, Tip.END);
			buildSinglePerson(augmentWords, 2, 3);
		} else {
			List<String> augmentWords = augmentList(words, 7, Tip.END);
			buildMarriagePerson(augmentWords, 2, 3, 5, 6);
		}
	}

	private void buildSinglePerson(final List<String> words, final int name,  final int surname) {
		person = (words.get(name) + " " + words.get(surname)).trim();
		personType = PersonType.SINGLE;
	}

	private void buildMarriagePerson(final List<String> words, final int bridegroomName,  final int bridegroomSurname,  final int brideName,  final int brideSurname) {
		StringBuilder sb = new StringBuilder();

		String bridegroom = words.get(bridegroomName) + " " + words.get(bridegroomSurname);
		String bride = words.get(brideName) + " " + words.get(brideSurname);

		sb.append(bridegroom.trim());
		sb.append(" " + conjunction + " ");
		sb.append(bride.trim());

		person = sb.toString();
		personType = PersonType.MARRIAGE;
	}

	private void searchNumbers() {
		int tempInt;
		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].trim();

			try {
				tempInt = Integer.parseInt(words[i]);
			} catch (NumberFormatException e) {
				continue;
			}

			if (isYear(tempInt))
				year = tempInt;
			else
			if (isOrdinal(tempInt))
				ordinal = tempInt;
		}

	}

	private boolean isOrdinal(final int number) {
		return number < 1000;
	}

	private boolean isYear(final int number) {
		return number > 1000;
	}

	private boolean isYear(final String word) {
		int number;

		try {
			number = Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return false;
		}

		return isYear(number);
	}
	
	private List<String> augmentList(String[] words, final int fullSize, final Tip tip) {
		List<String> augmentWords = new ArrayList<>(Arrays.asList(words));
		
		for (int i = fullSize - augmentWords.size(); i > 0; i--)
			if(tip == Tip.START)
				augmentWords.add(0, "");
			else
				augmentWords.add("");
		
		return augmentWords;
	}

}
