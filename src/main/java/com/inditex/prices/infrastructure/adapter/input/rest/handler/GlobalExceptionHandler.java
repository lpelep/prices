package com.inditex.prices.infrastructure.adapter.input.rest.handler;

import com.inditex.prices.domain.exception.PriceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Manejador global de excepciones para la API REST.
 *
 * <p>Centraliza el tratamiento de excepciones de la aplicación y devuelve
 * respuestas homogéneas basadas en RFC 7807 mediante {@link ProblemDetail}.
 *
 * <p>Además, registra los errores en logs para facilitar el diagnóstico y
 * añade metadatos útiles como timestamp y path de la petición.
 */
@RestControllerAdvice
@Slf4j
@SuppressWarnings("PMD.ShortVariable")
public class GlobalExceptionHandler {

  /**
   * Maneja excepciones de tipo PriceNotFoundException.
   *
   * @param ex excepción lanzada cuando no se encuentra precio
   * @param request petición HTTP actual
   * @return ProblemDetail con información del error 404
   */
  @ExceptionHandler(PriceNotFoundException.class)
  public ProblemDetail handlePriceNotFound(
    final PriceNotFoundException ex,
    final HttpServletRequest request) {

    log.warn("Precio no encontrado: {}", ex.getMessage());

    return buildProblemDetail(
      HttpStatus.NOT_FOUND,
      "Precio no encontrado",
      ex.getMessage(),
      URI.create("https://api.inditex.com/errors/not-found"),
      request
    );
  }

  /**
   * Maneja excepciones de tipo MethodArgumentTypeMismatchException.
   *
   * @param ex excepción lanzada por parámetros mal formateados
   * @param request petición HTTP actual
   * @return ProblemDetail con información del error 400
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ProblemDetail handleTypeMismatch(
    final MethodArgumentTypeMismatchException ex,
    final HttpServletRequest request) {

    log.warn("Parámetro inválido: {}", ex.getMessage());

    return buildProblemDetail(
      HttpStatus.BAD_REQUEST,
      "Error de validación de parámetros",
      "El formato de los parámetros es incorrecto",
      URI.create("https://api.inditex.com/errors/validation"),
      request
    );
  }

  /**
   * Maneja excepciones de validación de restricciones (Bean Validation).
   *
   * @param ex excepción lanzada por violaciones de @NotNull, @Positive, etc.
   * @param request petición HTTP actual
   * @return ProblemDetail con información del error 400
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(
    final ConstraintViolationException ex,
    final HttpServletRequest request) {

    log.warn("Violación de validación: {}", ex.getMessage());

    return buildProblemDetail(
      HttpStatus.BAD_REQUEST,
      "Error de validación de parámetros",
      ex.getMessage(),
      URI.create("https://api.inditex.com/errors/validation"),
      request
    );
  }

  /**
   * Maneja excepciones generales no controladas.
   *
   * @param ex excepción general capturada
   * @param request petición HTTP actual
   * @return ProblemDetail con información del error 500
   */
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleGeneralError(final Exception ex, final HttpServletRequest request) {

    log.error("Error inesperado en la aplicación", ex);

    return buildProblemDetail(
      HttpStatus.INTERNAL_SERVER_ERROR,
      "Error interno",
      "Ha ocurrido un error inesperado en el servidor",
      URI.create("https://api.inditex.com/errors/internal-server-error"),
      request
    );
  }

  /**
   * Construye una respuesta homogénea basada en {@link ProblemDetail}.
   *
   * <p>Añade información común a todas las respuestas de error como:
   * <ul>
   *   <li>status HTTP</li>
   *   <li>title descriptivo</li>
   *   <li>detail del error</li>
   *   <li>tipo de error</li>
   *   <li>timestamp</li>
   *   <li>path de la petición</li>
   * </ul>
   *
   * @param status código HTTP
   * @param title título del error
   * @param detail detalle descriptivo
   * @param type URI identificadora del tipo de error
   * @param request petición HTTP actual
   * @return instancia de {@link ProblemDetail}
   */
  private ProblemDetail buildProblemDetail(
    final HttpStatus status,
    final String title,
    final String detail,
    final URI type,
    final HttpServletRequest request) {

    final ProblemDetail problemDetail =
      ProblemDetail.forStatusAndDetail(status, detail);

    problemDetail.setTitle(title);
    problemDetail.setType(type);

    problemDetail.setProperty("timestamp", Instant.now());
    problemDetail.setProperty("path", request.getRequestURI());

    return problemDetail;
  }
}
