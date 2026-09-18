import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    //Tests the addsession method
    @Test
    void addSession(){
        MainGUI.Session test = new MainGUI.Session(1234,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.SessionList exp = MainGUI.addInOrder( test ,null);
        assertEquals(1234, exp.first().id());
    }
    //test the display session method
    @Test
    void displaySession(){
        MainGUI.Session test = new MainGUI.Session(1234,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.SessionList list = new MainGUI.SessionList(test,null);
        JTextArea area = new JTextArea();
        MainGUI.showSessions(list,area);
        assertEquals("ID = 1234 Title = whar Mentor = mr.dooks Date = 2026-09-01 Location = build 5 Max Participants = 15 Current Num = 2\n--------------------\n",area.getText() );

    }
    //test the search method- specifically id
    @Test
    void searchByID(){
        MainGUI.Session test = new MainGUI.Session(1234,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.SessionList list = new MainGUI.SessionList(test,null);
        MainGUI.Session exp = MainGUI.searchByID(list,1234);
        assertEquals(1234,exp.id());

    }
    //test the search by mentor method
    @Test
    void searchByMentor(){
        MainGUI.Session test = new MainGUI.Session(1234,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.Session test2 = new MainGUI.Session(1235,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.SessionList list = new MainGUI.SessionList(test,new MainGUI.SessionList(test2,null));

        MainGUI.SessionList exp = MainGUI.searchByMentor(list,"mr.dooks");
        assertEquals(1234,exp.first().id());
        assertEquals(1235,exp.rest().first().id());

    }
    //test remove session method
    @Test
    void removeSession(){
        MainGUI.Session test = new MainGUI.Session(1234,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.Session test2 = new MainGUI.Session(1235,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.SessionList list = new MainGUI.SessionList(test,new MainGUI.SessionList(test2,null));
        MainGUI.SessionList exp = MainGUI.remove(list,1234);
        assertEquals(1235,exp.rest().first().id());

    }
    @Test
    void register(){
        MainGUI.Session test = new MainGUI.Session(1234,"whar","mr.dooks","2026-09-01","build 5",15,2);
        MainGUI.Session test2 = new MainGUI.Session(1235,"whar","mr.dooks","2026-09-01","build 5",15,15);
        MainGUI.SessionList list = new MainGUI.SessionList(test,null);
        MainGUI.SessionList list2 = new MainGUI.SessionList(test2,null);
        MainGUI.SessionList exp = MainGUI.register(list,1234);
        MainGUI.SessionList exp2 = MainGUI.register(list2,1235);
        assertEquals(3,exp.first().curNum());
        assertEquals(15,exp2.first().curNum());

    }

}
