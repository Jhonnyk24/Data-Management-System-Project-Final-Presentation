import javax.swing.*;
import java.awt.*;

/**
 * Dialog window for adding or editing {@link Movie} objects.
 */
public class MovieDialogGUI extends JDialog {

    private JTextField titleField, yearField, directorField, ratingField, runtimeField, votesField;
    private JCheckBox watchedBox;
    private Movie movie;

    private MovieDialogGUI(Frame parent, String title) {
        super(parent, title, true);
        setSize(400, 420);
        setLayout(new GridLayout(9, 2, 8, 8));
        setLocationRelativeTo(parent);

        Color background = new Color(43, 43, 43);
        Color panelColor = new Color(58, 58, 58);
        Color textColor = Color.WHITE;
        Color gold = new Color(201, 168, 106);

        getContentPane().setBackground(background);

        addStyledLabel("Title:", textColor);
        titleField = createTextField(panelColor, textColor); add(titleField);

        addStyledLabel("Year:", textColor);
        yearField = createTextField(panelColor, textColor); add(yearField);

        addStyledLabel("Director:", textColor);
        directorField = createTextField(panelColor, textColor); add(directorField);

        addStyledLabel("Rating (0-10):", textColor);
        ratingField = createTextField(panelColor, textColor); add(ratingField);

        addStyledLabel("Runtime (minutes):", textColor);
        runtimeField = createTextField(panelColor, textColor); add(runtimeField);

        addStyledLabel("Votes:", textColor);
        votesField = createTextField(panelColor, textColor); add(votesField);

        addStyledLabel("Watched:", textColor);
        watchedBox = new JCheckBox();
        watchedBox.setBackground(background);
        watchedBox.setForeground(textColor);
        add(watchedBox);

        JButton confirmBtn = styledButton("Save", gold);
        JButton cancelBtn = styledButton("Cancel", gold);

        confirmBtn.addActionListener(e -> attemptSave());
        cancelBtn.addActionListener(e -> {
            movie = null;
            dispose();
        });

        add(confirmBtn);
        add(cancelBtn);
    }

    private void addStyledLabel(String text, Color textColor) {
        JLabel label = new JLabel(text);
        label.setForeground(textColor);
        add(label);
    }

    private JTextField createTextField(Color bg, Color fg) {
        JTextField f = new JTextField();
        f.setBackground(bg);
        f.setForeground(fg);
        f.setCaretColor(fg);
        return f;
    }

    private JButton styledButton(String text, Color gold) {
        JButton btn = new JButton(text);
        btn.setBackground(gold);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        return btn;
    }

    private void attemptSave() {
        try {
            String title = titleField.getText().trim();
            String director = directorField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            double rating = Double.parseDouble(ratingField.getText().trim());
            int runtime = Integer.parseInt(runtimeField.getText().trim());
            int votes = Integer.parseInt(votesField.getText().trim());
            boolean watched = watchedBox.isSelected();

            int currentYear = java.time.Year.now().getValue();
            if (title.isEmpty()) throw new IllegalArgumentException("Title cannot be empty.");
            if (director.isEmpty()) throw new IllegalArgumentException("Director cannot be empty.");
            if (year < 1888 || year > currentYear) throw new IllegalArgumentException("Year must be between 1888 and " + currentYear + ".");
            if (rating < 0 || rating > 10) throw new IllegalArgumentException("Rating must be between 0 and 10.");
            if (runtime <= 0) throw new IllegalArgumentException("Runtime must be positive.");
            if (votes < 0) throw new IllegalArgumentException("Votes cannot be negative.");

            movie = new Movie(0, title, year, director, rating, runtime, votes, watched);
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter valid numeric values for Year, Rating, Runtime, and Votes.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Movie showAddDialog(Frame parent) {
        MovieDialogGUI dialog = new MovieDialogGUI(parent, "Add Movie");
        dialog.setVisible(true);
        return dialog.movie;
    }

    public static Movie showEditDialog(Frame parent, Movie m) {
        MovieDialogGUI dialog = new MovieDialogGUI(parent, "Edit Movie");

        dialog.titleField.setText(m.getTitle());
        dialog.yearField.setText(String.valueOf(m.getYear()));
        dialog.directorField.setText(m.getDirector());
        dialog.ratingField.setText(String.valueOf(m.getRating()));
        dialog.runtimeField.setText(String.valueOf(m.getRuntimeMinutes()));
        dialog.votesField.setText(String.valueOf(m.getVotes()));
        dialog.watchedBox.setSelected(m.isWatched());

        dialog.setVisible(true);

        if (dialog.movie != null) {
            dialog.movie.setId(m.getId());
        }
        return dialog.movie;
    }

    public static void showScarinessDialog(Frame parent, Movie m) {
        double score = m.getScariness();
        JOptionPane.showMessageDialog(parent,
                "Scariness Score for \"" + m.getTitle() + "\":\n\n" + score + " / 10",
                "Scariness Score",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
