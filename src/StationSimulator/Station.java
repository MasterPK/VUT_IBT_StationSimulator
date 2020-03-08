package StationSimulator;

import java.util.ArrayList;
import java.util.List;

public class Station implements IStation {

    private Integer id;
    private String IP;
    private List<User> users;

    public Station(Integer id) {
        setId(id);
        setIP(new String());
        setUsers(new ArrayList());
    }

    public Station(Integer id, String IP) {
        setId(id);
        setIP(IP);
        setUsers(new ArrayList());
    }

    public Station(Integer id, String IP, List<User> users) {
        setId(id);
        setIP(IP);
        setUsers(users);
    }

    /**
     * Sets station id
     *
     * @param id id to be set
     * @throws IllegalArgumentException when wrong argument
     */
    @Override
    public void setId(Integer id) throws IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException();
        this.id = id;
    }

    /**
     * Get station id
     *
     * @return Station id
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public String getIP() {
        return this.IP;
    }

    @Override
    public void setIP(String IP) {
        if (IP == null)
            throw new IllegalArgumentException();
        this.IP = IP;
    }

    @Override
    public List<User> getUsers() {
        return this.users;
    }

    @Override
    public void addUser(User user) {
        getUsers().add(user);
    }

    @Override
    public User getUser(String rfid) {
        List<User> list = getUsers();
        for (User user : list) {
            if(user.getRfid().equals(rfid))
            {
                return user;
            }
        }
        return null;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
