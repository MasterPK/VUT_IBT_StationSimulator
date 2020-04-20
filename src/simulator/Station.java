package simulator;

import http.HttpsRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.javatuples.*;

import java.util.ArrayList;
import java.util.List;

public class Station implements IStation {

    private Integer id;
    public String protocol;
    private List<User> users;
    public String serverIP;
    private String stationIP;

    public Station(String protocol, String serverIP, Integer id) {
        this.protocol = protocol;
        this.serverIP = serverIP;
        setId(id);
        setStationIP(new String());
        setUsers(new ArrayList());
    }


    public Station(String protocol, String serverIP, Integer id, String stationIP) {
        this.protocol = protocol;
        this.serverIP = serverIP;
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
            if (user.getRfid().equals(rfid)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean checkAccess(String rfid) {
        User user = getUser(rfid);
        if (user == null) {
            sendAccessLog(rfid, false);
            return false;
        }

        if (user.getPermission() == 1) {
            sendAccessLog(rfid, true);
            return true;
        }
        sendAccessLog(rfid, false);
        return false;
    }

    private String executeRequest(String url) {
        return HttpsRequest.httpGetRequestWithResponse(url);
    }

    private String sendAccessLog(String rfid, boolean granted) {
        String request = this.protocol + "://" + this.serverIP + "/api/station/save-access?id_station=" + this.getId() + "&user_rfid=" + rfid + "&status=" + (granted ? 1 : 0);
        return HttpsRequest.httpGetRequestWithResponse(request);
    }

    @Override
    public Pair<Boolean, String> checkAccess(String rfid, String pin) {
        Pair<Boolean, String> result = new Pair<Boolean, String>(new Boolean(false), new String());
        User user = getUser(rfid);
        try {
            if (user == null) {

            }
            if (user.getPermission() == 1) {
                result=result.setAt0(true);
            }

            if ((user.getPermission() == 2 || user.getPermission() == 3) && user.getPin().equals(pin)) {
                result=result.setAt0(true);
            }
        } catch (Exception e) {
        }

        result=result.setAt1(sendAccessLog(rfid, result.getValue0()));
        return result;

    }

    @Override
    public Pair<Integer, String> updateStationsUser() {
        String request = this.protocol + "://" + this.serverIP + "/api/station/get-users?id_station=" + getId();
        Pair<String,String> response = null;
        try {
            response = HttpsRequest.httpGetRequestWithResponse(request,false);
            JSONObject jsonParser = new JSONObject(response.getValue1());
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
            return new Pair<>(0,response.getValue1());
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(1,"ERR");
        }
    }
}
