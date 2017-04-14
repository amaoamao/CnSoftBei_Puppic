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
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {


    public static final int SIGN_UP = 0;
    @BindView(R.id.login_progress)
    ProgressBar mProgressView;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.login_form)
    ScrollView mLoginFormView;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.btn_sign_in_or_register)
    Button btnSignInOrRegister;
    @BindView(R.id.btn_sign_in)
    Button btnSignIn;
    @BindView(R.id.tv_ask_if_register)
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
        ButterKnife.bind(this);
        signUpService = RetrofitSingleton.getInstance().create(SignUpService.class);
        logInService = RetrofitSingleton.getInstance().create(LogInService.class);
        animate();
    }

    private void animate() {
        AnimateHelper.slideUp(mLoginFormView);
    }

    private void hideKeyboard(View textView) {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnEditorAction(R.id.et_password)
    public boolean onEtPasswordAction() {
        LoginActivity.this.attemptLogin();
        return true;
    }

    @OnEditorAction(R.id.et_phone)
    public boolean onEtPhoneAction() {
        LoginActivity.this.registerOrLogIn();
        return true;
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
                        isSignedUpTask = null;
                        showProgress(false);
                    } else {
                        onFailure(null, null);
                    }
                }

                @Override
                public void onFailure(Call<IsSignedUpResult> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
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
    public void onSignInOrRegisterButtonClicked() {
        registerOrLogIn();
//        userIsSignedUp = false;
//        showProgress(false);
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInButtonClicked() {
        attemptLogin();
    }


    @OnClick(R.id.tv_ask_if_register)
    public void onAskIfRegisterClicked() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone", etPhone.getText().toString());
        startActivity(intent);
        startActivityForResult(intent, SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP && data != null) {
            String phone = data.getStringExtra("phone");
            String psw = data.getStringExtra("psw");
            etPhone.setText(phone);
            etPassword.setText(psw);
            userIsSignedUp = true;
            attemptLogin();
        }
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
                        Toast.makeText(LoginActivity.this, R.string.welcome_back, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        onFailure(null, null);
                    }

                }

                @Override
                public void onFailure(Call<LogInResult> call, Throwable t) {
//                    Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    etPassword.setError(getString(R.string.wrong_password));
                    etPassword.requestFocus();
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
        Log.e("TAG", "showProgress(" + show + ")");
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

