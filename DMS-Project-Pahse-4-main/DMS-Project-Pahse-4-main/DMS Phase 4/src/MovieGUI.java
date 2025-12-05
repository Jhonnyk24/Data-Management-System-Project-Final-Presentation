import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * Main GUI window for managing movies.
 */
public class MovieGUI extends JFrame {

    private JTable movieTable;
    private DefaultTableModel tableModel;
    private final MovieDatabaseManager db;

    public MovieGUI(MovieDatabaseManager db) {
        this.db = db;

        setTitle("Movie Database System");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Nimbus theme if available
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        Color background = new Color(43, 43, 43);
        Color panelColor = new Color(58, 58, 58);
        Color textColor = Color.WHITE;
        Color gold = new Color(201, 168, 106);
        Color goldHover = new Color(227, 200, 142);

        getContentPane().setBackground(background);

        tableModel = new DefaultTableModel(new Object[]{
                "ID", "Title", "Year", "Director", "Rating", "Runtime (min)", "Votes", "Watched"
        }, 0);

        movieTable = new JTable(tableModel);
        movieTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieTable.setBackground(panelColor);
        movieTable.setForeground(textColor);
        movieTable.setSelectionBackground(gold);
        movieTable.setSelectionForeground(Color.BLACK);
        movieTable.setGridColor(gold);
        movieTable.getTableHeader().setBackground(gold);
        movieTable.getTableHeader().setForeground(Color.BLACK);
        movieTable.setRowHeight(28);

        JScrollPane scrollPane = new JScrollPane(movieTable);
        scrollPane.getViewport().setBackground(panelColor);

        JButton addButton = new JButton("Add Movie");
        JButton editButton = new JButton("Edit Movie");
        JButton deleteButton = new JButton("Delete Movie");
        JButton scarinessButton = new JButton("Show Scariness");
        JButton refreshButton = new JButton("Refresh");

        JButton[] buttons = {addButton, editButton, deleteButton, scarinessButton, refreshButton};

        for (JButton btn : buttons) {
            btn.setBackground(gold);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(gold, 2),
                    BorderFactory.createEmptyBorder(6, 14, 6, 14)
            ));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(goldHover);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(goldHover, 2),
                            BorderFactory.createEmptyBorder(6, 14, 6, 14)
                    ));
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(gold);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(gold, 2),
                            BorderFactory.createEmptyBorder(6, 14, 6, 14)
                    ));
                    btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            });
        }

        addButton.addActionListener(this::addMovie);
        editButton.addActionListener(this::editMovie);
        deleteButton.addActionListener(this::deleteMovie);
        scarinessButton.addActionListener(this::showScariness);
        refreshButton.addActionListener(e -> loadMovies());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(background);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(scarinessButton);
        buttonPanel.add(refreshButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadMovies();
        setVisible(true);
    }

    public void loadMovies() {
        tableModel.setRowCount(0);
        ArrayList<Movie> movies = db.getAllMovies();
        for (Movie m : movies) {
            tableModel.addRow(new Object[]{
                    m.getId(), m.getTitle(), m.getYear(), m.getDirector(),
                    m.getRating(), m.getRuntimeMinutes(), m.getVotes(),
                    m.isWatched() ? "Yes" : "No"
            });
        }
    }

    private Movie getSelectedMovie() {
        int row = movieTable.getSelectedRow();
        if (row == -1) return null;
        return db.getMovieById((int) tableModel.getValueAt(row, 0));
    }

    private void addMovie(ActionEvent e) {
        Movie newMovie = MovieDialogGUI.showAddDialog(this);
        if (newMovie != null) {
            db.addMovie(newMovie);
            loadMovies();
        }
    }

    private void editMovie(ActionEvent e) {
        Movie selected = getSelectedMovie();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a movie to edit.");
            return;
        }
        Movie updated = MovieDialogGUI.showEditDialog(this, selected);
        if (updated != null) {
            db.updateMovie(updated);
            loadMovies();
        }
    }

    private void deleteMovie(ActionEvent e) {
        Movie selected = getSelectedMovie();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a movie to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete \"" + selected.getTitle() + "\"?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            db.deleteMovie(selected.getId());
            loadMovies();
        }
    }

    private void showScariness(ActionEvent e) {
        Movie selected = getSelectedMovie();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a movie first.");
            return;
        }
        MovieDialogGUI.showScarinessDialog(this, selected);
    }
}
