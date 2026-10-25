package com.automechanic.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automechanic.api.entities.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
}