package net.pyromonkey.penguinpredictor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.pyromonkey.penguinpredictor.view.RootLayoutController;


public class MainApp extends Application {

    private Stage primaryStage;

    private static final String[] onePointPenguinNames = {"Goblin Village bush", "Ice Mountain rock", "McGrubbors bush", "Rellekka rock", "Entrana barrel", "Draynor crate", "Mage Arena bush", "Barbarian Outpost bush", "Observatory crate", "Witchaven rock", "East Karamja bush", "White Wolf rock", "Lumbridge toadstool", "Digsite rock", "Wizards' Tower bush", "Eagles' Peak bush", "Castle Wars bush", "Central Karamja bush", "Burthorpe rock", "Smoky Well cactus", "Zanaris toadstool", "Ardougne bush", "Port Khazard barrel", "Gnome Maze bush", "Brimhaven bush", "Mudskipper bush", "KQ Lair cactus", "Dorgesh-Kaan barrel", "Lighthouse rock", "Feldip rock", "Gnome Stronghold bush", "Musa Point crate", "Sawmill crate", "Al Kharid cactus"};
    private static final String[] twoPointPenguinNames = {"Castle Wars rock", "Mort Myre rock", "Nardah cactus", "Mos Le'Harmless barrel", "Ape Atoll bush", "Jatizso rock", "Jatizso bush", "Lunar Isle rock", "Scorpion Pit rock", "Lava Maze rock", "Mort Myre bush", "Canifis crate", "Jiggig rock", "Inside Sophanem cactus", "Tyras Catapult toadstool", "Keldagrim rock", "Feldip bush", "Elf Limestone rock", "SW of KBD rock", "Dragontooth crate", "Ape Atoll barrel", "Miscellania bush", "Neitiznot rock", "Neitiznot bush", "Old SC rock", "North of KBD rock", "Mort Myre toadstool", "Canifis bush", "Port Phasmatys barrel", "Outside Sophanem cactus", "Elf Camp toadstool", "Lletya toadstool", "Piscatoris crate"};

    private static final int[] onePointOffsets = {17, 24, 21, 28, 17, 24, 15, 22, 15, 8, 19, 12, 19, 26, 15, 22, 15, 8, 19, 12, 19, 26, 15, 22, 17, 24, 17, 10, 17, 24, 17, 24, 17, 24, 17, 10, 17, 24, 15, 22, 19, 26, 19, 12, 15, 8, 15, 22, 19, 26, 19, 12, 15, 8, 15, 22, 17, 24, 21, 28, 17, 24, 13};
    private static final int[] twoPointOffsets = {28, 12, 12, 29, 28, 12, 26, 10, 13, 29, 30, 13, 10, 27, 26, 10, 13, 29, 30, 13, 10, 27, 26, 10, 28, 12, 15, 31, 28, 12, 8, 25, 28, 12, 15, 31, 28, 12, 26, 10, 10, 27, 30, 13, 13, 29, 26, 10, 10, 27, 30, 13, 13, 29, 26, 10, 28, 12, 12, 29, 28, 12, 11};

    private static final int[] disruptionWeekBasis = {192, 193, 210, 211, 228, 229, 247, 248, 265, 266, 283, 284, 301, 302};
    private static final int[][] disruptionWeekStrands = {{3,4,5}, {1,2}, {5}, {1,2,3,4}, {}, {1,2,3,4,5}, {2,3,4,5}, {1}, {4,5}, {1,2,3}, {}, {1,2,3,4,5}, {}, {1,2,3,4,5}};

    private int nextWeek;
    private ArrayList<Integer> npcWeekOffsets = new ArrayList<>();

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Penguin> onePointPenguins = FXCollections.observableArrayList();
    private ObservableList<Penguin> twoPointPenguins = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // figure out what next v-week is
        nextWeek = (int) ((System.currentTimeMillis()/1000)-1219190400)/(7*86400) + 1;

        // calculate current npc week offsets
        int[] offsetAdds = {32, 23, 14, 5, 28};
        for (int add : offsetAdds) {
            int offset = (((nextWeek - 3) % 32) + add) % 32;
            if (offset == 0) {
                offset = 32;
            }
            npcWeekOffsets.add(offset);
        }

        // put the penguins into their observable lists
        for (int i = 0; i < onePointPenguinNames.length; i++) {
            onePointPenguins.add(new Penguin(onePointPenguinNames[i], i));
        }

