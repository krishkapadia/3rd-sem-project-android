package com.example.mscictsem3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupActivity extends AppCompatActivity {


    EditText edt_uname,edt_pwd,edt_email,edt_phno;
    Button btn_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edt_uname=findViewById(R.id.edt_uname);
        edt_pwd=findViewById(R.id.edt_pwd);
        edt_email=findViewById(R.id.edt_email);
        edt_phno=findViewById(R.id.edt_phno);
        btn_signup=findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                async_signup async_signup=new async_signup();
                async_signup.execute(edt_uname.getText().toString(),edt_pwd.getText().toString(),edt_email.getText().toString(),edt_phno.getText().toString());
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_signup,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i ;
        switch (item.getItemId()){
            case R.id.Login:
                i = new Intent(SignupActivity.this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.Signup:
                i = new Intent(SignupActivity.this,SignupActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }

    public class async_signup extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(SignupActivity.this,"Please Wait..",null);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();

            try {
                if(!s.toString().equals("User Already Signup..")) {
                    Toast.makeText(SignupActivity.this, "Singup Successfull", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SignupActivity.this, s.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://10.0.2.2:8080/signup?username="+strings[0]+"&password="+strings[1]+"&email="+strings[2]+"&phone="+strings[3]+"");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                return bufferedReader.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}