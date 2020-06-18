package com.reward.scanner.api_client;


import com.reward.scanner.model.FindCustomerModel;
import com.reward.scanner.model.SuccessModel;

import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import static com.reward.scanner.api_client.Base_Url.add_customer;
import static com.reward.scanner.api_client.Base_Url.add_petrol_filling_info;
import static com.reward.scanner.api_client.Base_Url.save_qrcode_for_customer;
import static com.reward.scanner.api_client.Base_Url.scan_qr_code_for_customer_info;

/**
 * Created by Raghvendra Sahu on 20-Apr-20.
 */
public interface Api_Call {

    @Headers("API-KEY: 123456")
    @FormUrlEncoded
    @POST(add_customer)
    Observable<SuccessModel> AddNewCustomer(
            @Field("customer_name")  String customer_name,
            @Field("mobile_no")   String customer_mobile,
            @Field("vehicle_no")   String customer_vehical,
            @Field("io_xtrarewards_card_no")   String customer_reward_card,
            @Field("qrcode_id")  String qrData);

    @Headers("API-KEY: 123456")
    @FormUrlEncoded
    @POST(save_qrcode_for_customer)
    Observable<SuccessModel>  SaveQrCode(
            @Field("qrtext")  String qr_text,
            @Field("qrcode")  String s);

    @Headers("API-KEY: 123456")
    @FormUrlEncoded
    @POST(scan_qr_code_for_customer_info)
    Observable<FindCustomerModel>FindCustomer(
            @Field("qrtext") String qrData);

    @Headers("API-KEY: 123456")
    @Multipart
    @POST(add_petrol_filling_info)
    Observable<SuccessModel>  UploadMeter(
            @Part("amount") RequestBody etAmount,
            @Part("description") RequestBody etDesc,
            @Part("customer_id") RequestBody customerId,
            @Part("qrcode_id") RequestBody qrCodeInfo,
            @Part("upload_folder_name") RequestBody folderName,
            @Part  MultipartBody.Part body);

//    @GET(loginapi)
//    Observable<LoginModel> LoginUser(
//            // @Query("route") String loginapi,
//            @Query("email") String et_email,
//            @Query("password") String et_pw);
//



}
