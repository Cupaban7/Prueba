package com.ejemplo.relacionesjpa.dto;

import java.util.ArrayList;
import java.util.List;

import com.ejemplo.relacionesjpa.entity.Asociacion;
import com.ejemplo.relacionesjpa.entity.Competicion;
import com.ejemplo.relacionesjpa.entity.Entrenador;
import com.ejemplo.relacionesjpa.entity.Jugador;

public class ClubRequest {

    private String nombre;
    private String ciudad;
    private Entrenador entrenador;
    private Asociacion asociacion;
    private List<Jugador> jugadores = new ArrayList<>();
    private List<Competicion> competiciones = new ArrayList<>();

    public ClubRequest() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public void setEntrenador(Entrenador entrenador) {
        this.entrenador = entrenador;
    }

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Competicion> getCompeticiones() {
        return competiciones;
    }

    public void setCompeticiones(List<Competicion> competiciones) {
        this.competiciones = competiciones;
    }

    public Long getEntrenadorId() {
        if (entrenador == null) {
            return null;
        }

        return entrenador.getId();
    }
}