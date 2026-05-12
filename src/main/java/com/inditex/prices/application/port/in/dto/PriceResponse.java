package com.inditex.prices.application.port.in.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO que representa la respuesta de precios aplicables.
 * Contiene la información del precio encontrado para un producto específico.
 *
 * @param productId ID del producto
 * @param brandId   ID de la marca
 * @param brandName Nombre de la marca
 * @param priceList Número de lista de precios
 * @param startDate Fecha de inicio de vigencia del precio
 * @param endDate   Fecha de fin de vigencia del precio
 * @param price     Precio del producto
 * @param currency  Moneda del precio
 */
public record PriceResponse(
    Long productId,
    Long brandId,
    String brandName,
    Integer priceList,
    LocalDateTime startDate,
    LocalDateTime endDate,
    BigDecimal price,
    String currency
) {
}

