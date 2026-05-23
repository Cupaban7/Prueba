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
import com.ejemplo.relacionesjpa.repository.EntrenadorRepository;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {

    private final EntrenadorRepository entrenadorRepository;

    public EntrenadorController(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
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
        Entrenador entrenador = entrenadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con id: " + id));

        entrenador.setNombre(datosEntrenador.getNombre());
        entrenador.setApellido(datosEntrenador.getApellido());
        entrenador.setEdad(datosEntrenador.getEdad());
        entrenador.setNacionalidad(datosEntrenador.getNacionalidad());

        return entrenadorRepository.save(entrenador);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarEntrenador(@PathVariable Long id) {
        entrenadorRepository.deleteById(id);
    }
}