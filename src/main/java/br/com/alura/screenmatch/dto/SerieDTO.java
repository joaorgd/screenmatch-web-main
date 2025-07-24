package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;

/**
 * DTO (Data Transfer Object) para a entidade Serie.
 * Este record é uma representação simplificada e segura da Série,
 * contendo apenas os dados que devem ser expostos pela API.
 * Ele não possui lógicas de negócio ou relacionamentos complexos do JPA.
 */
public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse) {
}