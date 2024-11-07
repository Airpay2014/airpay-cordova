# Airpay Cordova Plugin
 
This is the official Cordova plugin for integrating Airpay payment gateway into Cordova applications.
 
## Supported Platforms
 
* Android (version compatibility details below)
  Upto Gradle version 8.7

## Requirements
 
* Cordova Version: Supports Cordova version up to v12.0.0 
* NodeJS Version: Supports NodeJS version up to v22.11.0
* Android Version: Compatible with Android SDK min version 24 to max version 34
* Android Build Tool: Supports Android Build Tool up to v22.11.0
   
## Installation

Note: For Windows users, please run the following commands on Git Bash instead of Command Prompt. You can download Git for Windows here.
  
Navigate to your project directory:

```
cd your-project-folder
```

Add the necessary platforms:

```
cordova platform add android
```

Install the Airpay plugin:

```
cordova plugin add https://github.com/Airpay2014/airpay-cordova.git#master 
``` 
### Executing program
 
 The following Android files are crucial for the Airpay Cordova integration and ensure seamless functionality:

* AndroidManifest.xml
  
  Manages app-level configurations and permissions.
  Add below code inside Application tag.

```
<uses-library
            android:name="org.apache.http.legacy"
            android:required="false" />

```
Make sure to give Internet Permission.

```
 <uses-permission android:name="android.permission.INTERNET" />

```
* MainActivity.java
  Serves as the main entry point for your Cordova application.

