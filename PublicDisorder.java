package org.example.java;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.time.LocalDateTime;

public class PublicDisorder extends Crime {

    // Form fields
    private ComboBox<String> disorderTypeCombo;
    private ComboBox<String> severityCombo;
    private CheckBox violenceCheckBox;
    private ComboBox<String> violenceTypeCombo;
    private CheckBox propertyDamageCheckBox;
    private ComboBox<String> propertyTypeCombo;
    private TextField estimatedCostField;
    private ComboBox<String> crowdSizeCombo;
    private CheckBox alcoholInvolvedCheckBox;
    private ComboBox<String> alcoholLevelCombo;
    private CheckBox publicEventCheckBox;
    private ComboBox<String> eventTypeCombo;
    private TextField eventNameField;
    private ComboBox<String> perpetratorCountCombo;
    private TextArea perpetratorDescriptionArea;
    private CheckBox policeCalledCheckBox;
    private ComboBox<String> responseTimeCombo;
    private CheckBox injuriesCheckBox;
    private ComboBox<String> injuryTypeCombo;
    private TextField injuryCountField;
    private TextArea witnessDetailsArea;
    private TextArea incidentDescriptionArea;
    private ComboBox<String> publicPlaceCombo;
    private TextField otherPlaceField;

    public PublicDisorder() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Report Public Disorder");
        dialog.setHeaderText("Please provide details about the public disorder incident");

        // Build form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        int row = 0;

        // Type of Public Disorder
        Label disorderTypeLabel = new Label("Type of Public Disorder:");
        disorderTypeCombo = new ComboBox<>();
        disorderTypeCombo.getItems().addAll(
                "Riot/Violent Disturbance",
                "Unlawful Assembly",
                "Obstruction of Traffic",
                "Public Fighting",
                "Vandalism/Property Damage",
                "Noise Disturbance",
                "Illegal Procession/Rally",
                "Market/Business Disruption",
                "Educational Institution Disturbance",
                "Religious/Community Conflict",
                "Other"
        );
        disorderTypeCombo.setPromptText("Select disorder type");
        grid.add(disorderTypeLabel, 0, row);
        grid.add(disorderTypeCombo, 1, row++);

        // Severity Level
        Label severityLabel = new Label("Severity Level:");
        severityCombo = new ComboBox<>();
        severityCombo.getItems().addAll("Minor", "Moderate", "Serious", "Critical");
        severityCombo.setPromptText("Select severity");
        grid.add(severityLabel, 0, row);
        grid.add(severityCombo, 1, row++);

        // Violence Involved
        violenceCheckBox = new CheckBox("Violence Involved");
        grid.add(violenceCheckBox, 0, row, 2, 1);
        row++;

        // Violence Type (dependent on violence checkbox)
        Label violenceTypeLabel = new Label("Type of Violence:");
        violenceTypeCombo = new ComboBox<>();
        violenceTypeCombo.getItems().addAll(
                "Physical assault",
                "Weapon use",
                "Throwing objects",
                "Threatening behavior",
                "Mob violence"
        );
        violenceTypeCombo.setPromptText("Select violence type");
        violenceTypeCombo.setDisable(true);
        grid.add(violenceTypeLabel, 0, row);
        grid.add(violenceTypeCombo, 1, row++);

        // Property Damage
        propertyDamageCheckBox = new CheckBox("Property Damage Occurred");
        grid.add(propertyDamageCheckBox, 0, row, 2, 1);
        row++;

        // Property Type (dependent on property damage checkbox)
        Label propertyTypeLabel = new Label("Type of Property Damaged:");
        propertyTypeCombo = new ComboBox<>();
        propertyTypeCombo.getItems().addAll(
                "Private Vehicle",
                "Public Vehicle",
                "Shop/Business",
                "Public Building",
                "Private Residence",
                "Street Furniture",
                "Other"
        );
        propertyTypeCombo.setPromptText("Select property type");
        propertyTypeCombo.setDisable(true);
        grid.add(propertyTypeLabel, 0, row);
        grid.add(propertyTypeCombo, 1, row++);

