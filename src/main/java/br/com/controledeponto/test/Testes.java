package br.com.controledeponto.test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;

public class Testes {
	
	public static void main(String[] args) {
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("'PT'HH'H'mm'M'ss'S'"))));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(("yyyy-MM-dd'T'HH:mm:ss"))));
	}

}
