package com.ejemplo.relacionesjpa.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.ejemplo.relacionesjpa.repository.ClubRepository;
import com.ejemplo.relacionesjpa.repository.EntrenadorRepository;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {

    private final EntrenadorRepository entrenadorRepository;
    private final ClubRepository clubRepository;

    public EntrenadorController(
            EntrenadorRepository entrenadorRepository,
            ClubRepository clubRepository) {
        this.entrenadorRepository = entrenadorRepository;
        this.clubRepository = clubRepository;
    }

    @GetMapping
    public List<Entrenador> listarEntrenadores() {
        return entrenadorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Entrenador buscarPorId(@PathVariable Long id) {
        return entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Entrenador crearEntrenador(@RequestBody Entrenador entrenador) {
        return entrenadorRepository.save(entrenador);
    }

    @PutMapping("/{id}")
    public Entrenador actualizarEntrenador(@PathVariable Long id, @RequestBody Entrenador datosEntrenador) {
        Entrenador entrenadorExistente = entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con id: " + id));

        entrenadorExistente.setNombre(datosEntrenador.getNombre());
        entrenadorExistente.setApellido(datosEntrenador.getApellido());
        entrenadorExistente.setEdad(datosEntrenador.getEdad());
        entrenadorExistente.setNacionalidad(datosEntrenador.getNacionalidad());

        return entrenadorRepository.save(entrenadorExistente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarEntrenador(@PathVariable Long id) {
        if (clubRepository.existsByEntrenador_Id(id)) {
            throw new RuntimeException("No se puede eliminar el entrenador porque está asignado a un club.");
        }

        entrenadorRepository.deleteById(id);
    }
}