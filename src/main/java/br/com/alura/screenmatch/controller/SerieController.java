package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Define a classe como um Controller de API REST, onde cada método retorna um objeto diretamente no corpo da resposta.
@RestController
// Mapeia todas as requisições que começam com "/series" para este controller.
@RequestMapping("/series")
public class SerieController {

    // Injeta a dependência da camada de serviço, que contém as regras de negócio.
    @Autowired
    private SerieService servico;

    // Mapeia o endpoint principal (ex: GET /series) para este método.
    @GetMapping
    public List<SerieDTO> obterSeries(){
        // Delega a responsabilidade de buscar e formatar os dados para a camada de serviço.
        return servico.obterTodasAsSeries();
    }

    // Mapeia o endpoint (ex: GET /series/top5) para este método.
    @GetMapping("/top5")
    public List<SerieDTO> obterTop5(){
        // Delega a busca das 5 melhores séries para a camada de serviço.
        return servico.obterTop5Series();
    }
}