package com.inditex.prices.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.inditex.prices.application.port.in.dto.PriceResponse;
import com.inditex.prices.domain.model.Brand;
import com.inditex.prices.domain.model.Price;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

@DisplayName("PriceMapper - Tests Unitarios")
class PriceMapperTest {

  private final PriceMapper priceMapper = Mappers.getMapper(PriceMapper.class);

  @Test
  @DisplayName("Debería mapear Price a PriceResponse correctamente")
  void shouldMapPriceToPriceResponse() {

    Brand brand = new Brand(1L, "ZARA");
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
    BigDecimal priceValue = new BigDecimal("35.50");
    Price domainPrice = new Price(brand, startDate, endDate, 1, 35455L, 0, priceValue, "EUR");

    PriceResponse expectedResponse = new PriceResponse(
        35455L,
        1L,
        "ZARA",
        1,
        startDate,
        endDate,
        priceValue,
        "EUR"
    );

    PriceResponse actualResponse = priceMapper.toResponse(domainPrice);

    assertThat(actualResponse).isEqualTo(expectedResponse);
  }

  @Test
  @DisplayName("Debería manejar null correctamente")
  void shouldHandleNullPrice() {

    Price domainPrice = null;

    PriceResponse actualResponse = priceMapper.toResponse(domainPrice);

    assertThat(actualResponse).isNull();
  }
}
