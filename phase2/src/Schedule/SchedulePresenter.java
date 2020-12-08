package Schedule;

import java.util.ArrayList;

public class SchedulePresenter {

    /**
     * Greets the user with their name.
     *
     */
    public void printHello(String name){
        System.out.println("Hello " + name);
    }

    /**
     * Prints a message letting the speaker it's returning to the main menu.
     */
    public void printGoodbye(String menu){
                System.out.println("Returning to " + menu + " menu...");
    }

    /**
     * On an unsuccessful attempt.
     */
    public void printTryAgain(String invalid){
        System.out.println("Invalid "+ invalid + ", please try again.");
    }

    public void InputCommandRequest(){
        System.out.println("Please input a command");
    }

    public void choose(String choice){
                System.out.println("Enter the number corresponding to each " + choice + "." + System.lineSeparator()+
                        "Press 0 to go back to the scheduling menu.");
    }

    public void printSuccess(){
        System.out.println("Success");
    }

    public void printGoBack(){
        System.out.println("Press 0 to go back to the scheduling menu");
    }

    public void printMenu(){}


    public void printByIndex(ArrayList<String> lst) {
        Integer i = 1;
        for (String s : lst){
            System.out.println(i + ") " + s);
            i++;
        }
    }


    public void printScheduleEmpty(int i){
        switch (i){
            case 1:
                System.out.println("Speaker is not scheduled to speak at any event at the moment.");
                break;
            case 2:
                System.out.println("No events are scheduled to occur within this room at the moment.");
                break;
            case 3:
                System.out.println("You are not currently registered for any events.");
                break;
        }
    }
}