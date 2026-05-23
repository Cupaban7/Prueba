package com.ejemplo.relacionesjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.relacionesjpa.entity.Jugador;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {
}