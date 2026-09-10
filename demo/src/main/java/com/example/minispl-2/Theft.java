package src.main;

import com.example.demo1.Crime;
import com.example.demo1.UserDashboard;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDateTime;

public class Theft extends Crime {

    public Theft() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Report Theft or Burglary");
        dialog.setHeaderText("Please provide details about the incident");

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));
        int row = 0;

        // 1. Type of Theft or Burglary
        grid.add(new Label("Type of Theft or Burglary:"), 0, row);
        ComboBox<String> theftTypeBox = new ComboBox<>();
        theftTypeBox.getItems().addAll(
                "House Burglary",
                "Shop Theft",
                "Vehicle Break-in",
                "Snatching",
                "Other"
        );
        theftTypeBox.setPrefWidth(300);
        grid.add(theftTypeBox, 1, row++);

        TextField theftOtherField = new TextField();
        theftOtherField.setPromptText("If other, specify...");
        theftOtherField.setDisable(true);
        grid.add(theftOtherField, 1, row++);

        theftTypeBox.setOnAction(e -> {
            theftOtherField.setDisable(!theftTypeBox.getValue().contains("Other"));
        });

        // 2. Date/Time of Incident
        grid.add(new Label("Incident Date:"), 0, row);
        DatePicker incidentDate = new DatePicker();
        grid.add(incidentDate, 1, row++);

        grid.add(new Label("Approximate Time of Incident:"), 0, row);
        TextField incidentTime = new TextField();
        incidentTime.setPromptText("e.g., Between 2–4 PM");
        grid.add(incidentTime, 1, row++);

        grid.add(new Label("When was it discovered?"), 0, row);
        TextField discoveryTime = new TextField();
        discoveryTime.setPromptText("e.g., Next morning at 6 AM");
        grid.add(discoveryTime, 1, row++);

        // 3. Location
        grid.add(new Label("Location of Incident:"), 0, row);
        TextField locationField = new TextField();
        locationField.setPromptText("Address or area");
        grid.add(locationField, 1, row++);

        // 4. Entry Method
        grid.add(new Label("How did the perpetrator enter?"), 0, row);
        VBox entryBox = new VBox(5);
        entryBox.getChildren().addAll(
                new CheckBox("Forced entry (broken locks/windows)"),
                new CheckBox("Unlocked access"),
                new CheckBox("Duplicate key used"),
                new CheckBox("Roof or backdoor entry")
        );
        grid.add(entryBox, 1, row++);

        // 5. Items Stolen
        grid.add(new Label("List of Items Stolen:"), 0, row);
        TextArea stolenItems = new TextArea();
        stolenItems.setPromptText("e.g., Mobile phone, laptop, jewelry");
        stolenItems.setPrefRowCount(3);
        grid.add(stolenItems, 1, row++);

        grid.add(new Label("Estimated Value (in BDT):"), 0, row);
        TextField estimatedValue = new TextField();
        grid.add(estimatedValue, 1, row++);

        // 6. Ownership Proof
        grid.add(new Label("Proof of Ownership (if any):"), 0, row);
        VBox proofBox = new VBox(5);
        proofBox.getChildren().addAll(
                new CheckBox("Receipts"),
                new CheckBox("Photos/Videos"),
                new CheckBox("Insurance Papers"),
                new CheckBox("Serial Numbers")
        );
        grid.add(proofBox, 1, row++);

        // 7. Witnesses
        grid.add(new Label("Were there any witnesses?"), 0, row);
        TextArea witnessInfo = new TextArea();
        witnessInfo.setPromptText("Describe any witnesses or neighbor reports");
        witnessInfo.setPrefRowCount(2);
        grid.add(witnessInfo, 1, row++);

        // 8. CCTV Footage
        grid.add(new Label("Is there any CCTV footage?"), 0, row);
        TextField cctvInfo = new TextField();
        cctvInfo.setPromptText("Source: home, shop, street, etc.");
        grid.add(cctvInfo, 1, row++);

        // 9. Past Incidents
        grid.add(new Label("Any previous similar incidents here?"), 0, row);
        ToggleGroup pastGroup = new ToggleGroup();
        RadioButton pastYes = new RadioButton("Yes");
        RadioButton pastNo = new RadioButton("No");
        pastYes.setToggleGroup(pastGroup);
        pastNo.setToggleGroup(pastGroup);
        grid.add(new HBox(15, pastYes, pastNo), 1, row++);

        // 10. Suspects
        grid.add(new Label("Do you suspect anyone?"), 0, row);
        TextArea suspectArea = new TextArea();
        suspectArea.setPromptText("Mention any possible suspects");
        suspectArea.setPrefRowCount(2);
        grid.add(suspectArea, 1, row++);

        // 11. Previously Reported
        grid.add(new Label("Reported elsewhere before?"), 0, row);
        ToggleGroup reportGroup = new ToggleGroup();
        RadioButton reportedYes = new RadioButton("Yes");
        RadioButton reportedNo = new RadioButton("No");
        reportedYes.setToggleGroup(reportGroup);
        reportedNo.setToggleGroup(reportGroup);
        grid.add(new HBox(15, reportedYes, reportedNo), 1, row++);

        // 12. Action Expected
        grid.add(new Label("What action do you expect from police?"), 0, row);
        TextArea actionArea = new TextArea();
        actionArea.setPromptText("e.g., Arrest, item recovery, increased patrol");
        actionArea.setPrefRowCount(2);
        grid.add(actionArea, 1, row++);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setPadding(new Insets(10));
        scroll.setPrefViewportHeight(700);
        scroll.setPrefViewportWidth(700);
        dialog.getDialogPane().setPrefSize(800, 750);
        dialog.getDialogPane().setContent(scroll);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (theftTypeBox.getValue() == null ||
                        (theftTypeBox.getValue().equals("Other") && theftOtherField.getText().trim().isEmpty())) {
                    showError("Please specify the type of theft or burglary.");
                    return;
                }
                if (incidentDate.getValue() == null || locationField.getText().trim().isEmpty()) {
                    showError("Please provide incident date and location.");
                    return;
                }
                if (stolenItems.getText().trim().isEmpty() || estimatedValue.getText().trim().isEmpty()) {
                    showError("Please list stolen items and estimated value.");
                    return;
                }

                UserDashboard.CaseRecord newCase = new UserDashboard.CaseRecord(
                        "CR" + System.currentTimeMillis(),
                        "Theft",
                        theftTypeBox.getValue(),
                        LocalDateTime.now(),
                        "Under Investigation"
                );

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Report Submitted");
                info.setHeaderText("Your report has been submitted successfully");
                info.setContentText("Case ID: " + newCase.getCaseId());
                info.showAndWait();
            }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Form Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
