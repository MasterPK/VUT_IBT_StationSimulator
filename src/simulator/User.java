package simulator;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String rfid;
    private long permission;
    private String pin;

    public User(String rfid, long permission)
    {
        setRfid(rfid);
        setPermission(permission);
        setPin("");
    }
    public User(String rfid, long permission, String pin)
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

    public long getPermission() {
        return permission;
    }

    public void setPermission(long permission) {
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
