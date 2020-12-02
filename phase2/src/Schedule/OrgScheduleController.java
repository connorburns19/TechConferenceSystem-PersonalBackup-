package Schedule;

import UserLogin.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A controller class describing the actions an organizer can perform in the program
 */
public class OrgScheduleController extends UserScheduleController {
    OrgSchedulePresenter orgSchedulePresenter;

    /**
     * Creates a new controller for the organizer.
     * @param eventManager The talkManager.
     * @param mainMenuController The mainMenuController.
     * @param scanner The scanner.
     */
    public OrgScheduleController(String email, EventManager eventManager, UserStorage userStorage,
                                 MainMenuController mainMenuController, RoomStorage roomStorage, Scanner scanner){
        super(email, eventManager, userStorage, mainMenuController, roomStorage, scanner);
        orgSchedulePresenter = new OrgSchedulePresenter();
    }

    /**
     * Allows the organizer to choose a speaker from a list of speakers.
     * @param scan The scanner.
     * @return Returns a Speaker representing the speaker chosen by the organizer.
     */

    public ArrayList<String> pickSpeakers(Scanner scan){
        orgSchedulePresenter.printAllSpeakers(userStorage.getSpeakerNameList());
        System.out.println("Enter the number of speakers");
        ArrayList<String>chosenSpeakers = new ArrayList<>();
        boolean doContinue = true;
        while(doContinue){
            int numberOfSpeakers = scan.nextInt();
            while (numberOfSpeakers > userStorage.getSpeakerNameList().size()){
                numberOfSpeakers = scan.nextInt();
            }
            System.out.println("Number of speakers chosen: " + numberOfSpeakers);
            while(chosenSpeakers.size() < numberOfSpeakers){
                orgSchedulePresenter.printMenu(8);
                String choice = scan.nextLine();
                try { int speakerIndex = Integer.parseInt(choice);
                    if (speakerIndex == 0){
                        orgSchedulePresenter.printMenu(16);
                        return null;
                    }
                    else if (speakerIndex -1 >= userStorage.getSpeakerEmailList().size()){
                        orgSchedulePresenter.printMenu(14);
                    }
                    else{
                        String chosenSpeaker = userStorage.getSpeakerEmailList().get(speakerIndex - 1);
                        if (!(chosenSpeakers.contains(chosenSpeaker))){
                            orgSchedulePresenter.printSchedule(
                                    userStorage.emailToTalkList(chosenSpeaker), eventManager, 2);
                            chosenSpeakers.add(chosenSpeaker);
                            System.out.println("Speaker added");
                        }
                    }}catch (NumberFormatException nfe){
                    presenter.printMenu(8);}
            }
            return chosenSpeakers;
        }
        return null;
    }
    /**
     * Allows the organizer to choose a room from a list of rooms.
     * @param scan The scanner.
     * @return Returns the name of the room chosen by the organizer.
     */
    //NATHAN NOV 28 (made this reference the RoomNameList instead of RoomList throughout)

    public String pickRoom(Scanner scan) {
        // first they pick a speaker, then they pick a room, then they pick a time and check if it works
        orgSchedulePresenter.printAllRooms(roomStorage.getRoomNameList());
        orgSchedulePresenter.printMenu(7);
        boolean doContinue  = true;
        while (doContinue){
            String choice = scan.nextLine();
            try { int roomIndex = Integer.parseInt(choice);
            if (roomIndex == 0){
                orgSchedulePresenter.printMenu(16); //return to main menu
                return null;
            }
            else if (roomIndex -1 >= roomStorage.getRoomNameList().size()){
                orgSchedulePresenter.printMenu(12); //invalid input
            }
            else{
                String chosenRoom = roomStorage.getRoomNameList().get(roomIndex - 1);
                orgSchedulePresenter.printSchedule(
                        roomStorage.roomNameToEventIds(chosenRoom), eventManager, 1);
                return chosenRoom;
            }} catch (NumberFormatException nfe){
            presenter.printMenu(8);}}
    return null;}

    /**
     * Allows the organizer to choose a day - in this case our conference is three-days long.
     * @param scan The scanner.
     * @return Returns a int representing the day chosen by the organizer.
     */
    public Integer pickDay(Scanner scan){
        boolean doContinue  = true;
        while(doContinue){
            try {orgSchedulePresenter.PrintRequestTalkProcess(1);
                String dayChoice = scan.nextLine();
                int days = Integer.parseInt(dayChoice);
                if (days == 0){
                    orgSchedulePresenter.printMenu(16);
                    return null;
                }
                else if (days > 3 || days < 0){
                    orgSchedulePresenter.printMenu(18);
                }
                else {
                    return days;
                }}catch (NumberFormatException nfe){
                presenter.printMenu(8);}}
    return null;}

