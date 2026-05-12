package com.inditex.prices.infrastructure.adapter.input.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.inditex.prices.application.port.in.dto.PriceResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@DisplayName("PriceController - Tests de Integración")
class PriceControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private static final String CURRENCY_EUR = "EUR";

  @BeforeEach
  void setUp() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @Nested
  @DisplayName("Casos de prueba de precios aplicables")
  class ApplicablePriceTests {

    @Test
    @DisplayName(
        "Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 "
            + "(ZARA)"
    )
    void shouldReturnBasePriceOnFirstDayMorning() throws Exception {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
      double expectedPrice = 35.50;
      Integer expectedPriceList = 1;


      MvcResult result = performPriceRequest(applicationDate, 35455L, 1L)
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.productId").value(35455))
          .andExpect(jsonPath("$.brandId").value(1))
          .andExpect(jsonPath("$.price").value(closeTo(expectedPrice, 0.001)))
          .andExpect(jsonPath("$.priceList").value(expectedPriceList))
          .andExpect(jsonPath("$.currency").value(CURRENCY_EUR))
          .andReturn();

      validateResponse(result, new BigDecimal(String.valueOf(expectedPrice)), expectedPriceList);
    }

    @Test
    @DisplayName(
        "Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 "
            + "(ZARA)"
    )
    void shouldReturnPriceAfternoonRange() throws Exception {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
      double expectedPrice = 25.45;
      Integer expectedPriceList = 2;

      MvcResult result = performPriceRequest(applicationDate, 35455L, 1L)
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.productId").value(35455))
          .andExpect(jsonPath("$.brandId").value(1))
          .andExpect(jsonPath("$.price").value(closeTo(expectedPrice, 0.001)))
          .andExpect(jsonPath("$.priceList").value(expectedPriceList))
          .andExpect(jsonPath("$.currency").value(CURRENCY_EUR))
          .andReturn();

      validateResponse(result, new BigDecimal(String.valueOf(expectedPrice)), expectedPriceList);
    }

    @Test
    @DisplayName(
        "Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 "
            + "(ZARA)"
    )
    void shouldReturnBasePrice() throws Exception {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 21, 0);
      double expectedPrice = 35.50;
      Integer expectedPriceList = 1;

      MvcResult result = performPriceRequest(applicationDate, 35455L, 1L)
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.productId").value(35455))
          .andExpect(jsonPath("$.brandId").value(1))
          .andExpect(jsonPath("$.price").value(closeTo(expectedPrice, 0.001)))
          .andExpect(jsonPath("$.currency").value(CURRENCY_EUR))
          .andReturn();

      validateResponse(result, new BigDecimal(String.valueOf(expectedPrice)), expectedPriceList);
    }

    @Test
    @DisplayName(
        "Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 "
            + "(ZARA)"
    )
    void shouldReturnPriceOnSecondDayMorning() throws Exception {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 15, 10, 0);
      double expectedPrice = 30.50;
      Integer expectedPriceList = 3;

      MvcResult result = performPriceRequest(applicationDate, 35455L, 1L)
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.productId").value(35455))
          .andExpect(jsonPath("$.brandId").value(1))
          .andExpect(jsonPath("$.price").value(closeTo(expectedPrice, 0.001)))
          .andExpect(jsonPath("$.priceList").value(expectedPriceList))
          .andExpect(jsonPath("$.currency").value(CURRENCY_EUR))
          .andReturn();

      validateResponse(result, new BigDecimal(String.valueOf(expectedPrice)), expectedPriceList);
    }

    @Test
    @DisplayName(
        "Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 "
            + "(ZARA)"
    )
    void shouldReturnPriceDay16Evening() throws Exception {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 16, 21, 0);
      double expectedPrice = 38.95;
      Integer expectedPriceList = 4;

      MvcResult result = performPriceRequest(applicationDate, 35455L, 1L)
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON))
          .andExpect(jsonPath("$.productId").value(35455))
          .andExpect(jsonPath("$.brandId").value(1))
          .andExpect(jsonPath("$.price").value(closeTo(expectedPrice, 0.001)))
          .andExpect(jsonPath("$.priceList").value(expectedPriceList))
          .andExpect(jsonPath("$.currency").value(CURRENCY_EUR))
          .andReturn();

      validateResponse(result, new BigDecimal(String.valueOf(expectedPrice)), expectedPriceList);
    }
  }

  @Nested
  @DisplayName("Casos de error y validación")
  class ErrorHandlingTests {

    @Test
    @DisplayName("Debería retornar 404 cuando no se encuentra precio para los parámetros dados")
    void shouldReturn404WhenPriceNotFound() throws Exception {

      LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 12, 0);
      long productId = 99999L;
      long brandId = 1L;

      mockMvc.perform(get("/api/v1/prices")
              .param("applicationDate", applicationDate.format(formatter))
              .param("productId", String.valueOf(productId))
              .param("brandId", String.valueOf(brandId))
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
          .andExpect(jsonPath("$.status").value(404))
          .andExpect(jsonPath("$.title").exists())
          .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("Debería retornar 400 cuando los parámetros son inválidos")
    void shouldReturn400WhenParametersAreInvalid() throws Exception {

      String invalidDate = "2020-06-14 25:00:00";

      mockMvc.perform(get("/api/v1/prices")
              .param("applicationDate", invalidDate)
              .param("productId", "35455")
              .param("brandId", "1")
              .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    }
  }

  private ResultActions performPriceRequest(
      LocalDateTime applicationDate, Long productId, Long brandId) throws Exception {

    return mockMvc.perform(get("/api/v1/prices")
        .param("applicationDate", applicationDate.format(formatter))
        .param("productId", productId.toString())
        .param("brandId", brandId.toString())
        .contentType(MediaType.APPLICATION_JSON));
  }

  private void validateResponse(MvcResult result, BigDecimal expectedPrice,
                                Integer expectedPriceList) throws Exception {
    String responseContent = result.getResponse().getContentAsString();

    var response = objectMapper.readValue(responseContent, PriceResponse.class);

    assertAll(
        () -> assertThat(response.productId()).isEqualTo(35455L),
        () -> assertThat(response.brandId()).isEqualTo(1L),
        () -> assertThat(response.price()).isEqualByComparingTo(expectedPrice),
        () -> assertThat(response.priceList()).isEqualTo(expectedPriceList),
        () -> assertThat(response.currency()).isEqualTo(CURRENCY_EUR),
        () -> assertThat(response.startDate()).isNotNull(),
        () -> assertThat(response.endDate()).isNotNull()
    );
  }
}
