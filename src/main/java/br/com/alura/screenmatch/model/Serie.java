package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.traducao.ConsultaMyMemory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

// @Entity marca esta classe como uma entidade JPA, ou seja, um objeto que pode ser persistido no banco de dados.
@Entity
// @Table especifica o nome da tabela no banco de dados que esta entidade irá representar.
@Table(name = "series")
public class Serie {
    // @Id designa este campo como a chave primária da tabela.
    @Id
    // @GeneratedValue configura a estratégia de geração da chave primária pelo próprio banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    // @Enumerated(EnumType.STRING) instrui o JPA a salvar o enum 'Categoria' como texto no banco.
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;
    private LocalDate dataLancamento;

    // @OneToMany define o relacionamento "um-para-muitos" com a entidade Episodio.
    // cascade = CascadeType.ALL propaga as operações da Série para seus Episódios.
    // fetch = FetchType.EAGER carrega os episódios junto com a série.
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JsonManagedReference gerencia a serialização JSON para evitar loops infinitos no relacionamento.
    @JsonManagedReference
    private List<Episodio> episodios = new ArrayList<>();

    // Construtor padrão (vazio) é uma exigência do framework JPA.
    public Serie() {}

    // Construtor que transforma os dados brutos da API (DadosSerie) em uma entidade pronta para o banco.
    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        // Garante que a aplicação não quebre se a avaliação não for um número válido (ex: "N/A").
        try {
            this.avaliacao = Double.valueOf(dadosSerie.avaliacao());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }
        // Converte a string de gênero da API para o tipo enum, pegando apenas a primeira categoria.
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        // Chama um serviço externo para traduzir a sinopse antes de salvá-la.
        this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
        // Garante que a aplicação não quebre se a data tiver um formato inválido.
        try {
            this.dataLancamento = LocalDate.parse(dadosSerie.dataLancamento());
        } catch (DateTimeParseException e) {
            this.dataLancamento = null;
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    // Garante que a referência bidirecional seja estabelecida ao associar episódios à série.
    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    // O método toString() foi ajustado para não imprimir a lista de episódios, evitando loops e problemas de performance.
    @Override
    public String toString() {
        return  "Gênero=" + genero +
                ", Título='" + titulo + '\'' +
                ", Total de Temporadas=" + totalTemporadas +
                ", Avaliação=" + avaliacao +
                ", Data de Lançamento=" + dataLancamento +
                ", Atores='" + atores + '\'' +
                ", Pôster='" + poster + '\'' +
                ", Sinopse='" + sinopse + '\'';
    }
}