```

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.airpay.airpaysdk_simplifiedotp.utils.Transaction;
import com.airpay.airpaysdk_simplifiedotp.view.ActionResultListener;
import com.example.myPlugin.MyPlugin;

import org.apache.cordova.*;

import java.util.zip.CRC32;

public class MainActivity extends CordovaActivity implements ActionResultListener
{
    public static ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                // Assuming Transaction is returned in the intent
                                Transaction transaction = (Transaction) data.getSerializableExtra("response");
                                if (transaction != null) {
                                    Log.d("MainActivity", "Transaction status: " + transaction.getSTATUS());
                                    // Handle successful transaction
                                    Toast.makeText(MainActivity.this, ""+transaction.getSTATUS(), Toast.LENGTH_SHORT).show();
                                    String transactionDetails =
                                            "\nSTATUS: " + transaction.getSTATUS() +
                                                    "\nSTATUSMSG: " + transaction.getSTATUSMSG() +
                                                    "\nTXN_MODE: " + transaction.getTXN_MODE() +
                                                    "\nTRANSACTIONID: " + transaction.getTRANSACTIONID() +
                                                    "\nTRANSACTIONAMT: " + transaction.getTRANSACTIONAMT() +
                                                    "\nTRANSACTIONSTATUS: " + transaction.getTRANSACTIONSTATUS() +
                                                    "\nMERCHANTTRANSACTIONID: " + transaction.getMERCHANTTRANSACTIONID() +
                                                    "\nMERCHANTPOSTTYPE: " + transaction.getMERCHANTPOSTTYPE() +
                                                    "\nMERCHANTKEY: " + transaction.getMERCHANTKEY() +
                                                    "\nSECUREHASH: " + transaction.getSECUREHASH() +
                                                    "\nCUSTOMVAR: " + transaction.getCUSTOMVAR() +
                                                    "\nTXN_DATE_TIME: " + transaction.getTXN_DATE_TIME() +
                                                    "\nTXN_CURRENCY_CODE: " + transaction.getTXN_CURRENCY_CODE() +
                                                    "\nTRANSACTIONVARIANT: " + transaction.getTRANSACTIONVARIANT() +
                                                    "\nCHMOD: " + transaction.getCHMOD() +
                                                    "\nBANKNAME: " + transaction.getBANKNAME() +
                                                    "\nCARDISSUER: " + transaction.getCARDISSUER() +
                                                    "\nFULLNAME: " + transaction.getFULLNAME() +
                                                    "\nEMAIL: " + transaction.getEMAIL() +
                                                    "\nCONTACTNO: " + transaction.getCONTACTNO() +
                                                    "\nISRISK: " + transaction.getISRISK() +
                                                    "\nMERCHANT_NAME: " + transaction.getMERCHANT_NAME() +
                                                    "\nSETTLEMENT_DATE: " + transaction.getSETTLEMENT_DATE() +
                                                    "\nSURCHARGE: " + transaction.getSURCHARGE() +
                                                    "\nBILLEDAMOUNT: " + transaction.getBILLEDAMOUNT() +
                                                    "\nCUSTOMERVPA: " + transaction.getCUSTOMERVPA() +
                                                    "\nAdditional Data (myMap): " + transaction.getMyMap().toString();


                                    MyPlugin.greet(transactionDetails, MyPlugin.callbackContext);
                                } else {
                                    Toast.makeText(MainActivity.this, "Transaction object is null", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("MainActivity", "Intent data is null");
                            }
                        } else {
                            //  Toast.makeText(MainActivity.this, "Payment result not OK, Result Code: ", Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", "Payment result not OK, Result Code: " + result.getResultCode());
                        }
                    }
                }
        );

        // enable Cordova apps to be started in the background
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }

        // Set by <content src="index.html" /> in config.xml
        loadUrl(launchUrl);
    }
    public static ActivityResultLauncher<Intent> getActivityResultLauncher() {
        return activityResultLauncher;
    }

    @Override
    public void onResult(Object o) {
        if (o instanceof Transaction) {
            Transaction transaction = (Transaction) o;

            Toast.makeText(MainActivity.this, transaction.getTRANSACTIONSTATUS() + "\n" + transaction.getSTATUSMSG(), Toast.LENGTH_LONG).show();
            if (transaction.getSTATUS() != null) {
                Log.e("STATUS -> ", "=" + transaction.getSTATUS());
            }
            if (transaction.getMERCHANTKEY() != null) {
                Log.e("MERCHANT KEY -> ", "=" + transaction.getMERCHANTKEY());
            }
            if (transaction.getMERCHANTPOSTTYPE() != null) {
                Log.e("MERCHANT POST TYPE ", "=" +
                        transaction.getMERCHANTPOSTTYPE());
            }
            if (transaction.getSTATUSMSG() != null) {
                Log.e("STATUS MSG -> ", "=" + transaction.getSTATUSMSG()); // success or fail
            }
            if (transaction.getTRANSACTIONAMT() != null) {
                Log.e("TRANSACTION AMT -> ", "=" + transaction.getTRANSACTIONAMT());
            }
            if (transaction.getTXN_MODE() != null) {
                Log.e("TXN MODE -> ", "=" + transaction.getTXN_MODE());
            }
            if (transaction.getMERCHANTTRANSACTIONID() != null) {
                Log.e("MERCHANT_TXN_ID -> ", "=" + transaction.getMERCHANTTRANSACTIONID()); // order id
            }
            if (transaction.getSECUREHASH() != null) {
                Log.e("SECURE HASH -> ", "=" + transaction.getSECUREHASH());
            }
            if (transaction.getCUSTOMVAR() != null) {
                Log.e("CUSTOMVAR -> ", "=" + transaction.getCUSTOMVAR());
            }
            if (transaction.getTRANSACTIONID() != null) {
                Log.e("TXN ID -> ", "=" + transaction.getTRANSACTIONID());
            }
            if (transaction.getTRANSACTIONSTATUS() != null) {
                Log.e("TXN STATUS -> ", "=" + transaction.getTRANSACTIONSTATUS());
            }
            if (transaction.getTXN_DATE_TIME() != null) {
                Log.e("TXN_DATETIME -> ", "=" + transaction.getTXN_DATE_TIME());
            }
            if (transaction.getTXN_CURRENCY_CODE() != null) {
                Log.e("TXN_CURRENCY_CODE -> ", "=" + transaction.getTXN_CURRENCY_CODE());
            }
            if (transaction.getTRANSACTIONVARIANT() != null) {
                Log.e("TRANSACTIONVARIANT -> ", "=" + transaction.getTRANSACTIONVARIANT());
            }
            if (transaction.getCHMOD() != null) {
                Log.e("CHMOD -> ", "=" + transaction.getCHMOD());
            }
            if (transaction.getBANKNAME() != null) {
                Log.e("BANKNAME -> ", "=" + transaction.getBANKNAME());
            }
            if (transaction.getCARDISSUER() != null) {
                Log.e("CARDISSUER -> ", "=" + transaction.getCARDISSUER());
            }
            if (transaction.getFULLNAME() != null) {
                Log.e("FULLNAME -> ", "=" + transaction.getFULLNAME());
            }
            if (transaction.getEMAIL() != null) {
                Log.e("EMAIL -> ", "=" + transaction.getEMAIL());
            }
            if (transaction.getCONTACTNO() != null) {
                Log.e("CONTACTNO -> ", "=" + transaction.getCONTACTNO());
            }
            if (transaction.getMERCHANT_NAME() != null) {
                Log.e("MERCHANT_NAME -> ", "=" + transaction.getMERCHANT_NAME());
            }
            if (transaction.getSETTLEMENT_DATE() != null) {
                Log.e("SETTLEMENT_DATE -> ", "=" + transaction.getSETTLEMENT_DATE());
            }
            if (transaction.getSURCHARGE() != null) {
                Log.e("SURCHARGE -> ", "=" + transaction.getSURCHARGE());
            }
            if (transaction.getBILLEDAMOUNT() != null) {
                Log.e("BILLEDAMOUNT -> ", "=" + transaction.getBILLEDAMOUNT());
            }
            if (transaction.getISRISK() != null) {
                Log.e("ISRISK -> ", "=" + transaction.getISRISK());
            }
            String transid = transaction.getMERCHANTTRANSACTIONID();
            String apTransactionID = transaction.getTRANSACTIONID();
            String amount = transaction.getTRANSACTIONAMT();
            String transtatus = transaction.getTRANSACTIONSTATUS();
            String message = transaction.getSTATUSMSG();

            String customer_vpa = "";
            if (!TextUtils.isEmpty(transaction.getCHMOD()) && transaction.getCHMOD().equalsIgnoreCase("upi")) {
                customer_vpa = ":" + transaction.getCUSTOMERVPA();
                Log.e("Verified Hash ==", "INSIDE CHMODE UPI CONSIDTION");
            }

            String merchantid = ""; //Please enter Merchant Id
            String username = "";   //Please enter Username
            String sParam = transid + ":" + apTransactionID + ":" + amount + ":" + transtatus + ":" + message + ":" + merchantid + ":" + username + customer_vpa;
            CRC32 crc = new CRC32();
            crc.update(sParam.getBytes());
            String sCRC = "" + crc.getValue();
            Log.e("Verified Hash ==", "sParam= " + sParam);
            Log.e("Verified Hash ==", "Calculate Hash= " + sCRC);
            Log.e("Verified Hash ==", "RESP Secure Hash= " + transaction.getSECUREHASH());

            if (sCRC.equalsIgnoreCase(transaction.getSECUREHASH())) {
                Log.e("Verified Hash ==", "SECURE HASH MATCHED");
            } else {
                Log.e("Verified Hash ==", "SECURE HASH MIS-MATCHED");
            }
        }
    }
}

```

