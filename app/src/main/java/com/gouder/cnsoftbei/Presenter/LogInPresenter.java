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

package com.gouder.cnsoftbei.Presenter;


import com.gouder.cnsoftbei.APIService.LogIn.LogInBuilder;
import com.gouder.cnsoftbei.APIService.LogIn.LogInService;
import com.gouder.cnsoftbei.APIService.SignUp.SignUpService;
import com.gouder.cnsoftbei.GouderApplication;
import com.gouder.cnsoftbei.Model.IsSignedUpResult;
import com.gouder.cnsoftbei.Model.LogInResult;
import com.gouder.cnsoftbei.Model.User;
import com.gouder.cnsoftbei.R;
import com.gouder.cnsoftbei.View.ILoginView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LogInPresenter implements ILoginPresenter {


    @Inject
    SignUpService signUpService = null;
    @Inject
    LogInService logInService = null;
    @Inject
    User user = null;
    private ILoginView loginView;

    public LogInPresenter(ILoginView view) {
        this.loginView = view;
        GouderApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void registerOrLogin() {
        loginView.hideKeyboard();
        loginView.clearPhoneError();
        user.setPhone(loginView.getPhone());
        if (!user.isPhoneValid()) {
            loginView.setPhoneError(R.string.error_invalid_phone);
            loginView.grantPhoneFocus();
            return;
        }
        loginView.showProgress(true);
        signUpService.isSignedUp(user.getPhone()).enqueue(new Callback<IsSignedUpResult>() {
            @Override
            public void onResponse(Call<IsSignedUpResult> call, Response<IsSignedUpResult> response) {
                IsSignedUpResult result = response.body();
                if (result.getError().getCode() == 0) {
                    if (result.getIsSignedUp().getIsSignedUp()) {
                        loginView.showTvAskIfRegister(false);
                        loginView.showPasswordForm();
                    } else {
                        loginView.showTvAskIfRegister(true);
                        loginView.nopeIfRegister();
                    }

                    loginView.showProgress(false);
                } else {
                    onFailure(null, null);
                }
            }

            @Override
            public void onFailure(Call<IsSignedUpResult> call, Throwable t) {
                loginView.severError();
                loginView.showProgress(false);
            }
        });

    }

    @Override
    public void login() {
        loginView.hideKeyboard();
        loginView.clearPasswordError();
        user.setPsw(loginView.getPassword());
        if (!user.isPasswordValid()) {
            loginView.setPasswordError(R.string.error_invalid_password);
            return;
        }
        loginView.showProgress(true);
        logInService.logIn(new LogInBuilder(user.getPhone(), user.getPsw()))
                .enqueue(new Callback<LogInResult>() {
                    @Override
                    public void onResponse(Call<LogInResult> call, Response<LogInResult> response) {
                        LogInResult result = response.body();
                        if (result != null && result.getError().getCode() == 0) {
                            loginView.showProgress(false);
                            User resultUser = response.body().getUser();
                            user.setName(resultUser.getName());
                            user.setCredit(resultUser.getCredit());
                            user.setGender(resultUser.getGender());
                            loginView.loginSucceed();
                        } else {
                            onFailure(null, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<LogInResult> call, Throwable t) {
                        loginView.showProgress(false);
                        loginView.setPasswordError(R.string.error_incorrect_password);
                    }
                });
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }
}
