package main;

import http.HttpsRequest;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import json.JSONLoader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import simulator.StationsList;
import simulator.Station;

import java.io.IOException;

public class Controller {
    public VBox responseVBox;
    public Label responseCodeLabel;
    public TextArea responseContentLabel;
    public Button sendButton;
    public ComboBox stationComboBox;
    public TextField urlTextField = new TextField();
    public Label selectedStationLabel;

    private TextField responseCode = new TextField();
    private TextField responseContent = new TextField();
    public TextField rfidCheckAccessTextField;

    private Station selectedStation;

    private void setResponseCodeText(String responseCode) {
        this.responseCode.setText(responseCode);
    }

    private void setResponseContentText(String responseContent) {
        this.responseContent.setText(responseContent);
    }

    private String getUrlTextField() {
        return this.urlTextField.getText();
    }
    public TextField pinCheckAccessTextField;
    private StationsList stationsList = new StationsList();

    public static void createDialogERR() {
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Error");
        dialog.setContentText("Empty or invalid arguments!");
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        dialog.show();
    }

    public static void createDialogOK() {
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Granted");
        dialog.setContentText("Access granted!");
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        dialog.show();
    }

    public static void createDialogNO() {
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Denied");
        dialog.setContentText("Access denied!");
        dialog.getDialogPane().getButtonTypes().add(loginButtonType);
        dialog.show();
    }

    @FXML
    private void initialize() throws InterruptedException {
        this.responseCode.textProperty().bindBidirectional(this.responseCodeLabel.textProperty());
        this.responseContent.textProperty().bindBidirectional(this.responseContentLabel.textProperty());

        JSONArray data=new JSONArray();
        try {
            data = JSONLoader.parseArray(HttpsRequest.httpGetRequestWithResponse("https://192.168.1.103/api/v1/station/all?userToken=x5px4jmfcap0dpo9"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (JSONObject loadedStation:(Iterable<JSONObject>)data) {
            if(((String) loadedStation.get("apiToken")).equals("")){
                continue;
            }
            Station newStation= new Station((String) loadedStation.get("apiToken"),"192.168.1.103", (String) loadedStation.get("name"));
            this.stationsList.addStation(newStation);
        }

        stationComboBox.setItems(FXCollections.observableArrayList(this.stationsList.getStations()));

        Callback<ListView<Station>, ListCell<Station>> cellFactory = new Callback<ListView<Station>, ListCell<Station>>() {

            @Override
            public ListCell<Station> call(ListView<Station> l) {
                return new ListCell<Station>() {

                    @Override
                    protected void updateItem(Station item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        };

        stationComboBox.setCellFactory(cellFactory);
        stationComboBox.setButtonCell(cellFactory.call(null));
        stationComboBox.getSelectionModel().select(0);
        stationComboBoxAction(null);
        this.selectedStation=(Station)this.stationComboBox.getSelectionModel().getSelectedItem();
    }

    public void stationComboBoxAction(ActionEvent event){
        Platform.runLater(() -> this.selectedStationLabel.textProperty().setValue("Selected station:  "+((Station)this.stationComboBox.getSelectionModel().getSelectedItem()).getName()));
        this.selectedStation=(Station)this.stationComboBox.getSelectionModel().getSelectedItem();
    }

    /**
     * Disable UI elements while performing async HTTP request
     *
     * @param value False - Enable elements, True - Disable elements
     */
    private void disableElements(Boolean value) {
        urlTextField.setDisable(value);
        sendButton.setDisable(value);
    }

    public void sendRequest() {

        disableElements(true);

        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    CloseableHttpResponse httpResponse = HttpsRequest.httpGetRequest(getUrlTextField());
                    Platform.runLater(() -> {
                        setResponseCodeText(httpResponse.getStatusLine().toString());
                        try {
                            setResponseContentText(HttpsRequest.getString(httpResponse));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
                return true;
            }
        };
        task.setOnSucceeded(event -> {
            disableElements(false);
        });
        task.setOnFailed(event -> {
            disableElements(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    public void checkAccess() {
        Pair<Boolean, String> result = this.selectedStation.checkAccess(rfidCheckAccessTextField.getText(), pinCheckAccessTextField.getText());
        if (result.getValue0()) {
            createDialogOK();
        } else {
            createDialogNO();
        }
        responseContent.setText(result.getValue1());
    }

    public void updateStationUsers()
    {
        Pair<Integer,String> result = this.selectedStation.updateStationsUser();
        responseContent.setText(result.getValue1());
    }

}
