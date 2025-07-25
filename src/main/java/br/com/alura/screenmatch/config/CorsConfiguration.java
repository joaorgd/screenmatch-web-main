package br.com.alura.screenmatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração global para o CORS (Cross-Origin Resource Sharing).
 * Permite que a aplicação front-end acesse a API do back-end,
 * mesmo que estejam em origens (domínios/portas) diferentes.
 */
@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    /**
     * Sobrescreve o método padrão para configurar os mapeamentos de CORS da aplicação.
     * @param registry O registro de configurações CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Configura as regras de CORS para toda a aplicação.
        registry.addMapping("/**")
                // Permite requisições vindas especificamente desta origem.
                // No caso, o endereço onde a aplicação front-end está rodando (Live Server do VS Code, por exemplo).
                .allowedOrigins("http://127.0.0.1:5500")
                // Libera a utilização dos seguintes métodos HTTP para a origem permitida.
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}