import javax.swing.*;
import java.awt.*;
import java.util.List;


public class MainGUI extends JFrame {
    private JTextField idField;
    private JTextField titleField;
    private JTextField mentorField;
    private JTextField dateField;
    private JTextField locationField;
    private JTextField maxField;
    private JTextField curField;
    private JTextArea outputArea;

    //WE ASSUMED THAT DATE FOLLOWS THE FORMAT AND DIDN'T CHECK FOR IT
    // there should be a private member variable named `sessions` :
    record Session(int id, String title, String mentor, String date, String location, int maxNum, int curNum) {};
    record SessionList(Session first, SessionList rest) {};
    //SessionList sessions = new SessionList(null, null);
    private SessionList sessions;

    // the constructor for the class. This will initialize
    // the class's member variables:
    public MainGUI() {
        // set sessions to a new empty list:
        sessions = null;
        //SessionList sessions = new SessionList()
        setTitle("Employee Mentorship and Inclusion Manager");
        setSize(600, 600);
        // when this frame/window closes, halt the whole program:
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createGUI();
        setVisible(true);
    }


    // Create all of the display elements in the frame:
    private void createGUI() {
        // first, the input panel contains all of the field entry elements:
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8,2,5,5));
        // these are all of the input fields that will be in the frame:
        idField = new JTextField();
        titleField = new JTextField();
        mentorField = new JTextField();
        dateField = new JTextField();
        locationField = new JTextField();
        maxField = new JTextField();
        curField = new JTextField();
        inputPanel.add(new JLabel("Session ID"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Title"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Mentor"));
        inputPanel.add(mentorField);
        inputPanel.add(new JLabel("Date"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Location"));
        inputPanel.add(locationField);
        inputPanel.add(new JLabel("Max Participants"));
        inputPanel.add(maxField);
        inputPanel.add(new JLabel("Current Participants"));
        inputPanel.add(curField);
        add(inputPanel, BorderLayout.NORTH);

        // next, the lower half of the window contains an output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        add(scroll, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Session");
        JButton displayButton = new JButton("Display");
        JButton searchButton = new JButton("Search");
        JButton removeButton = new JButton("Remove");
        JButton registerButton = new JButton("Register");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(addButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addSession());
        displayButton.addActionListener(e -> displaySessions(sessions));
        searchButton.addActionListener(e -> searchSession());
        removeButton.addActionListener(e -> removeSession());
        registerButton.addActionListener(e -> registerParticipant());
        exitButton.addActionListener(e -> System.exit(0));
    }

    // set all input fields to empty strings, give focus to the first
    private void clearFields() {
        idField.setText("");
        titleField.setText("");
        mentorField.setText("");
        dateField.setText("");
        locationField.setText("");
        maxField.setText("");
        curField.setText("");        // Put the cursor back in the first field
        idField.requestFocus();
    }


    public static boolean comesBefore(String date1, String date2){
        if(Integer.parseInt(date1.substring(0,4)) != Integer.parseInt(date2.substring(0,4))){
            return Integer.parseInt(date1.substring(0,4)) < Integer.parseInt(date2.substring(0,4));
        }
        if(Integer.parseInt(date1.substring(5,7)) != Integer.parseInt(date2.substring(5,7))){
            return Integer.parseInt(date1.substring(5,7)) < Integer.parseInt(date2.substring(5,7));
        }
        return Integer.parseInt(date1.substring(8)) <= Integer.parseInt(date2.substring(8));
    }

    public static SessionList addToEnd(SessionList s1, Session end){
        return switch(s1){
            case null -> new SessionList(end, null);
            case SessionList(Session f, SessionList r) ->
                    new SessionList(f, addToEnd(r, end));

        };
    }


    public static SessionList addToFront(SessionList s1, Session front){
        return  new SessionList(front, s1);
    }


    //need to figure out how to create new list
    public static SessionList addInOrder(Session s, SessionList s1){
        return switch(s1){
            case null -> addToEnd(s1, s);
            case SessionList(Session f, SessionList r) -> {
                if(comesBefore(s1.first.date, s.date)){
                    yield new SessionList(s1.first, addInOrder(s, s1.rest));
                } else{
                    yield addToFront(s1, s);
                }}
        };
    }


    // the action of the Add Session button
    private void addSession() {
        try {
            int id = Integer.parseInt(idField.getText());
            String title = titleField.getText();
            String mentor = mentorField.getText();
            String date = dateField.getText();
            String location = locationField.getText();
            int maxParticipants = Integer.parseInt(maxField.getText());
            int curNum = Integer.parseInt(curField.getText());

            // TO DO: construct a session object, insert it into
            // the list of sessions
            //record SessionList(Session first, SessionList rest) {};

            Session s1 = new Session(id, title, mentor, date, location, maxParticipants, curNum);
            //need to add in correct spot
            if(searchByID(sessions, s1.id)!=null){
                outputArea.setText("Duplicate id");
            }
            else{
                sessions = addInOrder(s1, sessions);
                outputArea.setText("Session Added Successfully\n");
                // Clear the input fields
                clearFields();
            }
        }
        catch(Exception e) {
            outputArea.setText("Invalid input");
        }
    }

    public static void showSessions(SessionList s1, JTextArea outputArea){
        switch (s1) {
            case null ->  outputArea.append("");
            case SessionList(Session f, SessionList r) ->
            {
                //automatically casts f.id to string
                outputArea.append("ID = " + f.id + " Title = " + f.title + " Mentor = " +
                        f.mentor + " Date = " + f.date + " Location = " + f.location
                        + " Max Participants = " +  f.maxNum + " Current Num = " + f.curNum + "\n--------------------\n");
                showSessions(r, outputArea);
            }
        }
    }

    // display all sessions in the output area
    private void displaySessions(SessionList s1) {
        outputArea.setText("");
        //sessions.forEach((n) -> {outputArea.append(n+ "/n");});
        showSessions(s1, outputArea);

        // iterate over sessions; display each one
        // to the output window, using the `append`
        // method of the outputArea.

        // between each one, print a separator line,
        // as e.g.
    }


    public static Session searchByID(SessionList s1, int id){
        return switch(s1){
            case null -> null;
            case SessionList(Session f, SessionList r) -> {
                if(f.id==id){
                    yield new Session(f.id, f.title, f.mentor, f.date, f.location, f.maxNum, f.curNum);
                } else{
                    yield searchByID(r, id);
                }}
            };
        }

    public static SessionList searchByMentor(SessionList s1, String mentor){
        return switch(s1){
            case null -> null;
            case SessionList(Session f, SessionList r) -> {
                if(f.mentor.equals(mentor)){
                    yield new SessionList(f, searchByMentor(r, mentor));
                } else{
                    yield searchByMentor(r, mentor);
                }}
        };
    }


    // search by ID if presesnt, mentor otherwise, display results
    private void searchSession() {
        // Search by ID if the ID field is not empty
        if (!idField.getText().trim().isEmpty()) {
            int id = Integer.parseInt(idField.getText().trim());
            // find session by ID, using a `searchByID` method
            Session result = searchByID(sessions, id);
             if (result != null) {
                 // display session to the output area...
                 outputArea.append("ID = " + result.id + " Title = " + result.title + " Mentor = " +
                         result.mentor + " Date = " + result.date + " Location = " + result.location
                         + " Max Participants = " + result.maxNum + " Current Num = " + result.curNum + "\n--------------------\n");
             }
            else {
                 outputArea.setText("Session not found.");
             }
        }
        // Otherwise, search by mentor if the Mentor field is not empty
        else if (!mentorField.getText().trim().isEmpty()) {
            String mentor = mentorField.getText().trim();
            // find session by mentor. In this case, the result
            // may be a list of sessions...
            // ... code here ...
            SessionList result = searchByMentor(sessions, mentor);
            if (result != null) {
                // display all sessions in the list
                displaySessions(result);
            }
            else {
                outputArea.setText("No session found for mentor: " + mentor);
            }
        }
        // Nothing entered
        else {
            outputArea.setText("Please enter a Session ID or Mentor name.");
        }
    }

    public static SessionList remove(SessionList s1, int id){
        return switch(s1){
            case null -> null;
            case SessionList(Session f, SessionList r) -> {
                if(f.id==id){
                    yield new SessionList(null, r);
                } else{
                    yield new SessionList(f, r);
                }}
        };
    }

    //this removes a session by its id in a list of session

    // given an id, remove that session from the list
    private void removeSession() {
        int id = Integer.parseInt(idField.getText());
        // remove the session, print an error to the outputArea
        // if it's not found
        if(searchByID(sessions, id)==null){
            outputArea.setText("No session with that id");
        }
        else{
            sessions = remove(sessions, id);
            outputArea.setText("removed the session");
        }
        // ... code here ...
    }

    public static SessionList register(SessionList s1, int id) {
        return switch (s1) {
            case null -> null;
            case SessionList(Session f, SessionList r) -> {
                if (f.id != id) {
                    yield new SessionList(f, register(r, id));
                } else if (f.curNum < f.maxNum) {
                    Session updated = new Session(f.id, f.title, f.mentor, f.date,
                            f.location, f.maxNum, f.curNum + 1);
                    yield new SessionList(updated, r);
                } else {
                    yield new SessionList(f, r);
                }

            }
        };
    }



    // add one to the count of the specified session.
    // MUTATES participant count of session.
    private void registerParticipant() {
        int id = Integer.parseInt(idField.getText());
        if(searchByID(sessions, id)==null){
            outputArea.setText("No session with that id");
        }
        else if(searchByID(sessions, id).curNum >= searchByID(sessions, id).maxNum){
            outputArea.setText("fail, class full");
        }
        else{
            outputArea.setText("success");
            sessions = register(sessions, id);
        }

        // increment participants field of session,
        // print success or failure message.
    }



    public static void main(String[] args) {
        new MainGUI();
    }
}
