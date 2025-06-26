package com.LFSoftware.BroAvaliacao.Repositorio;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.LFSoftware.BroAvaliacao.Entidade.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {

	Page<Item> findAllByDelecaoLogicaAndRestaurante_Id(boolean b, Long restauID, PageRequest request);

}
