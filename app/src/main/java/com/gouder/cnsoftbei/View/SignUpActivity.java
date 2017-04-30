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

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gouder.cnsoftbei.ApplicationComponent;
import com.gouder.cnsoftbei.BaseActivity;
import com.gouder.cnsoftbei.Presenter.ISignUpPresenter;
import com.gouder.cnsoftbei.Presenter.SignUpPresenter;
import com.gouder.cnsoftbei.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements ISignUpView {


    ISignUpPresenter presenter;

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignUp;
    private MaterialDialog progressDialog;
    private MaterialDialog codeAuthDialog;
    private AppCompatButton btnSendAuthCode;
    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnSendAuthCode.setText(String.format(Locale.getDefault(), "%d s", millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            btnSendAuthCode.setText(R.string.send);
            setBtnSendAuthCodeEnabled(true);
        }
    };
    private EditText etAuthCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBars();
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        presenter = new SignUpPresenter(this);
    }

    @Override
    public void injectComponent(ApplicationComponent component) {

    }

    @Override
    public void introAnimate() {
    }

    private void initCodeAuthDialog(MaterialDialog codeAuthDialog) {
        btnSendAuthCode = ButterKnife.findById(codeAuthDialog, R.id.btn_send_auth_code);
        etAuthCode = ButterKnife.findById(codeAuthDialog, R.id.et_auth_code);
        btnSendAuthCode.setOnClickListener((View v) -> presenter.sendAuthCode());
        codeAuthDialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
        etAuthCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                codeAuthDialog.getActionButton(DialogAction.POSITIVE).setEnabled(s.length() == 6);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @OnClick(R.id.tv_ask_if_login)
    void onTvAskIfLoginClicked() {
        finish();
    }


    @OnClick(R.id.btn_signup)
    void onSignUpButtonCLicked() {
        presenter.signUp();
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
    public void signUpSucceed() {
        Toast.makeText(SignUpActivity.this, R.string.sign_up_succeed, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showProgress(boolean show) {
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
    public void setBtnSendAuthCodeEnabled(boolean enabled) {
        btnSendAuthCode.setEnabled(enabled);
    }

    @Override
    public void showCodeAuthDialog(boolean show) {
        if (codeAuthDialog == null) {
            codeAuthDialog = new MaterialDialog.Builder(this)
                    .title(R.string.phone_auth)
                    .positiveText(R.string.ok)
                    .negativeText(R.string.cancel)
                    .onNegative((dialog, which) -> {
                        showCodeAuthDialog(false);
                        SignUpActivity.this.finish();
                    })
                    .onPositive((dialog, which) -> presenter.codeAuth()).cancelable(false)
                    .customView(R.layout.dialog_auth_code, true)
                    .autoDismiss(false).build();
            initCodeAuthDialog(codeAuthDialog);
        }

        if (show) {
            codeAuthDialog.show();
        } else {
            destroyCountDown();
            codeAuthDialog.dismiss();
        }

    }

    @Override
    public void codeSent(boolean succeed) {
        Toast.makeText(SignUpActivity.this, succeed ? R.string.send_succeed : R.string.send_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startCountDown() {
        countDownTimer.start();
    }

    @Override
    public void destroyCountDown() {
        countDownTimer.cancel();
    }

    @Override
    public String getName() {
        return etName.getText().toString();
    }


    @Override
    public String getPassword() {
        return etPassword.getText().toString();
    }

    @Override
    public String getGender() {
        return rgGender.getCheckedRadioButtonId() == R.id.radio_male ? "male" : "female";
    }

    @Override
    public String getCode() {
        return etAuthCode.getText().toString();
    }

    @Override
    public void authComplete(boolean succeed) {
        Toast.makeText(SignUpActivity.this, succeed ? R.string.auth_succeed : R.string.auth_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signUpFailed() {
        Toast.makeText(SignUpActivity.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
    }
}
