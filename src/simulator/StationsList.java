package simulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class StationsList {
    private ArrayList<Station> stations =new ArrayList<>();

    public void addStation(Station station) {
        if (station == null)
            throw new IllegalArgumentException();
        stations.add(station);
    }

    public Station getStation(String token) {
        for (Station station : stations) {
            if (station.getToken().equals(token)) {
                return station;
            }
        }
        return null;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }
}
