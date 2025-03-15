package it.marz.interview.controller;

import it.marz.interview.enums.EditorModeEnum;
import it.marz.interview.model.persona.Persona;
import it.marz.interview.model.persona.dao.PersonaDAO;
import it.marz.interview.model.persona.dao.PersonaFS;
import it.marz.interview.view.MainView;
import it.marz.interview.view.EditorView;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
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

        //TODO: remove listener
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

    public List<Persona> loadPersonaList() throws IOException {
        PersonaDAO personaDAO = new PersonaFS();
        return personaDAO.getAllPersona();
    }

}
