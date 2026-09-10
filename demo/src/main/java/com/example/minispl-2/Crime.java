package src.main;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import org.w3c.dom.Text;

import java.io.File;

public abstract class Crime
{
    TextField nameField;
    DatePicker datePicker;
    TextField timeField;
    TextField locationField;
    TextField fatherNameField;
    TextField motherNameField;
    TextField nidBcField;
    TextArea descriptionArea;
    TextField complainantPhoneField;
    Button photoButton ;
    public abstract void absMethod();
    public GridPane buildForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Complainant Information
        nameField = new TextField();
        nameField.setPromptText("Your Name (Complainant)");

        fatherNameField = new TextField();
        fatherNameField.setPromptText("Father's Name");

        motherNameField = new TextField();
        motherNameField.setPromptText("Mother's Name");

        complainantPhoneField = new TextField();
        complainantPhoneField.setPromptText("Complainant Phone Number");

        locationField = new TextField();
        locationField.setPromptText("Location of incident");

        datePicker = new DatePicker();
        datePicker.setPromptText("Date of Incident");

        timeField = new TextField();
        timeField.setPromptText("Time (e.g., 14:30 pm)");

        descriptionArea = new TextArea();
        descriptionArea.setPromptText("Detailed description of the incident");
        descriptionArea.setPrefRowCount(4);

        // NID/BC Number as TextField
         nidBcField = new TextField();
        nidBcField.setPromptText("Enter NID or Birth Certificate Number");

        // User Photo Upload
        photoButton = new Button("Upload Your Photo");
        Label photoLabel = new Label("No file chosen");
        photoLabel.setMinWidth(150);

        photoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                photoLabel.setText(selectedFile.getName());
            }
        });

        // Accused Person Info
        TextField accusedName = new TextField();
        accusedName.setPromptText("Accused Person's Name");

        TextField accusedPhone = new TextField();
        accusedPhone.setPromptText("Accused Phone Number");

        TextField accusedEmail = new TextField();
        accusedEmail.setPromptText("Accused Email");

        TextField accusedAddress = new TextField();
        accusedAddress.setPromptText("Accused Physical Address");

        int row = 0;
        grid.add(new Label("Your Name:"), 0, row); grid.add(nameField, 1, row++);
        grid.add(new Label("Father's Name:"), 0, row); grid.add(fatherNameField, 1, row++);
        grid.add(new Label("Mother's Name:"), 0, row); grid.add(motherNameField, 1, row++);
        grid.add(new Label("Phone Number:"), 0, row); grid.add(complainantPhoneField, 1, row++);
        grid.add(new Label("Location:"), 0, row); grid.add(locationField, 1, row++);
        grid.add(new Label("Date:"), 0, row); grid.add(datePicker, 1, row++);
        grid.add(new Label("Time:"), 0, row); grid.add(timeField, 1, row++);
        grid.add(new Label("Description:"), 0, row); grid.add(descriptionArea, 1, row++);
        grid.add(new Label("NID / Birth Certificate No:"), 0, row); grid.add(nidBcField, 1, row++);
        grid.add(new Label("Your Photo:"), 0, row); grid.add(photoButton, 1, row); grid.add(photoLabel, 2, row++);
        grid.add(new Label("Accused Name (if you know):"), 0, row); grid.add(accusedName, 1, row++);
        grid.add(new Label("Accused Phone (if you know):"), 0, row); grid.add(accusedPhone, 1, row++);
        grid.add(new Label("Accused Email (if you know):"), 0, row); grid.add(accusedEmail, 1, row++);
        grid.add(new Label("Accused Address (if you know):"), 0, row); grid.add(accusedAddress, 1, row++);

        return grid;
    }
    public boolean validateCommonFields() {
        if (nameField == null || nameField.getText().trim().isEmpty()) return false;
        if (nidBcField == null || nidBcField.getText().trim().isEmpty()) return false;
        if(complainantPhoneField == null || complainantPhoneField.getText().trim().isEmpty()) return false;
        if(locationField == null || locationField.getText().trim().isEmpty()) return false;
        if(datePicker == null || datePicker.getValue() == null) return false;
        if(timeField == null || timeField.getText().trim().isEmpty()) return false;
        if(descriptionArea == null || descriptionArea.getText().trim().isEmpty()) return false;
        if(photoButton == null || photoButton.getText().trim().isEmpty()) return false;
        if(fatherNameField == null || fatherNameField.getText().trim().isEmpty()) return false;
        if(motherNameField == null || motherNameField.getText().trim().isEmpty()) return false;

        return true;
    }


}
