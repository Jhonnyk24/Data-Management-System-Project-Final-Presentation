import java.time.Year;

/**
 * Represents a single movie entity within the Data Management System (DMS).
 * <p>
 * Each {@code Movie} object encapsulates metadata such as title, release year,
 * director, rating, runtime, votes, and whether it has been watched.
 * </p>
 *
 * <p><b>Role in the System:</b></p>
 * Acts as the data model for the application. It is used by
 * {@link MovieDatabaseManager} for database storage/retrieval,
 * and by the GUI classes (e.g., {@link MovieGUI}, {@link MovieDialogGUI})
 * to display and edit movie data.
 *
 * <p><b>Validation:</b></p>
 * This class includes input validation in methods such as {@link #fromCSV(String)}
 * and the GUI components that instantiate it.
 */
public class Movie {

    /** Unique identifier of the movie (primary key in the database). */
    private int id;

    private String title;
    private int year;
    private String director;
    private double rating;         // Range: 0.0 - 10.0
    private int runtimeMinutes;    // Must be positive
    private int votes;             // Non-negative
    private boolean watched;

    // ==================== CONSTRUCTORS ====================

    public Movie(int id, String title, int year, String director,
                 double rating, int runtimeMinutes, int votes, boolean watched) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
        this.votes = votes;
        this.watched = watched;
    }

    public Movie(String title, int year, String director,
                 double rating, int runtimeMinutes, int votes, boolean watched) {
        this.title = title;
        this.year = year;
        this.director = director;
        this.rating = rating;
        this.runtimeMinutes = runtimeMinutes;
        this.votes = votes;
        this.watched = watched;
    }

    /** Default no-argument constructor. */
    public Movie() {}

    // ==================== GETTERS ====================

    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getDirector() { return director; }
    public double getRating() { return rating; }
    public int getRuntimeMinutes() { return runtimeMinutes; }
    public int getVotes() { return votes; }
    public boolean isWatched() { return watched; }

    // ==================== SETTERS ====================

    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setYear(int year) { this.year = year; }
    public void setDirector(String director) { this.director = director; }
    public void setRating(double rating) { this.rating = rating; }
    public void setRuntimeMinutes(int runtimeMinutes) { this.runtimeMinutes = runtimeMinutes; }
    public void setVotes(int votes) { this.votes = votes; }
    public void setWatched(boolean watched) { this.watched = watched; }

    // ==================== METHODS ====================

    /**
     * Calculates a "scariness" score for the movie based on rating, votes, runtime, and watch status.
     */
    public double getScariness() {
        double score = rating;
        score += Math.min(votes / 500000.0, 2);
        if (runtimeMinutes > 120) score += 1;
        if (watched) score -= 1;
        score = Math.max(0, Math.min(10, score));
        return Math.round(score * 10.0) / 10.0;
    }

    /**
     * Returns a formatted string representation of the movie with key details.
     */
    public String prettyPrint() {
        return String.format(
                "%s (%d)\nDirector: %s\nRating: %.1f\nRuntime: %d min\nVotes: %d\nWatched: %s\nScariness: %.1f",
                title, year, director, rating, runtimeMinutes, votes,
                watched ? "Yes" : "No", getScariness()
        );
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%s,%.1f,%d,%d,%s",
                title, year, director, rating, runtimeMinutes, votes, watched);
    }

    /**
     * Parses a CSV line into a Movie. (Not used by DB version, but kept for completeness.)
     */
    public static Movie fromCSV(String line) {
        if (line == null) throw new IllegalArgumentException("Line is null");

        String[] parts = line.split(",", -1);
        if (parts.length != 7) throw new IllegalArgumentException("Expected 7 fields but found " + parts.length);

        for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

        String title = parts[0];
        if (title.isEmpty()) throw new IllegalArgumentException("Title is empty");

        int year;
        try {
            year = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year");
        }
        int currentYear = Year.now().getValue();
        if (year < 1888 || year > currentYear) throw new IllegalArgumentException("Invalid year");

        String director = parts[2];
        if (director.isEmpty()) throw new IllegalArgumentException("Director is empty");

        double rating;
        try {
            rating = Double.parseDouble(parts[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid rating");
        }
        if (rating < 0 || rating > 10) throw new IllegalArgumentException("Rating must be 0–10");

        int runtime;
        try {
            runtime = Integer.parseInt(parts[4]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid runtime");
        }
        if (runtime <= 0) throw new IllegalArgumentException("Runtime must be positive");

        int votes;
        try {
            votes = Integer.parseInt(parts[5]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid votes");
        }
        if (votes < 0) throw new IllegalArgumentException("Votes cannot be negative");

        boolean watched = parts[6].equalsIgnoreCase("true")
                || parts[6].equalsIgnoreCase("yes")
                || parts[6].equalsIgnoreCase("y")
                || parts[6].equals("1");

        return new Movie(title, year, director, rating, runtime, votes, watched);
    }
}