        for (int i = 0; i < twoPointPenguinNames.length; i++) {
            twoPointPenguins.add(new Penguin(twoPointPenguinNames[i], i));
        }

        // put the penguins in alphabetical order for improved human-friendliness
        Collections.sort(onePointPenguins);
        Collections.sort(twoPointPenguins);
    }

    public ObservableList<Penguin> getOnePointPenguins() {
        return onePointPenguins;
    }

    public ObservableList<Penguin> getTwoPointPenguins() {
        return twoPointPenguins;
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

    public String predictNextWeek(int[] onePointPenguins, int[] twoPointPenguins) {
        // determine if it is a disruption week
        boolean disruption = false;
        int[] disruptedStrands = {};
        for (int i = 0; i < disruptionWeekBasis.length; i++) {
            if ((nextWeek - disruptionWeekBasis[i]) % 128 == 0) {
                disruption = true;
                disruptedStrands = disruptionWeekStrands[i];
            }
        }

        ArrayList<String> onePointPredictions = new ArrayList<>();
        ArrayList<String> twoPointPredictions = new ArrayList<>();

        // for disruptions
        int set = 1;
        if (disruption) {
            if ((nextWeek - 247) % 73 == 0 || (nextWeek - 248) % 73 == 0) {
                // use set 3
                set = 3;
            } else {
                // use set 2
                set = 2;
            }
        }
        for (int i = 0; i < 5; i ++) {
            int strand = i + 1;
            int weekOffsetBase = (npcWeekOffsets.get(i) - 1) * 2;
            int onePointDisruptionOffset = 0;
            int twoPointDisruptionOffset = 0;
            if (disruption && inArray(strand, disruptedStrands)) {
                if (set == 3) {
                    // use set 3
                    onePointDisruptionOffset = 16;
                    twoPointDisruptionOffset = 2;
                } else {
                    // use set 2
                    onePointDisruptionOffset = 31;
                    twoPointDisruptionOffset = 27;
                }
            }
            onePointPredictions.add(strand + ") " + predictPenguin(onePointPenguins[i], onePointOffsets, weekOffsetBase, onePointPenguinNames, onePointDisruptionOffset));
            twoPointPredictions.add(strand + ") " + predictPenguin(twoPointPenguins[i], twoPointOffsets, weekOffsetBase, twoPointPenguinNames, twoPointDisruptionOffset));
        }

        String message = "For week v" + nextWeek + " I predict:\n\n1-Point Penguins\n" + onePointPredictions.stream().collect(Collectors.joining("\n"))
                + "\n\n2-Point-Penguins\n" + twoPointPredictions.stream().collect(Collectors.joining("\n"));

        if (disruption) {
            String disruptionMessage = "Warning! Week v" + nextWeek + " is a disruption week! The following strands will be disrupted using set " + set + ": ";
            if (disruptedStrands.length == 0) {
                disruptionMessage += "none  ";
            }
            for (int strand : disruptedStrands) {
                disruptionMessage += strand + ", ";
            }
            message = disruptionMessage.substring(0, disruptionMessage.length()-2) + ".\n\n" + message;
        }
        return message;
    }

    private boolean inArray(int value, int[] array) {
        for (int arrayValue : array) {
            if (value == arrayValue) {
                return true;
            }
        }
        return false;
    }

    private String predictPenguin(int currentWeek, int[] weekOffsets, int weekOffsetBase, String[] penguins, int disruptionOffset) {
        List<Integer> pengArrayOffsets = new ArrayList<>();
        if (weekOffsetBase + 1 < weekOffsets.length) {
            // two possibilities
            pengArrayOffsets.add((weekOffsets[weekOffsetBase] + currentWeek) % penguins.length);
            pengArrayOffsets.add((weekOffsets[weekOffsetBase + 1] + currentWeek) % penguins.length);
        } else {
            // only one possible option
            pengArrayOffsets.add((weekOffsets[weekOffsetBase] + currentWeek) % penguins.length);
        }

        if (disruptionOffset != 0) {
            int originalLength = pengArrayOffsets.size();
            for (int i = 0; i < originalLength; i++) {
                pengArrayOffsets.add((pengArrayOffsets.get(i) + disruptionOffset) % penguins.length);
            }
        }

        String message = "";
        for (int pengArrayOffset : pengArrayOffsets) {
            message += penguins[pengArrayOffset] + " or ";
        }

        return message.substring(0, message.length() - 4);
    }

    public static void main(String[] args) {
        launch(args);
    }
}