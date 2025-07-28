package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    // Busca séries que contenham o trecho de título informado, retornando uma lista.
    List<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    // Busca séries por ator e com avaliação maior ou igual à informada.
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

    // Busca as 5 séries com as melhores avaliações.
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    // Busca séries por uma categoria específica (enum).
    List<Serie> findByGenero(Categoria categoria);

    // Busca séries usando uma query JPQL customizada para mais complexidade.
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAValiacao(int totalTemporadas, double avaliacao);

    // Busca episódios por um trecho do título do episódio.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    // Busca os 5 melhores episódios de uma série específica.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    // Busca episódios de uma série a partir de um ano de lançamento.
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    // Consulta customizada para encontrar os lançamentos mais recentes.
    // Ordena as séries pela data de lançamento mais recente de seus episódios.
    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s.id ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();
}