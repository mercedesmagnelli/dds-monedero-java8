package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private BigDecimal saldo = new BigDecimal(0);
  private List<Extraccion> extracciones = new ArrayList<>();
  private List<Deposito> depositos = new ArrayList<>();

  public Cuenta() {
    saldo = new BigDecimal(0);
  }

  public Cuenta(BigDecimal montoInicial) {
    saldo = montoInicial;
  }

  public void poner(BigDecimal cuanto) {
    this.controlMontoNegativo(cuanto);
    this.controlLimiteDepositosDiarios(3);
   this.agregarDeposito(new Deposito(LocalDate.now(), cuanto));
  }

  public void sacar(BigDecimal cuanto) {
    this.controlMontoNegativo(cuanto);
    this.controlSaldoSuficiente(cuanto);
    this.controlLimiteCantidadExtraidaDiaria(cuanto);
    this.agregarExtraccion(new Extraccion(LocalDate.now(), cuanto));
  }

  public void agregarExtraccion(Extraccion unaExtraccion) {
    extracciones.add(unaExtraccion);
  }
  public void agregarDeposito(Deposito unDeposito) {
    depositos.add(unDeposito);
  }

  private void controlLimiteCantidadExtraidaDiaria(BigDecimal cuanto) {
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto.doubleValue() > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000 + " diarios, límite: " + limite);
    }
  }

  private void controlSaldoSuficiente(BigDecimal cuanto) {
    if (getSaldo().doubleValue() - cuanto.doubleValue() < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }
  private void controlLimiteDepositosDiarios(int cantidad) {
    if (depositos.size() >= cantidad ) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantidad + " depositos diarios");
    }
  }
  public void controlMontoNegativo(BigDecimal cuanto) {
    if (cuanto.doubleValue() <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }


  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }
  

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal monto) {
    this.saldo = saldo;
  }

}
