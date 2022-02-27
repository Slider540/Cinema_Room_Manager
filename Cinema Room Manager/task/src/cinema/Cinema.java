package cinema;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Cinema {

    static final int priceForSmallRoom = 10;
    static final int priceForFrontHalf = 10;
    static final int priceForBackHalf = 8;
    static int numberOfPurchasedTickets = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows:");
        final int numberOfRows = scanner.nextInt();
        System.out.println("Enter the number of seats in each row:");
        final int seatsPerRow = scanner.nextInt();
        String[][] room = new String[numberOfRows + 1][seatsPerRow + 1];
        final int numberOfSeats = numberOfRows * seatsPerRow;
        final boolean smallRoom = numberOfSeats <= 60;
        final int rowsInFrontHalf = numberOfRows / 2;
        final int rowsInBackHalf = numberOfRows - rowsInFrontHalf;

        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[i].length; j++) {
                if (i != 0 && j != 0) {
                    room[i][j] = "S";
                } else if (i == 0) {
                    room[i][j] = String.valueOf(j);
                } else {
                    room[i][j] = String.valueOf(i);
                }
            }
        }
        room[0][0] = " ";

        boolean flag = true;
        while (flag) {
            System.out.println("\n1. Show the seats\n" +
                    "2. Buy a ticket\n" +
                    "3. Statistics\n" +
                    "0. Exit");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    printRoom(room);
                    break;
                case 2:
                    buyTicket(room, scanner, smallRoom, rowsInFrontHalf);
                    break;
                case 3:
                    getStatistics(room, smallRoom, numberOfSeats, rowsInFrontHalf,
                            rowsInBackHalf, seatsPerRow);
                    break;
                case 0:
                    flag = false;
                    break;
            }
        }
    }

    public static void printRoom(String[][] room) {
        System.out.println("\nCinema:");
        for (String[] row : room) {
            for (int i = 0; i < row.length; i++) {
                if (i != row.length - 1) {
                    System.out.print(row[i] + " ");
                } else {
                    System.out.print(row[i]);
                }
            }
            System.out.println();
        }
    }

    public static void buyTicket(String[][] room, Scanner scanner,
                                 boolean smallRoom, int rowsInFrontHalf) {
        boolean wrongInput;
        boolean flag = true;
        int rowNumber = 0;
        int seatNumber = 0;

        do {
            wrongInput = false;
            do {
                System.out.println("\nEnter a row number:");
                try {
                    rowNumber = scanner.nextInt();
                    flag = false;
                } catch (InputMismatchException ime) {
                    System.out.println("Wrong input!");
                }
            } while (flag);

            flag = true;
            do {
                System.out.println("Enter a seat number in that row:");
                try {
                    seatNumber = scanner.nextInt();
                    flag = false;
                } catch (InputMismatchException ime) {
                    System.out.println("Wrong input!");
                }
            } while (flag);

            if (rowNumber < 1 || rowNumber > room.length - 1 ||
                    seatNumber < 1 || seatNumber > room[rowNumber].length - 1) {
                System.out.println("\nWrong input!");
                wrongInput = true;
            } else if ("B".equals(room[rowNumber][seatNumber])) {
                System.out.println("That ticket has already been purchased!");
                wrongInput = true;
            }
        } while (wrongInput);

        System.out.print("\nTicket price: $");
        if (smallRoom) {
            System.out.println(priceForSmallRoom);
        } else if (rowNumber <= rowsInFrontHalf) {
            System.out.println(priceForFrontHalf);
        } else {
            System.out.println(priceForBackHalf);
        }

        room[rowNumber][seatNumber] = "B";
        numberOfPurchasedTickets++;
    }

    public static void getStatistics(String[][] room, boolean smallRoom,
                                     int numberOfSeats, int rowsInFrontHalf,
                                     int rowsInBackHalf, int seatsPerRow) {
        System.out.println("\nNumber of purchased tickets: " + numberOfPurchasedTickets);
        System.out.printf(Locale.ENGLISH,"Percentage: %.2f", (double) numberOfPurchasedTickets /
                numberOfSeats * 100);
        System.out.println("%");

        System.out.print("Current income: $");
        int currentIncome = 0;
        for (int i = 1; i < room.length; i++) {
            for (int j = 1; j < room[i].length; j++) {
                if (smallRoom && "B".equals(room[i][j])) {
                    currentIncome += priceForSmallRoom;
                } else if ("B".equals(room[i][j])) {
                    if (i <= rowsInFrontHalf) {
                        currentIncome += priceForFrontHalf;
                    } else {
                        currentIncome += priceForBackHalf;
                    }
                }
            }
        }
        System.out.println(currentIncome);

        System.out.print("Total income: $");
        if (smallRoom) {
            System.out.println(numberOfSeats * priceForSmallRoom);
        } else {
            System.out.println((rowsInFrontHalf * priceForFrontHalf +
                    rowsInBackHalf * priceForBackHalf) * seatsPerRow);
        }
    }
}