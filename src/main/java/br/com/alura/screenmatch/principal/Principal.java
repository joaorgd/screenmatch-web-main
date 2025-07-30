package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoApi consumo = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private final SerieRepository repositorio;
    private Optional<Serie> serieBusca; // Usado para buscas que dependem de uma única série selecionada previamente.

    // Recebe o repositório via injeção de dependência, conectando a classe ao banco.
    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Método principal que exibe o menu e gerencia o fluxo de interação com o usuário.
    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    *************************************************
                    1 - Buscar e salvar nova série
                    2 - Buscar episódios de uma série
                    3 - Listar séries salvas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 Séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries por temporada e avaliação
                    9 - Buscar episódios por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                                    
                    0 - Sair                                 
                    *************************************************
                    """;

            System.out.println(menu);
            System.out.print("Digite sua opção: ");
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    // Orquestra a busca de dados na API e a persistência no banco.
    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println("\nSérie '" + serie.getTitulo() + "' salva no banco de dados!");
    }

    // Responsável por interagir com o usuário e obter os dados brutos da API.
    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        return conversor.obterDados(json, DadosSerie.class);
    }

    // Busca episódios de uma série, associa-os à entidade e salva tudo no banco.
    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Digite um trecho do nome da série para buscar os episódios");
        var nomeSerie = leitura.nextLine();

        List<Serie> seriesEncontradas = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(!seriesEncontradas.isEmpty()) {
            Serie serieEncontrada = seriesEncontradas.get(0);
            if(seriesEncontradas.size() > 1) {
                System.out.println("Múltiplas séries encontradas, processando a primeira: " + serieEncontrada.getTitulo());
            }

            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                temporadas.add(conversor.obterDados(json, DadosTemporada.class));
            }

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
            System.out.println("\nEpisódios de '" + serieEncontrada.getTitulo() + "' salvos no banco!");
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    // Lista todas as séries do banco, ordenadas por gênero.
    private void listarSeriesBuscadas(){
        List<Serie> series = repositorio.findAll();
        series.sort(Comparator.comparing(Serie::getGenero));
        System.out.println("\nSéries salvas no banco de dados (ordenadas por gênero):");
        series.forEach(System.out::println);
    }

    // Busca e exibe séries por um trecho do título.
    private void buscarSeriePorTitulo() {
        System.out.println("Digite um trecho do nome da série que deseja buscar:");
        var nomeSerie = leitura.nextLine();

        List<Serie> seriesEncontradas = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (!seriesEncontradas.isEmpty()) {
            System.out.println("Séries encontradas:");
            seriesEncontradas.forEach(System.out::println);
            this.serieBusca = Optional.of(seriesEncontradas.get(0));
        } else {
            System.out.println("Série não encontrada!");
            this.serieBusca = Optional.empty();
        }
    }

    // Busca e exibe séries por ator e avaliação mínima.
    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome do ator para busca?");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine(); // Limpa o buffer
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("\nSéries em que '" + nomeAtor + "' trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println("  - " + s.getTitulo() + " (Avaliação: " + s.getAvaliacao() + ")"));
    }

    // Busca e exibe as 5 séries com as melhores avaliações.
    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("\n--- TOP 5 SÉRIES ---");
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " (Avaliação: " + s.getAvaliacao() + ")"));
    }

    // Busca e exibe séries de uma categoria específica.
    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("\nSéries da categoria '" + nomeGenero + "':");
        seriesPorCategoria.forEach(System.out::println);
    }

    // Filtra séries por número máximo de temporadas e avaliação mínima.
    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAValiacao(totalTemporadas, avaliacao);
        System.out.println("\n*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }

    // Busca episódios por um trecho do título do episódio.
    private void buscarEpisodioPorTrecho(){
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s | Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));
    }

    // Busca e exibe os 5 melhores episódios de uma série previamente selecionada.
    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            System.out.println("\n--- TOP 5 EPISÓDIOS DA SÉRIE: " + serie.getTitulo() + " ---");
            topEpisodios.forEach(e ->
                    System.out.printf("  - Título: %s | Avaliação: %.1f\n",
                            e.getTitulo(), e.getAvaliacao()));
        }
    }

    // Busca episódios de uma série previamente selecionada, a partir de um ano de lançamento.
    private void buscarEpisodiosDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento:");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            System.out.println("\n--- EPISÓDIOS DE '" + serie.getTitulo() + "' A PARTIR DE " + anoLancamento + " ---");
            episodiosAno.forEach(System.out::println);
        }
    }
}