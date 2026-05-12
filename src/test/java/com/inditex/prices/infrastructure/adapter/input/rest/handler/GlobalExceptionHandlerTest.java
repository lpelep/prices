package com.inditex.prices.infrastructure.adapter.input.rest.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.inditex.prices.domain.exception.PriceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@DisplayName("GlobalExceptionHandler - Tests Unitarios")
class GlobalExceptionHandlerTest {

  private static final String REQUEST_PATH = "/api/v1/prices";

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("Debería manejar PriceNotFoundException correctamente")
  void shouldHandlePriceNotFoundException() {

    final HttpServletRequest request = mock(HttpServletRequest.class);

    final String errorMessage =
        "No se encontró precio para el producto 35455, brandId 1 en la fecha 2020-06-14T10:00:00";

    final PriceNotFoundException exception =
        new PriceNotFoundException(
            35455L,
            1L,
            "2020-06-14T10:00:00"
        );

    when(request.getRequestURI()).thenReturn(REQUEST_PATH);

    final ProblemDetail result = handler.handlePriceNotFound(exception, request);

    assertAll(
        () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value()),

        () -> assertThat(result.getDetail()).isEqualTo(errorMessage),

        () -> assertThat(result.getTitle()).isEqualTo("Precio no encontrado"),

        () -> assertThat(result.getType().toString()).isEqualTo("https://api.inditex.com/errors/not-found"),

        () -> assertThat(result.getProperties()).containsKey("timestamp"),

        () -> assertThat(result.getProperties().get("path")).isEqualTo(REQUEST_PATH)
    );
  }

  @Test
  @DisplayName("Debería manejar MethodArgumentTypeMismatchException correctamente")
  void shouldHandleMethodArgumentTypeMismatchException() {

    final HttpServletRequest request = mock(HttpServletRequest.class);

    final MethodArgumentTypeMismatchException exception =
          mock(MethodArgumentTypeMismatchException.class);

    when(request.getRequestURI()).thenReturn(REQUEST_PATH);

    final ProblemDetail result = handler.handleTypeMismatch(exception, request);

    assertAll(
        () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()),

        () -> assertThat(result.getDetail()).isEqualTo(
            "El formato de los parámetros es incorrecto"),

        () -> assertThat(result.getTitle()).isEqualTo("Error de validación de parámetros"),

        () -> assertThat(result.getProperties()).containsKey("timestamp"),

        () -> assertThat(result.getProperties().get("path")).isEqualTo(REQUEST_PATH)
    );
  }

  @Test
  @DisplayName("Debería manejar ConstraintViolationException correctamente")
  void shouldHandleConstraintViolationException() {

    final HttpServletRequest request = mock(HttpServletRequest.class);

    final ConstraintViolationException exception =
          new ConstraintViolationException("Validation failed", null);

    when(request.getRequestURI()).thenReturn(REQUEST_PATH);

    final ProblemDetail result = handler.handleConstraintViolation(exception, request);

    assertAll(
        () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()),

        () -> assertThat(result.getDetail()).isEqualTo("Validation failed"),

        () -> assertThat(result.getTitle()).isEqualTo("Error de validación de parámetros"),

        () -> assertThat(result.getType().toString()).isEqualTo("https://api.inditex.com/errors/validation"),

        () -> assertThat(result.getProperties()).containsKey("timestamp"),

        () -> assertThat(result.getProperties().get("path")).isEqualTo(REQUEST_PATH)
    );
  }

  @Test
  @DisplayName("Debería manejar Exception genérica correctamente")
  void shouldHandleGeneralException() {

    final HttpServletRequest request = mock(HttpServletRequest.class);

    final Exception exception = new RuntimeException("Error interno");

    when(request.getRequestURI()).thenReturn(REQUEST_PATH);

    final ProblemDetail result = handler.handleGeneralError(exception, request);

    assertAll(
        () -> assertThat(result.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value()),

        () -> assertThat(result.getDetail()).isEqualTo(
          "Ha ocurrido un error inesperado en el servidor"),

        () -> assertThat(result.getTitle()).isEqualTo("Error interno"),

        () -> assertThat(result.getType().toString()).isEqualTo(
            "https://api.inditex.com/errors/internal-server-error"),

        () -> assertThat(result.getProperties()).containsKey("timestamp"),

        () -> assertThat(result.getProperties().get("path")).isEqualTo(REQUEST_PATH)
    );
  }
}
