package com.LFSoftware.BroAvaliacao.Repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LFSoftware.BroAvaliacao.Entidade.Papel;


@Repository
public interface PapelRepository extends JpaRepository<Papel, Long> {
    // Método para buscar o Papel pelo campo autoridade
    Optional<Papel> findByAutoridade(String autoridade);
}
