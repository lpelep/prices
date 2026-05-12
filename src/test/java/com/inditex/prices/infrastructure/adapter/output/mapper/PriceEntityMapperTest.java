package com.inditex.prices.infrastructure.adapter.output.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.inditex.prices.domain.model.Brand;
import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.adapter.output.persistence.entity.BrandEntity;
import com.inditex.prices.infrastructure.adapter.output.persistence.entity.PriceEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("PriceEntityMapper - Tests Unitarios")
class PriceEntityMapperTest {

  private final PriceEntityMapper mapper = Mappers.getMapper(PriceEntityMapper.class);

  private static final String CURRENCY_EUR = "EUR";

  @Test
  @DisplayName("Debería mapear PriceEntity a Price correctamente")
  void shouldMapPriceEntityToPrice() {

    BrandEntity brandEntity = new BrandEntity(1L, "ZARA", null);
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
    BigDecimal priceValue = new BigDecimal("35.50");

    PriceEntity entity = new PriceEntity(
        1L,
        brandEntity,
        startDate,
        endDate,
        1,
        35455L,
        0,
        priceValue,
        CURRENCY_EUR
    );

    Brand expectedBrand = new Brand(1L, "ZARA");
    Price expectedPrice = new Price(
        expectedBrand,
        startDate,
        endDate,
        1,
        35455L,
        0,
        priceValue,
        CURRENCY_EUR
    );

    Price actualPrice = mapper.toDomain(entity);

    assertThat(actualPrice).isEqualTo(expectedPrice);
  }

  @Test
  @DisplayName("Debería manejar null correctamente")
  void shouldHandleNullEntity() {

    PriceEntity entity = null;

    Price actualPrice = mapper.toDomain(entity);

    assertThat(actualPrice).isNull();
  }
}
