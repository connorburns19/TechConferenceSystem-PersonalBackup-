package MessagingPresenters;

import Schedule.SpeakerScheduleManager;
import UserLogin.Speaker;
import UserLogin.User;

import java.time.LocalDateTime;
import java.util.*;

/**
 * A class that represents a speaker message controller.
 */

public class SpeakerMessengerController implements Observer{
    public SpeakerMessageManager userInfo;
    private ConversationStorage conversationStorage;
    private Speaker speaker;
    private SpeakerMessengerControllerPresenter presenter;


    /**
     * A speaker is required to create an instance of this class.
     * @param speaker the speaker
     */

    public SpeakerMessengerController(Speaker speaker) {
        this.userInfo = new SpeakerMessageManager(speaker);
        this.presenter = new SpeakerMessengerControllerPresenter();
    }

    /**
     * Sends a message containing </messageContent> to a user registered under the email </email>.
     * @param email a String representing the email of the recipient
     * @param messageContent a String representing the content of the message
     */

    private void message(String email, String messageContent){
        ConversationManager c = conversationStorage.getConversationManager(speaker.getEmail(), email);
        c.addMessage(email, speaker.getEmail(), LocalDateTime.now(), messageContent);
    }

    /**
     * Sends a message containing </messageContent> to all attendees.
     * @param messageContent a String representing the content of the message
     */

    public void messageAllAttendees(String messageContent){
        for (String email: userInfo.getAllAttendees()){
            message(email, messageContent);
        }
    }

    /**
     * Returns a boolean representing whether or not a reply containing </messageContent> was sent.
     * @param email a String representing the email of the recipient
     * @param messageContent a String representing the content of the message
     * @return a boolean representing whether or not the reply was sent
     */

    public boolean reply(String email, String messageContent){
        if (userInfo.canReply(email)){
            ConversationManager c = conversationStorage.getConversationManager(speaker.getEmail(), email);
            c.addMessage(email, speaker.getEmail(), LocalDateTime.now(), messageContent);
            return true;
        }
        return false;
    }

    public void run() {
        boolean flag = true;
        Scanner scan = new Scanner(System.in);
        while (flag) {
            presenter.printMenu(0);
            int option = scan.nextInt();

            if (option == 0) {
                flag = false;
                presenter.printMenu(1);
            }
            else if (option == 1) {
                presenter.printMenu(2);
                String email = scan.nextLine();
                presenter.printMenu(3);
                String body = scan.nextLine();

                message(email, body);
            }
            else if (option == 2) {
                presenter.printMenu(3);
                String body = scan.nextLine();
                messageAllAttendees(body);
            }
        }
    }

    /**
     * Updates </conversationStorage> if and only if </arg> is an instance of ConversationStorage.
     * @param o an observable parameter
     * @param arg an Object
     */

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ConversationStorage) {
            this.conversationStorage = (ConversationStorage) arg;
        }
    }
}
