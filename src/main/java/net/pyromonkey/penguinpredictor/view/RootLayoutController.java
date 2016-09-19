package net.pyromonkey.penguinpredictor.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import net.pyromonkey.penguinpredictor.MainApp;
import net.pyromonkey.penguinpredictor.Penguin;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RootLayoutController {

    @FXML private ChoiceBox<Penguin> onePointPenguins1;
    @FXML private ChoiceBox<Penguin> onePointPenguins2;
    @FXML private ChoiceBox<Penguin> onePointPenguins3;
    @FXML private ChoiceBox<Penguin> onePointPenguins4;
    @FXML private ChoiceBox<Penguin> onePointPenguins5;

    @FXML private ChoiceBox<Penguin> twoPointPenguins1;
    @FXML private ChoiceBox<Penguin> twoPointPenguins2;
    @FXML private ChoiceBox<Penguin> twoPointPenguins3;
    @FXML private ChoiceBox<Penguin> twoPointPenguins4;
    @FXML private ChoiceBox<Penguin> twoPointPenguins5;

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

        onePointPenguins1.setItems(mainApp.getOnePointPenguins());
        onePointPenguins2.setItems(mainApp.getOnePointPenguins());
        onePointPenguins3.setItems(mainApp.getOnePointPenguins());
        onePointPenguins4.setItems(mainApp.getOnePointPenguins());
        onePointPenguins5.setItems(mainApp.getOnePointPenguins());

        twoPointPenguins1.setItems(mainApp.getTwoPointPenguins());
        twoPointPenguins2.setItems(mainApp.getTwoPointPenguins());
        twoPointPenguins3.setItems(mainApp.getTwoPointPenguins());
        twoPointPenguins4.setItems(mainApp.getTwoPointPenguins());
        twoPointPenguins5.setItems(mainApp.getTwoPointPenguins());
    }

    public void doPredictions() {
        // gather selected penguins
        int[] onePointPenguins = new int[5];
        int[] twoPointPenguins = new int[5];

        ArrayList<String> missingPenguins = new ArrayList<>();

        onePointPenguins[0] = getOrdinalOrNegativeOne(onePointPenguins1.getValue());
        onePointPenguins[1] = getOrdinalOrNegativeOne(onePointPenguins2.getValue());
        onePointPenguins[2] = getOrdinalOrNegativeOne(onePointPenguins3.getValue());
        onePointPenguins[3] = getOrdinalOrNegativeOne(onePointPenguins4.getValue());
        onePointPenguins[4] = getOrdinalOrNegativeOne(onePointPenguins5.getValue());

        twoPointPenguins[0] = getOrdinalOrNegativeOne(twoPointPenguins1.getValue());
        twoPointPenguins[1] = getOrdinalOrNegativeOne(twoPointPenguins2.getValue());
        twoPointPenguins[2] = getOrdinalOrNegativeOne(twoPointPenguins3.getValue());
        twoPointPenguins[3] = getOrdinalOrNegativeOne(twoPointPenguins4.getValue());
        twoPointPenguins[4] = getOrdinalOrNegativeOne(twoPointPenguins5.getValue());

        for (int i=0; i < onePointPenguins.length; i++) {
            if (onePointPenguins[i] < 0) {
                missingPenguins.add("One point penguin for position " + (i+1));
            }
        }


        for (int i=0; i < twoPointPenguins.length; i++) {
            if (twoPointPenguins[i] < 0) {
                missingPenguins.add("Two point penguin for position " + (i+1));
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
            rightPaneTextArea.setText(mainApp.predictNextWeek(onePointPenguins, twoPointPenguins));
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
