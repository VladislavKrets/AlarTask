package com.alarstudios.alartask;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginButton;
    private EditText loginEditText, passwordEditText;
    private TextView errorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        initializeWidgets();

    }

    private void initializeWidgets() {
        loginButton = findViewById(R.id.loginButton);
        loginEditText = findViewById(R.id.loginEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        errorTextView = findViewById(R.id.errorTextView);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                try {
                    errorTextView.setText(new LoginTask().execute().get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    errorTextView.setText(getResources().getString(R.string.interrupted));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {
        private String login;
        private String password;
        private boolean isOk;
        private String code;

        @Override
        protected void onPreExecute() {
            login = loginEditText.getText().toString();
            password = passwordEditText.getText().toString();
            isOk = false;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (login == null || password == null
                    || login.isEmpty() || password.isEmpty())
                return getResources().getString(R.string.fields_not_filled);
            else {
                try {
                    String urlParameters = String.format("username=%s&password=%s", login, password);
                    String answer = Utils.getRequest("http://condor.alarstudios.com/test/auth.cgi", urlParameters);
                    Gson gson = new Gson();
                    JsonElement element = gson.fromJson(answer, JsonElement.class);
                    String status = element.getAsJsonObject().get("status").getAsString();
                    if (status.equals("ok")) {
                        code = element.getAsJsonObject().get("code").getAsString();
                        isOk = true;
                    } else {
                        return getResources().getString(R.string.invalid);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return getResources().getString(R.string.internet_connection);
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (isOk) {
                //todo
                Intent intent = new Intent(LoginActivity.this, DataActivity.class);
                intent.putExtra("code", code);
                startActivity(intent);
                finish();
            }

        }
    }
}
