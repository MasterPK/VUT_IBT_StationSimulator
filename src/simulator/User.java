package simulator;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String rfid;
    private Integer permission;
    private String pin;

    public User(String rfid, Integer permission)
    {
        setRfid(rfid);
        setPermission(permission);
        setPin(new String());
    }
    public User(String rfid, Integer permission, String pin)
    {
        setRfid(rfid);
        setPermission(permission);
        setPin(pin);
    }

    public String getPin() {
        return pin;
    }

    public String getRfid() {
        return rfid;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public List<Object> getAll()
    {
        List<Object> list = new ArrayList();
        list.add(rfid);
        list.add(permission);
        list.add(pin);
        return list;
    }
}
