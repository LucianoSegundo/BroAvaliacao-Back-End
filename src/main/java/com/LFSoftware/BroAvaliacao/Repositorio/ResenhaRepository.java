package com.LFSoftware.BroAvaliacao.Repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LFSoftware.BroAvaliacao.Entidade.Resenha;

public interface ResenhaRepository extends JpaRepository<Resenha, Long> {

	Page<Resenha> findAllByAutor_Id(long usuarioID, PageRequest request);

	Page<Resenha> findAllByItem_Id(Long itemID, PageRequest request);

	Page<Resenha> findAllByRestaurante_Id(Long restauID, PageRequest request);

}
