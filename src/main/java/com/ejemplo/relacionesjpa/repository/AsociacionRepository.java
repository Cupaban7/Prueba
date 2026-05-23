package com.ejemplo.relacionesjpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ejemplo.relacionesjpa.entity.Asociacion;

public interface AsociacionRepository extends JpaRepository<Asociacion, Long> {
}