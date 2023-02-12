package com.example.application.ticketchecker;

public class SqlStatements {
    private String checkTicket = "SELECT student_no, film_name, checker_name, time_format(date_time,\"%h:%i %p\") from tickets WHERE student_no = (?);";
    private String insertTicket = "INSERT INTO tickets\n" +
            "(checker_name,student_no,film_name,date_time)\n" +
            "values((?),(?),(?),current_timestamp());";

    public String getChecker() {
        return checkTicket;
    }

    public String getInserter() {
        return insertTicket;
    }
}
