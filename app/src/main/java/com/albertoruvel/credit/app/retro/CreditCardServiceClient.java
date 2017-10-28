package com.albertoruvel.credit.app.retro;

import com.albertoruvel.credit.app.data.req.NewCreditCardRequest;
import com.albertoruvel.credit.app.data.resp.CreditCard;
import com.albertoruvel.credit.app.data.resp.ExecutionResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by jose.rubalcaba on 10/27/2017.
 */

public interface CreditCardServiceClient {

    @POST("card/create")
    public Call<ExecutionResult> saveCreditCard(@Header("Authorization")String token, @Body NewCreditCardRequest request);

    @GET("card")
    public Call<List<CreditCard>> getCreditCards(@Header("Authorization")String token);
}
