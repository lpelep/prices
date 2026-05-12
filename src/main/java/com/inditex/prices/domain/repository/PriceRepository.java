package com.inditex.prices.domain.repository;

import com.inditex.prices.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio para el acceso a datos de precios.
 * Define las operaciones de persistencia para entidades Price.
 */
@FunctionalInterface
public interface PriceRepository {
  /**
   * Busca el precio aplicable para un producto, marca y fecha específicos.
   * Considera las fechas de vigencia y prioridad para seleccionar el precio correcto.
   *
   * @param productId       identificador del producto
   * @param brandId         identificador de la marca
   * @param applicationDate fecha de aplicación para buscar precios vigentes
   * @return Optional con el precio aplicable encontrado, o vacío si no existe
   */
  Optional<Price> findApplicablePrice(
      Long productId,
      Long brandId,
      LocalDateTime applicationDate
  );
}
