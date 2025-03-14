package it.marz.interview;

import it.marz.interview.controller.MainController;
import it.marz.interview.view.MainView;
import it.marz.interview.view.EditorView;

public class App {
    public static void main(String[] args) {
        MainView mainView = new MainView();
        MainController mainController = new MainController();
        mainController.init(mainView);
    }
}
