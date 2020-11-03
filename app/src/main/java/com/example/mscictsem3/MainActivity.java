package com.example.mscictsem3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    EditText edt_uname,edt_pwd;
    Button btn_login;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_uname=findViewById(R.id.edt_uname);
        edt_pwd=findViewById(R.id.edt_pwd);
        btn_login=findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                async_login obj =new async_login();
                obj.execute(edt_uname.getText().toString(),edt_pwd.getText().toString());
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
                i = new Intent(MainActivity.this,MainActivity.class);
                startActivity(i);
                break;
            case R.id.Signup:
                i = new Intent(MainActivity.this,SignupActivity.class);
                startActivity(i);
                break;
        }

        return true;
    }

    public class async_login extends AsyncTask<String,String,String>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=ProgressDialog.show(MainActivity.this,"Please Wait..",null);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.cancel();

            try {
                if(!s.toString().equals("Login Fail")) {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    Toast.makeText(MainActivity.this, jsonArray.getJSONObject(0).getString("username").toString(), Toast.LENGTH_LONG).show();
                    ;
                    sharedPreferences = getSharedPreferences("id",MODE_PRIVATE);
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putString("id",jsonArray.getJSONObject(0).getString("_id").toString());
                    editor.commit();
                }
                else
                {
                    Toast.makeText(MainActivity.this, s.toString(), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://10.0.2.2:8080/login?username="+strings[0]+"&password="+strings[1]+"");
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