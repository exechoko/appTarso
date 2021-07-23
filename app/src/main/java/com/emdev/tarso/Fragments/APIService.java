package com.emdev.tarso.Fragments;

import com.emdev.tarso.Notifications.MyResponse;
import com.emdev.tarso.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAmtdPgjo:APA91bGDb_QO25E6GZSpc5kkkvqXk_cBGGUukiRvbdHuE_eqCK2eTvJeSQVanmP85Hbf-ysYr6wlm1hEAc_m2vZtOoQw6aJgvtekw3LcIxFyzqLoVws4N4Of61mUrrtSNEPY9784h5Jr"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
