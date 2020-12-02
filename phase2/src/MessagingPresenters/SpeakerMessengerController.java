package MessagingPresenters;

import Schedule.Event;
import Schedule.EventManager;
import UserLogin.MainMenuController;
import UserLogin.User;
import UserLogin.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A class that represents a speaker message controller.
 */

public class SpeakerMessengerController extends MessengerController {
    public final SpeakerMessageManager messageManager;
    private final SpeakerMessengerPresenter presenter;

    /**
     * A speaker is required to create an instance of this class.
     *
     * @param speakerEmail the speaker
     */

    public SpeakerMessengerController(String speakerEmail, Scanner scanner, MainMenuController mainMenuController,
                                      UserStorage userStorage, ConversationStorage conversationStorage,
                                      EventManager eventManager) {
        super(speakerEmail, scanner, mainMenuController, userStorage, conversationStorage);
        this.messageManager = new SpeakerMessageManager(speakerEmail, userStorage, eventManager, conversationStorage);
        this.presenter = new SpeakerMessengerPresenter();
    }

    /**
     * Sends a message containing </messageContent> to a user registered under the email </email>.
     * @param otherEmail a String representing the email of the recipient
     * @param messageContent a String representing the content of the message
     */

    private void message(String otherEmail, String messageContent){
        messageManager.messageOne(otherEmail, messageContent);
    }


    /**
     * Sends a message containing </messageContent> to all attendees.
     * @param messageContent a String representing the content of the message
     */

    public void messageAllAttendees(String messageContent){
        messageManager.messageAllAttendees(messageContent);
    }

    /**
     * Returns a list of messages between this speaker and the user with email </email>.
     * @param otherEmail a String representing an email
     * @return an ArrayList containing messages
     */

    public ArrayList<Message> viewMessages(String otherEmail){
        if (conversationStorage.contains(otherEmail, email)){
            ConversationManager c = conversationStorage.getConversationManager(otherEmail, email);
            return c.getMessages();
        }
        return null;
    }

    public void markAsRead(String email){messageManager.markRead(email);}

    public void markAsUnread(String email){messageManager.markUnRead(email);}

    public void archive(String email){messageManager.archive(email);}

    public void unarchive(String email){messageManager.unarchive(email);}

    // message attendees of one talk functions needs to be added

    /**
     * Runs the presenters.
     */

    public void run() {
        boolean flag = true;
        OUTER_LOOP:
        while (flag) {
            presenter.printMenu(0);
            int option = Integer.parseInt(scan.nextLine());
            try {
                if (option == 0) {
                    flag = false;
                    presenter.printMenu(1);
                    mainMenuController.runMainMenu(email);
                } else if (option == 1) {
                    presenter.printMenu(2);
                    String email = "";
                    boolean valid_recipient = false;
                    while (!valid_recipient) {
                        email = scan.nextLine();
                        if (email.equals("0")) {
                            continue OUTER_LOOP;
                        }
                        if (messageManager.canMessage(email)) {
                            valid_recipient = true;
                        } else {
                            presenter.printMenu(5);
                        }
                    }
                    presenter.printMenu(3);
                    String body = scan.nextLine();

                    messageManager.messageOne(email, body);
                    presenter.printMenu(4);
                } else if (option == 2) {
                    presenter.printMenu(3);
                    String body = scan.nextLine();
                    if (body.equals("0")) {
                        continue;
                    }
                    messageManager.messageAllAttendees(body);
                    presenter.printMenu(4);
                } else if (option == 3) {
                    ArrayList<String> emails = messageManager.getRecipients();
                    presenter.viewChats(emails);
                    int index = Integer.parseInt(scan.nextLine());
                    if (index == 0 || emails.size() == 0) {
                        continue;
                    }
                    String email = emails.get(index - 1);
                    ArrayList<Message> messages = messageManager.viewMessages(email);
                    presenter.viewConversation(messages);
                } else if (option == 4) {
                    ArrayList<Event> events = messageManager.getSpeakerTalks();
                    presenter.viewTalks(events);
                    int index = Integer.parseInt(scan.nextLine());
                    if (index == 0 || events.size() == 0) {
                        continue;
                    }
                    Event event = events.get(index - 1);
                    ArrayList<User> emails = messageManager.getAttendeesOfTalk(event);
                    presenter.printMenu(3);
                    String body = scan.nextLine();
                    if (body.equals("0")) {
                        continue;
                    }
                    for (User user : emails) {
                        messageManager.messageOne(user.getEmail(), body);
                    }
                    presenter.printMenu(4);
                }
            } catch (NumberFormatException nfe) {
                presenter.printMenu(6);
            }
        }
    }
}

