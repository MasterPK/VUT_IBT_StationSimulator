package simulator;

import org.javatuples.*;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface IStation {
    /**
     * Sets station id
     *
     * @param id id to be set
     * @throws IllegalArgumentException when wrong argument
     */
    void setId(Integer id) throws IllegalArgumentException;

    /**
     * Get station id
     *
     * @return Station id
     */
    Integer getId();

    String getStationIP();

    void setStationIP(String id) throws IllegalArgumentException;


    List<User> getUsers();
    void addUser(User user);
    User getUser(String rfid);

    void setUsers(List<User> users);

    boolean checkAccess(String rfid);

    public Pair<Boolean,String> checkAccess(String rfid, String pin);
    public Pair<Integer, String> updateStationsUser();
}
