import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FriendRequestManager {
    private Map<User, List<User>> friendRequests = new HashMap<>();

    public void sendFriendRequest(User from, User to) {
        friendRequests.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        System.out.println("Friend request sent from " + from.getUsername() + " to " + to.getUsername());
    }

    public void acceptFriendRequest(User to, User from) {
        List<User> requests = friendRequests.getOrDefault(to, new ArrayList<>());
        if (requests.contains(from)) {
            requests.remove(from);
            System.out.println("Friend request accepted from " + from.getUsername() + " to " + to.getUsername());
        } else {
            System.out.println("No friend request from " + from.getUsername() + " to " + to.getUsername());
        }
    }
}
