package com.albertoruvel.credit.app.retro;

import com.albertoruvel.credit.app.data.req.SigninRequest;
import com.albertoruvel.credit.app.data.req.SignupRequest;
import com.albertoruvel.credit.app.data.resp.AuthenticationResult;
import com.albertoruvel.credit.app.data.resp.ExecutionResult;
import com.albertoruvel.credit.app.data.resp.TokenValidationResult;
import com.albertoruvel.credit.app.data.resp.UserConfiguration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by jose.rubalcaba on 10/25/2017.
 */

public interface AccountServiceClient {

    /**
     * Sign in to cloud
     * @param request
     * @return
     */
    @POST("account/signin")
    public Call<AuthenticationResult> signin(@Body SigninRequest request);

    /**
     * Sign up to application
     * @param request
     * @return
     */
    @POST("account/signup")
    public Call<AuthenticationResult> signup(@Body SignupRequest request);

    /**
     * Validate token on the cloud
     * @param token
     * @return
     */
    @POST("account/validateToken")
    public Call<TokenValidationResult> validateToken(@Query("token")String token);

    /**
     * Get user configuration
     * @param token
     * @return
     */
    @GET("account/config")
    public Call<UserConfiguration> getConfiguration(@Header("Authorization")String token);

    /**
     * save a user configuration
     * @param token
     * @param request
     * @return
     */
    @POST("account/config")
    public Call<ExecutionResult> saveUserConfiguration(@Header("Authorization")String token, @Body UserConfiguration request);
}
