package sample;

import http.HttpsRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.apache.http.client.methods.CloseableHttpResponse;
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

    private TextField responseCode = new TextField();
    private TextField responseContent = new TextField();
    private Simulator simulator = new Simulator("","");

    private void setResponseCodeText(String responseCode) {
        this.responseCode.setText(responseCode);
    }

    private void setResponseContentText(String responseContent) {
        this.responseContent.setText(responseContent);
    }

    private String getUrlTextField() {
        return this.urlTextField.getText();
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
    }

    /**
     * Disable UI elements while performing async HTTP request
     * @param value False - Enable elements, True - Disable elements
     */
    private void disableElements(Boolean value)
    {
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
                    Platform.runLater(()->{
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
        task.setOnSucceeded( event -> {
            disableElements(false);
        });
        task.setOnFailed( event -> {
            disableElements(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    public void createStation()
    {
        try
        {
            Station station = new Station(protocolComboBox.getValue().toString(),ipTextArea.getText(),Integer.parseInt(stationIdTextArea.getText()));
            simulator.addStation(station);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }



    }

}
