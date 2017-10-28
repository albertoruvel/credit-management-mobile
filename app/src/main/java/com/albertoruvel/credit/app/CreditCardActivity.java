package com.albertoruvel.credit.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CreditCardActivity extends AppCompatActivity {

    public static final String CREDIT_CARD_ID_PARAM = "creditCardIdParam";

    private String creditCardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        this.creditCardId = getIntent().getStringExtra(CREDIT_CARD_ID_PARAM);
    }
}
