package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Categoria;
import java.time.LocalDate;

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