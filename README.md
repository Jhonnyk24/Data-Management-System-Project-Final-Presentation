📽️ Movie Data Management System (DMS) — Phase 4
A Java GUI + MySQL CRUD Application with Custom Scariness Calculator
📌 Project Overview

The Movie Data Management System (DMS) is a full-stack desktop application developed in Java.
It allows users to Create, Read, Update, and Delete (CRUD) movie records stored in a MySQL database.
The system features an elegant graphical interface, robust validation, and a unique Scariness Score feature.

This project was developed following the Software Development Life Cycle (SDLC) and completed across multiple phases (Logic → GUI → Database → Debugging).

🛠️ Features

✔️ Graphical User Interface (Java Swing)
✔️ Full CRUD operations
✔️ Connects to a user-provided MySQL database (no hardcoding!)
✔️ Robust validation and error handling
✔️ Custom Scariness Score calculation
✔️ Professionally styled GUI (dark/gold theme)
✔️ Modular, well-commented object-oriented architecture

🗂️ Project Structure
src/
 ├── Main.java                   → Program entry point  
 ├── DBConnectionDialog.java     → Collects and validates DB connection details  
 ├── Movie.java                  → Movie data model  
 ├── MovieDatabaseManager.java   → CRUD operations using JDBC  
 ├── MovieDialogGUI.java         → Add/Edit movie popup dialog  
 └── MovieGUI.java               → Main GUI window  

⚙️ Technologies Used

Java 17

Swing GUI Framework

MySQL Database

JDBC

IntelliJ IDEA (recommended IDE)

🗄️ Database Requirements

Create a database named:

moviedb


Create the required table:

CREATE TABLE movies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    director VARCHAR(255) NOT NULL,
    rating DOUBLE NOT NULL,
    runtimeMinutes INT NOT NULL,
    votes INT NOT NULL,
    watched TINYINT(1) NOT NULL
);


Insert sample data (optional):

INSERT INTO movies (title, year, director, rating, runtimeMinutes, votes, watched)
VALUES
('The Conjuring', 2013, 'James Wan', 7.5, 112, 850000, 1),
('Hereditary', 2018, 'Ari Aster', 7.3, 127, 500000, 0);

🚀 How to Run the Project
1. Open project in IntelliJ
2. Add MySQL Connector/J

Download MySQL JDBC Driver:
https://dev.mysql.com/downloads/connector/j/

Add the .jar file to your project:
File → Project Structure → Libraries → Add

3. Run Main.java

The program will open a database connection window:

JDBC Examples
Database Type	Example URL
MySQL	jdbc:mysql://localhost:3306/moviedb
SQLite	jdbc:sqlite:C:/data/movies.db

Enter your username & password → Click Connect

4. Enjoy the GUI

You can now:

Add movies

Edit movies

Delete movies

Refresh database

Calculate scariness score

🎃 Scariness Score Formula

Each movie receives a score 0–10 based on:

IMDb rating

Number of votes

Runtime

Whether you’ve watched it

public double getScariness() {
    double score = rating;
    score += Math.min(votes / 500000.0, 2);
    if (runtimeMinutes > 120) score += 1;
    if (watched) score -= 1;
    return Math.max(0, Math.min(10, score));
}

📸 Screenshots (Optional)

You can add images here later:

![Main GUI](images/gui.png)
![Add Movie Dialog](images/add_movie.png)

📘 SDLC Summary
Phase 1 — Logic

Created Movie class

Added validation + CSV support

Designed scariness score algorithm

Phase 2 — GUI

Built Swing interface

Table display + buttons

Dialog windows for editing movies

Phase 3 — Database Integration

Added MySQL support

Implemented full CRUD using JDBC

Added connection dialog

Phase 4 — Debugging and Polishing

Refactored code structure

Improved comments

Applied consistent styling

Ensured rubric compliance

🧑‍💻 Author

Jhonny Sousa
Data Management System — Final Project
