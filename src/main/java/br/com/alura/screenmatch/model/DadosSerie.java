package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// @JsonIgnoreProperties ignora quaisquer campos do JSON que não foram mapeados aqui, evitando erros.
@JsonIgnoreProperties(ignoreUnknown = true)
// Este 'record' serve como um DTO para receber os dados brutos e aninhados da API OMDb.
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         // @JsonAlias mapeia o nome do campo no JSON (ex: "Genre") para o nome do atributo em Java (ex: "genero").
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopse,
                         @JsonAlias("Released") String dataLancamento) {
}