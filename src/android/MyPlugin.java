package com.example.myPlugin;

import android.util.Log;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;


import com.airpay.airpaysdk_simplifiedotp.AirpayConfig;
import com.airpay.airpaysdk_simplifiedotp.constants.ConfigConstants;
import com.airpay.airpaysdk_simplifiedotp.utils.Utils;
import com.example.cordovaapp.MainActivity;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyPlugin extends CordovaPlugin  {
    private static final String TAG = "MyPlugin";
    public static CallbackContext callbackContext;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);


    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("greet")) {
            this.callbackContext = callbackContext;  // Save callback context to return result later
            JSONObject formData = args.getJSONObject(0);
            // Trigger payment initiation
            this.initiatePayment(formData);

            return true;
        }
        return false;
    }

    public static void greet(String name, CallbackContext callbackContext) {
        if (name != null && name.length() > 0) {
            Log.d("success",name);
            callbackContext.success( name);
        } else {
            Log.d("error","Expected one non-empty string argument.");
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    private void initiatePayment(JSONObject formData) {

        try {
            // Extract data from JSON object
            String firstName = formData.getString("firstName");
            String lastName = formData.getString("lastName");
            String email = formData.getString("email");
            String phone = formData.getString("phone");
            String address = formData.getString("address");
            String city = formData.getString("city");
            String state = formData.getString("state");
            String country = formData.getString("country");
            String pincode = formData.getString("pincode");
            String orderId = formData.getString("orderId");
            String amount = formData.getString("amount");


            DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            String sCurDate1 = df1.format(new Date());

            String sAllData1 = email + firstName
                    + lastName + address
                    + city + state
                    + country + amount
                    + orderId + sCurDate1;



            // Merchant details
            String sMid = ""; // Please enter Merchant Id
            String sSecret = ""; // Please enter Secret Key
            String sUserName = ""; // Please enter Username
            String sPassword = ""; // Please enter Password
            // private key
            String sTemp = sSecret + "@" + sUserName + ":|:" + sPassword;
            String sPrivateKey = Utils.sha256(sTemp);

            // key for checksum
            String sTemp3 = sUserName + "~:~" + sPassword;
            String sKey1 = Utils.sha256(sTemp3);

            // checksum
            sAllData1 = sKey1 + "@" + sAllData1;
            String sChecksum1 = Utils.sha256(sAllData1);


            // Use extracted data to configure and initiate payment
            new AirpayConfig.Builder(cordova.getActivity(), ((MainActivity) cordova.getActivity()).getActivityResultLauncher())
                    .setEnvironment(ConfigConstants.PRODUCTION)
                    .setType(102)
                    .setPrivateKey(sPrivateKey)
                    .setMerchantId(sMid)
                    .setOrderId(orderId)
                    .setCurrency("356")
                    .setIsoCurrency("INR")
                    .setEmailId(email)
                    .setMobileNo(phone)
                    .setBuyerFirstName(firstName)
                    .setBuyerLastName(lastName)
                    .setBuyerAddress(address)
                    .setBuyerCity(city)
                    .setBuyerState(state)
                    .setBuyerCountry(country)
                    .setBuyerPinCode(pincode)
                    .setAmount(amount)
                    .setWallet("0")
                    .setCustomVar("")
                    .setTxnSubType("")
                    .setChmod("")
                    .setChecksum(sChecksum1)
                    .setSuccessUrl("") // Please enter Success URL
                    .setFailedUrl("") // Please enter Failed URL
                    .setLanguage("EN")
                    .build()
                    .initiatePayment();

            callbackContext.success("Payment initiated with form data");
        } catch (JSONException e) {
            callbackContext.error("Failed to parse form data: " + e.getMessage());
        }
    }


}
