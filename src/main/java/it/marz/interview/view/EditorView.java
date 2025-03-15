package it.marz.interview.view;

import it.marz.interview.enums.EditorModeEnum;
import it.marz.interview.model.persona.Persona;

import javax.swing.*;
import java.awt.*;

public class EditorView {
    private JFrame frame;
    private JTextField nomeField;
    private JTextField cognomeField;
    private JTextField telefonoField;
    private JTextField indirizzoField;
    private JTextField etaField;
    private JButton saveNewBtn;
    private JButton saveEditBtn;
    private JButton cancelBtn;
    private EditorModeEnum mode;

    //TODO: make input field only numerical

    public EditorView(EditorModeEnum editorModeEnum) {
        this.mode = editorModeEnum;
        frame = new JFrame(this.mode == EditorModeEnum.NEW ? "Inserisci Persona" : "Modifica Persona");
        frame.setSize(600, 350);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nomeField = new JTextField(15);
        cognomeField = new JTextField(15);
        indirizzoField = new JTextField(15);
        telefonoField = new JTextField(15);
        etaField = new JTextField(5);

        mainPanel.add(createFieldPanel("Nome", nomeField));
        mainPanel.add(createFieldPanel("Cognome", cognomeField));
        mainPanel.add(createFieldPanel("Indirizzo", indirizzoField));
        mainPanel.add(createFieldPanel("Telefono", telefonoField));
        mainPanel.add(createFieldPanel("Età", etaField));

        JPanel buttonPanel = new JPanel();
        if(this.mode == EditorModeEnum.NEW){
            saveNewBtn = new JButton("Salva");
            saveNewBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(saveNewBtn);
        } else{
            saveEditBtn = new JButton("Salva");
            saveEditBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            buttonPanel.add(saveEditBtn);
        }

        cancelBtn = new JButton("Annulla");

        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(Color.RED);
        cancelBtn.setOpaque(true);
        cancelBtn.setBorderPainted(false);

        buttonPanel.add(cancelBtn);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }


    private JPanel createFieldPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    public void setPersona(Persona persona) {
        nomeField.setText(persona.getNome());
        cognomeField.setText(persona.getCognome());
        indirizzoField.setText(persona.getIndirizzo());
        telefonoField.setText(persona.getTelefono());
        etaField.setText(String.valueOf(persona.getEta()));
    }

    public void clearFields() {
        nomeField.setText("");
        cognomeField.setText("");
        indirizzoField.setText("");
        telefonoField.setText("");
        etaField.setText("");
    }

    public String getNome() {
        return nomeField.getText();
    }

    public String getCognome() {
        return cognomeField.getText();
    }
    public String getIndirizzo() {
        return indirizzoField.getText();
    }

    public String getTelefono() {
        return telefonoField.getText();
    }
    public String getEta() {
        return etaField.getText();
    }

    public JButton getSaveBtn() {
        return this.mode == EditorModeEnum.NEW ? saveNewBtn : saveEditBtn;
    }

    public JButton getCancelBtn() {
        return cancelBtn;
    }

    public JFrame getFrame() {
        return frame;
    }
}
