package com.inditex.prices.application.port.in;

import com.inditex.prices.application.port.in.dto.PriceResponse;
import com.inditex.prices.domain.exception.PriceNotFoundException;
import java.time.LocalDateTime;

/**
 * Caso de uso para obtener precios aplicables de productos.
 * Define el contrato para la lógica de negocio de consulta de tarifas.
 */
@FunctionalInterface
public interface GetPriceUseCase {

  /**
   * Obtiene el precio aplicable para un producto, marca y fecha específicos.
   *
   * @param applicationDate fecha de aplicación para buscar el precio
   * @param productId       identificador del producto
   * @param brandId         identificador de la marca
   * @return DTO con la información del precio encontrado
   * @throws PriceNotFoundException cuando no se encuentra un precio aplicable
   */
  PriceResponse getApplicablePrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
