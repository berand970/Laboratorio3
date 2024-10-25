/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

/**
 *
 * @author User
 */
public class Espacios extends Lugares {

    private boolean generado;

    public Espacios(int valor) {
        super(valor);
        this.generado = false;
    }

    public boolean esGenerado() {
        return generado;
    }

    public void setGenerado(boolean generado) {
        this.generado = generado;
    }

    @Override
    public boolean esValido() {
        return valor >= 1 && valor <= 9;
    }
}
