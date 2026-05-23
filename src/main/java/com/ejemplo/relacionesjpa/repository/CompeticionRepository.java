package com.ejemplo.relacionesjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.relacionesjpa.entity.Competicion;

public interface CompeticionRepository extends JpaRepository<Competicion, Long> {
}