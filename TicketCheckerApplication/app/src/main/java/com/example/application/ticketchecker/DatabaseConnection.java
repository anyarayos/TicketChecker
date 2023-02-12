package com.example.application.ticketchecker;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseConnection extends AsyncTask<String, String, Object> {
    private String TAG = "DATABASE_CONNECTION_DEBUG";
    private String method;
    private AsyncResponse asyncResponse;
    private PreparedStatement statement;
    private SqlStatements sqlStatements = new SqlStatements();
    private ResultSet resultSet;
    private Connection connection;

    public static String check_Ticket = "check_Ticket";
    public static String insert_Ticket = "insert_Ticket";

    public DatabaseConnection(String method,AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
        this.method = method;
    }

    public DatabaseConnection(String method) {
        this.method = method;
    }

    private void setConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.11:3306/ticket_checker", "admin", "admin");
        } catch (Exception e) {
            Log.i("DATABASE CONNECTION:", e.toString());
        }
    }

    public static  void disconnect(ResultSet rs, PreparedStatement stat, Connection cn){
        String TAG = "disconnect";
        try{
            if(rs!=null) rs.close();
        }catch(SQLException sqlEx){
            Log.d(TAG, "resultset disconnection error ");
        }
        try{
            if(stat!=null) stat.close();
        }catch(SQLException sqlEx){
            Log.d(TAG, "statement disconnection error ");

        }
        try{
            if(cn!=null) cn.close();
        }catch(SQLException sqlEx){
            Log.d(TAG, "connection disconnection error ");

        }
    }

    @Override
    protected Object doInBackground(String... params) {
        setConnection();
        if(connection!=null){
            try{
                if(method.equals(check_Ticket)){
                    statement = connection.prepareStatement(sqlStatements.getChecker());
                    ArrayList<Ticket>tickets = new ArrayList<>();
                    String student_no = params[0];
                    statement.setString(1,student_no);
                    resultSet = statement.executeQuery();
                    if (!resultSet.isBeforeFirst()) {
                        Log.d(TAG, "NO DATA FOUND");
                    } else {
                        Log.d(TAG, "DATA FOUND");
                        while (resultSet.next()) {
                            tickets.add(new Ticket(resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4)));
                        }
                        return tickets;
                    }
                }
                if(method.equals(insert_Ticket)){
                    statement = connection.prepareStatement(sqlStatements.getInserter());
                    String checker_name = params[0];
                    String student_no = params[1];
                    String film_name = params[2];
                    statement.setString(1,checker_name);
                    statement.setString(2,student_no);
                    statement.setString(3,film_name);
                    statement.executeUpdate();
                }
                disconnect(resultSet,statement,connection);
            }
            catch(Exception e){
                Log.i("SQL statements", e.toString());
            }
        }
        return null;
        }

    @Override
    protected void onPostExecute(Object o) {
        try {
            super.onPostExecute(o);
            if(o!=null){
                asyncResponse.onFinish(o);
            }else{
                asyncResponse.onFinish(null);
            }
        }
        catch (Exception e){
            Log.i("onPostExecute", e.toString());
        }
    }
}
