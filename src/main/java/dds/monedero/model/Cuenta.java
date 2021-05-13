package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Extraccion> extracciones = new ArrayList<>();
  private List<Deposito> depositos = new ArrayList<>();


  public Cuenta(double montoInicial) {
    this.saldo = montoInicial;
  }

  public void poner(double cuanto) {
    this.controlParaDeposito(cuanto);
    this.agregarDeposito(new Deposito(LocalDate.now(), cuanto));
    saldo += cuanto;
  }

  public void controlParaDeposito(double cuanto) {
    this.controlMontoNegativo(cuanto);
    this.controlLimiteDepositosDiarios(3);
  }


  public void sacar(double cuanto) {
   this.controlesParaExtraccion(cuanto);
    this.agregarExtraccion(new Extraccion(LocalDate.now(), cuanto));
    saldo -= cuanto;
  }
  public void controlesParaExtraccion(double cuanto) {
    this.controlMontoNegativo(cuanto);
    this.controlLimiteCantidadExtraidaDiaria(cuanto);
    this.controlSaldoSuficiente(cuanto);

  }

  public void agregarExtraccion(Extraccion unaExtraccion) {
    extracciones.add(unaExtraccion);
  }
  public void agregarDeposito(Deposito unDeposito) {
    depositos.add(unDeposito);
  }

  private void controlLimiteCantidadExtraidaDiaria(double cuanto) {
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000 + " diarios, límite: " + limite);
    }
  }

  private void controlSaldoSuficiente(double cuanto) {
    if (cuanto > getSaldo()) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }
  private void controlLimiteDepositosDiarios(int cantidad) {
    if (depositos.size() >= cantidad ) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + cantidad + " depositos diarios");
    }
  }
  public void controlMontoNegativo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return extracciones.stream()
        .filter(movimiento -> movimiento.getFecha().equals(fecha))
        .mapToDouble(mov -> mov.getMonto())
        .sum();
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double monto) {
    this.saldo = monto;
  }

}