    /**
     * Allows the organizer to choose an hour between 9am and 5pm.
     * @param scan The scanner.
     * @return Returns a int representing the hour chosen by the organizer.
     */
    public Integer pickHour(Scanner scan){
        boolean doContinue = true;
        while(doContinue) {
            try{
                orgSchedulePresenter.PrintRequestTalkProcess(2);
                String hourChoice = scan.nextLine();
                int hours = Integer.parseInt(hourChoice);
                if (hours == 0){
                    orgSchedulePresenter.printMenu(16);
                    return null;
                }
                else if (hours > 16 || hours < 9) {
                    orgSchedulePresenter.printMenu(19);
                }
                else {
                    return hours;
                }}catch (NumberFormatException nfe){
                presenter.printMenu(8);}}
    return null;}

    /**
     * Allows the organizer to choose a day and hour for the start time of the talk.
     * @param scan The scanner.
     * @return A LocalDateTime representing the start time chosen by the organizer.
     */
    public LocalDateTime pickTime(Scanner scan) {
        // first they pick a speaker, then they pick a room, then they pick a time and check if it works
        Integer day = pickDay(scan);
        if (day == null){return null;}
        Integer hour = pickHour(scan);
        if(hour == null){return null;}
        LocalDateTime dateTime = LocalDateTime.of(2020, 11,20+day,hour,0);
        return dateTime;}

    /**
     * Check if the room and speaker are double-booked, or if just the speaker is double-booked, or if just the room
     * is double booked.
     * @param speaker The speaker.
     * @param room The room.
     * @param dateTime The start time.
     * @return An int representing one of the three aforementioned options.
     */
    public int checkDoubleBooking(String speaker, String room, LocalDateTime startTime, LocalDateTime endTime){
        //LocalDateTime end = dateTime.plusHours(1);
         if(!eventManager.checkDoubleBooking(startTime, endTime, userStorage.emailToTalkList(speaker))
                && !eventManager.checkDoubleBooking(startTime, endTime, roomStorage.roomNameToEventIds(room))){return 1;}
        else if(!eventManager.checkDoubleBooking(startTime, endTime, userStorage.emailToTalkList(speaker))){
            return 2;
        }
        else if(!eventManager.checkDoubleBooking(startTime, endTime, roomStorage.roomNameToEventIds(room))){return 3;}
        else{return 0;}
    }
    public boolean checkDoubleBookingSpeakers(ArrayList<String> speakers, String room, LocalDateTime startTime, LocalDateTime endTime){
        for (String s : speakers){
            if (!(checkDoubleBooking(s, room, startTime, endTime)==0)){
                return false;
            }
        }
        return true;
    }
    /**
     * Allows the organizer to create talk.
     * @param scan The scanner.
     * @return A boolean notifying the organizer that they have successfully created a talk.
     */
    public boolean requestEvent(Scanner scan){
        ArrayList<String> speakers = pickSpeakers(scan);
        if (speakers == null){ return false;}
        System.out.println(speakers.size()+" speakers chosen");
        String room = pickRoom(scan);
        if (room == null){return false;}
        LocalDateTime startTime = pickTime(scan);
        if (startTime==null){ return false;}
        LocalDateTime endTime = pickTime(scan);
        if (endTime == null){ return false;}
        if (speakers.size() == 0){
            String talkTitle = scan.nextLine();
            if (eventManager.createEvent(talkTitle, speakers, room, startTime, endTime,
                    "None")){
                orgSchedulePresenter.PrintRequestTalkProcess(7);
                return true;
            }
            else{return  false;}
        }
        boolean doubleBookingChecker = checkDoubleBookingSpeakers(speakers, room, startTime, endTime);
        while(!doubleBookingChecker){
            System.out.println("Invalid room or time. Please select new room and time");
            room = pickRoom(scan);
            if (room == null){return false;}
            startTime = pickTime(scan);
            if (startTime==null){ return false;}
            endTime = pickTime(scan);
            if (endTime == null){ return false;}
            doubleBookingChecker = checkDoubleBookingSpeakers(speakers, room, startTime, endTime);
        }
        System.out.println("Valid speakers, room and time for an event.");
        System.out.println("Enter the title of the event.");
        String talkTitle = scan.nextLine();
        if (eventManager.createEvent(talkTitle, speakers, room, startTime, endTime,
                "None")){
            System.out.println(talkTitle + " added successfully");
            return true;
        }
        else{return  false;}


    }

    /**
     * Allows the organizer to create a room with the specified name.
     * @param roomName The name of the room.
     * @return A boolean notifying the organizer if they have successfully created a room.
     */
    public boolean addRoom(String roomName) {
        return this.roomStorage.createRoom(roomName);
    }

