package com.LFSoftware.BroAvaliacao.Entidade;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean notificar;
    private boolean notificarPorEmail;
    
	public Configuracao() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNotificar() {
		return notificar;
	}

	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}

	public boolean isNotificarPorEmail() {
		return notificarPorEmail;
	}

	public void setNotificarPorEmail(boolean notificarPorEmail) {
		this.notificarPorEmail = notificarPorEmail;
	}
    
    
}

