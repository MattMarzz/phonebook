package it.marz.interview.view;

import javax.swing.*;
import java.awt.*;

public class MainView {
    private JFrame frame;
    private JTable table;
    private JButton newBtn;
    private JButton editBtn;
    private JButton deleteBtn;

    public MainView() {
        frame = new JFrame("Rubrica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Tabella per visualizzare le persone
        String[] columnNames = {"Nome", "Cognome", "Telefono"};
        table = new JTable(new Object[][]{}, columnNames);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        // Pannello per i bottoni
        JPanel panel = new JPanel();
        newBtn = new JButton("Nuovo");
        editBtn = new JButton("Modifica");
        deleteBtn = new JButton("Elimina");
        panel.add(newBtn);
        panel.add(editBtn);
        panel.add(deleteBtn);

        frame.add(panel, BorderLayout.SOUTH);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getNewBtn() {
        return newBtn;
    }

    public JButton getEditBtn() {
        return editBtn;
    }

    public JButton getDeleteBtn() {
        return deleteBtn;
    }

    public void show() {
        frame.setVisible(true);
    }
}
