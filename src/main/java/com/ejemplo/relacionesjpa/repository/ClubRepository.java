package com.ejemplo.relacionesjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.relacionesjpa.entity.Club;

public interface ClubRepository extends JpaRepository<Club, Long> {

    boolean existsByEntrenador_Id(Long entrenadorId);

    boolean existsByEntrenador_IdAndIdNot(Long entrenadorId, Long clubId);
}