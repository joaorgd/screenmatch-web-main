package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Marca a classe como um componente de serviço, onde reside a lógica de negócio.
@Service
public class SerieService {
    // Injeta a dependência do repositório para interagir com o banco de dados.
    @Autowired
    private SerieRepository repositorio;

    // Busca todas as séries e as converte para DTO.
    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repositorio.findAll());
    }

    // Busca as 5 séries com melhor avaliação e as converte para DTO.
    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    // Busca as séries com os episódios mais recentes e as converte para DTO.
    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.encontrarEpisodiosMaisRecentes());
    }

    // Busca uma série por ID, retornando o DTO ou nulo se não for encontrada.
    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        // Se a série for encontrada no banco, converte para DTO e a retorna.
        if (serie.isPresent()) {
            Serie s = serie.get();
            return converteDado(s);
        }
        // Se a série não for encontrada, o método agora retorna nulo.
        return null;
    }

    // Método auxiliar para converter uma lista de Entidades para uma lista de DTOs.
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(this::converteDado)
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter uma única Entidade para um DTO.
    private SerieDTO converteDado(Serie s) {
        return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(),s.getPoster(), s.getSinopse());
    }
}