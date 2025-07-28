package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.traducao.ConsultaMyMemory;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

// Marca esta classe como uma entidade que será mapeada para uma tabela no banco de dados.
@Entity
// Especifica o nome da tabela no banco de dados.
@Table(name = "series")
public class Serie {
    // Define este campo como a chave primária da tabela.
    @Id
    // Configura a geração automática do valor da chave primária pelo banco de dados.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    // Salva o enum como texto (ex: "ACAO") no banco, em vez de um número (padrão).
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String atores;
    private String poster;
    private String sinopse;

    // Define um relacionamento "um-para-muitos" com a entidade Episodio.
    // cascade = CascadeType.ALL propaga as operações (salvar, deletar) da Série para seus Episódios.
    // fetch = FetchType.EAGER carrega os episódios junto com a série, o que é útil para evitar erros em certas operações.
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // Gerencia a serialização JSON para evitar loops infinitos no relacionamento bidirecional.
    @JsonManagedReference
    private List<Episodio> episodios = new ArrayList<>();

    // Construtor padrão exigido pelo JPA para criar instâncias da entidade.
    public Serie() {}

    // Construtor para criar a entidade a partir dos dados brutos recebidos da API (DTO).
    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        // Garante que a conversão da avaliação não quebre se o valor não for um número (ex: "N/A").
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

    // O método toString() foi ajustado para não imprimir a lista de episódios,
    // o que evita loops infinitos e problemas de performance.
    @Override
    public String toString() {
        return  "Gênero=" + genero +
                ", Título='" + titulo + '\'' +
                ", Total de Temporadas=" + totalTemporadas +
                ", Avaliação=" + avaliacao +
                ", Atores='" + atores + '\'' +
                ", Pôster='" + poster + '\'' +
                ", Sinopse='" + sinopse + '\'';
    }
}