package com.LFSoftware.BroAvaliacao.Repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LFSoftware.BroAvaliacao.Entidade.Restaurante;

public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

	Page<Restaurante> findAllByDelecaoLogica(Boolean delecaoLogica, PageRequest request);

}
