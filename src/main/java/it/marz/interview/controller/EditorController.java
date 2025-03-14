package it.marz.interview.controller;

import it.marz.interview.exception.ItemAlreadyExistsException;
import it.marz.interview.model.persona.Persona;
import it.marz.interview.model.persona.dao.PersonaDAO;
import it.marz.interview.model.persona.dao.PersonaFS;
import it.marz.interview.view.EditorView;
import it.marz.interview.view.MainView;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class EditorController {

    public void initNew(EditorView editorView){
        editorView.clearFields();
        editorView.getFrame().setVisible(true);
        editorView.getSaveBtn().addActionListener(e -> savePersona(editorView));
    }

    public void initEdit(MainView view, List<Persona> personaList, EditorView editorView){
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow != -1) {
            Persona selectedPersona = personaList.get(selectedRow);
            //set fields of selected person
            editorView.setPersona(selectedPersona);
            editorView.getFrame().setVisible(true);
            editorView.getSaveBtn().addActionListener(e -> editPersona(editorView, selectedPersona));
        } else {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Seleziona una persona per modificarla.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void savePersona(EditorView editorView) {
        Persona persona = setPersonaFromView(editorView);
        try {
            PersonaDAO personaDAO = new PersonaFS();
            personaDAO.insertPersona(persona);
            JOptionPane.showMessageDialog(editorView.getFrame(), "Persona salvata!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            editorView.getFrame().setVisible(false);
        } catch (IOException | ItemAlreadyExistsException e) {
            JOptionPane.showMessageDialog(editorView.getFrame(),
                    "Ops... Qualcosa non è andato come previsto!", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void editPersona(EditorView editorView, Persona selectedPersona){
        Persona persona = setPersonaFromView(editorView);
        persona.setId(selectedPersona.getId());
        try {
            PersonaDAO personaDAO = new PersonaFS();
            personaDAO.editPersona(persona);
            JOptionPane.showMessageDialog(editorView.getFrame(), "Persona modificata!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            editorView.getFrame().setVisible(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(editorView.getFrame(),
                    "Ops... Qualcosa non è andato come previsto!", "Errore", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
    }


    public Persona setPersonaFromView(EditorView editorView){
        String nome = editorView.getNome();
        String cognome = editorView.getCognome();
        String indirizzo = editorView.getIndirizzo();
        String telefono = editorView.getTelefono();
        String etaString =editorView.getEta();

        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                indirizzo == null || indirizzo.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty() ||
                etaString == null || etaString.trim().isEmpty()){
            JOptionPane.showMessageDialog(editorView.getFrame(),
                    "Compila tutti i campi.", "Errore", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException();
        }

        int eta;
        try {
            eta = Integer.parseInt(etaString);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(editorView.getFrame(),
                    "L'età deve essere un numero.", "Errore", JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("L'età deve essere un numero intero valido.");
        }

        return new Persona(nome, cognome, indirizzo, telefono, eta);
    }
}

