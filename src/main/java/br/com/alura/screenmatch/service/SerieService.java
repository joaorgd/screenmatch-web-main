package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {
    @Autowired
    private SerieRepository repositorio;

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repositorio.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repositorio.findTop5ByOrderByDataLancamentoDesc());
    }

    // --- LÓGICA CONSTRUÍDA ABAIXO ---

    /**
     * Busca uma única série pelo seu ID.
     * @param id O ID da série a ser buscada.
     * @return um SerieDTO com os dados da série, ou null se não for encontrada.
     */
    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            // Reutiliza o método de conversão para manter o código limpo
            return converteDado(s);
        }
        return null; // Retorna nulo se a série não for encontrada.
    }

    /**
     * Busca todos os episódios de uma série específica.
     * @param id O ID da série da qual os episódios serão buscados.
     * @return Uma lista de EpisodioDTO, ou null se a série não for encontrada.
     */
    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serie = repositorio.findById(id);

        if (serie.isPresent()) {
            Serie s = serie.get();
            // Mapeia a lista de entidades 'Episodio' para uma lista de 'EpisodioDTO'.
            return s.getEpisodios().stream()
                    .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                    .collect(Collectors.toList());
        }
        return null; // Retorna nulo se a série não for encontrada.
    }

    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(this::converteDado) // Reutiliza o método de conversão individual
                .collect(Collectors.toList());
    }

    // Método auxiliar para converter uma única Entidade para um DTO.
    private SerieDTO converteDado(Serie s) {
        return new SerieDTO(s.getId(), s.getTitulo(), s.getTotalTemporadas(), s.getAvaliacao(), s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse(), s.getDataLancamento());
    }

    public List<SerieDTO> obterSeriesPorCategoria(String genero) {
        Categoria categoria = Categoria.fromPortugues(genero);
        return converteDados(repositorio.findByGenero(categoria));
    }
}