        // Estimated Cost (dependent on property damage)
        Label estimatedCostLabel = new Label("Estimated Damage Cost (BDT):");
        estimatedCostField = new TextField();
        estimatedCostField.setPromptText("e.g., 50000");
        estimatedCostField.setDisable(true);
        grid.add(estimatedCostLabel, 0, row);
        grid.add(estimatedCostField, 1, row++);

        // Crowd Size
        Label crowdSizeLabel = new Label("Approximate Crowd Size:");
        crowdSizeCombo = new ComboBox<>();
        crowdSizeCombo.getItems().addAll(
                "5-10 people",
                "10-25 people",
                "25-50 people",
                "50-100 people",
                "100-500 people",
                "500+ people"
        );
        crowdSizeCombo.setPromptText("Select crowd size");
        grid.add(crowdSizeLabel, 0, row);
        grid.add(crowdSizeCombo, 1, row++);

        // Alcohol Involved
        alcoholInvolvedCheckBox = new CheckBox("Alcohol/Drugs Involved");
        grid.add(alcoholInvolvedCheckBox, 0, row, 2, 1);
        row++;

        // Alcohol Level (dependent on alcohol checkbox)
        Label alcoholLevelLabel = new Label("Level of Intoxication:");
        alcoholLevelCombo = new ComboBox<>();
        alcoholLevelCombo.getItems().addAll("Mild", "Moderate", "Severe", "Unknown");
        alcoholLevelCombo.setPromptText("Select intoxication level");
        alcoholLevelCombo.setDisable(true);
        grid.add(alcoholLevelLabel, 0, row);
        grid.add(alcoholLevelCombo, 1, row++);

        // Public Event
        publicEventCheckBox = new CheckBox("Related to Public Event");
        grid.add(publicEventCheckBox, 0, row, 2, 1);
        row++;

        // Event Type (dependent on public event checkbox)
        Label eventTypeLabel = new Label("Type of Event:");
        eventTypeCombo = new ComboBox<>();
        eventTypeCombo.getItems().addAll(
                "Political Rally",
                "Sports Event",
                "Religious Gathering",
                "Cultural Festival",
                "Educational Program",
                "Business/Trade Event",
                "Other"
        );
        eventTypeCombo.setPromptText("Select event type");
        eventTypeCombo.setDisable(true);
        grid.add(eventTypeLabel, 0, row);
        grid.add(eventTypeCombo, 1, row++);

        // Event Name (dependent on public event checkbox)
        Label eventNameLabel = new Label("Event Name:");
        eventNameField = new TextField();
        eventNameField.setPromptText("Name of the event");
        eventNameField.setDisable(true);
        grid.add(eventNameLabel, 0, row);
        grid.add(eventNameField, 1, row++);

        // Number of Perpetrators
        Label perpetratorCountLabel = new Label("Number of Perpetrators:");
        perpetratorCountCombo = new ComboBox<>();
        perpetratorCountCombo.getItems().addAll(
                "1 person",
                "2-5 people",
                "6-10 people",
                "11-25 people",
                "26-50 people",
                "50+ people",
                "Unknown"
        );
        perpetratorCountCombo.setPromptText("Select count");
        grid.add(perpetratorCountLabel, 0, row);
        grid.add(perpetratorCountCombo, 1, row++);

        // Perpetrator Description
        Label perpetratorDescLabel = new Label("Perpetrator Description:");
        perpetratorDescriptionArea = new TextArea();
        perpetratorDescriptionArea.setPromptText("Physical description, clothing, behavior, etc.");
        perpetratorDescriptionArea.setPrefRowCount(3);
        grid.add(perpetratorDescLabel, 0, row);
        grid.add(perpetratorDescriptionArea, 1, row++);

        // Police Called
        policeCalledCheckBox = new CheckBox("Police were called to scene");
        grid.add(policeCalledCheckBox, 0, row, 2, 1);
        row++;

        // Response Time (dependent on police called checkbox)
        Label responseTimeLabel = new Label("Police Response Time:");
        responseTimeCombo = new ComboBox<>();
        responseTimeCombo.getItems().addAll(
                "Within 5 minutes",
                "5-15 minutes",
                "15-30 minutes",
                "30-60 minutes",
                "More than 1 hour",
                "Police did not arrive"
        );
        responseTimeCombo.setPromptText("Select response time");
        responseTimeCombo.setDisable(true);
        grid.add(responseTimeLabel, 0, row);
        grid.add(responseTimeCombo, 1, row++);

