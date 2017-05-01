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


public interface ISignUpView {

    void initBars();

    void signUpSucceed();

    void showProgress(boolean show);

    void setBtnSendAuthCodeEnabled(boolean enabled);

    void showCodeAuthDialog(boolean show);

    void codeSent(boolean succeed);

    void startCountDown();

    void destroyCountDown();

    String getName();

    String getPassword();

    String getGender();

    String getCode();

    void authComplete(boolean succeed);

    void signUpFailed();

    void selectAvatar();

    void uploadSucceed(boolean b);

    void setAvatar(String url);
}
