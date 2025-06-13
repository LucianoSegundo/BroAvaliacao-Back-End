package com.LFSoftware.BroAvaliacao.Controladores.DTO;

public record RestauranteResponse(Long id, String nome, String proprietario, String Abertura, String fechamento, Boolean temProprietario) {

}
