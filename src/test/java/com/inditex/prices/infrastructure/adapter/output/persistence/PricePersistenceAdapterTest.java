package com.inditex.prices.infrastructure.adapter.output.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.prices.domain.model.Brand;
import com.inditex.prices.domain.model.Price;
import com.inditex.prices.infrastructure.adapter.output.mapper.PriceEntityMapper;
import com.inditex.prices.infrastructure.adapter.output.persistence.entity.PriceEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PricePersistenceAdapterTest {

  @Mock
  private PriceJpaRepository jpaRepository;

  @Mock
  private PriceEntityMapper mapper;

  @InjectMocks
  private PricePersistenceAdapter adapter;

  private static final String CURRENCY_EUR = "EUR";

  @Nested
  @DisplayName("findApplicablePrice")
  class FindApplicablePriceTests {

    @Test
    @DisplayName("Debería devolver precio cuando existe")
    void shouldReturnPriceWhenExists() {

      Long productId = 35455L;
      Long brandId = 1L;
      LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);

      Brand expectedBrand = new Brand(1L, "ZARA");
      LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
      LocalDateTime endDate = LocalDateTime.of(2020, 12, 31, 23, 59);
      BigDecimal priceValue = new BigDecimal("35.50");

      PriceEntity entity = new PriceEntity(
          1L,
          null,
          startDate,
          endDate,
          1,
          productId,
          0,
          priceValue,
          CURRENCY_EUR
      );

      Price expectedPrice = new Price(
          expectedBrand,
          startDate,
          endDate,
          1,
          productId,
          0,
          priceValue,
          CURRENCY_EUR
      );

      when(jpaRepository.findApplicablePrice(productId, brandId, date))
          .thenReturn(Optional.of(entity));
      when(mapper.toDomain(entity)).thenReturn(expectedPrice);

      Optional<Price> result = adapter.findApplicablePrice(productId, brandId, date);

      assertAll(
          () -> assertThat(result).isPresent(),
          () -> assertThat(result.get()).isEqualTo(expectedPrice),
          () -> verify(jpaRepository).findApplicablePrice(productId, brandId, date),
          () -> verify(mapper).toDomain(entity)
      );
    }

    @Test
    @DisplayName("Debería devolver vacío cuando no existe")
    void shouldReturnEmptyWhenNotExists() {

      Long productId = 35455L;
      Long brandId = 1L;
      LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);

      when(jpaRepository.findApplicablePrice(productId, brandId, date))
          .thenReturn(Optional.empty());

      Optional<Price> result = adapter.findApplicablePrice(productId, brandId, date);

      assertAll(
          () -> assertThat(result).isEmpty(),
          () -> verify(jpaRepository).findApplicablePrice(productId, brandId, date),
          () -> verify(mapper, never()).toDomain(any())
      );
    }
  }
}
