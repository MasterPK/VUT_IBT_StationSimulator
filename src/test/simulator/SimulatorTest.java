package simulator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SimulatorTest {

    @BeforeEach
    void setUp() {
    }

    /*@Test
    void createStation() {
        Simulator simulator = new Simulator("https","192.168.1.11");
        simulator.createStation(1);
        assertNotEquals(null,simulator.getStations());
        assertEquals(1,simulator.getStation(1).getId());

    }

    @Test
    void updateUsersId() {
        Simulator simulator = new Simulator("https","192.168.1.11");
        simulator.createStation(1);
        try {
            simulator.updateStationsUser(1);
        }catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
        assertNotEquals(null,simulator.getStation(1).getUsers().get(0).getRfid());

    }

    @Test
    void updateUsersObject() {
        Simulator simulator = new Simulator("https","192.168.1.11");
        simulator.createStation(1);
        try {
            simulator.updateStationsUser(simulator.getStation(1));
        }catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
        assertNotEquals(null,simulator.getStation(1).getUsers().get(0).getRfid());
    }

    @Test
    void rfidAccessNo(String rfid, Integer station)
    {
        Simulator simulator = new Simulator("https","192.168.1.11");
        simulator.createStation(1);
        try {
            simulator.updateStationsUser(simulator.getStation(1));
        }catch (Exception e)
        {
            e.printStackTrace(System.err);
        }
        assertNotEquals(null,simulator.getStation(1).getUsers().get(0).getRfid());
    }*/

    @Test
    void complexTest1()
    {
        Station station1=new Station("https","192.168.1.11",1,"192.168.1.XX");
        station1.updateStationsUser();
        assertTrue(station1.checkAccess("3aec7415"));
        assertFalse(station1.checkAccess("c92bb399"));
        /*assertFalse(station1.checkAccess("c92bb399","123"));
        assertTrue(station1.checkAccess("c92bb399","1234"));*/
    }
}