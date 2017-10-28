package com.albertoruvel.credit.app.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.albertoruvel.credit.app.R;
import com.albertoruvel.credit.app.adapter.CreditCardsAdapter;
import com.albertoruvel.credit.app.data.resp.CreditCard;
import com.albertoruvel.credit.app.retro.CreditCardServiceClient;
import com.albertoruvel.credit.app.retro.RetroFactory;
import com.albertoruvel.credit.app.util.PrefsUtils;
import com.albertoruvel.credit.app.util.RecyclerViewMarginDecorator;

import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreditCardsFragment extends Fragment {

    @BindView(R.id.creditCardsRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.creditCardsProgressBar)
    ProgressBar progressBar;

    @BindView(R.id.creditCardsMessage)
    TextView message;

    @BindView(R.id.creditCardsRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private View currentView;
    private final CreditCardServiceClient serviceClient = RetroFactory.getInstance(CreditCardServiceClient.class);
    private static final String DEBUG_TAG = CreditCardsFragment.class.getCanonicalName();
    private final Logger logger = Logger.getLogger(getClass().getCanonicalName());

    public CreditCardsFragment() {
        // Required empty public constructor
    }

    public static CreditCardsFragment newInstance() {
        CreditCardsFragment fragment = new CreditCardsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(currentView == null){
            currentView = inflater.inflate(R.layout.fragment_credit_cards, container, false);
            ButterKnife.bind(this, currentView);
            initRecyclerView();
        }
        getCreditCards();
        return currentView;
    }

    private void getCreditCards(){
        final String token = PrefsUtils.getUserToken(getContext());
        serviceClient.getCreditCards(token).enqueue(new Callback<List<CreditCard>>() {
            @Override
            public void onResponse(Call<List<CreditCard>> call, Response<List<CreditCard>> response) {
                switch (response.code()){
                    case 200:
                        List<CreditCard> creditCards = response.body();
                        if (creditCards.isEmpty()){
                            recyclerView.setVisibility(View.GONE);
                            message.setText("You haven't registered any credit card, click on the add button to get started");
                            message.setVisibility(View.VISIBLE);
                        }else{
                            CreditCardsAdapter adapter = new CreditCardsAdapter(creditCards);
                            recyclerView.setAdapter(adapter);
                            message.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                        progressBar.setVisibility(View.GONE);
                        break;
                    case 500:
                        Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
                        break;
                    case 401:
                        Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), "You should not be here, will shut down this stuff right now", Snackbar.LENGTH_LONG).show();
                        //TODO: delete shared preferences and finish application
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CreditCard>> call, Throwable t) {
                Snackbar.make(getActivity().findViewById(R.id.coordinatorLayout), getString(R.string.unexpectedErrorMessage), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.addItemDecoration(new RecyclerViewMarginDecorator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCreditCards();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        getCreditCards();
    }
}
