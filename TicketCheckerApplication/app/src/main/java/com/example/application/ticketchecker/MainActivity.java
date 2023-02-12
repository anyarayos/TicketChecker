package com.example.application.ticketchecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.os.Bundle;
import android.text.BidiFormatter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editTextStudentNumber;
    Spinner spinnerMovies;
    Button buttonRegisterStudent;

    Dialog dialog;
    String ticketCheckerName, studentNumber, movieName;

    String existingStudentNumber, existingMovieName, existingChecker, existingTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        editTextStudentNumber = findViewById(R.id.studentNumber_);
        spinnerMovies = findViewById(R.id.studentMovie_);
        buttonRegisterStudent = findViewById(R.id.registerStudent);

        editTextStudentNumber.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    buttonRegisterStudent.setEnabled(false);
                } else {
                    buttonRegisterStudent.setEnabled(true);
                }
            }
        });

        ticketCheckerName = "";

        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.background_dialog));
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        if (ticketCheckerName.equals("")){
            dialog.setContentView(R.layout.dialog_user);
            Button buttonSaveChecker = dialog.findViewById(R.id.saveChecker);
            EditText editTextCheckerName = dialog.findViewById(R.id.checkerName_);

            editTextCheckerName.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override public void afterTextChanged(Editable s) { }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(s.toString().trim().length()==0){
                        buttonSaveChecker.setEnabled(false);
                    } else {
                        buttonSaveChecker.setEnabled(true);
                    }
                }
            });

            buttonSaveChecker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ticketCheckerName = editTextCheckerName.getText().toString();
                    ticketCheckerName = ticketCheckerName.substring(0,1).toUpperCase() + ticketCheckerName.substring(1).toLowerCase();
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this,"Welcome "+ticketCheckerName+". Goodluck and thank you!",Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();
        }
    }

    public void registerMethod (View view) {
        studentNumber = editTextStudentNumber.getText().toString();
        movieName = spinnerMovies.getSelectedItem().toString();
        //ticketCheckerName = may value na to dapat pag nag login

        /*
        NAT. ETO YUNG NEEDED NA COLUMN
        - ID AUTO INCREMENT
        - STUDENT_NUM = (studentNumber)
        - MOVIE = (movieName)
        - CHECKER_NAME = (ticketCheckerName)
        - IME Kelan na register. pwede ba yon? hahahahaha

        yung equals parenthesis, yun yung string. nandito na din sa method na to yooon.

         */

        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.this);
        loadingDialog.startLoadingDialog();
        DatabaseConnection checkStudentNo = new DatabaseConnection(DatabaseConnection.check_Ticket,
                new AsyncResponse() {
                    @Override
                    public void onFinish(Object output) {
                        if(output==null) {
                            DatabaseConnection insertTicket = new DatabaseConnection(DatabaseConnection.insert_Ticket);
                            insertTicket.execute(ticketCheckerName,studentNumber,movieName);//checker_name,student_no,film_name

                            loadingDialog.dismissDialog();

                            //condition if successfully added to database
                            Toast.makeText(MainActivity.this, "Student Added! Add ticket now.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //condition if not successfully added to database
                            Toast.makeText(MainActivity.this,"Student is already on the list.",Toast.LENGTH_SHORT).show();
                            ArrayList<Ticket>tickets = new ArrayList<>();
                            tickets = (ArrayList<Ticket>) output;

                            //lagaaaay mo din pala nat yung mga value ditoo pag existing na. thaaankss naaat
                            existingStudentNumber = tickets.get(0).getStudentNumber();
                            existingMovieName = tickets.get(0).getMovieName();
                            existingChecker = tickets.get(0).getCheckerName();
                            existingTime = tickets.get(0).getTime();

                            loadingDialog.dismissDialog();

                            Toast.makeText(MainActivity.this, "Existing Info: "+ existingStudentNumber
                                    + existingMovieName + existingChecker + existingTime, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        checkStudentNo.execute(studentNumber);



    }
}