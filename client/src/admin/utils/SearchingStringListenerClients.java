package src.admin.utils;

import javafx.event.*;
import javafx.scene.input.*;    
import javafx.scene.control.*;
import javafx.collections.*;

import java.util.*;


public class SearchingStringListenerClients implements EventHandler<KeyEvent> {
    private ComboBox comboBox;
    private ObservableList fios;
    private ObservableList numbers;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public SearchingStringListenerClients(final ComboBox comboBox, List<ClientInfo> clientsInfo) {
        this.comboBox = comboBox;
        List<String> fiosList = new ArrayList<>();
        List<String> numbersList = new ArrayList<>();
        


        for(int i = 0; i < clientsInfo.size(); i++){
            fiosList.add(clientsInfo.get(i).getFio());
            numbersList.add(clientsInfo.get(i).getPhone());
        }

        this.fios = FXCollections.observableArrayList(fiosList);
        this.numbers = FXCollections.observableArrayList(numbersList);

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(SearchingStringListenerClients.this);
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


        ObservableList list = FXCollections.observableArrayList();
        for (int i = 0; i < fios.size(); i++) {
            String input = comboBox.getEditor().getText().toLowerCase().trim();
            
            if(input.length() > 0 && Character.isDigit(input.charAt(0))){
                if(numbers.get(i).toString().toLowerCase().endsWith(input)){
                    list.add(fios.get(i) + " (" + numbers.get(i) + ")"); 
                } 
            }
            else{
                if(fios.get(i).toString().toLowerCase().startsWith(input)) {
                    list.add(fios.get(i) + " (" + numbers.get(i) + ")");
                }    
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