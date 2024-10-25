/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sudoku;

import java.util.Random;

/**
 *
 * @author User
 */
public class Tablero implements Interface {
    
    // Atributos

    private Espacios[][] tablero;
    private Cajas[][] regiones;
    private Random rand = new Random();

    // Constructor 
    public Tablero() {
        tablero = new Espacios[9][9];
        regiones = new Cajas[3][3];
        inicializarTablero();
        generarTableroCompleto();
        eliminarNumerosParaPuzzle();
    }

    public void inicializarTablero() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j] = new Espacios(0);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                regiones[i][j] = new Cajas();
                for (int fila = 0; fila < 3; fila++) {
                    for (int col = 0; col < 3; col++) {
                        regiones[i][j].setCelda(fila, col, tablero[i * 3 + fila][j * 3 + col]);
                    }
                }
            }
        }
    }

    public boolean resolverTablero(int fila, int col) {
        if (fila == 9) {
            fila = 0;
            if (++col == 9) {
                return true;
            }
        }

        if (tablero[fila][col].getValor() != 0) {
            return resolverTablero(fila + 1, col);
        }

        for (int num = 1; num <= 9; num++) {
            if (esNumeroValido(fila, col, num)) {
                tablero[fila][col].setValor(num);
                if (resolverTablero(fila + 1, col)) {
                    return true;
                }
                tablero[fila][col].setValor(0);
            }
        }

        return false;
    }

    private void generarTableroCompleto() {
        resolverTablero(0, 0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j].getValor() != 0) {
                    tablero[i][j].setGenerado(true);
                }
            }
        }
    }

    private void eliminarNumerosParaPuzzle() {
        int numCeldasAEliminar = 40 + rand.nextInt(11);
        while (numCeldasAEliminar > 0) {
            int fila = rand.nextInt(9);
            int col = rand.nextInt(9);
            if (tablero[fila][col].getValor() != 0) {
                int temp = tablero[fila][col].getValor();
                tablero[fila][col].setValor(0);
                tablero[fila][col].setGenerado(false);
                if (!esSolucionUnica()) {
                    tablero[fila][col].setValor(temp);
                    tablero[fila][col].setGenerado(true);
                } else {
                    numCeldasAEliminar--;
                }
            }
        }
    }

    private boolean esSolucionUnica() {
        return contarSoluciones(0, 0) == 1;
    }

    private int contarSoluciones(int fila, int col) {
        if (fila == 9) {
            fila = 0;
            if (++col == 9) {
                return 1;
            }
        }

        if (tablero[fila][col].getValor() != 0) {
            return contarSoluciones(fila + 1, col);
        }

        int count = 0;
        for (int num = 1; num <= 9; num++) {
            if (esNumeroValido(fila, col, num)) {
                tablero[fila][col].setValor(num);
                count += contarSoluciones(fila + 1, col);
                tablero[fila][col].setValor(0);
                if (count > 1) {
                    return count;
                }
            }
        }

        return count;
    }

    public boolean esNumeroValido(int fila, int col, int valor) {
        for (int i = 0; i < 9; i++) {
            if (tablero[fila][i].getValor() == valor || tablero[i][col].getValor() == valor) {
                return false;
            }
        }
        int regionFila = fila / 3;
        int regionCol = col / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (regiones[regionFila][regionCol].getCelda(i, j).getValor() == valor) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean esModificable(int fila, int col) {
        return !tablero[fila][col].esGenerado();
    }

    @Override
    public boolean esValido() {
        for (int i = 0; i < 9; i++) {
            boolean[] filaVistos = new boolean[10];
            boolean[] colVistos = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int valorFila = tablero[i][j].getValor();
                int valorCol = tablero[j][i].getValor();
                if ((valorFila != 0 && filaVistos[valorFila]) || (valorCol != 0 && colVistos[valorCol])) {
                    return false;
                }
                filaVistos[valorFila] = true;
                colVistos[valorCol] = true;
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!regiones[i][j].esValido()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void limpiar() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                tablero[i][j].limpiar();
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                regiones[i][j].limpiar();
            }
        }
    }

    public void setValor(int fila, int col, int valor) {
        tablero[fila][col].setValor(valor);
    }

    public int getValor(int fila, int col) {
        return tablero[fila][col].getValor();
    }

    public Espacios getCasilla(int fila, int col) {
        return tablero[fila][col];
    }
}
