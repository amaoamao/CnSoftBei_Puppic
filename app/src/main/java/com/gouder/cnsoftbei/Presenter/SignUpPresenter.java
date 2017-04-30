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


import android.util.Log;

import com.google.gson.Gson;
import com.gouder.cnsoftbei.APIService.SignUp.SignUpBuilder;
import com.gouder.cnsoftbei.APIService.SignUp.SignUpService;
import com.gouder.cnsoftbei.GouderApplication;
import com.gouder.cnsoftbei.Model.CodeAuthResult;
import com.gouder.cnsoftbei.Model.SendAuthCodeResult;
import com.gouder.cnsoftbei.Model.SignUpResult;
import com.gouder.cnsoftbei.Model.Token;
import com.gouder.cnsoftbei.Model.User;
import com.gouder.cnsoftbei.View.ISignUpView;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPresenter implements ISignUpPresenter {

    @Inject
    SignUpService signUpService;
    @Inject
    User user;
    private ISignUpView view;
    private Token token;

    public SignUpPresenter(ISignUpView view) {
        this.view = view;
        GouderApplication.getApplicationComponent().inject(this);
        view.showCodeAuthDialog(true);
    }

    @Override
    public void sendAuthCode() {
        view.setBtnSendAuthCodeEnabled(false);
        signUpService.sendAuthCode(user.getPhone()).enqueue(new Callback<SendAuthCodeResult>() {
            @Override
            public void onResponse(Call<SendAuthCodeResult> call, Response<SendAuthCodeResult> response) {
                if (response.body() != null && response.body().getError().getCode() == 0) {
                    view.startCountDown();
                    view.codeSent(true);
                } else {
                    onFailure(null, null);
                }
            }

            @Override
            public void onFailure(Call<SendAuthCodeResult> call, Throwable t) {
                view.codeSent(false);
                view.setBtnSendAuthCodeEnabled(true);
            }
        });
    }

    @Override
    public void codeAuth() {
        view.showProgress(true);
        signUpService.codeAuth(user.getPhone(), view.getCode()).enqueue(new Callback<CodeAuthResult>() {
            @Override
            public void onResponse(Call<CodeAuthResult> call, Response<CodeAuthResult> response) {
                Log.e("TAG", new Gson().toJson(response.body()));
                if (response.body() != null && response.body().getError().getCode() == 0) {
                    token = response.body().getToken();
                    view.showProgress(false);
                    view.showCodeAuthDialog(false);
                    view.authComplete(true);
                } else {
                    onFailure(null, null);
                }
            }

            @Override
            public void onFailure(Call<CodeAuthResult> call, Throwable t) {
                view.showProgress(false);
                view.authComplete(false);
            }
        });
    }

    @Override
    public void signUp() {
        user.setPsw(view.getPassword());
        user.setGender(view.getGender());
        user.setName(view.getName());
        Log.e("FFFFUCK", new Gson().toJson(user));
        signUpService.signUp(new SignUpBuilder(user, token.getToken())).enqueue(new Callback<SignUpResult>() {
            @Override
            public void onResponse(Call<SignUpResult> call, Response<SignUpResult> response) {
                Log.e("FFFFUCK", new Gson().toJson(response.body(), SignUpResult.class));
                if (response.body() != null && response.body().getError().getCode() == 0) {
                    view.signUpSucceed();
                } else {
                    onFailure(null, null);
                }
            }

            @Override
            public void onFailure(Call<SignUpResult> call, Throwable t) {
                view.showProgress(false);
                view.signUpFailed();
            }
        });
    }

    @Override
    public void onDestroy() {
        view = null;
    }
}
