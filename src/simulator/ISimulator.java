package simulator;

import javafx.collections.ObservableList;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Simulator is object that associates stations
 *
 */
public interface ISimulator {
    void addStation(Station station);
    void removeStation(Integer id);
    Station getStation(Integer id);
    ObservableList<Station> getStations();
}
