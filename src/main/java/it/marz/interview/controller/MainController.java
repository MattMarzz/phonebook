package it.marz.interview.controller;

import it.marz.interview.enums.EditorModeEnum;
import it.marz.interview.model.persona.Persona;
import it.marz.interview.model.persona.dao.PersonaDAO;
import it.marz.interview.model.persona.dao.PersonaFS;
import it.marz.interview.view.MainView;
import it.marz.interview.view.EditorView;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class MainController {

    public void init(MainView view) {
        List<Persona> personaList;
        try {
            personaList = loadPersonaList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.show();
        view.updateTable(personaList);

        //new listener
        view.getNewBtn().addActionListener(e -> showEditorForNewPersona(view));

        //edit listener
        view.getEditBtn().addActionListener(e -> showEditorForEditPersona(view));

        //remove listener
        view.getDeleteBtn().addActionListener(e -> removeSelectedPersona(view));
    }

    private void showEditorForNewPersona(MainView mainView){
        EditorController editorController = new EditorController();
        EditorView editorView = new EditorView(EditorModeEnum.NEW);
        editorController.initNew(mainView, editorView);
    }


    private void showEditorForEditPersona(MainView view) {
        EditorController editorController = new EditorController();
        EditorView editorView = new EditorView(EditorModeEnum.EDIT);
        try {
            editorController.initEdit(view, loadPersonaList(), editorView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeSelectedPersona(MainView view) {
        int selectedRow = view.getTable().getSelectedRow();
        List<Persona> personaList;
        try {
            personaList = loadPersonaList();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Ops... Qualcosa non è andato come previsto!", "Errore", JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);
        }
        if (selectedRow != -1) {
            Persona selectedPersona = personaList.get(selectedRow);
            int choice = JOptionPane.showConfirmDialog(view.getFrame(),
                    "Eliminare la persona " + selectedPersona.getNome().toUpperCase() +
                            " " + selectedPersona.getCognome().toUpperCase() + "?",
                    "Conferma eliminazione",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                PersonaDAO personaDAO;
                try {
                    personaDAO = new PersonaFS();
                    personaDAO.removePersona(selectedPersona);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                view.updateTable(personaDAO.getAllPersona());
            }
        } else {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Seleziona una persona per eliminarla.", "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Persona> loadPersonaList() throws IOException {
        PersonaDAO personaDAO = new PersonaFS();
        return personaDAO.getAllPersona();
    }

}