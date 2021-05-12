package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Deposito extends Movimiento{

  public Deposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
}
