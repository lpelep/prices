package com.inditex.prices.application.mapper;

import com.inditex.prices.application.port.in.dto.PriceResponse;
import com.inditex.prices.domain.model.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper para convertir entre entidades de dominio Price y DTOs PriceResponse.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
@Mapper(componentModel = "spring")
public interface PriceMapper {

  /**
   * Convierte una entidad Price a un DTO PriceResponse.
   *
   * @param price la entidad de dominio a convertir
   * @return el DTO PriceResponse con los datos mapeados
   */
  @Mapping(source = "brand.name", target = "brandName")
  @Mapping(source = "brand.id", target = "brandId")
  PriceResponse toResponse(Price price);
}
