package com.automechanic.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automechanic.api.entities.Peca;

@Repository
public interface PecaRepository extends JpaRepository<Peca, Long> {
    
    Optional<Peca> findByCodigo(String codigo);
}