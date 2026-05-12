package com.inditex.prices.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OpenApiConfigTest {

  @Test
  @DisplayName("Debería crear OpenAPI con configuración correcta")
  void shouldCreateOpenApiWithCorrectConfiguration() {

    OpenApiConfig config = new OpenApiConfig();

    OpenAPI openApi = config.customOpenApi();

    assertThat(openApi).isNotNull();

    Info info = openApi.getInfo();
    assertThat(info).isNotNull();
    assertThat(info.getTitle()).isEqualTo("Ecommerce Prices API");
    assertThat(info.getVersion()).isEqualTo("1.0");
    assertThat(info.getDescription())
        .isEqualTo(
            "Servicio para consultar el precio final aplicable a un producto de la cadena ZARA.");

    Contact contact = info.getContact();
    assertThat(contact).isNotNull();
    assertThat(contact.getName()).isEqualTo("Luis");
    assertThat(contact.getEmail()).isEqualTo("lpelep@gmail.com");
  }
}
