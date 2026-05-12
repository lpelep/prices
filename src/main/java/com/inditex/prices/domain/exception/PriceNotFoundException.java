package com.inditex.prices.domain.exception;

/**
 * Excepción lanzada cuando no se encuentra un precio aplicable
 * para los parámetros especificados.
 */
public class PriceNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * Crea una nueva instancia de PriceNotFoundException.
   *
   * @param productId identificador del producto no encontrado
   * @param brandId   identificador de la marca no encontrada
   * @param date      fecha de aplicación sin precios
   */
  public PriceNotFoundException(final Long productId, final Long brandId, final String date) {
    super(String.format("No se encontró precio para el producto %d, brandId %d en la fecha %s",
        productId, brandId, date));
  }
}
