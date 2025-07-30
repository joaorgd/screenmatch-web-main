package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Anotação que define esta classe como um serviço, o cérebro da lógica de negócio da API.
@Service
public class SerieService {
    // Injeta a dependência do repositório para interagir com o banco de dados.
    @Autowired
    private SerieRepository repositorio;

    // Busca todas as séries e as converte para o formato DTO antes de retorná-las.
    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repositorio.findAll());
    }

    // Busca as 5 séries com melhor avaliação.
    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    // Busca as séries com os lançamentos mais recentes.
    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.findTop5ByOrderByDataLancamentoDesc());
    }

    // Método privado que centraliza a lógica de conversão de Entidade para DTO, evitando código duplicado.
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse(), s.getDataLancamento()))
                .collect(Collectors.toList());
    }
}