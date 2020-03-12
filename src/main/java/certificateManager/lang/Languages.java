package certificateManager.lang;

import java.util.Locale;

import lombok.Getter;

public enum Languages {
	POLISH(new Locale("pl","PL"), "polish"),
	ENGLISH(Locale.ENGLISH, "english");
	
	private static Languages defaultLanguage = ENGLISH;
	
	
	@Getter
	private Locale locale;
	@Getter
	private String label;
	
	private Languages(Locale locale, String label) {
		this.locale = locale;
		this.label = label;
	}
	
	Languages get(Locale language) {
		if (language == null) return defaultLanguage;
		
		Languages[] langs = Languages.values();
		
		for(Languages l : langs)
			if (l.locale.equals(language))
				return l;
		
		return defaultLanguage;
	}

}
