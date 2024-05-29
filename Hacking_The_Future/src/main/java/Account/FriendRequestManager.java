package account;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestManager  {
//    public HashMap<User, List<User>> friendRequests = new HashMap<>();
//
//    // Send a friend request from one user to another
//    public void sendFriendRequest(User from, User to) {
//        if (from.equals(to)) {
//            System.out.println("Cannot send friend request to oneself.");
//            return;
//        }
//        List<User> requestsTo = friendRequests.getOrDefault(to, new ArrayList<>());
//        if (!requestsTo.contains(from)) {
//            requestsTo.add(from);
//            friendRequests.put(to, requestsTo);
//            System.out.println(from.getUsername() + " has sent a friend request to " + to.getUsername());
//        } else {
//            System.out.println("Friend request already sent to " + to.getUsername());
//        }
//    }
//
//    // Accept a friend request
//    public void acceptFriendRequest(User to, User from) {
//        List<User> requestsTo = friendRequests.getOrDefault(to, new ArrayList<>());
//        if (requestsTo.contains(from)) {
//            requestsTo.remove(from);
//            if (requestsTo.isEmpty()) {
//                friendRequests.remove(to);
//            } else {
//                friendRequests.put(to, requestsTo);
//            }
//            to.addFriend(from);
//            from.addFriend(to);
//            System.out.println(to.getUsername() + " has accepted a friend request from " + from.getUsername());
//        } else {
//            System.out.println("No friend request from " + from.getUsername() + " to " + to.getUsername());
//        }
//    }
//
//    // View pending friend requests for a user
//    public void viewFriendRequests(User user) {
//        List<User> requests = friendRequests.getOrDefault(user, new ArrayList<>());
//        if (requests.isEmpty()) {
//            System.out.println("No pending friend requests for " + user.getUsername());
//        } else {
//            System.out.println("Pending friend requests for " + user.getUsername() + ":");
//            for (User requestor : requests) {
//                System.out.println(requestor.getUsername());
//            }
//        }
//    }
}
