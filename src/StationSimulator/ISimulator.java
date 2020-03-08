package StationSimulator;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface ISimulator {
    void createStation(Integer id);
    void createStation(Integer id, String IP);
    void createStation(Integer id, String IP, List<User> users);
    void removeStation(Integer id);
    Station getStation(Integer id);

    String executeRequest(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException;
    void updateStationsUser(Integer stationId);
}
