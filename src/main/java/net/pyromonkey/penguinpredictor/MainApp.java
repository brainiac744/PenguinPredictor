package net.pyromonkey.penguinpredictor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.pyromonkey.penguinpredictor.view.RootLayoutController;


public class MainApp extends Application {

    private Stage primaryStage;

    private static final String[] onePointPenguinNames = {"Smoky Well cactus", "Lumbridge toadstool", "Draynor crate", "Fort Forinthry crate", "Mudskipper bush", "Burthorpe rock", "White Wolf rock", "Entrana barrel", "Musa Point crate", "Brimhaven bush", "Central Karamja bush", "East Karamja bush", "Rellekka rock", "Gnome Stronghold bush", "Gnome Maze bush", "Castle Wars bush", "Witchaven rock", "McGrubor's Wood bush", "Feldip rock", "Port Khazard barrel", "Eagles' Peak bush", "Observatory crate", "Ice Mountain rock", "Lighthouse rock", "Ardougne bush", "Wizards' Tower bush", "Barbarian Outpost bush", "Goblin Village bush", "Dorgesh-Kaan barrel", "Zanaris toadstool", "Digsite rock", "Mage Arena bush", "Al Kharid cactus", "KQ Lair cactus"};
    private static final String[] twoPointPenguinNames = {"Lletya toadstool", "Tyras Catapult toadstool", "Elf Camp toadstool", "North Sophanem cactus", "South Sophanem cactus", "Jiggig rock", "Port Phasmatys barrel", "Canifis crate", "Canifis bush", "Mort Myre bush", "Mort Myre toadstool", "Lava Maze rock", "North of KBD rock", "Scorpion Pit rock", "Old SC rock", "Lunar Isle rock", "Neitiznot bush", "Jatizso bush", "Neitiznot rock", "Jatizso rock", "Miscellania bush", "Ape Atoll bush", "Ape Atoll barrel", "Mos Le'Harmless barrel", "Dragontooth crate", "Nardah cactus", "SW of KBD rock", "Mort Myre rock", "Elf Limestone rock", "Castle Wars rock", "Feldip bush", "Piscatoris crate", "Keldagrim rock", "Desert TARDIS cactus"};
    private static final String[] bearNames = {"Varrock Well", "Rellekka Well", "Falador Well", "Ardougne Well", "Musa Point Well", "Rimmington Well", "Varrock Well", "Rellekka Well", "Falador Well", "Ardougne Well", "Musa Point Well", "Rimmington Well", "Varrock Well", "Rellekka Well", "Falador Well", "Ardougne Well", "Musa Point Well", "Rimmington Well", "Varrock Well", "Rellekka Well", "Falador Well", "Ardougne Well", "Musa Point Well", "Rimmington Well", "Varrock Well", "Rellekka Well", "Falador Well", "Ardougne Well", "Rellekka Well", "Rellekka Well", "Falador Well", "Ardougne Well", "Musa Point Well", "Rimmington Well"};

    private int nextWeek;
    private int runeDate;


    /**
     * Constructor
     */
    public MainApp() {
        // figure out what next v-week is
        setNextWeek((int) ((System.currentTimeMillis()/1000)-1219190400)/(7*86400) + 1);
    }

    public int getNextWeek() {
        return (int) ((System.currentTimeMillis()/1000)-1219190400)/(7*86400) + 1;
    }

    public void setNextWeek(int nextWeek) {
        this.nextWeek = nextWeek;
        this.runeDate = nextWeek * 7 + 2366;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Penguin Predictor");

        // Set the application icon.
        this.primaryStage.getIcons().add(new Image("file:resources/images/penguin-icon.png"));

        initRootLayout();
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class
                    .getResource("/view/RootLayout.fxml"));
            SplitPane rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return the main stage.
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public String predictNextWeek() {

        ArrayList<String> onePointPredictions = new ArrayList<>();
        ArrayList<String> twoPointPredictions = new ArrayList<>();

        // calculate 1 points with random seed of the week
        for (int i=0; i<5; i++) {
            long seed = (1L << 32) + runeDate + i;
           onePointPredictions.add((i+1) + ") " + onePointPenguinNames[(new Random(seed)).nextInt(34)]);
        }

        // calculate 2 points with random seed of the week
        for (int i=0; i<5; i++) {
            long seed = (1L << 32) + runeDate + i;
            twoPointPredictions.add((i+6) + ") " + twoPointPenguinNames[(new Random(seed)).nextInt(34)]);
        }

        // calculate ghost first spawn
        twoPointPredictions.add("G) " + twoPointPenguinNames[(new Random((1L << 32) + runeDate + 5)).nextInt(34)]);

        // calculate freezer
        twoPointPredictions.add("F) " + twoPointPenguinNames[(new Random((1L << 32) + runeDate + 6)).nextInt(34)]);

        // calculate bear
        String bear = bearNames[(new Random((1L << 32) + runeDate + 4)).nextInt(34)];

        return "For week v" + nextWeek + " I predict:\n\n1-Point Penguins\n" + String.join("\n", onePointPredictions)
                + "\n\n2-Point-Penguins\n" + String.join("\n", twoPointPredictions)
                + "\n\nBear: " + bear;
    }

    public static void main(String[] args) {
        launch(args);
    }
}