        // Injuries
        injuriesCheckBox = new CheckBox("Injuries Occurred");
        grid.add(injuriesCheckBox, 0, row, 2, 1);
        row++;

        // Injury Type (dependent on injuries checkbox)
        Label injuryTypeLabel = new Label("Type of Injuries:");
        injuryTypeCombo = new ComboBox<>();
        injuryTypeCombo.getItems().addAll(
                "Minor cuts/bruises",
                "Serious injuries",
                "Fractures/broken bones",
                "Head injuries",
                "Multiple injuries"
        );
        injuryTypeCombo.setPromptText("Select injury type");
        injuryTypeCombo.setDisable(true);
        grid.add(injuryTypeLabel, 0, row);
        grid.add(injuryTypeCombo, 1, row++);

        // Number of Injured (dependent on injuries checkbox)
        Label injuryCountLabel = new Label("Number of Injured:");
        injuryCountField = new TextField();
        injuryCountField.setPromptText("e.g., 3");
        injuryCountField.setDisable(true);
        grid.add(injuryCountLabel, 0, row);
        grid.add(injuryCountField, 1, row++);

        // Public Place Type
        Label publicPlaceLabel = new Label("Type of Public Place:");
        publicPlaceCombo = new ComboBox<>();
        publicPlaceCombo.getItems().addAll(
                "Street/Road",
                "Market/Shopping Area",
                "Park/Recreation Area",
                "Government Building",
                "Educational Institution",
                "Religious Place",
                "Transport Hub",
                "Residential Area",
                "Other"
        );
        publicPlaceCombo.setPromptText("Select place type");
        grid.add(publicPlaceLabel, 0, row);
        grid.add(publicPlaceCombo, 1, row++);

        // Other Place (dependent on "Other" selection)
        Label otherPlaceLabel = new Label("Other Place Details:");
        otherPlaceField = new TextField();
        otherPlaceField.setPromptText("Specify other place type");
        otherPlaceField.setDisable(true);
        grid.add(otherPlaceLabel, 0, row);
        grid.add(otherPlaceField, 1, row++);

        // Witness Details
        Label witnessLabel = new Label("Witness Details:");
        witnessDetailsArea = new TextArea();
        witnessDetailsArea.setPromptText("Names, contact numbers, and details of witnesses");
        witnessDetailsArea.setPrefRowCount(3);
        grid.add(witnessLabel, 0, row);
        grid.add(witnessDetailsArea, 1, row++);

        // Incident Description
        Label incidentDescLabel = new Label("Detailed Incident Description:");
        incidentDescriptionArea = new TextArea();
        incidentDescriptionArea.setPromptText("Provide a detailed description of what happened");
        incidentDescriptionArea.setPrefRowCount(4);
        grid.add(incidentDescLabel, 0, row);
        grid.add(incidentDescriptionArea, 1, row++);

        // Set up dependent field listeners
        setupDependentFields();

