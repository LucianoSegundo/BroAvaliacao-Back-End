package com.LFSoftware.BroAvaliacao.Servicos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormTempo {

	public static String formatarData(LocalDateTime datahora) {

		String rettorno = "";
		DateTimeFormatter formatador;

		formatador = DateTimeFormatter.ofPattern("d MMMM", new Locale("pt", "BR"));
		rettorno = datahora.format(formatador);

		if (datahora.getYear() != LocalDateTime.now().getYear()) {
			rettorno += " de " + datahora.getYear();
		}

		return rettorno;
	}

	public static String formatarhora(LocalDateTime dataHora) {

		DateTimeFormatter formatador;

		formatador = DateTimeFormatter.ofPattern("hh:mm", new Locale("pt", "BR"));
		return dataHora.format(formatador);

	}

}
