package com.LFSoftware.BroAvaliacao.Repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.LFSoftware.BroAvaliacao.Entidade.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	public Boolean existsByUsuario(String nome);
	public Boolean existsByEmail(String nome);

	
	Optional<Usuario> findByUsuario(String nome);
	
}