    /**
     * Allows the organizer to create a speaker with the specified name, password, and email.
     * @param name The name of the speaker.
     * @param password The password of the speaker.
     * @param email The email of the speaker.
     * @return A boolean notifying the organizer if they have successfully created a speaker.
     */
    public boolean requestUser(String name, String password, String email, String type) {
        return this.userStorage.createUser(type, name, password, email);
    }
    /**
     * Uses the addRoom method to register a room.
     * @param scan The scanner.
     */
    public void registerRoom(Scanner scan){
        orgSchedulePresenter.printMenu(9);
        boolean doContinue = true;
        while(doContinue){
        String roomName = scan.nextLine();
        try{
        if (Integer.parseInt(roomName) == 0){
            return;
        }
    }catch (NumberFormatException nfe){
            if (this.addRoom(roomName)){
                orgSchedulePresenter.printMenu(11);
                return;}
            else{
                orgSchedulePresenter.printMenu(20);
            }}
        }}

    /**
     * Uses the requestSpeaker to create a speaker.
     * @param scan The Scanner.
     */

    public void registerUser(Scanner scan){
        orgSchedulePresenter.printMenu(10);
        String name = scan.nextLine();
        String password = scan.nextLine();
        String email = scan.nextLine();
        while(!email.contains("@")){
            orgSchedulePresenter.printMenu(21);
            email = scan.nextLine();
        }
        String type = scan.nextLine();
        while (!(type.equals("Attendee") || type.equals("Speaker") || type.equals("Organizer"))){
            orgSchedulePresenter.printMenu(22);

        }
        if (this.requestUser(name, password, email, type)){
            orgSchedulePresenter.printMenu(11);
        }

    }
    /**
     * Lists all the available actions an organizer can perform and choose from, takes their input and outputs a text UI.
     */
    public void cancelEvent(Scanner scan){
        orgSchedulePresenter.printAllTalks(eventManager);
        System.out.println("Which event would you like to cancel");
        boolean doContinue = true;
        while(doContinue){
            String choice = scan.nextLine();
            try {
                int eventIndex = Integer.parseInt(choice);
                if (eventIndex == 0){
                    presenter.printMenu(10);
                    return;
                }
                else if (getEventByIndex(eventIndex) == null){
                    presenter.printMenu(7);
                }
                else{
                    String eventIdToRegister = getEventByIndex(eventIndex);
                    if (this.eventManager.cancelEvent(eventIdToRegister)) {
                        // prints "Success"
                        System.out.println("Event " + eventIndex + " Cancelled");
                        return;
                    }
                }}
            catch (NumberFormatException nfe){
                presenter.printMenu(8);;
            }
        }
    }
    public void run(){
        orgSchedulePresenter.printHello(this.userStorage.emailToName(email));
        orgSchedulePresenter.printMenu(1);
        orgSchedulePresenter.printMenu(2);
        Scanner scan = new Scanner(System.in);
        boolean doContinue = true;
        while(doContinue) {
            String choice = scan.nextLine();
            try {
                int command = Integer.parseInt(choice);
            //if they want to register for a talk
            if (command == 1) {
                this.registerTalk(presenter,scan);
                orgSchedulePresenter.printMenu(1);
                //If they want to see all available talks
            }else if (command == 2) {
                this.seeAllTalks(presenter,scan);
                orgSchedulePresenter.printMenu(1);
                //if they want to see all the talks they are currently registered for
            }else if (command == 3) {
                this.seeAllRegistered(presenter,scan);
                orgSchedulePresenter.printMenu(1);
                // if they want to cancel a registration
            }else if (command == 4) {
                this.cancelATalk(presenter,scan);
                orgSchedulePresenter.printMenu(1);
            }else if (command == 5){
                this.requestEvent(scan);
                orgSchedulePresenter.printMenu(1);
            }else if (command == 6){
                this.registerRoom(scan);
                orgSchedulePresenter.printMenu(1);
            }else if (command == 7){
                this.registerUser(scan);
                orgSchedulePresenter.printMenu(1);
            }
            else if (command == 8){
                this.cancelEvent(scan);
                orgSchedulePresenter.printMenu(1);
            }
            else if (command == 9){
                this.seeAllSpeakers(presenter, scan);
                orgSchedulePresenter.printMenu(1);
            }
            else if (command == 10){
                this.seeAllDays(presenter, scan);
                orgSchedulePresenter.printMenu(1);
            }
            else if (command ==0){
                doContinue = false;
                mainMenuController.runMainMenu(email);
            }
            else{orgSchedulePresenter.printMenu(15);}}
            catch (NumberFormatException nfe){
                orgSchedulePresenter.printMenu(15);;
            }

        }
    }
}
