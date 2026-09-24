// ===================== BOOK CLASS =====================

class Book {
    int bookId;
    String title;
    String author;
    double price;

    public Book(int bookId, String title, String author, double price) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
    }
}

public class LibraryManagementSystem {
    static Book[] books = new Book[]{
            new Book(101, "Data Structures", "Mark", 400.0),
            new Book(101, "Data Structures", "Mark", 400.0),
            new Book(102, "Java Basics", "James", 300.0),
            new Book(103, "Python Guide", "Guido", 600.0),
            new Book(104, "Database Systems", "Raghu", 500.0),
            new Book(105, "Computer Networks", "Andrew", 700.0)
    };
        public static void main(String[] args) {

        int n = books.length;
        int count = removeDuplicates(books, n);
        System.out.println("Unique Books Count: " + count);
        for (int i = 0; i < count; i++) {
            System.out.println("[" + books[i].bookId + "] " + books[i].title + " - Rs. " + books[i].price);
        }

        System.out.println();
        searchByTitle(books, count, "data");

        System.out.println();
        sortByPrice(books, count);

        System.out.println();
        int index = searchByPrice(books, count, 500.0);
        if (index != -1) {
            System.out.println("Found at index " + index + ": [" + books[index].bookId + "] " + books[index].title);
        } else {
            System.out.println("Not found");
        }

        System.out.println();
        int minBooks = minBooksForTargetCost(books, count, 1000.0);
        System.out.println("Minimum Consecutive Books Needed: " + minBooks);
    }
    
    //TASK 1

    static int removeDuplicates(Book[] books, int n) {
        if (n == 0) return 0;

        int index = 0;
        for (int i = 1; i < n; i++) {
            if (books[i].bookId != books[index].bookId) {
                index++;
                books[index] = books[i];
            }
        }
        return index + 1;
    }

    //TASK 2

    static void searchByTitle(Book[] books, int count, String query) {
        query = query.toLowerCase();
        System.out.println("Search Results for '" + query + "':");

        for (int i = 0; i < count; i++) {
            if (books[i].title.toLowerCase().contains(query)) {
                System.out.println("- Found: [" + books[i].bookId + "] " + books[i].title + " (Rs. " + books[i].price + ")");
            }
        }
    }

    //TASK 3

    static void sortByPrice(Book[] books, int count) {
        int swaps = 0;

        for (int i = 0; i < count - 1; i++) {
            int min = i;
            for (int j = i + 1; j < count; j++) {
                if (books[j].price < books[min].price) {
                    min = j;
                }
            }
            if (min != i) {
                Book temp = books[i];
                books[i] = books[min];
                books[min] = temp;
                swaps++;
            }
        }

        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". [" + books[i].bookId + "] " + books[i].title + " - Rs. " + books[i].price);
        }
        System.out.println("Total Swaps: " + swaps);
    }

    //TASK 4

    static int searchByPrice(Book[] books, int count, double targetPrice) {
        int low = 0, high = count - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            if (books[mid].price == targetPrice) {
                return mid;
            } else if (books[mid].price < targetPrice) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    //TASK 5

    static int minBooksForTargetCost(Book[] books, int count, double targetCost) {
    int left = 0;
    double sum = 0;
    int minLength = Integer.MAX_VALUE;
    for (int right = 0; right < count; right++) {
        sum += books[right].price;
        while (sum >= targetCost) {
            int len = right - left + 1;
            if (len < minLength) {
                minLength = len;
            }
            sum -= books[left].price;
            left++;
        }
    }
    if (minLength == Integer.MAX_VALUE) {
        return 0;
    }
    return minLength;
}
}