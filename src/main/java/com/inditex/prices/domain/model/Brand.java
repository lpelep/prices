package com.inditex.prices.domain.model;

/**
 * Entidad de dominio que representa una marca comercial.
 *
 * @param id   Identificador único de la marca
 * @param name Nombre de la marca
 */
@SuppressWarnings("PMD.ShortVariable")
public record Brand(Long id, String name) {
}
