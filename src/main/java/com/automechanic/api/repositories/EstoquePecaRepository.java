package com.automechanic.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automechanic.api.entities.EstoquePeca;

@Repository
public interface EstoquePecaRepository extends JpaRepository<EstoquePeca, Long> {
}