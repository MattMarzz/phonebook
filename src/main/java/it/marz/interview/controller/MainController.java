package it.marz.interview.controller;

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
        EditorView editorView = new EditorView();
        try {
            personaList = loadPersonaList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view.show();
        view.updateTable(personaList);

        //new listener
        view.getNewBtn().addActionListener(e -> showEditorForNewPersona(editorView));

        //edit listener
        view.getEditBtn().addActionListener(e -> showEditorForEditPersona(view, personaList, editorView));

        //TODO: remove listener
    }

    private void showEditorForNewPersona(EditorView editorView){
        EditorController editorController = new EditorController();
        editorController.initNew(editorView);
        System.out.println("Aggiorno");
    }


    private void showEditorForEditPersona(MainView view, List<Persona> personaList, EditorView editorView) {
        EditorController editorController = new EditorController();
        editorController.initEdit(view, personaList, editorView);
    }

    public List<Persona> loadPersonaList() throws IOException {
        PersonaDAO personaDAO = new PersonaFS();
        return personaDAO.getAllPersona();
    }

}
