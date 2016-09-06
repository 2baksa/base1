package kvartira;

import java.sql.*;
import java.util.Scanner;




public class Main {
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/mydatabase";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "cnfc88cnfc";

    static Connection conn;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            try {
                // create connection
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                initDB();

                while (true) {
                    System.out.println("1: add Flat");
                    System.out.println("2: Search Flat");
                    System.out.println("3: view flat");
                    System.out.print("-> ");


                   /* System.out.println("1: add client");
                    System.out.println("2: add random clients");
                    System.out.println("3: delete client");
                    System.out.println("4: change client");
                    System.out.println("5: view clients");
                    System.out.print("-> ");*/

                    String s = sc.nextLine();
                    switch (s) {
                        case "1":
                            addFlat(sc);
                            break;
                        case "2":
                            searchFlat(sc);
                            break;

                        case "3":
                            viewFlat();
                            break;
                        default:
                            return;
                    }
                }
            } finally {
                sc.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return;
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS Flat");
            st.execute("CREATE TABLE Flat (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "state VARCHAR(128) NOT NULL, " +
                    "address  VARCHAR(128) NOT NULL, " +
                    "square FLOAT(10,2) NOT NULL," +
                    "rooms INT NOT NULL," +
                    "price DEC(15,2) NOT NULL)");
        } finally {
            st.close();
        }
    }

    private static void addFlat(Scanner sc) throws SQLException {
        System.out.print("Enter state: ");
        String state = sc.nextLine();
        System.out.print("Enter address: ");
        String address = sc.nextLine();
        System.out.print("Enter square: ");
        String Ssquare = sc.nextLine();
        double square = Double.parseDouble(Ssquare);
        System.out.print("Enter rooms: ");
        String RRooms = sc.nextLine();
        int rooms = Integer.parseInt(RRooms);
        System.out.print("Enter price: ");
        String PPrice = sc.nextLine();
        double price = Double.parseDouble(PPrice);


        PreparedStatement ps = conn.prepareStatement("INSERT INTO Flat (state, address, square, rooms, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, state);
            ps.setString(2, address);
            ps.setDouble(3, square);
            ps.setInt(4, rooms);
            ps.setDouble(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }


    private static void searchFlat(Scanner sc) throws SQLException {
        System.out.println("Enter parameter for search (state, square, address, price, rooms)");
        String parameter = sc.nextLine();
        System.out.print("Enter value: ");
        String value = sc.nextLine();
        PreparedStatement ps = null;
        try {
            switch (parameter) {
                case "state":
                    ps = conn.prepareStatement("SELECT * FROM Flats WHERE state = ?");
                    ps.setString(1, value);
                    break;
                case "square":
                    ps = conn.prepareStatement("SELECT * FROM Flats WHERE square = ?");
                    ps.setDouble(1, Double.parseDouble(value));
                    break;
                case "rooms":
                    ps = conn.prepareStatement("SELECT * FROM Flats WHERE rooms = ?");
                    ps.setInt(1, Integer.parseInt(value));
                    break;
                case "address":
                    ps = conn.prepareStatement("SELECT * FROM Flats WHERE address = ?");
                    ps.setString(1, value);
                    break;
                case "price":
                    ps = conn.prepareStatement("SELECT * FROM Flats WHERE price = ?");
                    ps.setDouble(1, Double.parseDouble(value));
                    break;
            }
            if (ps != null) {
                ResultSet rs= ps.executeQuery();
               printResultSet(rs);
            }
        } finally {
            ps.close();
        }
    }


    private static void viewFlat() throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM flat");
        try {
            // table of data representing a database result set,
            ResultSet rs = ps.executeQuery();
            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t\t\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t\t\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }


        }
    }



