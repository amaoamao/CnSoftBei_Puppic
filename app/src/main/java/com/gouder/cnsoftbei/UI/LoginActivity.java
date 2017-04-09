/*
 * Copyright (c) 2017 Peter Mao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gouder.cnsoftbei.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gouder.cnsoftbei.API.LogIn.LogInBuilder;
import com.gouder.cnsoftbei.API.LogIn.LogInService;
import com.gouder.cnsoftbei.API.SignUp.SignUpService;
import com.gouder.cnsoftbei.Entity.IsSignedUpResult;
import com.gouder.cnsoftbei.Entity.LogInResult;
import com.gouder.cnsoftbei.Helper.AnimateHelper;
import com.gouder.cnsoftbei.R;
import com.gouder.cnsoftbei.Singleton.RetrofitSingleton;
import com.gouder.cnsoftbei.Singleton.UserSingleton;

import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    @InjectView(R.id.login_progress)
    ProgressBar mProgressView;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.login_form)
    ScrollView mLoginFormView;
    @InjectView(R.id.til_phone)
    TextInputLayout tilPhone;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.til_password)
    TextInputLayout tilPassword;
    @InjectView(R.id.btn_sign_in_or_register)
    Button btnSignInOrRegister;
    @InjectView(R.id.btn_sign_in)
    Button btnSignIn;
    @InjectView(R.id.tv_ask_if_register)
    TextView tvAskIfRegister;

    private SignUpService signUpService = null;

    private LogInService logInService = null;

    private Boolean userIsSignedUp = null;
    private Call<IsSignedUpResult> isSignedUpTask;
    private Call<LogInResult> logInTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBars();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                LoginActivity.this.registerOrLogIn();
                return true;
            }
        });
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                LoginActivity.this.attemptLogin();
                return true;
            }
        });
        signUpService = RetrofitSingleton.getInstance().create(SignUpService.class);
        logInService = RetrofitSingleton.getInstance().create(LogInService.class);
        AnimateHelper.slideUp(mLoginFormView);
    }

    private void hideKeyboard(View textView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void registerOrLogIn() {
        hideKeyboard(etPhone);
        if (isSignedUpTask != null) {
            return;
        }
        etPhone.setError(null);
        String phone = etPhone.getText().toString();
        boolean cancel = false;
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            etPhone.setError(getString(R.string.error_invalid_phone));
            cancel = true;
        }
        if (cancel) {
            etPhone.requestFocus();
        } else {
            showProgress(true);
            isSignedUpTask = signUpService.isSignedUp(phone);
            isSignedUpTask.enqueue(new Callback<IsSignedUpResult>() {
                @Override
                public void onResponse(Call<IsSignedUpResult> call, Response<IsSignedUpResult> response) {
                    IsSignedUpResult result = response.body();
                    if (result != null && result.getError().getCode() == 0) {
                        userIsSignedUp = result.getIsSignedUp().getIsSignedUp();
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        userIsSignedUp = null;
                    }
                    isSignedUpTask = null;
                    showProgress(false);
                }

                @Override
                public void onFailure(Call<IsSignedUpResult> call, Throwable t) {
                    isSignedUpTask = null;
                    userIsSignedUp = null;
                    showProgress(false);
                }
            });
        }
    }


    private void initBars() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);
    }

    @OnClick(R.id.btn_sign_in_or_register)
    public void onSignInOrRegisterButtonClicked(View v) {
        registerOrLogIn();
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInButtonClicked(View v) {
        attemptLogin();
    }


    @OnClick(R.id.tv_ask_if_register)
    public void onAskIfRegisterClicked(View v) {
        Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
    }

    private void attemptLogin() {
        hideKeyboard(etPhone);
        if (logInTask != null) {
            return;
        }
        etPassword.setError(null);
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();
        boolean cancel = false;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
        }
        if (cancel) {
            etPassword.requestFocus();
        } else {
            showProgress(true);
            logInTask = logInService.logIn(new LogInBuilder(phone, password));
            logInTask.enqueue(new Callback<LogInResult>() {
                @Override
                public void onResponse(Call<LogInResult> call, Response<LogInResult> response) {
                    LogInResult result = response.body();
                    if (result != null && result.getError().getCode() == 0) {
                        UserSingleton.getInstance().setUser(result.getUser());
                        Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                        //TODO login success
                    } else {
                        Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                    showProgress(false);
                    logInTask = null;
                }

                @Override
                public void onFailure(Call<LogInResult> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    showProgress(false);
                    logInTask = null;
                }
            });
        }
    }

    private boolean isPhoneValid(String phone) {
        return Pattern.compile("^((13[0-9])|(15[^4])|(18[0235-9])|(17[0-8])|(147))\\d{8}$").matcher(phone).matches();
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        if (userIsSignedUp != null && !show) {
            tilPhone.setVisibility(userIsSignedUp ? View.GONE : View.VISIBLE);
            btnSignInOrRegister.setVisibility(userIsSignedUp ? View.GONE : View.VISIBLE);
            tilPassword.setVisibility(userIsSignedUp ? View.VISIBLE : View.GONE);
            btnSignIn.setVisibility(userIsSignedUp ? View.VISIBLE : View.GONE);
            tvAskIfRegister.setVisibility(userIsSignedUp ? View.GONE : View.VISIBLE);
            if (!userIsSignedUp) {
                AnimateHelper.nope(tvAskIfRegister);
            }
        }
    }

}

