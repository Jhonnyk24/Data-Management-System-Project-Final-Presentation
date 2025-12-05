import javax.swing.*;
import java.sql.Connection;

/**
 * Main entry point for the Movie Data Management System (DMS).
 */
public class Main {

    public static void main(String[] args) {
        // Ask the user for DB connection info
        Connection connection = DBConnectionDialog.showDialog(null);

        if (connection == null) {
            System.out.println("No database connection. Program exiting.");
            return;
        }

        MovieDatabaseManager db = new MovieDatabaseManager(connection);

        SwingUtilities.invokeLater(() -> new MovieGUI(db));
    }
}
