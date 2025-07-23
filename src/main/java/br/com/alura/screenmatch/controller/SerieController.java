package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SerieController {

    // Injeta a dependência do repositório para que possamos usá-lo.
    @Autowired
    private SerieRepository repositorio;

    @GetMapping("/series")
    public List<Serie> obterSeries(){
        // Agora, o método busca todas as séries do banco e as retorna.
        return repositorio.findAll();
    }
}