package com.LFSoftware.BroAvaliacao.Repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LFSoftware.BroAvaliacao.Entidade.LogAtualizacao;

public interface LogRepository extends JpaRepository<LogAtualizacao, Long> {

	Page<LogAtualizacao> findAllByLocalRestaurante_Id(Long restauranteId, PageRequest request);

	Page<LogAtualizacao> findAllByLocalItem_Id(Long restauranteId, PageRequest request);

}
