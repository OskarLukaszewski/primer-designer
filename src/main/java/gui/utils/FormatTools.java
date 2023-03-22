package gui.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatTools {

	public static void setFormatter(TextField textField, String pattern) {
		TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
			if (!change.isContentChange()) return change;

			String text = change.getControlNewText();
			final Pattern p = Pattern.compile(pattern);
			final Matcher m = p.matcher(text);

			if (m.find()) return change;
			else return null;
		});
		textField.setTextFormatter(textFormatter);
	}
}
