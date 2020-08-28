package com.kiduyu.meshproject.a2nkbooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kiduyu.meshproject.a2nkbooking.ApplicationHelper.isValidMobileNumber;
import static com.kiduyu.meshproject.a2nkbooking.ApplicationHelper.isValidPassword;
import static com.kiduyu.meshproject.a2nkbooking.ApplicationHelper.lockView;
import static com.kiduyu.meshproject.a2nkbooking.ApplicationHelper.releaseView;
import static com.kiduyu.meshproject.a2nkbooking.ApplicationHelper.writeToSharedPreferences;

public class Login extends AppCompatActivity {
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    ActionProcessButton login;
    User user = new User();
    private String serverMessage;
    private int serverSuccess;
    private CountDownTimer activityStarter;


    @OnClick(R.id.login)
    public void login() {
        lockView(login);
        String userInputMobile, userInputPassword;
        userInputMobile = mobile.getText().toString();
        userInputPassword = password.getText().toString();
        if (!TextUtils.isEmpty(userInputMobile)) {
            //if (true) {

            final RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("prathab", response.toString());
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (!obj.getBoolean("error")) {
                            user.setUserId(Integer.parseInt(obj.getString("id")));
                            Log.d("prathab", obj.getString("id"));
                            login.setProgress(100);
                            user.setEmail(obj.getString("email"));
                            user.setMobile(userInputMobile);
                            user.setPassword(userInputPassword);
                            user.setAddress("Nyeri");
                            user.setName("Kiduyu Klaus");
                            user.setBalance(300.00);
                            writeToSharedPreferences(Constants.USER_DATA_OBJECT, User.getUserJson(user));
                            writeToSharedPreferences(Constants.SUCCESSFUL_LOGIN_HISTORY, true);
                            startActivity(new Intent(getApplicationContext(), Home.class));
                            finish();
                        } else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    obj.getString("message"),
                                    Toast.LENGTH_LONG
                            ).show();
                            releaseView(login);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("prathab","tes"+ error.getMessage());
                    Snackbar.make(login, error.getMessage(), Snackbar.LENGTH_SHORT).show();
                    login.setProgress(-1);
                    new CountDownTimer(2000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            login.setProgress(0);
                            releaseView(login);
                        }
                    }.start();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("username", userInputMobile);
                    params.put("password", userInputPassword);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
            login.setProgress(1);
        } else {
            Snackbar.make(login, "Enter Valid Details", Snackbar.LENGTH_SHORT).show();
            releaseView(login);
        }
    }

    private void jsonParser(JSONObject response) {
        try {
            serverSuccess = response.getInt("status");
            if (serverSuccess == 1) {
                user.setUserId(Integer.parseInt(response.getString("user_id")));
            }
            serverMessage = response.getString("message");
            finalDecision();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void finalDecision() {
        if (serverSuccess == 1) {
            login.setProgress(100);
            writeToSharedPreferences(Constants.USER_DATA_OBJECT, User.getUserJson(user));
            writeToSharedPreferences(Constants.SUCCESSFUL_LOGIN_HISTORY, true);
            activityStarter = new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                }
            };
            activityStarter.start();

        } else {
            writeToSharedPreferences(Constants.USER_DATA_OBJECT, "");
            writeToSharedPreferences(Constants.SUCCESSFUL_LOGIN_HISTORY, false);
            login.setProgress(-1);
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    login.setProgress(0);
                    releaseView(login);
                }
            }.start();
            Snackbar.make(login, serverMessage, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (readFromSharedPreferences(Constants.SUCCESSFUL_LOGIN_HISTORY, false)) {
        if (false) {
            startActivity(new Intent(Login.this, Home.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mobile.setText("klaus");
        password.setText("12345");
    }

    @OnClick(R.id.forgot_password)
    public void forgotPasssword() {
        SpannableString message = new SpannableString("To reset Password visit \nwww.ibts.com/forgotpassword.php");
        Linkify.addLinks(message, Linkify.ALL);
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this, R.style.AlertDialogLight);
        builder.setCancelable(false);
        builder.setTitle("Password Reset");
        builder.setMessage(message);
        builder.setPositiveButton("Okay", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ((TextView) alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void onPause() {
        super.onPause();
        login.setProgress(0);
        if (activityStarter != null) {
            activityStarter.cancel();
        }
    }

    @OnClick(R.id.create_account)
    public void createAccount() {
        startActivity(new Intent(Login.this, SignUp.class));
    }
}