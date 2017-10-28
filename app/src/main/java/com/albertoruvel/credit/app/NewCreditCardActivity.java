package com.albertoruvel.credit.app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.albertoruvel.credit.app.data.req.NewCreditCardRequest;
import com.albertoruvel.credit.app.data.resp.ExecutionResult;
import com.albertoruvel.credit.app.retro.CreditCardServiceClient;
import com.albertoruvel.credit.app.retro.RetroFactory;
import com.albertoruvel.credit.app.util.PrefsUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCreditCardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.newCreditCardName)
    EditText name;

    @BindView(R.id.newCreditCardAnnualFee)
    EditText annualFee;

    @BindView(R.id.newCreditCardAllowedSalary)
    EditText allowedSalary;

    @BindView(R.id.newCreditCardPeriodDate)
    Button periodDate;

    @BindView(R.id.newCreditCardPayDate)
    Button payDate;

    private DatePickerDialog datePickerDialog;
    private static final int PAY_DATE = 1;
    private static final int PERIOD_DATE = 2;
    private int currentDatePicker;
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private final CreditCardServiceClient serviceClient = RetroFactory.getInstance(CreditCardServiceClient.class);
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_credit_card);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //create date picker dialog
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, R.style.AppTheme_Dark_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                final String date = dateFormat.format(calendar.getTime());
                switch (currentDatePicker) {
                    case PAY_DATE:
                        //set date
                        payDate.setText(date);
                        break;
                    case PERIOD_DATE:
                        periodDate.setText(date);
                        break;
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
    }

    @OnClick(R.id.newCreditCardPayDate)
    public void payDateClick() {
        currentDatePicker = PAY_DATE;
        datePickerDialog.setTitle("Choose pay date");
        datePickerDialog.show();
    }

    @OnClick(R.id.newCreditCardPeriodDate)
    public void periodDateClick() {
        currentDatePicker = PERIOD_DATE;
        datePickerDialog.setTitle("Choose period date");
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_credit_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.newCreditCardSave:
                saveCreditCard();
                break;
        }
        return true;
    }

    private void saveCreditCard() {
        Date payDateValue;
        Date periodDate;
        try {
            payDateValue = dateFormat.parse(payDate.getText().toString());
        } catch (ParseException ex) {
            //haven't choose a date for pay date
            Snackbar.make(coordinatorLayout, "Pay date has no date selected", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
            return;
        }

        try {
            periodDate = dateFormat.parse(payDate.getText().toString());
        } catch (ParseException ex) {
            //haven't choose a date for pay date
            Snackbar.make(coordinatorLayout, "Period date has no date selected", BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
            return;
        }

        //create a new credit card
        final String nameValue = name.getText().toString();
        final String annualFeeValue = annualFee.getText().toString();
        final String allowedSalaryValue = allowedSalary.getText().toString();
        NewCreditCardRequest request = new NewCreditCardRequest();
        request.setAnnualFee(Double.parseDouble(annualFeeValue));
        request.setMaxSalary(Double.parseDouble(allowedSalaryValue));
        request.setName(nameValue);
        request.setPayDate(dateFormat.format(payDateValue));
        request.setPeriodDate(dateFormat.format(periodDate));

        //show progress dialog
        progressDialog = new ProgressDialog(this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Saving your credit card on server");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final String token = PrefsUtils.getUserToken(this);
        serviceClient.saveCreditCard(token, request).enqueue(new Callback<ExecutionResult>() {
            @Override
            public void onResponse(Call<ExecutionResult> call, Response<ExecutionResult> response) {
                switch (response.code()) {
                    case 200:
                        if (response.body().isOk()) {
                            Toast.makeText(NewCreditCardActivity.this, "Your credit card has been created", Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                        }
                        break;
                    case 500:
                        Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                        break;
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ExecutionResult> call, Throwable t) {
                Snackbar.make(coordinatorLayout, getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }
}
