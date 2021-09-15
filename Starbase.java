// I worked on the homework assignment alone, using only course materials.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * This file contains the Starbase class which is the base for my JavaFX application.
 * This GUI will keep track of what port a starship is in.
 * @author Divyam Gupta
 * @version 1.0
 */

public class Starbase extends Application {
    /** A String Array that holds the text that goes inside each dock. */
    private static String[] starshipArray;
    /** A BorderPane that consists all major buttons and shapes. */
    private static BorderPane border;
    /** A String Array that holds data for starships not docked. */
    private static ArrayList<String> notDocked = new ArrayList<String>();
    /** A String Array that holds data for starships docked. */
    private static ArrayList<String> docked = new ArrayList<String>();


    @Override
    public void start(Stage primaryStage) throws Exception {
        starshipArray = new String[8];
        border = new BorderPane();
        border.setTop(addHBoxTop());
        border.setBottom(addGridPane());
        border.setCenter(addHBoxCenter());

        Image image = new Image("Space.jpg");
        ImageView myview = new ImageView(image);

        StackPane finalPane = new StackPane();
        finalPane.getChildren().addAll(myview, border);
        Scene scene = new Scene(finalPane, 1050, 300);

        primaryStage.setTitle("Starbase Command");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // top
    /**
     * Creates a new HBox which designs the top of the BorderPane
     * @return  HBox for the top of BorderPane
     */
    private HBox addHBoxTop() {
        HBox hbox = new HBox();
        Label label = new Label("Welcome to Starbase 1331!");
        label.setFont(new Font("Avenir", 35));
        label.setTextFill(Color.web("#FFFFFF"));
        hbox.setAlignment(Pos.TOP_CENTER);
        hbox.getChildren().add(label);
        return hbox;
    }

    // bottom
    /**
     * Creates a new GridPane which designs the bottom of the BorderPane
     * @return  GridPane for the top of BorderPane
     */
    private GridPane addGridPane() {
        GridPane grid = new GridPane();

        // textField for name of the starship;
        Label nameLabel = new Label("Enter Starship's Name: ");
        nameLabel.setTextFill(Color.web("#FFFFFF"));
        TextField nameField = new TextField();
        nameField.setPromptText("Starship's Name");
        grid.add(nameLabel, 1, 0);
        grid.add(nameField, 2, 0);

        // dropdown for the type of the starship;
        Label typeLabel = new Label(" Choose Starship's Type: ");
        typeLabel.setTextFill(Color.web("#FFFFFF"));
        ChoiceBox<Starship> choicebox = new ChoiceBox<>();
        Starship[] starship = Starship.values();
        for (Starship type : starship) {
            choicebox.getItems().add(type);
        }
        grid.add(typeLabel, 3, 0);
        grid.add(choicebox, 4, 0);

        // dock button:
        for (int i = 0; i < 8; i++) {
            starshipArray[i] = "Empty";
        }
        Button dockButton = new Button(" Request Docking Clearance ");
        dockButton.setStyle("-fx-outer-border: BLACK");
        dockButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String starshipName = nameField.getText();
                nameField.clear();
                if (starshipName.trim().isEmpty()) {
                    choicebox.getSelectionModel().clearSelection();
                    Alert textAlert = new Alert(AlertType.ERROR);
                    textAlert.setTitle("Error Dialog");
                    textAlert.setHeaderText("Invalid Text Input");
                    textAlert.setContentText("Enter a valid name. You have not been granted docking clearance.");
                    textAlert.showAndWait();
                } else {
                    Starship starshipType = choicebox.getValue();
                    choicebox.getSelectionModel().clearSelection();
                    if (starshipType == null) {
                        nameField.clear();
                        Alert typeAlert = new Alert(AlertType.ERROR);
                        typeAlert.setTitle("Error Dialog");
                        typeAlert.setHeaderText("Invalid Type Input");
                        typeAlert.setContentText("Choose a valid type. You have not been granted docking clearance.");
                        typeAlert.showAndWait();
                    } else {
                        for (int j = 0; j < 8; j++) {
                            if (starshipArray[j].equals("Empty")) {
                                starshipArray[j] = starshipName + "\n" + starshipType;
                                border.setCenter(addHBoxCenter());
                                docked.add(starshipName + "("
                                    + starshipType + ") was granted clearance on "
                                    + new Date().toString() + ".");
                                    try {
                                    File fileDocked = new File("fileDocked.txt");
                                    if (!fileDocked.exists()) {
                                        fileDocked.createNewFile();
                                    }
                                    PrintWriter pw2 = new PrintWriter(fileDocked);
                                    for (String data : docked) {
                                        pw2.println(data);
                                    }
                                    pw2.close();
                                    }   catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                break;
                            }
                            if (j == 7 && !(starshipArray[j].equals("Empty"))) {
                                Alert maxAlert = new Alert(AlertType.ERROR);
                                maxAlert.setTitle("Error Dialog");
                                maxAlert.setHeaderText("Max Reached!");
                                maxAlert.setContentText("Ooops, "
                                    + starshipName
                                    + " did not receive docking clearance! There are no more ports available.");
                                maxAlert.showAndWait();
                                notDocked.add(starshipName + "("
                                    + starshipType + ") was not granted clearance on "
                                    + new Date().toString() + ".");
                                    try {
                                    File fileNotDocked = new File("fileNotDocked.txt");
                                    if (!fileNotDocked.exists()) {
                                        fileNotDocked.createNewFile();
                                    }
                                    PrintWriter pw = new PrintWriter(fileNotDocked);
                                    for (String data : notDocked) {
                                        pw.println(data);
                                    }
                                    pw.close();
                                    }   catch (IOException e) {
                                        e.printStackTrace();
                                    }
                            }
                        }
                    }
                }
            }
        });
        grid.add(dockButton, 5, 0);

        // evacuate button:
        Button evacuateButton = new Button("        !!!!!  EVACUATE  !!!!!        ");
        evacuateButton.setStyle("-fx-background-color: GOLD");
        evacuateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int j = 0; j < 8; j++) {
                    starshipArray[j] = "Empty";
                }
                border.setCenter(addHBoxCenter());
            }
        });
        grid.add(evacuateButton, 6, 0);
        grid.setAlignment(Pos.BOTTOM_CENTER);
        return grid;
    }
    // center
    /**
     * Creates a new HBox which designs the center of the BorderPane
     * @return  HBox for the center of BorderPane
     */
    private HBox addHBoxCenter() {
        HBox hbox = new HBox();
        Region rectangle;
        Text insideText;
        Text betweenText;
        // first
        rectangle = new Region();
        if (starshipArray[0].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneOne = new StackPane();
        insideText = new Text(starshipArray[0]);
        stackPaneOne.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneOne, betweenText);
        stackPaneOne.setOnMouseClicked(event -> {
            starshipArray[0] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // second
        rectangle = new Region();
        if (starshipArray[1].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneTwo = new StackPane();
        insideText = new Text(starshipArray[1]);
        stackPaneTwo.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneTwo, betweenText);
        stackPaneTwo.setOnMouseClicked(event -> {
            starshipArray[1] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // third
        rectangle = new Region();
        if (starshipArray[2].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneThree = new StackPane();
        insideText = new Text(starshipArray[2]);
        stackPaneThree.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneThree, betweenText);
        stackPaneThree.setOnMouseClicked(event -> {
            starshipArray[2] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // forth
        rectangle = new Region();
        if (starshipArray[3].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneFour = new StackPane();
        insideText = new Text(starshipArray[3]);
        stackPaneFour.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneFour, betweenText);
        stackPaneFour.setOnMouseClicked(event -> {
            starshipArray[3] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // fifth
        rectangle = new Region();
        if (starshipArray[4].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneFive = new StackPane();
        insideText = new Text(starshipArray[4]);
        stackPaneFive.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneFive, betweenText);
        stackPaneFive.setOnMouseClicked(event -> {
            starshipArray[4] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // sixth
        rectangle = new Region();
        if (starshipArray[5].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneSix = new StackPane();
        insideText = new Text(starshipArray[5]);
        stackPaneSix.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneSix, betweenText);
        stackPaneSix.setOnMouseClicked(event -> {
            starshipArray[5] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // seventh
        rectangle = new Region();
        if (starshipArray[6].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneSeven = new StackPane();
        insideText = new Text(starshipArray[6]);
        stackPaneSeven.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneSeven, betweenText);
        stackPaneSeven.setOnMouseClicked(event -> {
            starshipArray[6] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        // eigthth
        rectangle = new Region();
        if (starshipArray[7].equals("Empty")) {
            rectangle.setStyle("-fx-background-color: DARKSALMON; -fx-min-width: 120;");
        } else {
            rectangle.setStyle("-fx-background-color: LIGHTGREEN; -fx-min-width: 120;");
        }
        StackPane stackPaneEight = new StackPane();
        insideText = new Text(starshipArray[7]);
        stackPaneEight.getChildren().addAll(rectangle, insideText);
        betweenText = new Text("  ");
        hbox.getChildren().addAll(stackPaneEight, betweenText);
        stackPaneEight.setOnMouseClicked(event -> {
            starshipArray[7] = "Empty";
            border.setCenter(addHBoxCenter());
        });
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }
}
