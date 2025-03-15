package it.marz.interview.view;

import it.marz.interview.enums.EditorModeEnum;
import it.marz.interview.model.persona.Persona;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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

        setTextFieldFilter(nomeField, "[a-zA-ZÀ-ÿ ]*");
        setTextFieldFilter(cognomeField, "[a-zA-ZÀ-ÿ ]*");
        setFixedLengthPhoneFilter(telefonoField, 10);
        setNumericRangeFilter(etaField, 1, 120);

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

    private void setTextFieldFilter(JTextField textField, String regex) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text.matches(regex)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }
    private void setNumericRangeFilter(JTextField textField, int min, int max) {
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textField.getText();
                if (!text.isEmpty()) {
                    try {
                        int value = Integer.parseInt(text);
                        if (value < min || value > max) {
                            textField.setText("");
                            JOptionPane.showMessageDialog(frame, "Inserire un'età compresa tra " + min + " e " + max,
                                    "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        textField.setText("");
                    }
                }
            }
        });
    }

    private void setFixedLengthPhoneFilter(JTextField textField, int requiredLength) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);

                if (newText.matches("\\d*") && newText.length() <= requiredLength) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            @Override
            public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr)
                    throws BadLocationException {
                replace(fb, offset, 0, text, attr);
            }
        });

        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().length() != requiredLength) {
                    JOptionPane.showMessageDialog(textField,
                            "Numero di telefono non valido",
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    textField.setText("");
                }
            }
        });
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