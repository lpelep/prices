package com.inditex.prices.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la documentación OpenAPI para la API de precios.
 * Define la información general de la API y contacto.
 */
@Configuration
@NoArgsConstructor
public class OpenApiConfig {

  /**
   * Crea y configura la documentación OpenAPI personalizada.
   *
   * @return objeto OpenAPI con información de la API
   */
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
      .info(new Info()
        .title("Ecommerce Prices API")
        .version("1.0")
        .description(
          "Servicio para consultar el precio final aplicable a un producto de la cadena "
              + "ZARA.")
        .contact(new Contact()
          .name("Luis")
          .email("lpelep@gmail.com")));
  }
}