        // Wrap in ScrollPane
        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setPrefSize(700,600);
        dialog.getDialogPane().setContent(scroll);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle result
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button okbutton= (Button) .getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            if (!validate()) {
                event.consume(); // Prevent dialog from closing
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // This will only execute if validation passes
                // Record the case
                UserDashboard.CaseRecord newCase = new UserDashboard.CaseRecord(
                        "CR" + System.currentTimeMillis(),
                        "Public Disorder",
                        disorderTypeCombo.getValue() + " - " + severityCombo.getValue(),
                        LocalDateTime.now(),
                        "Under Investigation"
                );

                // Confirmation
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Report Submitted");
                info.setHeaderText("Your public disorder report has been submitted successfully");
                info.setContentText("Case ID: " + newCase.getCaseId());
                info.showAndWait();
            }
        });
    }
    private void setupDependentFields() {
        // Violence dependent fields
        violenceCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            violenceTypeCombo.setDisable(!newVal);
            if (!newVal) {
                violenceTypeCombo.setValue(null);
            }
        });

        // Property damage dependent fields
        propertyDamageCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            propertyTypeCombo.setDisable(!newVal);
            estimatedCostField.setDisable(!newVal);
            if (!newVal) {
                propertyTypeCombo.setValue(null);
                estimatedCostField.clear();
            }
        });

        // Alcohol dependent fields
        alcoholInvolvedCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            alcoholLevelCombo.setDisable(!newVal);
            if (!newVal) {
                alcoholLevelCombo.setValue(null);
            }
        });

        // Public event dependent fields
        publicEventCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            eventTypeCombo.setDisable(!newVal);
            eventNameField.setDisable(!newVal);
            if (!newVal) {
                eventTypeCombo.setValue(null);
                eventNameField.clear();
            }
        });

        // Police called dependent fields
        policeCalledCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            responseTimeCombo.setDisable(!newVal);
            if (!newVal) {
                responseTimeCombo.setValue(null);
            }
        });

        // Injuries dependent fields
        injuriesCheckBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            injuryTypeCombo.setDisable(!newVal);
            injuryCountField.setDisable(!newVal);
            if (!newVal) {
                injuryTypeCombo.setValue(null);
                injuryCountField.clear();
            }
        });

        // Other place dependent field
        publicPlaceCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            otherPlaceField.setDisable(!"Other".equals(newVal));
            if (!"Other".equals(newVal)) {
                otherPlaceField.clear();
            }
        });
    }

    private boolean validate() {
        StringBuilder errors = new StringBuilder();

        // Required fields validation
        if (disorderTypeCombo.getValue() == null) {
            errors.append("• Type of Public Disorder is required\n");
        }

        if (severityCombo.getValue() == null) {
            errors.append("• Severity Level is required\n");
        }

        if (crowdSizeCombo.getValue() == null) {
            errors.append("• Approximate Crowd Size is required\n");
        }

        if (perpetratorCountCombo.getValue() == null) {
            errors.append("• Number of Perpetrators is required\n");
        }

        if (publicPlaceCombo.getValue() == null) {
            errors.append("• Type of Public Place is required\n");
        }

        if (incidentDescriptionArea.getText().trim().isEmpty()) {
            errors.append("• Detailed Incident Description is required\n");
        }

        // Dependent field validation
        if (violenceCheckBox.isSelected() && violenceTypeCombo.getValue() == null) {
            errors.append("• Type of Violence is required when violence is involved\n");
        }

        if (propertyDamageCheckBox.isSelected()) {
            if (propertyTypeCombo.getValue() == null) {
                errors.append("• Type of Property Damaged is required when property damage occurred\n");
            }
            if (estimatedCostField.getText().trim().isEmpty()) {
                errors.append("• Estimated Damage Cost is required when property damage occurred\n");
            } else {
                try {
                    Double.parseDouble(estimatedCostField.getText().trim());
                } catch (NumberFormatException e) {
                    errors.append("• Estimated Damage Cost must be a valid number\n");
                }
            }
        }

        if (alcoholInvolvedCheckBox.isSelected() && alcoholLevelCombo.getValue() == null) {
            errors.append("• Level of Intoxication is required when alcohol/drugs are involved\n");
        }

        if (publicEventCheckBox.isSelected()) {
            if (eventTypeCombo.getValue() == null) {
                errors.append("• Type of Event is required when related to public event\n");
            }
            if (eventNameField.getText().trim().isEmpty()) {
                errors.append("• Event Name is required when related to public event\n");
            }
        }

        if (policeCalledCheckBox.isSelected() && responseTimeCombo.getValue() == null) {
            errors.append("• Police Response Time is required when police were called\n");
        }

        if (injuriesCheckBox.isSelected()) {
            if (injuryTypeCombo.getValue() == null) {
                errors.append("• Type of Injuries is required when injuries occurred\n");
            }
            if (injuryCountField.getText().trim().isEmpty()) {
                errors.append("• Number of Injured is required when injuries occurred\n");
            } else {
                try {
                    Integer.parseInt(injuryCountField.getText().trim());
                } catch (NumberFormatException e) {
                    errors.append("• Number of Injured must be a valid number\n");
                }
            }
        }

        if ("Other".equals(publicPlaceCombo.getValue()) && otherPlaceField.getText().trim().isEmpty()) {
            errors.append("• Other Place Details is required when 'Other' is selected\n");
        }

        // Show validation errors if any
        if (errors.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errors.toString());
            alert.showAndWait();
            return false;
        }

        return true;
    }
}