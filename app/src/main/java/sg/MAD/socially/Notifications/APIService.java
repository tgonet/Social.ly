package sg.MAD.socially.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import sg.MAD.socially.Notifications.MyResponse;
import sg.MAD.socially.Notifications.Sender;

public interface APIService {

    @Headers(
            {
                "Content-Type:application/json",
                "Authorization: key=AAAA6sMe-64:APA91bGzvmY2ESdiyMnjWU4yfUXPj9u0nql5uqUGRORMaqbvHft3YX3oE8sicFsKpuowsEVun_xz2unJT5FuqP0sgf9QrmtSOO96v3Iv3u3_yw_pjxVTanNLKA7MiSVtKfpvQI6h7P35"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
