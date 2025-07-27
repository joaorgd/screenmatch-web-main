package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Anotação que marca a classe como um componente de serviço, tornando-a elegível para injeção de dependência.
@Service
public class SerieService {
    // Injeta a dependência do repositório para interagir com o banco de dados.
    @Autowired
    private SerieRepository repositorio;

    // Método que contém a lógica de negócio para buscar todas as séries.
    public List<SerieDTO> obterTodasAsSeries() {
        // Busca as entidades do banco e as converte para DTOs antes de retornar.
        return converteDados(repositorio.findAll());
    }

    // Método com a lógica para buscar as 5 séries com melhor avaliação.
    public List<SerieDTO> obterTop5Series() {
        // Chama o método específico do repositório e converte o resultado para DTOs.
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    // Método privado auxiliar para reutilizar a lógica de conversão de Entidade para DTO.
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                // Mapeia cada entidade 'Serie' para um novo objeto 'SerieDTO'.
                .map(s -> new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(),s.getPoster(), s.getSinopse()))
                // Coleta os resultados em uma nova lista.
                .collect(Collectors.toList());
    }
}