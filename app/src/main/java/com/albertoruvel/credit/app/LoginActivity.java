package com.albertoruvel.credit.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.albertoruvel.credit.app.data.req.SigninRequest;
import com.albertoruvel.credit.app.data.resp.AuthenticationResult;
import com.albertoruvel.credit.app.data.resp.TokenValidationResult;
import com.albertoruvel.credit.app.retro.AccountServiceClient;
import com.albertoruvel.credit.app.retro.RetroFactory;
import com.albertoruvel.credit.app.util.PrefsUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loginTextView)
    TextView textView;

    @BindView(R.id.loginEmailText)
    EditText email;

    @BindView(R.id.loginPasswordText)
    EditText password;

    @BindView(R.id.loginButton)
    Button login;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    private ProgressDialog progressDialog;
    private final AccountServiceClient client = RetroFactory.getInstance(AccountServiceClient.class);
    private static final String DEBUG_TAG = LoginActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        //TODO:check if there are any security token
    }

    @OnClick(R.id.loginTextView)
    public void onCLick() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    @OnClick(R.id.loginButton)
    public void login() {
        //validate fields
        final String emailValue = email.getText().toString();
        final String passwordValue = password.getText().toString();
        if (emailValue.isEmpty()) {
            Snackbar.make(coordinatorLayout, getString(R.string.invalidEmailMessage), Snackbar.LENGTH_LONG).show();
            return;
        } else if (passwordValue.isEmpty()) {
            Snackbar.make(coordinatorLayout, getString(R.string.invalidPasswordMessage), Snackbar.LENGTH_LONG).show();
            return;
        }

        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage(getString(R.string.signinMessage));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        //create sign in call
        client.signin(new SigninRequest(emailValue, passwordValue))
                .enqueue(new Callback<AuthenticationResult>() {
                    @Override
                    public void onResponse(Call<AuthenticationResult> call, Response<AuthenticationResult> response) {
                        switch (response.code()) {
                            case 200:
                                //get body
                                AuthenticationResult result = response.body();
                                if (result.isSuccess()) {
                                    //save token and start main activity
                                    PrefsUtils.saveUserToken(LoginActivity.this, result.getToken());
                                    //start main activity
                                    Toast.makeText(LoginActivity.this, getString(R.string.welcomeMessage), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    //show snackbar
                                    Snackbar.make(coordinatorLayout, getString(R.string.invalidCredentialsMessage), Snackbar.LENGTH_LONG).show();
                                }
                                break;
                            case 500:
                                try {
                                    Log.e(DEBUG_TAG, response.errorBody().string());
                                    Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                                } catch (IOException ex) {
                                    Log.e(DEBUG_TAG, getString(R.string.responseIOExceptionMessage));
                                }
                                break;
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<AuthenticationResult> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.e(DEBUG_TAG, "Error trying to sign in", t);
                        Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                    }
                });

    }
}
