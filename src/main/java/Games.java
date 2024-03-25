import java.sql.*;
import java.util.Scanner;

public class Games {
    private static java.sql.Connection con;
    private static int currentScreen = 0;
    private static int id;
    private static String usuario;

    public static void main(String[] args) throws SQLException {
        String host = "jdbc:sqlite:/media/alu10675957/T7/1DAW/Programación/Games/src/main/resources/Untitled";
        con = java.sql.DriverManager.getConnection(host);
        int option;
        while (true) {
            printMenu();
            option = getOption();
            if (option == 0) break;
            if (currentScreen == 0) {
                switch (option) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                }
            } else {
                switch (option) {
                    case 1:
                        misJuegos();
                        break;
                    case 2:
                        comprarJuego();
                        break;
                    case 3:
                        otrosJuegos();
                        break;
                    case 4:
                        logout();
                        break;
                }
            }
        }
    }

    private static int getOption() {
        Scanner scanner = new Scanner(System.in);
        int option = -1;
        try {
            option = Integer.parseInt(scanner.next());
            if ((currentScreen == 0 && option > 3) || (currentScreen == 1 && option > 6)) {
                System.out.println("Incorrect option");
            }
        } catch (IllegalArgumentException iae) {
            System.out.println("Incorrect Option");
        }
        return option;
    }

    private static void printMenu() {
        System.out.println("_________________________________________________________________________________");
        if (currentScreen == 0) {
            System.out.println("\n" +
                    "\n" +
                    "  ▄████  ▄▄▄       ███▄ ▄███▓▓█████   ██████ \n" +
                    " ██▒ ▀█▒▒████▄    ▓██▒▀█▀ ██▒▓█   ▀ ▒██    ▒ \n" +
                    "▒██░▄▄▄░▒██  ▀█▄  ▓██    ▓██░▒███   ░ ▓██▄   \n" +
                    "░▓█  ██▓░██▄▄▄▄██ ▒██    ▒██ ▒▓█  ▄   ▒   ██▒\n" +
                    "░▒▓███▀▒ ▓█   ▓██▒▒██▒   ░██▒░▒████▒▒██████▒▒\n" +
                    " ░▒   ▒  ▒▒   ▓▒█░░ ▒░   ░  ░░░ ▒░ ░▒ ▒▓▒ ▒ ░\n" +
                    "  ░   ░   ▒   ▒▒ ░░  ░      ░ ░ ░  ░░ ░▒  ░ ░\n" +
                    "░ ░   ░   ░   ▒   ░      ░      ░   ░  ░  ░  \n" +
                    "      ░       ░  ░       ░      ░  ░      ░  \n" +
                    "\n");
            System.out.println("0 Exit / 1 Login / 2 Register");
        } else if (currentScreen == 1) {
            System.out.println("0 Exit / 1 Mis juegos / 2 Comprar juegos / 3 Otros juegos / 4 LogOut " + " - " + usuario);
        }
        System.out.println("_________________________________________________________________________________");
    }

    private static void login() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Usuario:");
        String name = scanner.nextLine();
        PreparedStatement st = null;
        String query = "SELECT * FROM persona WHERE usuario = ?";
        st = con.prepareStatement(query);
        st.setString(1, name);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
            usuario = rs.getString("usuario");
            currentScreen = 1;
        } else {
            System.out.println("User not found");
        }
    }

    private static void register() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement st = null;
        System.out.println("Nombre:");
        String nombre = scanner.nextLine();
        System.out.println("Apellidos:");
        String apellidos = scanner.nextLine();
        System.out.println("Usuario:");
        String usuario = scanner.nextLine();
        String query = "INSERT INTO persona (nombre, apellidos, usuario) VALUES (?, ?, ?)";
        st = con.prepareStatement(query);
        st.setString(1, nombre);
        st.setString(2, apellidos);
        st.setString(3, usuario);
        st.executeUpdate();
        System.out.println("Cuenta creada correctamente");
    }

    private static void misJuegos() throws SQLException {
        PreparedStatement st = null;
        String query = "SELECT juego.id, juego.nombre, usuario FROM persona INNER JOIN juego on persona.id = juego.id_persona WHERE persona.id = ?";
        st = con.prepareStatement(query);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " - " + rs.getString("nombre") + " - " + usuario);
        }
    }

    private static void comprarJuego() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        PreparedStatement st = null;
        otrosJuegos();
        System.out.println("Introduce el id del juego::");
        String id_juego = scanner.nextLine();
        System.out.println("Introduce el nombre del juego::");
        String nombre = scanner.nextLine();
        System.out.println("Precio:");
        int precio = scanner.nextInt();
        System.out.println("Compra realizada correctamente");
        String query = "INSERT INTO juego (nombre, precio, id_persona) VALUES (?, ?, ?)";
        st = con.prepareStatement(query);
        st.setString(1, nombre);
        st.setInt(2, precio);
        st.setInt(3, id);
        st.executeUpdate();
    }

    private static void otrosJuegos() throws SQLException {
        PreparedStatement st = null;
        String query = "SELECT juego.id, juego.nombre, juego.precio FROM juego INNER JOIN persona on persona.id = juego.id_persona WHERE persona.id != ?";
        st = con.prepareStatement(query);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getInt("id") + " - " + rs.getString("nombre") + " - " + rs.getInt("precio") + "€");
        }
    }

    private static void logout(){
        currentScreen = 0;
    }

}