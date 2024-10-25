/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Sudoku extends JFrame {

    private Tablero tablero;
    private JTextField[][] cajitas;

    public Sudoku() {
        tablero = new Tablero();
        cajitas = new JTextField[9][9];
        
        inicializarGUI();
    }

    private void inicializarGUI() {
        setTitle("Sudoku");
        setLayout(new GridLayout(4, 3)); 
        setResizable(false);

        JPanel[][] panelesRegiones = new JPanel[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                panelesRegiones[i][j] = new JPanel(new GridLayout(3, 3));
                add(panelesRegiones[i][j]);
            }
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cajitas[i][j] = new JTextField();
                cajitas[i][j].setHorizontalAlignment(JTextField.CENTER);
                cajitas[i][j].setFont(new Font("Arial", Font.PLAIN, 18)); 
                cajitas[i][j].setEditable(false);
                cajitas[i][j].setBackground(Color.WHITE);

                cajitas[i][j].addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent evt) {
                        JTextField campo = (JTextField) evt.getSource();
                        int fila = -1, col = -1;
                        boolean found = false;
                        for (int i = 0; i < 9 && !found; i++) {
                            for (int j = 0; j < 9 && !found; j++) {
                                if (cajitas[i][j] == campo) {
                                    fila = i;
                                    col = j;
                                    found = true;
                                }
                            }
                        }
                        if (found) {
                            if (tablero.esModificable(fila, col)) {
                                if (!cajitas[fila][col].getText().isEmpty()) {
                                    int respuesta = JOptionPane.showOptionDialog(
                                            null,
                                            "Deseas borrar el numero?",
                                            "Confirmar",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            new Object[]{"si", "no"},
                                            "si"
                                    );
                                    if (respuesta == JOptionPane.YES_OPTION) {
                                        tablero.setValor(fila, col, 0);
                                        actualizarGUI();
                                    }
                                } else {
                                    abrirDialogoAgregarNumero(fila, col);
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "no se puede modificar un numero inicial", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                });
                panelesRegiones[i / 3][j / 3].add(cajitas[i][j]);
            }
        }

        JButton botonResolver = new JButton("Resolver");
        botonResolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tablero.esValido()) {
                    tablero.resolverTablero(0, 0); 
                    actualizarGUI();
                } else {
                    JOptionPane.showMessageDialog(null, "El tablero actual no es valido", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JPanel panelBoton = new JPanel();
        panelBoton.add(botonResolver);
        add(panelBoton);

        actualizarGUI();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null); 
        setVisible(true);
    }

    private void abrirDialogoAgregarNumero(int fila, int col) {
        String numero = JOptionPane.showInputDialog(this, "Ingresa el numero que quieres agregar:");
        if (numero != null) {
            try {
                int valor = Integer.parseInt(numero);
                if (valor < 1 || valor > 9) {
                    JOptionPane.showMessageDialog(this, "Error, numero fuera de rango", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!tablero.esNumeroValido(fila, col, valor)) {
                    JOptionPane.showMessageDialog(this, "Error, numero repetido", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    tablero.setValor(fila, col, valor);
                    actualizarGUI();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error, numero inválido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void actualizarGUI() {
        boolean completo = true;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int valor = tablero.getValor(i, j);
                cajitas[i][j].setText(valor == 0 ? "" : String.valueOf(valor));
                cajitas[i][j].setForeground(tablero.getCasilla(i, j).esGenerado() ? Color.BLACK : Color.BLUE);
                
                if (valor == 0) {
                    completo = false;
                }
            }
        }

        if (completo && tablero.esValido()) {
            JOptionPane.showMessageDialog(this, "victoria");
            System.exit(0);
        }
    }
}
