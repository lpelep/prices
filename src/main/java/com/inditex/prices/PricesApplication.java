package com.inditex.prices;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PricesApplication {

  /**
   * Método principal de arranque.
   *
   * @param args argumentos de entrada
   */
  public static void main(final String[] args) {
    SpringApplication.run(PricesApplication.class, args);
  }

}