* build.gradle (app)
   Defines build configurations and dependencies for the app. 
   For latest dependency, kindly refer the kit on sanctum portal link -  	
```
implementation("com.airpay:Airpay-India-Kit:--version") {
        exclude group: 'androidx.core', module: 'core'
        // or
        exclude group: 'androidx.legacy', module: 'legacy-support-v4'
    }
    implementation 'com.android.volley:volley:1.2.1'

```

* repository.gradle
   Specifies repositories for dependency management.
   For Username and Password, kindly refer the kit on sanctum portal link -  	
```
 maven { url 'https://maven.google.com'
    }
    maven {  url'https://gitlab.com/api/v4/projects/60989340/packages/maven'
        name = "GitLab"
        credentials {
            username = "" //Enter Username
            password = "" //Enter Access Token 
        }
    }

```
*  MyPlugin.java
   Implements the Cordova plugin’s core logic.
   Please enter the merchant configuration details in required below fields -

```
 // Merchant details
            String sMid = ""; // Please enter Merchant Id
            String sSecret = ""; // Please enter Secret Key
            String sUserName = ""; // Please enter Username
            String sPassword = ""; // Please enter Password


.setSuccessUrl("") // Please enter Success URL
.setFailedUrl("") // Please enter Failed URL
                    
```
 Note :- For above merchant configuration details, Kindly contact with Airpay Support Team.

## Help
 
Any advise for common problems or issues.

```

command to run if program contains helper info

```
 
## Support
 
* Tech/integration Support Team
* Customer Support Team
 
## Version History
 
* 0.1

    * Initial Release
 
## License
 
This project is licensed under the [Airpay Payment Service] License 
 

 
