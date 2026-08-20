package com.svgs;

import com.svgs.framework.frontend.PreferenceRegistry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class SecondaryController {
  @FXML
  private Button addAddGaugeButton;

  @FXML
  private ListView<String> parList;


  

  private static final ObservableList<String> names = FXCollections.observableArrayList(
      PreferenceRegistry.getPreferenceTitles());

  public static ObservableList<String> getMetricNames() {
    return names;
  }

  @FXML
  void initialize() {
    parList.setItems(names);
  }

  @FXML
  void doTheThing(ActionEvent event) {
    if (parList.getSelectionModel().getSelectedItem() == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("None Selected Warning");
      alert.setHeaderText("Select One! or not");
      alert.setContentText(
          "You haven't selected a gauge type yet. If you want to return without changes, press the return button.");
      alert.showAndWait();
      return;
    } else {
      int selected = parList.getSelectionModel().getSelectedIndex();

      GaugeCreator.createGuage(parList.getSelectionModel().getSelectedItem());
      try {
        App.setRoot("mainScreen");
      } catch (Exception e) {
        System.out.println("doTheThing error");
        System.out.println(e);
      }
    }

  }

}
