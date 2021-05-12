package dds.monedero.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Movimiento {

  private LocalDate fecha;
  //En ningún lenguaje de programación usen jamás doubles para modelar dinero en el mundo real
  //siempre usen numeros de precision arbitraria, como BigDecimal en Java y similares
  private BigDecimal monto;

  public Movimiento(LocalDate fecha, BigDecimal monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public boolean fueOperado(LocalDate fecha) {
    return esDeLaFecha(fecha);
  }

  public boolean esDeLaFecha(LocalDate fecha) {
    return this.fecha.equals(fecha);
  }




  public double calcularValor(Cuenta cuenta) {
    if (esDeposito) {
      return cuenta.getSaldo() + getMonto();
    } else {
      return cuenta.getSaldo() - getMonto();
    }
  }

}

}
