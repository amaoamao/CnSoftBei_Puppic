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

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.gouder.cnsoftbei.API.SignUp.SignUpBuilder;
import com.gouder.cnsoftbei.API.SignUp.SignUpService;
import com.gouder.cnsoftbei.Entity.CodeAuthResult;
import com.gouder.cnsoftbei.Entity.SendAuthCodeResult;
import com.gouder.cnsoftbei.Entity.SignUpResult;
import com.gouder.cnsoftbei.Entity.Token;
import com.gouder.cnsoftbei.Entity.User;
import com.gouder.cnsoftbei.R;
import com.gouder.cnsoftbei.Singleton.RetrofitSingleton;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {


    SignUpService signUpService;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    String phone;
    CountDownTimer countDownTimer;
    Token token;
    MaterialDialog authenticatingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBars();
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        signUpService = RetrofitSingleton.getInstance().create(SignUpService.class);
        phone = getIntent().getStringExtra("phone");
        authenticatingDialog = new MaterialDialog.Builder(SignUpActivity.this)
                .progress(true, 100)
                .content(R.string.authenticating).cancelable(false).build();
        MaterialDialog codeAuthDialog = new MaterialDialog.Builder(this)
                .title(R.string.phone_auth)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                    SignUpActivity.this.finish();
                })
                .onPositive((dialog, which) -> {
                    authenticatingDialog.show();
                    signUpService.codeAuth(phone, ((EditText) dialog.findViewById(R.id.et_auth_code)).getText().toString()).enqueue(new Callback<CodeAuthResult>() {
                        @Override
                        public void onResponse(Call<CodeAuthResult> call, Response<CodeAuthResult> response) {
                            Log.e("TAG", new Gson().toJson(response.body()));
                            if (response.body() != null && response.body().getError().getCode() == 0) {
                                token = response.body().getToken();
                                authenticatingDialog.dismiss();
                                dialog.dismiss();
                                Toast.makeText(SignUpActivity.this, R.string.auth_succeed, Toast.LENGTH_SHORT).show();
                            } else {
                                onFailure(null, null);
                            }
                        }

                        @Override
                        public void onFailure(Call<CodeAuthResult> call, Throwable t) {
                            authenticatingDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
                        }
                    });

                }).cancelable(false)
                .customView(R.layout.dialog_auth_code, true)
                .autoDismiss(false).build();
        initCodeAuthDialog(codeAuthDialog);
        codeAuthDialog.setOnDismissListener(dialog -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        });
        codeAuthDialog.show();
    }

    private void initCodeAuthDialog(MaterialDialog codeAuthDialog) {
        Button btnSendAuthCode = ButterKnife.findById(codeAuthDialog, R.id.btn_send_auth_code);
        EditText etAuthCode = ButterKnife.findById(codeAuthDialog, R.id.et_auth_code);
        btnSendAuthCode.setOnClickListener((View v) -> {
            signUpService.sendAuthCode(phone).enqueue(new Callback<SendAuthCodeResult>() {
                @Override
                public void onResponse(Call<SendAuthCodeResult> call, Response<SendAuthCodeResult> response) {
                    if (response.body() != null && response.body().getError().getCode() == 0) {
                        countDownTimer = new CountDownTimer(60000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                btnSendAuthCode.setText(String.format(Locale.getDefault(), "%d s", millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {
                                btnSendAuthCode.setText(R.string.send);
                                btnSendAuthCode.setEnabled(true);
                            }
                        }.start();

                        Toast.makeText(SignUpActivity.this, R.string.send_succeed, Toast.LENGTH_SHORT).show();
                    } else {
                        onFailure(null, null);
                    }
                }

                @Override
                public void onFailure(Call<SendAuthCodeResult> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, R.string.send_failed, Toast.LENGTH_SHORT).show();
                    btnSendAuthCode.setEnabled(true);
                }
            });
            btnSendAuthCode.setEnabled(false);
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.tv_ask_if_login)
    void onTvAskIfLoginClicked() {
        finish();
    }


    @OnClick(R.id.btn_signup)
    void onSignUpButtonCLicked() {
        //TODO input validation
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();
        String gender = rgGender.getCheckedRadioButtonId() == R.id.radio_male ? "male" : "female";
        signUpService.signUp(new SignUpBuilder(new User(name, phone, password, gender), token.getToken())).enqueue(new Callback<SignUpResult>() {
            @Override
            public void onResponse(Call<SignUpResult> call, Response<SignUpResult> response) {
                if (response.body() != null && response.body().getError().getCode() == 0) {
                    Toast.makeText(SignUpActivity.this, R.string.sign_up_succeed, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    intent.putExtra("psw", password);
                    SignUpActivity.this.setResult(LoginActivity.SIGN_UP, intent);
                } else {
                    onFailure(null, null);
                }
            }

            @Override
            public void onFailure(Call<SignUpResult> call, Throwable t) {
                authenticatingDialog.dismiss();
                Toast.makeText(SignUpActivity.this, R.string.sign_up_failed, Toast.LENGTH_SHORT).show();
            }
        });
        authenticatingDialog.show();
    }
}
