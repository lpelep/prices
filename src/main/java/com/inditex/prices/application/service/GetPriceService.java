package com.inditex.prices.application.service;

import com.inditex.prices.application.mapper.PriceMapper;
import com.inditex.prices.application.port.in.GetPriceUseCase;
import com.inditex.prices.application.port.in.dto.PriceResponse;
import com.inditex.prices.domain.exception.PriceNotFoundException;
import com.inditex.prices.domain.repository.PriceRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio de aplicación para obtener precios aplicables.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GetPriceService implements GetPriceUseCase {

  private final PriceRepository priceRepository;
  private final PriceMapper priceMapper;

  @Override
  public PriceResponse getApplicablePrice(
      final LocalDateTime applicationDate,
      final Long productId,
      final Long brandId
  ) {
    log.info("Buscando precio para el producto: {}, brand: {}, date: {}", productId, brandId,
        applicationDate);

    final PriceResponse response =
        priceRepository.findApplicablePrice(productId, brandId, applicationDate)
            .map(priceMapper::toResponse)
            .orElseThrow(
                () -> new PriceNotFoundException(productId, brandId, applicationDate.toString()));

    log.info("Precio encontrado: {}", response);
    return response;
  }
}
