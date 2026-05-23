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
import org.springframework.web.server.ResponseStatusException;

import com.ejemplo.relacionesjpa.dto.ClubRequest;
import com.ejemplo.relacionesjpa.entity.Asociacion;
import com.ejemplo.relacionesjpa.entity.Club;
import com.ejemplo.relacionesjpa.entity.Competicion;
import com.ejemplo.relacionesjpa.entity.Entrenador;
import com.ejemplo.relacionesjpa.repository.AsociacionRepository;
import com.ejemplo.relacionesjpa.repository.ClubRepository;
import com.ejemplo.relacionesjpa.repository.CompeticionRepository;
import com.ejemplo.relacionesjpa.repository.EntrenadorRepository;

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club no encontrado"));
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Club crearClub(@RequestBody ClubRequest request) {
        Entrenador entrenador = obtenerEntrenadorDisponible(request.getEntrenadorId(), null);

        Club club = new Club();
        copiarDatosAlClub(club, request, entrenador);

        return clubRepository.save(club);
    }

    @PutMapping("/{id}")
    @Transactional
    public Club actualizarClub(@PathVariable Long id, @RequestBody ClubRequest request) {
        Club clubExistente = clubRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club no encontrado"));

        Entrenador entrenador = obtenerEntrenadorDisponible(request.getEntrenadorId(), id);

        copiarDatosAlClub(clubExistente, request, entrenador);

        return clubRepository.save(clubExistente);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarClub(@PathVariable Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club no encontrado"));

        club.getCompeticiones().clear();
        clubRepository.save(club);
        clubRepository.delete(club);
    }

    private Entrenador obtenerEntrenadorDisponible(Long entrenadorId, Long clubIdActual) {
        if (entrenadorId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes seleccionar un entrenador existente.");
        }

        Entrenador entrenador = entrenadorRepository.findById(entrenadorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entrenador no encontrado."));

        if (clubIdActual == null) {
            if (clubRepository.existsByEntrenador_Id(entrenadorId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este entrenador ya está asignado a otro club.");
            }
        } else {
            boolean entrenadorAsignadoAOtroClub =
                    clubRepository.existsByEntrenador_IdAndIdNot(entrenadorId, clubIdActual);

            if (entrenadorAsignadoAOtroClub) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este entrenador ya está asignado a otro club.");
            }
        }

        return entrenador;
    }

    private void copiarDatosAlClub(Club club, ClubRequest request, Entrenador entrenador) {
        club.setNombre(request.getNombre());
        club.setCiudad(request.getCiudad());
        club.setEntrenador(entrenador);

        if (request.getAsociacion() != null) {
            Asociacion asociacionGuardada = asociacionRepository.save(request.getAsociacion());
            club.setAsociacion(asociacionGuardada);
        }

        club.getJugadores().clear();

        if (request.getJugadores() != null) {
            club.getJugadores().addAll(request.getJugadores());
        }

        club.getCompeticiones().clear();

        if (request.getCompeticiones() != null) {
            List<Competicion> competicionesGuardadas = guardarCompeticiones(request.getCompeticiones());
            club.getCompeticiones().addAll(competicionesGuardadas);
        }
    }

    private List<Competicion> guardarCompeticiones(List<Competicion> competiciones) {
        List<Competicion> competicionesGuardadas = new ArrayList<>();

        for (Competicion competicion : competiciones) {
            if (competicion.getId() != null && competicionRepository.existsById(competicion.getId())) {
                Competicion existente = competicionRepository.findById(competicion.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Competición no encontrada"));
                competicionesGuardadas.add(existente);
            } else {
                Competicion nueva = competicionRepository.save(competicion);
                competicionesGuardadas.add(nueva);
            }
        }

        return competicionesGuardadas;
    }
}