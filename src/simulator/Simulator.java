package simulator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Simulator implements ISimulator {
    public ObservableList<Station> stations = FXCollections.observableList(new ArrayList<>());
    public String protocol;
    public String IP;

    public Simulator(String protocol, String IP) {
        this.protocol = protocol;
        this.IP = IP;
    }


    //TODO duplicate
    @Override
    public void addStation(Station station) {
        if (station == null)
            throw new IllegalArgumentException();
        stations.add(station);
    }

    @Override
    public void removeStation(Integer id) {
        Station station = getStation(id);
        if (station == null)
            return;
        stations.remove(station);
    }

    @Override
    public Station getStation(Integer id) {
        for (Station station : stations) {
            if (station.getId().equals(id)) {
                return station;
            }
        }
        return null;
    }

    @Override
    public ObservableList<Station> getStations() {
        if (stations.isEmpty())
            return null;
        return stations;
    }


    public ObservableList<String> getStationsToString() {
        //TODO
        /*List<String> list = new ArrayList<>();
        for (Station station : stations) {

        }
        ObservableList<String> list = FXCollections.observableList(stations);*/
        return null;
    }

    /*@Override
    public void updateStationsUser(Integer stationId) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        Station station = getStation(stationId);
        if(station==null)
            throw new IllegalArgumentException();
        updateStationsUser(station);

    }

    @Override
    public void updateStationsUser(Station station) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        String request = protocol+"://"+IP+"/api/station/get-users?id_station="+station.getId();
        String response = HttpsRequest.httpGetRequestWithResponse(request);
        JSONObject jsonParser = new JSONObject(response);
        JSONArray users = jsonParser.getJSONObject("m").getJSONArray("u");
        List<User> usersList = new ArrayList<>();
        for (Object user:users) {
            User userL = new User(((JSONObject)user).getString("r"),((JSONObject)user).getInt("p"));
            String pin;
            try{
                pin = ((JSONObject)user).getString("i");
                userL.setPin(pin);
            }catch (JSONException e)
            {

            }
            usersList.add(userL);
        }
        station.setUsers(usersList);
    }*/


}
