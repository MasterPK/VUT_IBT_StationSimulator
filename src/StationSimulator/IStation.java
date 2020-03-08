package StationSimulator;

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

    void setIP(String id) throws IllegalArgumentException;
    String getIP();


    List<User> getUsers();
    void addUser(User user);
    User getUser(String rfid);

    void setUsers(List<User> users);
}
