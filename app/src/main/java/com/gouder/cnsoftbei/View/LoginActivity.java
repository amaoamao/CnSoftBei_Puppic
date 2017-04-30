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

package com.gouder.cnsoftbei.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
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

import com.afollestad.materialdialogs.MaterialDialog;
import com.gouder.cnsoftbei.ApplicationComponent;
import com.gouder.cnsoftbei.BaseActivity;
import com.gouder.cnsoftbei.Helper.AnimateHelper;
import com.gouder.cnsoftbei.Presenter.LogInPresenter;
import com.gouder.cnsoftbei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;


public class LoginActivity extends BaseActivity implements ILoginView {


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

    LogInPresenter presenter = null;
    private MaterialDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBars();
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LogInPresenter(this);
        introAnimate();
    }

    @Override
    public void injectComponent(ApplicationComponent component) {
    }

    @Override
    public void introAnimate() {
        AnimateHelper.slideUp(mLoginFormView);
    }


    @OnEditorAction(R.id.et_password)
    public boolean onEtPasswordAction() {
        presenter.login();
        return true;
    }

    @OnEditorAction(R.id.et_phone)
    public boolean onEtPhoneAction() {
        presenter.registerOrLogin();
        return true;
    }


    @OnClick(R.id.btn_sign_in_or_register)
    public void onSignInOrRegisterButtonClicked() {
        presenter.registerOrLogin();
    }

    @OnClick(R.id.btn_sign_in)
    public void onSignInButtonClicked() {
        presenter.login();
    }


    @OnClick(R.id.tv_ask_if_register)
    public void onAskIfRegisterClicked() {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("phone", etPhone.getText().toString());
        startActivityForResult(intent, SIGN_UP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_UP && data != null) {
            String phone = data.getStringExtra("phone");
            String psw = data.getStringExtra("psw");
            setPhone(phone);
            setPassword(psw);
            presenter.login();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }


    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(etPhone.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public Boolean isSignedUp() {
        return null;
    }

    @Override
    public void initBars() {
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

    @Override
    public void setPhoneError(@StringRes int text) {
        etPhone.setError(getString(text));
    }

    @Override
    public String getPhone() {
        return etPhone.getText().toString();
    }

    @Override
    public void setPhone(String string) {
        etPhone.setText(string);
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public void setPassword(String string) {
        etPassword.setText(string);
    }

    @Override
    public void clearPhoneError() {
        etPhone.setError(null);
    }

    @Override
    public void setPasswordError(@StringRes int text) {
        etPassword.setError(getString(text));
    }

    @Override
    public void grantPhoneFocus() {
        etPhone.requestFocus();
    }

    @Override
    public void severError() {
        Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordForm() {
        tilPhone.setVisibility(View.GONE);
        btnSignInOrRegister.setVisibility(View.GONE);
        tilPassword.setVisibility(View.VISIBLE);
        btnSignIn.setVisibility(View.VISIBLE);
        tvAskIfRegister.setVisibility(View.GONE);
    }

    @Override
    public void showTvAskIfRegister(boolean show) {
        tvAskIfRegister.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void nopeIfRegister() {
        AnimateHelper.nope(tvAskIfRegister);
    }

    @Override
    public void clearPasswordError() {
        etPassword.setError(null);
    }

    @Override
    public void grantPasswordFocus() {
        etPassword.requestFocus();
    }


    @Override
    public void showProgress(final boolean show) {
        if (progressDialog == null) {
            progressDialog = new MaterialDialog.Builder(this).title(R.string.authenticating).progress(true, 100).content(R.string.wait).build();
        }
        if (show) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    @Override
    public void loginSucceed() {
        Toast.makeText(LoginActivity.this, R.string.welcome_back, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }


}

