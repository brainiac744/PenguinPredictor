package net.pyromonkey.penguinpredictor.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import net.pyromonkey.penguinpredictor.MainApp;

public class RootLayoutController {

    @FXML private TextField vWeek;

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

        // do the predictions
        rightPaneTextArea.setText(mainApp.predictNextWeek());
    }
}
