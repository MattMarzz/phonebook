package it.marz.interview.view;

import it.marz.interview.model.persona.Persona;
import it.marz.interview.utils.LoggerManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class MainView {
    private JFrame frame;
    private JTable table;
    private JButton newBtn;
    private JButton editBtn;
    private JButton deleteBtn;

    public MainView() {
        frame = new JFrame("Rubrica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        //table creation
        String[] columnNames = {"Nome", "Cognome", "Telefono"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columnNames);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCursor(new Cursor(Cursor.HAND_CURSOR));
        table.setFillsViewportHeight(true);

        //table scroller
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        //button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        newBtn = createButton("Nuovo", "icons/add-user.png");
        editBtn = createButton("Modifica", "icons/editing.png");
        deleteBtn = createButton("Elimina", "icons/trash.png");
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(Color.RED);
        deleteBtn.setOpaque(true);
        deleteBtn.setBorderPainted(false);

        buttonPanel.add(newBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);

        frame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            URL iconURL = getClass().getClassLoader().getResource(iconPath);
            if (iconURL != null) {
                ImageIcon originalIcon = new ImageIcon(iconURL);
                Image scaledImage = originalIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImage));
            }
        } catch (Exception e) {
            LoggerManager.logInfoException("Impossibile caricare le icone", e);
        }
        return button;
    }

    public void updateTable(List<Persona> personaList) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (Persona persona : personaList) {
            model.addRow(new Object[]{persona.getNome(), persona.getCognome(), persona.getTelefono()});
        }
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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
