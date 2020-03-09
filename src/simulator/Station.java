package simulator;

import http.HttpsRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Station implements IStation {

    private Integer id;
    public String protocol;
    private List<User> users;
    public String serverIP;
    private String stationIP;

    public Station(String protocol,String serverIP, Integer id) {
        this.protocol=protocol;
        this.serverIP=serverIP;
        setId(id);
        setStationIP(new String());
        setUsers(new ArrayList());
    }

    public Station(String protocol,String serverIP,Integer id, String stationIP) {
        this.protocol=protocol;
        this.serverIP=serverIP;
        setId(id);
        setStationIP(stationIP);
        setUsers(new ArrayList());
    }

    /*public Station(String protocol,String serverIP,Integer id, String stationIP, List<User> users) {
        this.protocol=protocol;
        this.serverIP=serverIP;
        setId(id);
        setStationIP(stationIP);
        setUsers(users);
    }*/

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
    public String getStationIP() {
        return this.stationIP;
    }

    @Override
    public void setStationIP(String stationIP) {
        if (stationIP == null)
            throw new IllegalArgumentException();
        this.stationIP = stationIP;
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

    public boolean checkAccess(String rfid)
    {
        User user = getUser(rfid);
        if(user==null)
            return false;

        if(user.getPermission()==1)
            return true;

        return false;
    }

    private String executeRequest(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        return HttpsRequest.httpGetRequestWithResponse(url);
    }

    @Override
    public boolean checkAccess(String rfid, String pin)
    {
        User user = getUser(rfid);
        if(user==null)
            return false;

        if(user.getPermission()==1)
            return true;

        if((user.getPermission()==2 || user.getPermission()==3) && user.getPin().equals(pin))
            return true;

        return false;

    }

    @Override
    public void updateStationsUser() {
        String request = this.protocol + "://" + this.serverIP + "/api/station/get-users?id_station=" + getId();
        String response = null;
        try {
            response = HttpsRequest.httpGetRequestWithResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        JSONObject jsonParser = new JSONObject(response);
        JSONArray users = jsonParser.getJSONObject("m").getJSONArray("u");
        List<User> usersList = new ArrayList<>();
        for (Object user : users) {
            User userL = new User(((JSONObject) user).getString("r"), ((JSONObject) user).getInt("p"));
            String pin;
            try {
                pin = ((JSONObject) user).getString("i");
                userL.setPin(pin);
            } catch (JSONException e) {

            }
            usersList.add(userL);
        }
        setUsers(usersList);
    }
}
