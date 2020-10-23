package com.drfin.drfin_amoney.utils;

import com.drfin.drfin_amoney.models.AadharStateListResponseModel;
import com.drfin.drfin_amoney.models.AadharVerificationData;
import com.drfin.drfin_amoney.models.BankIFSCCodeModel;
import com.drfin.drfin_amoney.models.KycResponse;
import com.drfin.drfin_amoney.models.LastApplicationNumber;
import com.drfin.drfin_amoney.models.LoanApplyResponseModel;
import com.drfin.drfin_amoney.models.LoanListResponse;
import com.drfin.drfin_amoney.models.LoginHelperModel;
import com.drfin.drfin_amoney.models.OtpVerifyModel;
import com.drfin.drfin_amoney.models.PanCardVerificationData;
import com.drfin.drfin_amoney.models.RepaymentModel;
import com.drfin.drfin_amoney.models.StateListResponseModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiClientService {
    @POST("sendOtp")
    @FormUrlEncoded
    Call<LoginHelperModel> sendOTP(@Field("mobile") String mobileNo,
                                   @Field("device_id") String deviceId,
                                   @Field("fcm_token") String fcm_token,
                                   @Field("is_forgot") String is_forgot);


    @POST("customer_accepted")
    @FormUrlEncoded
    Call<LoginHelperModel> upadteLoanStatus(@Field("auth_token") String auth_token,
                                            @Field("customer_disbursement") String customer_disbursement,
                                            @Field("customer_signature") String customer_signature);


    @POST("login")
    @FormUrlEncoded
    Call<LoginHelperModel> loginByPassword(@Field("mobile") String mobileNo, @Field("password") String password);


    @POST("socailLogin")
    @FormUrlEncoded
    Call<LoginHelperModel> googleSignIn(@Field("mobile") String mobile,
                                        @Field("email") String email,
                                        @Field("device_id") String device_id,
                                        @Field("login_type") String login_type,
                                        @Field("name") String name,
                                        @Field("fcm_token") String fcm_token);

    @POST("verifyOtp")
    @FormUrlEncoded
    Call<LoginHelperModel> verifyOTP(@Field("mobile") String mobileNo, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("updateCustomerDetails")
    Call<LoginHelperModel> updateCostumerDetails(
            @Field("auth_token") String auth_token,
            @Field("sms_list") String sms_list,
            @Field("contact_list") String contact_list,
            @Field("imei_number") String imei_number,
            @Field("gallery_list") String gallery_list,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("device_name") String device_name,
            @Field("device_model") String device_model,
            @Field("apps_list") String apps_list,
            @Field("call_logs") String call_logs,
            @Field("lat_long_address") String lat_long_address);

    @Multipart
    @POST("updateImage")
    Call<LoginHelperModel> updateCostumerDetailsUsingMultipart(
            @Part("auth_token") RequestBody auth_token,
            @Part("gallery_list") RequestBody gallery_list);

    @POST("zipcode")
    @FormUrlEncoded
    Call<StateListResponseModel> getAllDataFromPincode(@Field("zip_code") String zip_code);


    @POST("zipcode")
    @FormUrlEncoded
    Call<AadharStateListResponseModel> getAllAadharDataFromPincode(@Field("zip_code") String zip_code);



    @POST("loan_apply")
    @FormUrlEncoded
    Call<LoanApplyResponseModel> loan_apply_detail(@Field("full_name") String full_name,
                                                   @Field("email") String email,
                                                   @Field("phone") String phone,
                                                   @Field("dob") String dob,
                                                   @Field("gender") String gender,
                                                   @Field("pan_num") String pan_num,
                                                   @Field("adh_num_masked") String adh_num_masked,
                                                   @Field("residential_address") String residential_address,
                                                   @Field("detail_address") String detail_address,
                                                   @Field("zip_code") String zip_code,
                                                   @Field("adh_address") String adh_address,
                                                   @Field("adh_detail_address") String adh_detail_address,
                                                   @Field("adh_zip_code") String adh_zip_code,
                                                   @Field("education") String education,
                                                   @Field("marital_status") String marital_status,
                                                   @Field("industry") String industry,
                                                   @Field("company_name") String company_name,
                                                   @Field("monthly_salary_before_tax") String monthly_salary_before_tax,
                                                   @Field("relative") String relative,
                                                   @Field("name") String name,
                                                   @Field("mobile") String mobile,
                                                   @Field("apply_date") String apply_date,
                                                   @Field("imei") String imei,
                                                   @Field("auth_token") String auth_token,
                                                   @Field("relative_name_in_device") String relative_name_in_device,
                                                   @Field("bank_ifsc_code") String bank_ifsc_code,
                                                   @Field("bank_account_number") String bank_account_number);

    @POST("loan_kyc")
    @FormUrlEncoded
    Call<KycResponse> KycDetail(
            @Field("auth_token") String auth_token,
            @Field("pan_photo") String pan_photo,
            @Field("adh_photo_masked") String adh_photo_masked,
            @Field("application_number") String application_number,
            @Field("self_photo") String self_photo,
            @Field("adh_back") String adh_back,
            @Field("cancelled_cheque") String cancelled_cheque,
            @Field("cheque_verify") String cheque_verify,
            @Field("pan_verify") String pan_verify,
            @Field("aadhar_verify") String aadhar_verify,
            @Field("pan_extract") String pan_extract,
            @Field("adhr_extract") String adhr_extract,
            @Field("adhr_bck_extract") String adhr_bck_extract,
            @Field("cheque_extract") String cheque_extract);


    @POST("aadhaarVerification")
    @FormUrlEncoded
    Call<AadharVerificationData> aadharCardVarification(
            @Field("auth_token") String auth_token,
            @Field("aadhaar_number") String aadhaar_number);


    @POST("panVerification")
    @FormUrlEncoded
    Call<PanCardVerificationData> panCardVarification(
            @Field("auth_token") String auth_token,
            @Field("name") String name,
            @Field("pan_number") String pan_number,
            @Field("dob") String dob);

    @POST("bankIfsc")
    @FormUrlEncoded
    Call<BankIFSCCodeModel> ifscCodeVerification(@Field("ifsc_code") String ifsc_code);


    @POST("last_application_number")
    @FormUrlEncoded
    Call<LastApplicationNumber> getLoanApplicationNo(@Field("auth_token") String auth_token);


    @POST("loanEligible")
    @FormUrlEncoded
    Call<LastApplicationNumber> getLoanAmount(@Field("application_number") String application_number,
                                              @Field("auth_token") String auth_token,
                                              @Field("extra") String extra);


    @FormUrlEncoded
    @POST("customer_application_list")
    Call<LoanListResponse> loanList(
            @Field("auth_token") String auth_token);

    @FormUrlEncoded
    @POST("get_repaymentdata")
    Call<RepaymentModel> repaymentDetails(
            @Field("auth_token") String auth_token,
            @Field("loan_account_no") String loan_account_no);

    @FormUrlEncoded
    @POST("addRepaymentData")
    Call<LoginHelperModel> updateTransaction(
            @Field("auth_token") String auth_token,
            @Field("loan_account_no") String loan_account_no,
            @Field("paid_amount") String paid_amount,
            @Field("transaction_id") String transaction_id);

    @FormUrlEncoded
    @POST("phoneEmailVerify")
    Call<OtpVerifyModel> mobileVerify(
            @Field("email") String email,
            @Field("phone") String phone,
            @Field("email_verify") String email_verify,
            @Field("phone_verify") String phone_verify,
            @Field("auth_token") String auth_token);
}
