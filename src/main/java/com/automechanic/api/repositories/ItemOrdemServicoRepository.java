package com.automechanic.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automechanic.api.entities.ItemOrdemServico;

@Repository
public interface ItemOrdemServicoRepository extends JpaRepository<ItemOrdemServico, Long> {
}