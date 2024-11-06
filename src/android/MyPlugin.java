package com.example.myPlugin;

import android.util.Log;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.airpay.sdk.ConfigConstants;
import com.airpay.sdk.airpay.AirpayConfig;
import com.airpay.sdk.airpay.Transaction;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

public class MyPlugin extends CordovaPlugin {
    private static final String TAG = "MyPlugin";
    private CallbackContext callbackContext;

    private ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        // Initialize ActivityResultLauncher to handle result from Airpay SDK
        activityResultLauncher = cordova.getActivity().registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Intent data = result.getData();
                            Transaction transaction = (Transaction) data.getSerializableExtra("response");
                            Log.d(TAG, "Received result: " + transaction.getSTATUS());

                            if (transaction != null && callbackContext != null) {
                                callbackContext.success(transaction.getSTATUS()); // Pass result to JavaScript
                            }
                        } else {
                            Log.d(TAG, "No result received");
                            if (callbackContext != null) {
                                callbackContext.error("No result received");
                            }
                        }
                    }
                }
        );
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        // if (action.equals("greet")) {
        //     String name = args.getString(0);
        //     this.greet(name, callbackContext);
        //     return true;
        // }
        // return false;

        if (action.equals("greet")) {
            this.callbackContext = callbackContext;  // Save callback context to return result later

            // Trigger payment initiation
            this.initiatePayment();

            return true;
        }
        return false;
    }

    // private void greet(String name, CallbackContext callbackContext) {
    //     if (name != null && name.length() > 0) {
    //         Log.d("success",name);
    //         callbackContext.success("Hello, " + name);
    //     } else {
    //         Log.d("error","Expected one non-empty string argument.");
    //         callbackContext.error("Expected one non-empty string argument.");
    //     }
    // }

    private void initiatePayment() {
        new AirpayConfig.Builder(cordova.getActivity(), activityResultLauncher)
                .setEnvironment(ConfigConstants.STAGING)  // Environment is staging
                .setType(102)  // Provided type
                .setPrivateKey("de9b03dfcb9641f3d7b0d32485ef9f24b516a7d168369724d03ca60d81dc64ee")  // Provided private key
                .setMerchantId("18999")  // Merchant ID
                .setOrderId("cavg")  // Order ID
                .setCurrency("356")  // Currency code
                .setIsoCurrency("INR")  // ISO Currency
                .setEmailId("abhijeet_mule@gmail.com")  // Email ID
                .setMobileNo("8355942271")  // Mobile number
                .setBuyerFirstName("abhijeet")  // First name
                .setBuyerLastName("mule")  // Last name
                .setBuyerAddress("")  // Address is empty
                .setBuyerCity("Mumbai")  // City
                .setBuyerState("")  // State is empty
                .setBuyerCountry("")  // Country is empty
                .setBuyerPinCode("")  // Pincode is empty
                .setAmount("100")  // Amount
                .setWallet("0")  // Wallet value
                .setCustomVar("")  // CustomVar is empty
                .setTxnSubType("")  // txnSubType is empty
                .setChmod("")  // Mode is empty
                .setChecksum("8d28caef2de078d64975078f9bfb28c8189fe08276c9132dbbd181d4baf00555")  // Checksum
                .setSuccessUrl("https://apple.nowpay.co.in/airpay_php_v3/responsefromairpay.php")  // Success URL
                .setFailedUrl("https://apple.nowpay.co.in/airpay_php_v3/responsefromairpay.php")  // Failure URL
                .setLanguage("EN")  // Language
                .build()
                .initiatePayment();
    }
}
