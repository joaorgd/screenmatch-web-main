package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Define a classe como um Controller REST, responsável por expor os endpoints da API.
@RestController
// Mapeia todas as requisições que começam com "/series" para este controller.
@RequestMapping("/series")
public class SerieController {

    // Injeta a dependência da camada de serviço, que contém as regras de negócio.
    @Autowired
    private SerieService servico;

    // Mapeia o endpoint principal (GET /series) para listar todas as séries.
    @GetMapping
    public List<SerieDTO> obterSeries(){
        // Delega a busca de todas as séries para a camada de serviço.
        return servico.obterTodasAsSeries();
    }

    // Mapeia o endpoint (GET /series/top5).
    @GetMapping("/top5")
    public List<SerieDTO> obterTop5(){
        // Delega a busca das 5 melhores séries para a camada de serviço.
        return servico.obterTop5Series();
    }

    // Mapeia o endpoint (GET /series/lancamentos).
    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        // Delega a busca dos lançamentos mais recentes para a camada de serviço.
        return servico.obterLancamentos();
    }

    // Mapeia um endpoint dinâmico para buscar uma série por seu ID (ex: GET /series/1).
    @GetMapping("/{id}")
    // @PathVariable extrai o valor do {id} da URL e o passa como parâmetro para o método.
    public SerieDTO obterPorId(@PathVariable Long id) {
        // Delega a busca por ID para a camada de serviço.
        return servico.obterPorId(id);
    }
}