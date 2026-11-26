/*
 * ============================================================================
 *  PROBLEM 7 : SINGLY LINKED LIST - SOCIAL MEDIA FRIEND CONNECTIONS
 *  Node data  : User ID, Name, Age, List of Friend IDs
 *  Design     : OUTER singly linked list  -> users
 *               INNER (nested) singly linked list inside every user -> friend IDs
 *  Operations : 1) Add friend connection      2) Remove friend connection
 *               3) Mutual friends             4) Display friends of a user
 *               5) Search user by Name / ID   6) Count friends of each user
 *  Run        : javac Q7_SocialMediaFriends.java
 *               java Q7_SocialMediaFriends
 * ============================================================================
 */
public class Q7_SocialMediaFriends {

    // ------------------- INNER NODE: one friend ID --------------------------
    static class FriendNode {
        int friendId;
        FriendNode next;
        FriendNode(int friendId) { this.friendId = friendId; }
    }

    // ------------------- OUTER NODE: one user -------------------------------
    static class User {
        int id;
        String name;
        int age;
        FriendNode friends;              // head of this user's friend list
        int friendCount;
        User next;                       // next user in the main list

        User(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        boolean hasFriend(int fid) {
            for (FriendNode f = friends; f != null; f = f.next)
                if (f.friendId == fid) return true;
            return false;
        }

        void addFriendId(int fid) {                 // insert at beginning of friend list: O(1)
            FriendNode node = new FriendNode(fid);
            node.next = friends;
            friends = node;
            friendCount++;
        }

        boolean removeFriendId(int fid) {
            if (friends == null) return false;
            if (friends.friendId == fid) { friends = friends.next; friendCount--; return true; }
            FriendNode t = friends;
            while (t.next != null && t.next.friendId != fid) t = t.next;
            if (t.next == null) return false;
            t.next = t.next.next;
            friendCount--;
            return true;
        }
    }

    // ------------------- OUTER LIST: the social network ---------------------
    static class SocialNetwork {
        private User head;

        // Add a new user at the end
        void addUser(int id, String name, int age) {
            if (findById(id) != null) { System.out.println("User ID " + id + " already exists."); return; }
            User u = new User(id, name, age);
            if (head == null) head = u;
            else {
                User t = head;
                while (t.next != null) t = t.next;
                t.next = u;
            }
            System.out.println("User added: " + name + " (ID " + id + ")");
        }

        // ---- 5a) SEARCH BY USER ID ------------------------------------------
        User findById(int id) {
            for (User u = head; u != null; u = u.next) if (u.id == id) return u;
            return null;
        }

        // ---- 5b) SEARCH BY NAME ---------------------------------------------
        User findByName(String name) {
            for (User u = head; u != null; u = u.next) if (u.name.equalsIgnoreCase(name)) return u;
            return null;
        }

        void searchById(int id) {
            User u = findById(id);
            System.out.println(u == null ? "No user with ID " + id : "FOUND -> " + describe(u));
        }

        void searchByName(String name) {
            User u = findByName(name);
            System.out.println(u == null ? "No user named " + name : "FOUND -> " + describe(u));
        }

        // ---- 1) ADD FRIEND CONNECTION (both directions) ---------------------
        void addFriend(int a, int b) {
            User ua = findById(a), ub = findById(b);
            if (ua == null || ub == null) { System.out.println("Cannot connect: user not found."); return; }
            if (a == b) { System.out.println("A user cannot befriend themselves."); return; }
            if (ua.hasFriend(b)) { System.out.println(ua.name + " and " + ub.name + " are already friends."); return; }
            ua.addFriendId(b);
            ub.addFriendId(a);
            System.out.println("Connected: " + ua.name + " <-> " + ub.name);
        }

        // ---- 2) REMOVE FRIEND CONNECTION ------------------------------------
        void removeFriend(int a, int b) {
            User ua = findById(a), ub = findById(b);
            if (ua == null || ub == null) { System.out.println("Cannot remove: user not found."); return; }
            if (ua.removeFriendId(b) && ub.removeFriendId(a))
                System.out.println("Disconnected: " + ua.name + " X " + ub.name);
            else System.out.println(ua.name + " and " + ub.name + " are not friends.");
        }

        // ---- 3) MUTUAL FRIENDS ----------------------------------------------
        // For every friend of A, check whether B also has that friend:  O(f1 * f2)
        void mutualFriends(int a, int b) {
            User ua = findById(a), ub = findById(b);
            if (ua == null || ub == null) { System.out.println("User not found."); return; }
            System.out.print("Mutual friends of " + ua.name + " & " + ub.name + ": ");
            boolean any = false;
            for (FriendNode f = ua.friends; f != null; f = f.next) {
                if (ub.hasFriend(f.friendId)) {
                    User m = findById(f.friendId);
                    System.out.print(m.name + " (ID " + m.id + ")  ");
                    any = true;
                }
            }
            System.out.println(any ? "" : "None");
        }

        // ---- 4) DISPLAY FRIENDS OF A USER -----------------------------------
        void displayFriends(int id) {
            User u = findById(id);
            if (u == null) { System.out.println("User not found."); return; }
            System.out.print("Friends of " + u.name + ": ");
            if (u.friends == null) { System.out.println("(none)"); return; }
            for (FriendNode f = u.friends; f != null; f = f.next) {
                User fr = findById(f.friendId);
                System.out.print(fr.name + " (ID " + fr.id + ")  ");
            }
            System.out.println();
        }

        // ---- 6) COUNT FRIENDS OF EACH USER ----------------------------------
        void countFriendsOfAll() {
            System.out.println("---- Friend counts ----");
            for (User u = head; u != null; u = u.next)
                System.out.println("  " + u.name + " (ID " + u.id + ") -> " + u.friendCount + " friend(s)");
        }

        private String describe(User u) {
            return "ID=" + u.id + ", Name=" + u.name + ", Age=" + u.age + ", Friends=" + u.friendCount;
        }
    }

    // ------------------------------ MAIN (DEMO) -----------------------------
    public static void main(String[] args) {
        SocialNetwork net = new SocialNetwork();

        System.out.println(">> Adding users");
        net.addUser(1, "Karan", 20);
        net.addUser(2, "Riya", 19);
        net.addUser(3, "Aman", 21);
        net.addUser(4, "Neha", 20);
        net.addUser(5, "Dev", 22);
        net.addUser(3, "Dup", 30);

        System.out.println("\n>> Adding friend connections");
        net.addFriend(1, 2);
        net.addFriend(1, 3);
        net.addFriend(2, 3);
        net.addFriend(2, 4);
        net.addFriend(3, 5);
        net.addFriend(1, 2);      // already friends
        net.addFriend(1, 1);      // self
        net.addFriend(1, 99);     // missing user

        System.out.println("\n>> Display friends");
        net.displayFriends(1);
        net.displayFriends(2);
        net.displayFriends(5);

        System.out.println("\n>> Mutual friends");
        net.mutualFriends(1, 2);  // Aman
        net.mutualFriends(1, 4);  // Riya
        net.mutualFriends(4, 5);  // none

        System.out.println("\n>> Search");
        net.searchById(3);
        net.searchByName("riya");
        net.searchByName("Zed");

        System.out.println("\n>> Remove connection");
        net.removeFriend(1, 2);
        net.removeFriend(1, 2);   // no longer friends
        net.displayFriends(1);

        System.out.println();
        net.countFriendsOfAll();
    }
}
