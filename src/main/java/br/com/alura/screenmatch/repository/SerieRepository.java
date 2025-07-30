package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// A interface estende JpaRepository, herdando automaticamente métodos CRUD (save, findAll, etc.).
public interface SerieRepository extends JpaRepository<Serie, Long> {

    // Retorna uma lista de séries, permitindo múltiplos resultados para uma busca por título.
    List<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    // Consulta derivada que combina dois critérios: busca por ator E avaliação mínima.
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

    // Consulta derivada que busca o Top 5, ordenando pela avaliação em ordem decrescente.
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    // Busca séries por uma categoria específica, recebendo o tipo Enum diretamente.
    List<Serie> findByGenero(Categoria categoria);

    // Busca as 5 séries com a data de lançamento mais recente.
    List<Serie> findTop5ByOrderByDataLancamentoDesc();

    // Consulta JPQL customizada para filtros com parâmetros nomeados.
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAValiacao(int totalTemporadas, double avaliacao);

    // Consulta JPQL que busca episódios fazendo JOIN com a entidade Serie.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    // Consulta JPQL para buscar o Top 5 episódios de uma série específica.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    // Consulta JPQL que filtra episódios de uma série por ano de lançamento.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s.id ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();
}