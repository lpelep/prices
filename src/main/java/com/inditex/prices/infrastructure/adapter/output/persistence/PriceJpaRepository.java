package com.inditex.prices.infrastructure.adapter.output.persistence;

import com.inditex.prices.infrastructure.adapter.output.persistence.entity.PriceEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repositorio JPA para entidades PriceEntity.
 */
@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

  /**
   * Busca precios aplicables según los criterios de producto, marca y fecha.
   * Considera las fechas de vigencia y ordena por prioridad descendente.
   *
   * @param productId       identificador del producto
   * @param brandId         identificador de la marca
   * @param applicationDate fecha de aplicación para buscar precios vigentes
   * @return Optional con la entidad PriceEntity de mayor prioridad, o vacío si no existe
   */
  @Query(
      """
      SELECT p FROM PriceEntity p
      WHERE p.productId = :productId
      AND p.brand.id = :brandId
      AND :applicationDate BETWEEN p.startDate AND p.endDate
      ORDER BY p.priority DESC
      LIMIT 1
      """)
  Optional<PriceEntity> findApplicablePrice(
      @Param("productId") Long productId,
      @Param("brandId") Long brandId,
      @Param("applicationDate") LocalDateTime applicationDate
  );
}
