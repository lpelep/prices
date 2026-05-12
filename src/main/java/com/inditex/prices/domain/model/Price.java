package com.inditex.prices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de dominio que representa un precio de producto.
 * Contiene la información de vigencia y prioridad del precio.
 *
 * @param brand     Marca del producto
 * @param startDate Fecha de inicio de vigencia
 * @param endDate   Fecha de fin de vigencia
 * @param priceList Identificador de la lista de precios
 * @param productId Identificador del producto
 * @param priority  Prioridad del precio (mayor número = mayor prioridad)
 * @param price     Valor del precio
 * @param currency  Moneda del precio
 */
public record Price(
    Brand brand,
    LocalDateTime startDate,
    LocalDateTime endDate,
    Integer priceList,
    Long productId,
    Integer priority,
    BigDecimal price,
    String currency
) {
}
