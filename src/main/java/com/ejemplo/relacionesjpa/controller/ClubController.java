package com.ejemplo.relacionesjpa.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.ejemplo.relacionesjpa.entity.Entrenador;
import com.ejemplo.relacionesjpa.repository.EntrenadorRepository;
import com.ejemplo.relacionesjpa.entity.Asociacion;
import com.ejemplo.relacionesjpa.entity.Club;
import com.ejemplo.relacionesjpa.entity.Competicion;
import com.ejemplo.relacionesjpa.repository.AsociacionRepository;
import com.ejemplo.relacionesjpa.repository.ClubRepository;
import com.ejemplo.relacionesjpa.repository.CompeticionRepository;

@RestController
@RequestMapping("/api/clubes")
public class ClubController {

    private final ClubRepository clubRepository;
    private final AsociacionRepository asociacionRepository;
    private final CompeticionRepository competicionRepository;
    private final EntrenadorRepository entrenadorRepository;

    public ClubController(
            ClubRepository clubRepository,
            AsociacionRepository asociacionRepository,
            CompeticionRepository competicionRepository,
            EntrenadorRepository entrenadorRepository) {
        this.clubRepository = clubRepository;
        this.asociacionRepository = asociacionRepository;
        this.competicionRepository = competicionRepository;
        this.entrenadorRepository = entrenadorRepository;
    }
    @GetMapping
    public List<Club> listarClubes() {
        return clubRepository.findAll();
    }

    @GetMapping("/{id}")
    public Club buscarPorId(@PathVariable Long id) {
        return clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Club crearClub(@RequestBody Club club) {
        prepararRelaciones(club);
        if (club.getEntrenador() != null && club.getEntrenador().getId() != null) {
            Entrenador entrenadorGuardado = entrenadorRepository.findById(club.getEntrenador().getId())
                    .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
            club.setEntrenador(entrenadorGuardado);
        }
        return clubRepository.save(club);
    }

    @PutMapping("/{id}")
    @Transactional
    public Club actualizarClub(@PathVariable Long id, @RequestBody Club datosClub) {
        Club clubExistente = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con id: " + id));

        clubExistente.setNombre(datosClub.getNombre());
        clubExistente.setCiudad(datosClub.getCiudad());
        clubExistente.setEntrenador(datosClub.getEntrenador());

        if (datosClub.getAsociacion() != null) {
            Asociacion asociacionGuardada = asociacionRepository.save(datosClub.getAsociacion());
            clubExistente.setAsociacion(asociacionGuardada);
        }

        clubExistente.getJugadores().clear();

        if (datosClub.getJugadores() != null) {
            clubExistente.getJugadores().addAll(datosClub.getJugadores());
        }

        clubExistente.getCompeticiones().clear();

        if (datosClub.getCompeticiones() != null) {
            List<Competicion> competicionesGuardadas = guardarCompeticiones(datosClub.getCompeticiones());
            clubExistente.getCompeticiones().addAll(competicionesGuardadas);
        }

        return clubRepository.save(clubExistente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarClub(@PathVariable Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Club no encontrado con id: " + id));

        club.getCompeticiones().clear();
        clubRepository.save(club);
        clubRepository.delete(club);
    }

    private void prepararRelaciones(Club club) {
        if (club.getAsociacion() != null) {
            Asociacion asociacionGuardada = asociacionRepository.save(club.getAsociacion());
            club.setAsociacion(asociacionGuardada);
        }

        if (club.getCompeticiones() != null) {
            List<Competicion> competicionesGuardadas = guardarCompeticiones(club.getCompeticiones());
            club.setCompeticiones(competicionesGuardadas);
        }
    }

    private List<Competicion> guardarCompeticiones(List<Competicion> competiciones) {
        List<Competicion> competicionesGuardadas = new ArrayList<>();

        for (Competicion competicion : competiciones) {
            Competicion competicionGuardada = competicionRepository.save(competicion);
            competicionesGuardadas.add(competicionGuardada);
        }

        return competicionesGuardadas;
    }
}