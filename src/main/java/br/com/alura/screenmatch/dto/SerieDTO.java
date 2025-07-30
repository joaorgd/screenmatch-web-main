package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;
import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) para a entidade Serie.
 * Este 'record' é uma representação simplificada e segura dos dados,
 * contendo apenas os campos que devem ser expostos pela nossa API REST.
 */
public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse,
                       LocalDate dataLancamento) {
}