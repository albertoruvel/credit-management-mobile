package com.albertoruvel.credit.app.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.albertoruvel.credit.app.CreditCardActivity;
import com.albertoruvel.credit.app.R;
import com.albertoruvel.credit.app.data.resp.CreditCard;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jose.rubalcaba on 10/27/2017.
 */

public class CreditCardsAdapter extends RecyclerView.Adapter<CreditCardsAdapter.CreditCardViewHolder>{

    private List<CreditCard> creditCards;

    public CreditCardsAdapter(List<CreditCard> creditCards){
        this.creditCards = creditCards;
    }

    @Override
    public CreditCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CreditCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CreditCardViewHolder holder, int position) {
        final CreditCard creditCard = creditCards.get(position);
        holder.name.setText(creditCard.getName());
        holder.registrationDate.setText("Registration date - " + creditCard.getRegistrationDate());
        holder.salary.setText("$ " + creditCard.getCurrentSalary());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreditCardActivity.class);
                intent.putExtra(CreditCardActivity.CREDIT_CARD_ID_PARAM, creditCard.getId());
                view.getContext().startActivity(intent);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(view.getContext(), R.style.AppTheme_Dark_Dialog)
                        .setItems(new CharSequence[]{ "Register payment", "Add new purchase", "Close period"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return creditCards.size();
    }

    class CreditCardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardItemCard)
        CardView cardView;

        @BindView(R.id.cardItemName)
        TextView name;

        @BindView(R.id.cardItemRegistrationDate)
        TextView registrationDate;

        @BindView(R.id.cardItemSalary)
        TextView salary;

        public CreditCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
