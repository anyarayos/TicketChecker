package com.example.application.ticketchecker;

public class Ticket {

    String studentNumber, movieName, checkerName, time;

    public Ticket(String studentNumber, String movieName, String checkerName, String time) {
        this.studentNumber = studentNumber;
        this.movieName = movieName;
        this.checkerName = checkerName;
        this.time = time;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public String getTime() {
        return time;
    }
}
