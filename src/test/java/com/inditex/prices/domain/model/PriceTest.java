package com.inditex.prices.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Price - Tests Unitarios")
class PriceTest {

  private static final String CURRENCY_EUR = "EUR";

  @Test
  @DisplayName("Debería crear Price correctamente")
  void shouldCreatePriceSuccessfully() {

    Brand brand = new Brand(1L, "ZARA");
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
    Integer priceList = 1;
    Long productId = 35455L;
    Integer priority = 0;
    BigDecimal priceValue = new BigDecimal("35.50");

    Price price = new Price(
        brand,
        startDate,
        endDate,
        priceList,
        productId,
        priority,
        priceValue,
        CURRENCY_EUR
    );

    assertAll(
        () -> assertThat(price.brand()).isEqualTo(brand),
        () -> assertThat(price.startDate()).isEqualTo(startDate),
        () -> assertThat(price.endDate()).isEqualTo(endDate),
        () -> assertThat(price.priceList()).isEqualTo(priceList),
        () -> assertThat(price.productId()).isEqualTo(productId),
        () -> assertThat(price.priority()).isEqualTo(priority),
        () -> assertThat(price.price()).isEqualTo(priceValue),
        () -> assertThat(price.currency()).isEqualTo(CURRENCY_EUR)
    );
  }

  @Test
  @DisplayName("Debería verificar si fecha está dentro de vigencia")
  void shouldCheckDateInRange() {

    Brand brand = new Brand(1L, "ZARA");
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
    LocalDateTime testDate = LocalDateTime.of(2020, 8, 15, 10, 30);

    Price price = new Price(
        brand,
        startDate,
        endDate,
        1,
        35455L,
        0,
        new BigDecimal("35.50"),
        CURRENCY_EUR
    );

    assertAll(
        () -> assertThat(price).isNotNull(),
        () -> assertThat(testDate).isAfter(startDate),
        () -> assertThat(testDate).isBefore(endDate)
    );
  }

  @Test
  @DisplayName("Debería mantener igualdad entre Prices")
  void shouldMaintainEquality() {

    Brand brand = new Brand(1L, "ZARA");
    LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
    LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
    BigDecimal priceValue = new BigDecimal("35.50");

    Price price1 = new Price(brand, startDate, endDate, 1, 35455L, 0, priceValue, CURRENCY_EUR);
    Price price2 = new Price(brand, startDate, endDate, 1, 35455L, 0, priceValue, CURRENCY_EUR);

    assertAll(
        () -> assertThat(price1).isEqualTo(price2),
        () -> assertThat(price1.hashCode()).isEqualTo(price2.hashCode())
    );
  }
}
