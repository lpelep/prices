package com.inditex.prices.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inditex.prices.application.mapper.PriceMapper;
import com.inditex.prices.application.port.in.dto.PriceResponse;
import com.inditex.prices.domain.exception.PriceNotFoundException;
import com.inditex.prices.domain.model.Brand;
import com.inditex.prices.domain.model.Price;
import com.inditex.prices.domain.repository.PriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetPriceService - Tests Unitarios")
class GetPriceServiceTest {

  @Mock
  private PriceRepository priceRepository;

  @Mock
  private PriceMapper priceMapper;

  @Mock
  private CacheManager cacheManager;

  @InjectMocks
  private GetPriceService getPriceService;

  private static final String CURRENCY_EUR = "EUR";

  @Nested
  @DisplayName("Casos de error y excepciones")
  class ErrorHandlingTests {

    @Test
    @DisplayName("Debería lanzar PriceNotFoundException cuando no se encuentra precio")
    void shouldThrowPriceNotFoundExceptionWhenPriceNotFound() {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
      Long productId = 35455L;
      Long brandId = 1L;

      when(priceRepository.findApplicablePrice(productId, brandId, applicationDate))
          .thenReturn(Optional.empty());

      assertAll(
          () -> assertThatThrownBy(() ->
              getPriceService.getApplicablePrice(applicationDate, productId, brandId))
              .isInstanceOf(PriceNotFoundException.class)
              .hasMessageContaining(
                  "No se encontró precio para el producto 35455, brandId 1 en la fecha "
                      + "2020-06-14T10:00"),
          () -> verify(priceRepository, times(1))
              .findApplicablePrice(productId, brandId, applicationDate),
          () -> verify(priceMapper, never()).toResponse(any())
      );
    }
  }

  @Nested
  @DisplayName("Casos de éxito y precios encontrados")
  class SuccessScenariosTests {

    @ParameterizedTest
    @DisplayName("Debería devolver precio correctamente")
    @MethodSource("providePriceTestData")
    void shouldReturnPrice(
        String testName,
        LocalDateTime applicationDate,
        Long productId,
        Long brandId,
        Price domainPrice,
        PriceResponse expectedResponse
    ) {

      when(priceRepository.findApplicablePrice(productId, brandId, applicationDate))
          .thenReturn(Optional.of(domainPrice));
      when(priceMapper.toResponse(domainPrice))
          .thenReturn(expectedResponse);

      PriceResponse actualResponse =
          getPriceService.getApplicablePrice(applicationDate, productId, brandId);

      InOrder inOrder = inOrder(priceRepository, priceMapper);

      assertAll(
          () -> assertThat(actualResponse).isNotNull(),
          () -> assertThat(actualResponse.productId()).isEqualTo(expectedResponse.productId()),
          () -> assertThat(actualResponse.brandId()).isEqualTo(expectedResponse.brandId()),
          () -> assertThat(actualResponse.price()).isEqualTo(expectedResponse.price()),
          () -> assertThat(actualResponse.priceList()).isEqualTo(expectedResponse.priceList()),
          () -> assertThat(actualResponse.startDate()).isEqualTo(expectedResponse.startDate()),
          () -> assertThat(actualResponse.endDate()).isEqualTo(expectedResponse.endDate()),
          () -> assertThat(actualResponse.currency()).isEqualTo(expectedResponse.currency()),
          () -> {
            inOrder.verify(priceRepository)
                .findApplicablePrice(productId, brandId, applicationDate);
            inOrder.verify(priceMapper).toResponse(domainPrice);
            inOrder.verifyNoMoreInteractions();
          }
      );
    }

