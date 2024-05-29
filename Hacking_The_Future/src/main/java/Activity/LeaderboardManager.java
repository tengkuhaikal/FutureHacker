//import java.util.List;
//import java.util.Collections;
//import java.util.Comparator;
//
//public class LeaderboardManager {
//    private List<User> users;
//
//    public LeaderboardManager(List<User> users) {
//        this.users = users;
//    }
//
//    public void displayLeaderboard() {
//        Collections.sort(users, Comparator.comparingInt(User::getCurrentPoints).reversed());
//        System.out.println("Global Leaderboard:");
//        for (User user : users) {
//            System.out.println(user.getUsername() + " - Points: " + user.getCurrentPoints());
//        }
//    }
//}
