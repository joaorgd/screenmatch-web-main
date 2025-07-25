package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repositorio;

    @GetMapping("/series")
    // O método agora retorna uma lista de SerieDTO, e não mais a entidade Serie.
    // Isso garante que a API exponha apenas os dados definidos no DTO.
    public List<SerieDTO> obterSeries(){
        // O PONTO-CHAVE: Conversão de Entidade para DTO.
        // 1. Busca a lista de entidades 'Serie' do banco de dados.
        return repositorio.findAll()
                // 2. Transforma o Stream de 'Serie' em um Stream de 'SerieDTO'.
                .stream()
                // 3. Para cada série 's' da lista, cria um novo SerieDTO mapeando os campos.
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(),s.getPoster(), s.getSinopse()))
                // 4. Coleta os objetos SerieDTO resultantes em uma nova lista.
                .collect(Collectors.toList());
    }

    @GetMapping("/inicio")
    public String retornarInicio(){
        return "Bem-vindo ao screenmatch!";
    }
}