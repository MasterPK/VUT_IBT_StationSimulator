package simulator;

import http.HttpsRequest;
import org.javatuples.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class Station {

    private final String protocol="https";
    private List<User> users = new ArrayList<>();
    public String serverIP;
    private String token;
    private String name;

    public Station(String token, String serverIP, String name) {
        this.serverIP = serverIP;
        this.token=token;
        this.name=name;
        this.updateStationsUser();
    }


    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return this.users;
    }

    public void addUser(User user) {
        this.users.add(user);
    }

    public User getUser(String rfid) {
        List<User> list = getUsers();
        for (User user : list) {
            if (user.getRfid().equals(rfid)) {
                return user;
            }
        }
        return null;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

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

    private String sendAccessLog(String rfid, boolean granted) {
        String request = this.protocol + "://" + this.serverIP + "/api/save-access?token=" + this.token + "&user_rfid=" + rfid + "&status=" + (granted ? 1 : 0);
        return HttpsRequest.httpGetRequestWithResponse(request);
    }

    public Pair<Boolean, String> checkAccess(String rfid, String pin) {
        Pair<Boolean, String> result = new Pair<Boolean, String>(Boolean.FALSE, "");
        User user = getUser(rfid);
        if (user == null) {
            result=result.setAt0(false);
            result=result.setAt1(sendAccessLog(rfid, result.getValue0()));
            return result;
        }
        try {

            if (user.getPermission() == 1) {
                result=result.setAt0(true);
            }

            if ((user.getPermission() == 2 || user.getPermission() == 3) && user.getPin().equals(pin)) {
                result=result.setAt0(true);
            }
        } catch (Exception ignored) {
            result=result.setAt0(false);
        }

        result=result.setAt1(sendAccessLog(rfid, result.getValue0()));
        return result;

    }

    public Pair<Integer, String> updateStationsUser() {
        String request = this.protocol + "://" + this.serverIP + "/api/get-users-v2?token=" + this.token;
        Pair<String,String> response = null;
        try {
            response = HttpsRequest.httpGetRequestWithResponse(request,false);

            JSONParser jsonParser = new JSONParser();
            assert response != null;
            JSONObject data = (JSONObject) jsonParser.parse(response.getValue1());
            JSONObject message = (JSONObject) data.get("m");
            JSONArray users = (JSONArray) message.get("u");
            for (JSONArray user:(Iterable<JSONArray>) users) {
                User newUser;
                try{
                    newUser=new User((String) user.get(0), (long) user.get(1), (String) user.get(2));
                }catch (Exception e){
                    newUser=new User((String) user.get(0),(long)user.get(1));
                }
                addUser(newUser);
            }
            return new Pair<>(0,response.getValue1());
        } catch (Exception e) {
            e.printStackTrace();
            return new Pair<>(1,"ERR");
        }
    }
}
