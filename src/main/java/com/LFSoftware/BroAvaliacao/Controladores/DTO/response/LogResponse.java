package com.LFSoftware.BroAvaliacao.Controladores.DTO.response;

import java.util.List;

public record LogResponse(String mensagem, List<ModificacoesResponse> modificacoes) {

}
