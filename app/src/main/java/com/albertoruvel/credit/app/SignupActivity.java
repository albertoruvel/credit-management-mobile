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

import com.albertoruvel.credit.app.data.req.SignupRequest;
import com.albertoruvel.credit.app.data.resp.AuthenticationResult;
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

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.signupName)
    EditText name;

    @BindView(R.id.signupLastName)
    EditText lastName;

    @BindView(R.id.signupEmail)
    EditText email;

    @BindView(R.id.signupPassword)
    EditText password;

    @BindView(R.id.signupButton)
    Button signupButton;

    private static final String DEBUG_TAG = SignupActivity.class.getCanonicalName();
    private AccountServiceClient serviceClient = RetroFactory.getInstance(AccountServiceClient.class);
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signupButton)
    public void signup() {
        final String nameValue = name.getText().toString();
        final String lastNameValue = lastName.getText().toString();
        final String emailValue = email.getText().toString();
        final String passwordValue = password.getText().toString();

        //validate
        if (nameValue.isEmpty()) {
            Snackbar.make(coordinatorLayout, getString(R.string.invalidNameMessage), Snackbar.LENGTH_LONG).show();
            return;
        } else if (lastNameValue.isEmpty()) {
            Snackbar.make(coordinatorLayout, getString(R.string.invalidLastNameMessage), Snackbar.LENGTH_LONG).show();
            return;
        } else if (emailValue.isEmpty()) {
            Snackbar.make(coordinatorLayout, getString(R.string.invalidEmailMessage), Snackbar.LENGTH_LONG).show();
            return;
        } else if (passwordValue.isEmpty()) {
            Snackbar.make(coordinatorLayout, getString(R.string.invalidPasswordMessage), Snackbar.LENGTH_LONG).show();
            return;
        }

        //show progress dialog
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage(getString(R.string.signupMessage));
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        //create account
        serviceClient.signup(new SignupRequest(emailValue, nameValue, lastNameValue, passwordValue))
                .enqueue(new Callback<AuthenticationResult>() {
                    @Override
                    public void onResponse(Call<AuthenticationResult> call, Response<AuthenticationResult> response) {
                        switch (response.code()) {
                            case 200:
                                AuthenticationResult result = response.body();
                                if (result.isSuccess()) {
                                    //save token to preferences
                                    PrefsUtils.saveUserToken(SignupActivity.this, result.getToken());
                                    //start main activity
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } else {
                                    //user could not be created due something else
                                    final String message = result.getMessage();
                                    Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
                                }
                                break;
                            case 500:
                                try{
                                    Log.e(DEBUG_TAG, response.errorBody().string());
                                    Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                                }catch(IOException ex){
                                    Log.e(DEBUG_TAG, "Could not create account", ex);
                                }
                                break;
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<AuthenticationResult> call, Throwable t) {
                        Log.e(DEBUG_TAG, getString(R.string.unexpectedErrorMessage), t);
                    }
                });
    }
}
