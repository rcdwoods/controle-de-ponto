package br.com.controledeponto.validator;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidacaoDeDatas {

	private DateTimeFormatter dateTimeFormatter;

	public ValidacaoDeDatas(DateTimeFormatter dateTimeFormatter) {
		this.dateTimeFormatter = dateTimeFormatter;
	}

	public boolean validaData(String data) {
		try {
			this.dateTimeFormatter.parse(data);
		} catch (DateTimeParseException e) {
			throw e;
		}
		return true;
	}

}
