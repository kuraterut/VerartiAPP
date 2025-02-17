package org.admin.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class SearchingStringListenerServices implements EventHandler<KeyEvent> {
    private final ComboBox<ServiceInfo> comboBox;
    private final ObservableList<String> names;
    private final ObservableList<ServiceInfo> services;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public SearchingStringListenerServices(final ComboBox<ServiceInfo> comboBox, List<ServiceInfo> serviceInfoList) {
        this.comboBox = comboBox;

        this.names = FXCollections.observableArrayList();
        this.services = FXCollections.observableArrayList();
        this.services.addAll(serviceInfoList);


        for(int i = 0; i < serviceInfoList.size(); i++){
            this.names.add(serviceInfoList.get(i).getName());
        }


        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(SearchingStringListenerServices.this);

    }

    @Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }



        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }

        ObservableList<ServiceInfo> list = FXCollections.observableArrayList();
        for (int i = 0; i < names.size(); i++) {
            String input = comboBox.getEditor().getText().toLowerCase().trim();
            if(names.get(i).toLowerCase().startsWith(input)) {
                list.add(services.get(i));
            }
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);

        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }

        if(event.getCode() == KeyCode.ENTER){
            caretPos = -1;
            moveCaret(t.length());
            comboBox.hide();
        }
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        moveCaretToPos = false;
    }
}