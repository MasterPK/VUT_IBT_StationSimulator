package StationSimulator;

import HttpClient.HttpsRequest;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Simulator implements ISimulator {
    private List<Station> stations;

    public Simulator() {
        stations = new ArrayList();
    }

    @Override
    public void createStation(Integer id) {
        stations.add(new Station(id));
    }

    @Override
    public void createStation(Integer id, String IP) {
        stations.add(new Station(id, IP));
    }

    @Override
    public void createStation(Integer id, String IP, List<User> users) {
        stations.add(new Station(id, IP, users));
    }

    @Override
    public void removeStation(Integer id) {

    }

    @Override
    public String executeRequest(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return HttpsRequest.httpGetRequestWithResponse(url);
    }

    @Override
    public Station getStation(Integer id) {
        for (Station station : stations) {
            if(station.getId().equals(id)) {
                return station;
            }
        }
        return null;
    }

    @Override
    public void updateStationsUser(Integer stationId) {

    }
}
