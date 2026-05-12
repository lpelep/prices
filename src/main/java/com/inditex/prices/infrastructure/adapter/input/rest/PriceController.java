package com.inditex.prices.infrastructure.adapter.input.rest;

import com.inditex.prices.application.port.in.GetPriceUseCase;
import com.inditex.prices.application.port.in.dto.PriceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la consulta de precios de productos.
 * Expone el endpoint para obtener tarifas aplicables según fecha, producto y marca.
 */
@RestController
@RequestMapping("/api/v1/prices")
@RequiredArgsConstructor
@Tag(name = "Prices", description = "API para consulta de tarifas de precios")
@org.springframework.validation.annotation.Validated
public class PriceController {

  private final GetPriceUseCase getPriceUseCase;

  /**
   * Obtiene el precio aplicable para los parámetros especificados.
   *
   * @param applicationDate fecha de aplicación en formato yyyy-MM-dd HH:mm:ss
   * @param productId       identificador del producto
   * @param brandId         identificador de la marca
   * @return ResponseEntity con el precio encontrado o error correspondiente
   */
  @GetMapping
  @Operation(
      summary = "Obtener precio aplicable para producto",
      description =
          "Retorna la información de precio para un producto específico, marca y fecha de "
              + "aplicación. "
              + "En caso de solapamiento de fechas, se selecciona la tarifa con mayor prioridad."
  )
  @ApiResponses({
    @ApiResponse(
      responseCode = "200",
      description = "Precio encontrado exitosamente",
      content = @Content(
        mediaType = "application/json",
        schema = @Schema(implementation = PriceResponse.class)
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "No se encontró ningún precio para los parámetros proporcionados",
      content = @Content(
        schema = @Schema(implementation = ProblemDetail.class))
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Parámetros inválidos o formato de fecha incorrecto",
      content = @Content(
        schema = @Schema(implementation = ProblemDetail.class))
    )
  })
  public ResponseEntity<PriceResponse> getPrice(
      @Parameter(
          description = "Fecha de aplicación (yyyy-MM-dd HH:mm:ss)",
          example = "2020-06-14 10:00:00",
          required = true
      )
      @RequestParam
      @NotNull
      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
      final LocalDateTime applicationDate,

      @Parameter(description = "ID del producto", example = "35455", required = true)
      @RequestParam
      @NotNull @Positive
      final Long productId,

      @Parameter(description = "ID de la marca", example = "1", required = true)
      @RequestParam
      @NotNull @Positive
      final Long brandId
  ) {
    return ResponseEntity.ok(
      getPriceUseCase.getApplicablePrice(applicationDate, productId, brandId));
  }
}
