package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    @Query("SELECT DISTINCT s FROM Serie s JOIN FETCH s.episodios")
    List<Serie> findAllWithEpisodios();

    // Busca séries que contenham o trecho de título informado, retornando uma lista.
    List<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    // Busca séries por ator e com avaliação maior ou igual à informada.
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    // Busca as 5 séries com as melhores avaliações.
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    // Busca séries por uma categoria específica.
    List<Serie> findByGenero(Categoria categoria);

    // Busca séries com um número de temporadas menor ou igual e avaliação maior ou igual ao informado.
    // Esta é uma query JPQL customizada usando a anotação @Query.
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAValiacao(int totalTemporadas, double avaliacao);

    // As queries abaixo não foram pedidas, mas seriam necessárias para as funções
    // de busca de episódio que você criou. Adicionei-as aqui para completar a funcionalidade.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}