    @Test
    @DisplayName("Debería verificar comportamiento consistente en llamadas múltiples")
    void shouldReturnConsistentResultsOnMultipleCalls() {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
      Long productId = 35455L;
      Long brandId = 1L;
      Price domainPrice = createTestPrice();
      PriceResponse expectedResponse = createTestPriceResponse();

      when(priceRepository.findApplicablePrice(productId, brandId, applicationDate))
          .thenReturn(Optional.of(domainPrice));
      when(priceMapper.toResponse(domainPrice))
          .thenReturn(expectedResponse);

      PriceResponse firstCall =
          getPriceService.getApplicablePrice(applicationDate, productId, brandId);

      PriceResponse secondCall =
          getPriceService.getApplicablePrice(applicationDate, productId, brandId);

      assertAll(
          () -> assertThat(firstCall).isEqualTo(secondCall),
          () -> assertThat(firstCall).isEqualTo(expectedResponse),
          () -> verify(priceRepository, times(2))
              .findApplicablePrice(productId, brandId, applicationDate),
          () -> verify(priceMapper, times(2)).toResponse(domainPrice)
      );
    }

    private static Stream<Arguments> providePriceTestData() {
      return Stream.of(
        Arguments.of(
          "Caso 1: Tarifa estándar - 10:00 del día 14",
          LocalDateTime.of(2020, 6, 14, 10, 0),
          35455L,
          1L,
          createTestPrice(1L, 1L, 35455L, 1, 0,
            LocalDateTime.of(2020, 6, 14, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            new BigDecimal("35.50"), CURRENCY_EUR),
          createTestPriceResponse(1L, 1L, 35455L, 1, 0,
            LocalDateTime.of(2020, 6, 14, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            new BigDecimal("35.50"), CURRENCY_EUR)
        ),
        Arguments.of(
          "Caso 2: Tarifa especial - 16:00 del día 14",
          LocalDateTime.of(2020, 6, 14, 16, 0),
          35455L,
          1L,
          createTestPrice(1L, 1L, 35455L, 2, 1,
            LocalDateTime.of(2020, 6, 14, 15, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30),
            new BigDecimal("25.45"), CURRENCY_EUR),
          createTestPriceResponse(1L, 1L, 35455L, 2, 1,
            LocalDateTime.of(2020, 6, 14, 15, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30),
            new BigDecimal("25.45"), CURRENCY_EUR)
        ),
        Arguments.of(
          "Caso 3: Diferente brandId - 10:00 del día 14",
          LocalDateTime.of(2020, 6, 14, 10, 0),
          35455L,
          2L,
          createTestPrice(2L, 35455L, 1L, 1, 0,
            LocalDateTime.of(2020, 6, 14, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            new BigDecimal("35.50"), CURRENCY_EUR),
          createTestPriceResponse(2L, 35455L, 1L, 1, 0,
            LocalDateTime.of(2020, 6, 14, 0, 0),
            LocalDateTime.of(2020, 12, 31, 23, 59, 59),
            new BigDecimal("35.50"), CURRENCY_EUR)
        )
      );
    }
  }

  private static Price createTestPrice() {
    return createTestPrice(1L, 1L, 35455L, 1, 0,
      LocalDateTime.of(2020, 6, 14, 0, 0),
      LocalDateTime.of(2020, 12, 31, 23, 59, 59),
      new BigDecimal("35.50"), CURRENCY_EUR);
  }

  private static Price createTestPrice(Long brandId, Long productId, Long priceId,
                                       Integer priceList, Integer priority,
                                       LocalDateTime startDate, LocalDateTime endDate,
                                       BigDecimal price, String currency) {
    Brand brand = new Brand(brandId, "ZARA");
    return new Price(
        brand,
        startDate,
        endDate,
        priceList,
        productId,
        priority,
        price,
        currency
    );
  }

  private static PriceResponse createTestPriceResponse() {
    return createTestPriceResponse(1L, 1L, 35455L, 1, 0,
      LocalDateTime.of(2020, 6, 14, 0, 0),
      LocalDateTime.of(2020, 12, 31, 23, 59, 59),
      new BigDecimal("35.50"), CURRENCY_EUR);
  }

  private static PriceResponse createTestPriceResponse(Long brandId, Long productId, Long priceId,
                                                       Integer priceList, Integer priority,
                                                       LocalDateTime startDate,
                                                       LocalDateTime endDate,
                                                       BigDecimal price, String currency) {
    return new PriceResponse(
        productId,
        brandId,
        "ZARA",
        priceList,
        startDate,
        endDate,
        price,
        currency
    );
  }
}
