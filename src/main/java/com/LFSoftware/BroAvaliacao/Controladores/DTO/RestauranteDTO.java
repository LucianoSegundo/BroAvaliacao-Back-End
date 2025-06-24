package com.LFSoftware.BroAvaliacao.Controladores.DTO;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public record RestauranteDTO(String nome, 
		   @Schema(
			        type = "string",
			        format = "time",
			        description = "Horário de abertura (formato HH:mm:ss)",
			        example = "07:30:00"
			    )
		@JsonFormat(pattern = "HH:mm:ss") LocalTime Abertura,
		   @Schema(
			        type = "string",
			        format = "time",
			        description = "Horário de fechamento (formato HH:mm:ss)",
			        example = "08:30:00"
			    )
		@JsonFormat(pattern = "HH:mm:ss") LocalTime fechamento) {

}
