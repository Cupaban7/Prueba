package com.ejemplo.relacionesjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.relacionesjpa.entity.Entrenador;

public interface EntrenadorRepository extends JpaRepository<Entrenador, Long> {
}