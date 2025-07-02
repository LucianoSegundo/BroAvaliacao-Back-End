package com.LFSoftware.BroAvaliacao.Repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LFSoftware.BroAvaliacao.Entidade.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

	Page<Comentario> findAllByResenha_Id(PageRequest request, Long resenhaID);

	Page<Comentario> findAllByComentariopai_Id(PageRequest request, Long comentarioID);

}
