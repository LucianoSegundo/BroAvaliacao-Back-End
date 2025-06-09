package com.LFSoftware.BroAvaliacao.Entidade;

import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Papel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(unique = true)
	private String autoridade;
	
	@ManyToMany
	@JoinTable(
	name = "usuario_papel",
	joinColumns = @JoinColumn(name = "papel_id"),
	inverseJoinColumns = @JoinColumn(name = "usuario_id")
	)
	Set<Usuario> usuarios;
	
	public Papel() {
		super();
		
	}

	public Papel( String autoridade, Set<Usuario> usuario) {
		super();
		this.autoridade = autoridade;
		this.usuarios = usuario;
	}
	
	public void addUsuario(Usuario user) {
		if(user != null) {
			usuarios.add(user);
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuario(Set<Usuario> usuario) {
		this.usuarios = usuario;
	}

	public String getAutoridade() {
		return autoridade;
	}

	public void setAutoridade(String autoridade) {
		this.autoridade = autoridade;
	}

	@Override
	public int hashCode() {
		return Objects.hash(autoridade, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Papel other = (Papel) obj;
		return Objects.equals(autoridade, other.autoridade) && id == other.id;
	}

	@Override
	public String toString() {
		return autoridade;
	}

	
	
	
	
}
