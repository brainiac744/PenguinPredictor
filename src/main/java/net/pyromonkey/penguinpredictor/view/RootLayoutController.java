package net.pyromonkey.penguinpredictor.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.pyromonkey.penguinpredictor.MainApp;
import net.pyromonkey.penguinpredictor.Penguin;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RootLayoutController {

    @FXML private TextField vWeek;
    @FXML private ChoiceBox<Penguin> onePointPenguins1;
    @FXML private ChoiceBox<Penguin> onePointPenguins2;
    @FXML private ChoiceBox<Penguin> onePointPenguins3;
    @FXML private ChoiceBox<Penguin> onePointPenguins4;
    @FXML private ChoiceBox<Penguin> onePointPenguins5;
    @FXML private ChoiceBox<Penguin> freezerPenguin;

    @FXML private TextArea rightPaneTextArea;


    // Reference to the main application
    private MainApp mainApp;

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp the main application.
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        vWeek.setText(Integer.toString(mainApp.getNextWeek()));

        onePointPenguins1.setItems(mainApp.getOnePointPenguins());
        onePointPenguins2.setItems(mainApp.getOnePointPenguins());
        onePointPenguins3.setItems(mainApp.getOnePointPenguins());
        onePointPenguins4.setItems(mainApp.getOnePointPenguins());
        onePointPenguins5.setItems(mainApp.getOnePointPenguins());
        freezerPenguin.setItems(mainApp.getTwoPointPenguins());
    }

    public void doPredictions() {
        // try to parse vWeek
        String vWeekString = vWeek.getText();
        int vWeekInt = -1;
        try {
            vWeekInt = Integer.parseInt(vWeekString);
        } catch (NullPointerException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid vWeek");
            alert.setHeaderText("Invalid vWeek");
            alert.setContentText("The vWeek is not a valid integer, please correct the value and try again");

            alert.showAndWait();
            return;
        }

        if (vWeekInt < 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid vWeek");
            alert.setHeaderText("Invalid vWeek");
            alert.setContentText("The vWeek must be positive, please correct the value and try again");

            alert.showAndWait();
            return;
        }

        mainApp.setNextWeek(vWeekInt);

        // gather selected penguins
        int[] onePointPenguins = new int[6];

        ArrayList<String> missingPenguins = new ArrayList<>();

        onePointPenguins[0] = getOrdinalOrNegativeOne(onePointPenguins1.getValue());
        onePointPenguins[1] = getOrdinalOrNegativeOne(onePointPenguins2.getValue());
        onePointPenguins[2] = getOrdinalOrNegativeOne(onePointPenguins3.getValue());
        onePointPenguins[3] = getOrdinalOrNegativeOne(onePointPenguins4.getValue());
        onePointPenguins[4] = getOrdinalOrNegativeOne(onePointPenguins5.getValue());
        onePointPenguins[5] = getOrdinalOrNegativeOne(freezerPenguin.getValue());

        for (int i=0; i < onePointPenguins.length; i++) {
            if (onePointPenguins[i] < 0) {
                missingPenguins.add("One point penguin for position " + (i+1));
            }
        }

        if (!missingPenguins.isEmpty()) {
            // Nothing selected.
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Missing Penguins");
            alert.setHeaderText("Some Penguins Missing");
            alert.setContentText("The following penguins were not selected:\n" + missingPenguins.stream().collect(Collectors.joining("\n")));

            alert.showAndWait();
        } else {
            // do the predictions
            rightPaneTextArea.setText(mainApp.predictNextWeek(onePointPenguins));
        }
    }

    private int getOrdinalOrNegativeOne(Penguin penguin) {
        if (penguin == null) {
            return -1;
        } else {
            return penguin.getOrdinal();
        }
    }
}
