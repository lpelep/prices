package com.inditex.prices.infrastructure.adapter.output.mapper;

import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.adapter.output.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para convertir entre entidades JPA y entidades de dominio.
 * Realiza el mapeo entre PriceEntity y Price del dominio.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
@Mapper(componentModel = "spring")
public interface PriceEntityMapper {

  /**
   * Convierte una entidad PriceEntity a una entidad de dominio Price.
   *
   * @param entity entidad JPA a convertir
   * @return entidad de dominio Price con los datos mapeados
   */
  Price toDomain(PriceEntity entity);
}
