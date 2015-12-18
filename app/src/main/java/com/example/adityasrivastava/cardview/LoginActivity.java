package com.example.adityasrivastava.cardview;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends ActionBarActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static final String TAG = "Login Acitivty" ;

    private static final int RC_GP_SIGN_IN = 9001;
    private static int RC_FB_SIGN_IN;
    private GoogleApiClient mGoogleApiClient;
    private TextView facebook_info;
    private LoginButton facebook_loginButton;
    protected CallbackManager facebook_callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.login_view);

        hideStatusBar();

        facebook_info = (TextView)findViewById(R.id.info);
        if(!isNetworkAvailable()){
            facebook_info.setText("Oops! No Internet :(");
        }

        initializeFacebookLogin();
        initializeGooglePlus();
    }

    private void hideStatusBar(){
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            View decorView = getWindow().getDecorView();
            // Hide the status bar.
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }


    private void initializeGooglePlus(){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewById(R.id.google_sign_in_button).setOnClickListener(this);

    }

    private void initializeFacebookLogin(){

        AccessToken token = AccessToken.getCurrentAccessToken();
        facebook_callbackManager = CallbackManager.Factory.create();
        facebook_loginButton = (LoginButton)findViewById(R.id.facebook_login_button);
        RC_FB_SIGN_IN = facebook_loginButton.getRequestCode();

        facebook_loginButton.registerCallback(facebook_callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Intent main_activity_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_activity_intent);

                facebook_info.setText("Login Successful");

            }

            @Override
            public void onCancel() {

                facebook_info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException e) {

                facebook_info.setText("Login attempt failed.");

            }
        });
    }




    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.google_sign_in_button:
                System.out.println("Google Clicked");
                signIn();
                break;
            case R.id.facebook_login_button:
                System.out.println("Facebook Clicked");
                break;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_GP_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        if(requestCode == RC_FB_SIGN_IN){
            facebook_callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GP_SIGN_IN);
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            facebook_info.setText("Login Successful");
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent main_activity_intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(main_activity_intent);

        } else {

            facebook_info.setText("Login attempt failed.");

        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LoginManager.getInstance().logOut();
    }
}
