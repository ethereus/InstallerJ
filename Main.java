import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.Scanner;

public class Main {
    private Connection connection;

    public Main() {
        try {
            String url = "jdbc:sqlite:packages.db";
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS packages (identifier TEXT PRIMARY KEY, name TEXT, url TEXT, location TEXT, type TEXT, version TEXT)");
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public void addPackage(String identifier, String name, String url, String location, String type, String version) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO packages (identifier, name, url, location, type, version) VALUES (?,?,?,?,?,?)");
            statement.setString(1, identifier);
            statement.setString(2, name);
            statement.setString(3, url);
            statement.setString(4, location);
            statement.setString(5, type);
            statement.setString(6, version);
            statement.execute();
            System.out.println(name + " has been added.");
        } catch (SQLException ignored) {

        }
    }

    public void removePackage(String url) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM packages WHERE url = ?");
            statement.setString(1, url);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Packages have been removed.");
            } else {
                System.out.println("Packages not found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to remove packages: " + e.getMessage());
        }
    }

    public void updatePackage(String identifier, String name, String url, String location, String type, String version) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE packages SET name = ?, url = ?, location = ?, type = ?, version = ? WHERE identifier = ?");
            statement.setString(1, name);
            statement.setString(2, url);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setString(5, version);
            statement.setString(6, identifier);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Package has been updated.");
            } else {
                System.out.println("Package was not found.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to update package: " + e.getMessage());
        }
    }

    public void listPackages() {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM packages");
            System.out.println("Identifier | Name | URL | Location | Type | Version");
            while (result.next()) {
                System.out.println(result.getString("identifier") + " | " + result.getString("name") + " | " + result.getString("url") + " | " + result.getString("location") + " | " + result.getString("type") + " | " + result.getString("version"));
            }
        } catch (SQLException e) {
            System.out.println("Failed to list packages: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        Scanner scanner = new Scanner(System.in);

        try {

            System.out.println("Welcome back!");
            System.out.println("Refreshing repos...");

            URL url = new URL("https://raw.githubusercontent.com/ethereus/InstallerJRepo/main/Packages.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            JSONParser parser = new JSONParser();

            Object obj = parser.parse(result.toString());
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject installerObject = (JSONObject) jsonObject.get("Installer");
            JSONArray packagesArray = (JSONArray) installerObject.get("Packages");

            for (Object packageObject : packagesArray) {
                JSONObject packageJson = (JSONObject) packageObject;
                String identifier = (String) packageJson.get("identifier");
                String name = (String) packageJson.get("name");
                String urlStr = (String) packageJson.get("url");
                String locationStr = (String) packageJson.get("location");
                String type = (String) packageJson.get("type");
                String version = (String) packageJson.get("version");

                main.addPackage(identifier, name, urlStr, locationStr, type, version);
            }
        } catch (Exception e) {
            System.out.println("Error reading from URL: " + e.getMessage());
        }

        System.out.println("Done!");

        boolean run = true;

        while (run) {
            System.out.println("Select an option:");
            System.out.println("1. Add repo");
            System.out.println("2. Remove repo");
            System.out.println("3. Install package");
            System.out.println("4. Remove package");
            System.out.println("5. Update packages");
            System.out.println("6. List all packages");
            System.out.println("7. Exit");
            int option = scanner.nextInt();
            switch (option) {
                case 1 -> {
                    try {

                        URL url = new URL("https://raw.githubusercontent.com/ethereus/InstallerJRepo/main/Packages.json");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        reader.close();

                        JSONParser parser = new JSONParser();

                        Object obj = parser.parse(result.toString());
                        JSONObject jsonObject = (JSONObject) obj;
                        JSONObject installerObject = (JSONObject) jsonObject.get("Installer");
                        JSONArray packagesArray = (JSONArray) installerObject.get("Packages");

                        for (Object packageObject : packagesArray) {
                            JSONObject packageJson = (JSONObject) packageObject;
                            String identifier = (String) packageJson.get("identifier");
                            String name = (String) packageJson.get("name");
                            String urlStr = (String) packageJson.get("url");
                            String locationStr = (String) packageJson.get("location");
                            String type = (String) packageJson.get("type");
                            String version = (String) packageJson.get("version");

                            main.addPackage(identifier, name, urlStr, locationStr, type, version);
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading from URL: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Enter url: ");
                    String removeUrl = scanner.next();
                    main.removePackage(removeUrl);
                }
                case 3 -> {

                    //install package function goes here
                    //this is a temporary line
                    System.out.println("Coming Soon.");

                }
                case 4 -> {

                    //remove package function goes here
                    //this is a temporary line
                    System.out.println("Coming Soon.");

                }
                case 5 -> {

                    //update packages function goes here
                    //this is a temporary line
                    System.out.println("Coming Soon.");

                }
                case 6 -> main.listPackages();
                case 7 -> run = false;
                default -> System.out.println("Invalid option, try again.");
            }
        }
        scanner.close();
    }
}
