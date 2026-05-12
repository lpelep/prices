package com.inditex.prices.infrastructure.adapter.output.persistence;

import com.inditex.prices.domain.model.Price;
import com.inditex.prices.domain.repository.PriceRepository;
import com.inditex.prices.infrastructure.adapter.output.mapper.PriceEntityMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adaptador de persistencia para precios.
 * Implementa el repositorio de dominio utilizando JPA y mapeadores.
 */
@Component
@RequiredArgsConstructor
public class PricePersistenceAdapter implements PriceRepository {

  private final PriceJpaRepository jpaRepository;
  private final PriceEntityMapper mapper;

  /**
   * Busca el precio aplicable utilizando el repositorio JPA.
   * Convierte las entidades de persistencia a entidades de dominio.
   *
   * @param productId identificador del producto
   * @param brandId   identificador de la marca
   * @param date      fecha de aplicación para buscar precios vigentes
   * @return Optional con el precio de dominio encontrado, o vacío si no existe
   */
  @Override
  public Optional<Price> findApplicablePrice(
      final Long productId,
      final Long brandId,
      final LocalDateTime date
  ) {
    return jpaRepository.findApplicablePrice(productId, brandId, date)
      .map(mapper::toDomain);
  }
}
