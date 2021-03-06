package MessagingPresenters;

import Schedule.EventManager;
import UserLogin.*;
import java.util.*;

/**
 * A class that manages messaging.
 */

public class AttendeeMessageManager extends MessageManager {
    /**
     * A user is needed to create an instance of AttendeeMessageManager.
     */

    public AttendeeMessageManager(String email, UserManager userManager, ConversationStorage conversationStorage, EventManager eventManager) {

        super(email, userManager, conversationStorage, eventManager);
    }

    /**
     * Returns a list of users that this user is allowed to message. Attendees can message all attendees and speakers,
     * organizers can message all users, and speakers can message all attendees.
     *
     * @return the HashSet of users that user can message
     */

    public HashSet<User> getFriendsList() {
        HashSet<User> friends = new HashSet<>();
        for (int i = 0; i < userManager.userList.size(); i++) {
            if (userManager.getUserList().get(i) instanceof Attendee || userManager.getUserList().get(i) instanceof Speaker) {
                if (!userManager.getUserList().get(i).getEmail().equals(user.getEmail())) {
                    friends.add(userManager.getUserList().get(i));
                }
            }
        }
        return friends;
    }
}
