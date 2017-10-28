package com.albertoruvel.credit.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.albertoruvel.credit.app.data.resp.ExecutionResult;
import com.albertoruvel.credit.app.data.resp.UserConfiguration;
import com.albertoruvel.credit.app.retro.AccountServiceClient;
import com.albertoruvel.credit.app.retro.RetroFactory;
import com.albertoruvel.credit.app.util.PrefsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.settingsNotificationLayout)
    LinearLayout notificationLayout;

    @BindView(R.id.settingsNotificationMessage)
    TextView notificationMessage;

    @BindView(R.id.settingsNotificationDismissButton)
    Button notificationDismiss;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.settingsName)
    TextView name;

    @BindView(R.id.settingsEmail)
    TextView email;

    @BindView(R.id.settingsMonthlyIncome)
    EditText monthlyIncome;

    @BindView(R.id.settingsManageCreditCards)
    TextView manageCreditCards;

    @BindView(R.id.settingsNotifications)
    Switch notificationsSwitch;

    @BindView(R.id.settingsBadRated)
    TextView badRated;

    private final AccountServiceClient serviceClient = RetroFactory.getInstance(AccountServiceClient.class);
    private ProgressDialog progressDialog;
    private static final String DEBUG_TAG = SettingsActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (PrefsUtils.isFirstTimeUser(this)) {
            //show notification message with first time usage
            notificationMessage.setText(getString(R.string.firstTimeUsageSettingsMessage));
        } else {
            notificationMessage.setText(getString(R.string.dataSavingMessage));
        }
        notificationLayout.setVisibility(View.VISIBLE);
        //get settings
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.configurationFetchMessage));
        progressDialog.show();

        final String token = PrefsUtils.getUserToken(this);
        serviceClient.getConfiguration(token).enqueue(new Callback<UserConfiguration>() {
            @Override
            public void onResponse(Call<UserConfiguration> call, Response<UserConfiguration> response) {
                switch (response.code()) {
                    case 200:
                        UserConfiguration result = response.body();
                        //load config to UI
                        loadConfiguration(result);
                        break;
                    case 500:
                        Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                        break;
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserConfiguration> call, Throwable t) {
                Log.e(DEBUG_TAG, "Error retrieving user configuration", t);
                Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    private void loadConfiguration(UserConfiguration result) {
        name.setText(result.getName());
        email.setText(result.getEmail());
        monthlyIncome.setText(result.getMonthlyIncome().toString());
        notificationsSwitch.setChecked(result.isNotificationEnabled());

    }

    @OnClick(R.id.settingsName)
    public void nameClick() {
        showPlaceholderSnackbar();
    }

    @OnClick(R.id.settingsEmail)
    public void emailClick() {
        showPlaceholderSnackbar();
    }

    @OnClick(R.id.settingsManageCreditCards)
    public void manageCreditCardsClick() {
        showPlaceholderSnackbar();
    }

    @OnClick(R.id.settingsDeleteData)
    public void deleteDataClick() {
        showPlaceholderSnackbar();
    }

    @OnClick(R.id.settingsBadRated)
    public void badRatedClick() {
        showPlaceholderSnackbar();
    }


    private void showPlaceholderSnackbar() {
        Snackbar.make(coordinatorLayout, "This functionality will be added soon", Snackbar.LENGTH_LONG).show();
    }


    @OnClick(R.id.settingsNotificationDismissButton)
    public void dismissNotification() {
        notificationLayout.setVisibility(View.GONE);
        //TODO: set firstTime user preference to false and message will not be shown again
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.settingsSave:
                //save and exit
                try {
                    saveCurrentValues();
                } catch (Exception ex) {
                    //ignore as message has been shown
                }
                break;
        }

        return true;
    }

    private void saveCurrentValues() throws Exception {
        final String currentName = name.getText().toString();
        final String currentEmail = email.getText().toString();
        final boolean notificationsEnabled = notificationsSwitch.isChecked();
        final Double currentMonthlyIncome = Double.parseDouble(monthlyIncome.getText().toString());

        //ask if wants to save
        new AlertDialog.Builder(this, R.style.AppTheme_Dark_Dialog)
                .setMessage("Do you want to save this configuration?")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialogInterface, int i) {
                        final String token = PrefsUtils.getUserToken(SettingsActivity.this);
                        final UserConfiguration request = new UserConfiguration();
                        request.setName(currentName);
                        request.setEmail(currentEmail);
                        request.setNotificationEnabled(notificationsEnabled);
                        request.setMonthlyIncome(currentMonthlyIncome);
                        serviceClient.saveUserConfiguration(token, request).enqueue(new Callback<ExecutionResult>() {
                            @Override
                            public void onResponse(Call<ExecutionResult> call, Response<ExecutionResult> response) {
                                switch (response.code()) {
                                    case 200:
                                        if (response.body().isOk()) {
                                            Toast.makeText(SettingsActivity.this, "Preferences have been saved", Toast.LENGTH_LONG).show();
                                            PrefsUtils.saveCurrentUserConfiguration(SettingsActivity.this, request);
                                            finish();
                                        } else {
                                            Snackbar.make(coordinatorLayout, "Could not save preferences: " + response.body().getMessage(), Snackbar.LENGTH_LONG).show();
                                        }
                                        break;
                                    case 500:
                                        Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                                        break;
                                }
                                dialogInterface.dismiss();
                                finish();
                            }

                            @Override
                            public void onFailure(Call<ExecutionResult> call, Throwable t) {
                                Log.e(DEBUG_TAG, "Failed to save preferences", t);
                                Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .create().show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
}
