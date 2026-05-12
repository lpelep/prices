package com.inditex.prices.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Brand - Tests Unitarios")
class BrandTest {

  @Test
  @DisplayName("Debería crear Brand correctamente")
  void shouldCreateBrandSuccessfully() {

    Long expectedId = 1L;
    String expectedName = "ZARA";

    Brand brand = new Brand(expectedId, expectedName);

    assertAll(
        () -> assertThat(brand.id()).isEqualTo(expectedId),
        () -> assertThat(brand.name()).isEqualTo(expectedName)
    );
  }

  @Test
  @DisplayName("Debería mantener igualdad entre Brands")
  void shouldMaintainEquality() {

    Brand brand1 = new Brand(1L, "ZARA");
    Brand brand2 = new Brand(1L, "ZARA");

    assertAll(
        () -> assertThat(brand1).isEqualTo(brand2),
        () -> assertThat(brand1.hashCode()).isEqualTo(brand2.hashCode())
    );
  }

  @Test
  @DisplayName("Debería mantener desigualdad entre Brands diferentes")
  void shouldMaintainInequality() {

    Brand brand1 = new Brand(1L, "ZARA");
    Brand brand2 = new Brand(2L, "Bershka");

    assertThat(brand1).isNotEqualTo(brand2);
  }
}
