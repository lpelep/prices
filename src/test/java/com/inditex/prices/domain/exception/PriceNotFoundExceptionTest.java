package com.inditex.prices.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PriceNotFoundException - Tests Unitarios")
class PriceNotFoundExceptionTest {

  @Test
  @DisplayName("Debería crear excepción con mensaje correcto")
  void shouldCreateExceptionWithCorrectMessage() {

    Long productId = 35455L;
    Long brandId = 1L;
    String date = "2020-06-14T10:00:00";
    String expectedMessage = "No se encontró precio para el producto 35455, brandId 1 en la fecha "
        + "2020-06-14T10:00:00";

    PriceNotFoundException exception = new PriceNotFoundException(productId, brandId, date);

    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
  }

  @Test
  @DisplayName("Debería ser RuntimeException")
  void shouldBeRuntimeException() {

    PriceNotFoundException exception = new PriceNotFoundException(1L, 1L, "2020-06-14");

    assertThat(exception).isInstanceOf(RuntimeException.class);
  }
}
