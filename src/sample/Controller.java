package sample;

import http.HttpsRequest;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.javatuples.Pair;
import simulator.Simulator;
import simulator.Station;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public VBox responseVBox;
    public Label responseCodeLabel;
    public TextArea responseContentLabel;
    public Button sendButton;
    public ComboBox protocolComboBox;
    public TextField ipTextArea;
    public TextField stationIdTextArea;
    public TextField urlTextField = new TextField();
    public ListView<Station> listView;
    public Label selectedStationLabel;

    private TextField responseCode = new TextField();
    private TextField responseContent = new TextField();
    public TextField rfidCheckAccessTextField;

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
    private Simulator simulator = new Simulator("", "");

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
        /*slider1.valueProperty().addListener((observableValue, oldValue, newValue) ->
        {
            if (newValue == null) {
                return;
            }
            Integer tmpI = newValue.intValue();
            value.setText(tmpI.toString());

        });*/
        List<String> list = new ArrayList<String>();
        list.add("HTTP");
        list.add("HTTPS");
        ObservableList<String> protocolList = FXCollections.observableList(list);
        protocolComboBox.setItems(protocolList);
        listView.setItems(simulator.stations);
        listView.setCellFactory(param -> new ListCell<Station>() {
            @Override
            protected void updateItem(Station item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId().toString());
                }
            }
        });
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Station> stations = listView.getSelectionModel().getSelectedItems();

        StringProperty stationIdProperty = new SimpleStringProperty("No station selected!");

        stations.addListener(new ListChangeListener<Station>() {
            @Override
            public void onChanged(Change<? extends Station> c) {
                if (c.getList().isEmpty()) {
                    stationIdProperty.setValue("No station selected!");
                } else if (c.getList().size() > 1) {
                    stationIdProperty.setValue("Error: Multiple stations selected!");
                } else {
                    stationIdProperty.setValue(c.getList().get(0).getId().toString());
                }
            }
        });
        selectedStationLabel.textProperty().bindBidirectional(stationIdProperty);

        Station station = new Station("https","192.168.1.11",1);
        simulator.addStation(station);
        station = new Station("https","192.168.1.11",2);
        simulator.addStation(station);
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

    public void createStation() {
        try {
            Station station = new Station(protocolComboBox.getValue().toString(), ipTextArea.getText(), Integer.parseInt(stationIdTextArea.getText()));
            simulator.addStation(station);
            ipTextArea.clear();
            stationIdTextArea.clear();
        } catch (Exception e) {
            createDialogERR();
            return;
        }
    }

    public void checkAccess() {
        Station station = simulator.getStation(Integer.parseInt(selectedStationLabel.getText()));
        Pair<Boolean, String> result = station.checkAccess(rfidCheckAccessTextField.getText(), pinCheckAccessTextField.getText());
        if (result.getValue0()) {
            createDialogOK();
        } else {
            createDialogNO();
        }
        responseContent.setText(result.getValue1());
    }

    public void updateStationUsers()
    {
        Station station = simulator.getStation(Integer.parseInt(selectedStationLabel.getText()));
        Pair<Integer,String> result = station.updateStationsUser();
        responseContent.setText(result.getValue1());
    }

}
