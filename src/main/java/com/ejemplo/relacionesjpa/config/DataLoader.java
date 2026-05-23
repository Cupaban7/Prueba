package com.ejemplo.relacionesjpa.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ejemplo.relacionesjpa.repository.EntrenadorRepository;
import com.ejemplo.relacionesjpa.entity.Asociacion;
import com.ejemplo.relacionesjpa.entity.Club;
import com.ejemplo.relacionesjpa.entity.Competicion;
import com.ejemplo.relacionesjpa.entity.Entrenador;
import com.ejemplo.relacionesjpa.entity.Jugador;
import com.ejemplo.relacionesjpa.repository.AsociacionRepository;
import com.ejemplo.relacionesjpa.repository.ClubRepository;
import com.ejemplo.relacionesjpa.repository.CompeticionRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatos(
    		EntrenadorRepository entrenadorRepository,
            AsociacionRepository asociacionRepository,
            CompeticionRepository competicionRepository,
            ClubRepository clubRepository) {

        return args -> {

            Asociacion fcf = new Asociacion("Federación Colombiana de Fútbol", "Colombia", "Presidente FCF");
            asociacionRepository.save(fcf);

            Competicion liga = new Competicion(
                    "Liga Colombiana",
                    500000000,
                    LocalDate.of(2026, 1, 20),
                    LocalDate.of(2026, 6, 30)
            );

            Competicion copa = new Competicion(
                    "Copa Colombia",
                    250000000,
                    LocalDate.of(2026, 3, 1),
                    LocalDate.of(2026, 11, 15)
            );

            competicionRepository.save(liga);
            competicionRepository.save(copa);
            

            Entrenador entrenador = new Entrenador("Carlos", "Gómez", 48, "Colombiana");
            entrenadorRepository.save(entrenador);
            Club club = new Club("Tigres de Curumaní", "Curumaní", entrenador, fcf);

            club.agregarJugador(new Jugador("Andrés", "Pérez", 10, "Volante"));
            club.agregarJugador(new Jugador("Luis", "Martínez", 9, "Delantero"));
            club.agregarJugador(new Jugador("Juan", "Rodríguez", 1, "Arquero"));

            club.agregarCompeticion(liga);
            club.agregarCompeticion(copa);

            clubRepository.save(club);
        };
    }
}