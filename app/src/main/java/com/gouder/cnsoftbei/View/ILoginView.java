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

import android.support.annotation.StringRes;

public interface ILoginView {

    void hideKeyboard();

    Boolean isSignedUp();

    void initBars();

    void setPhoneError(@StringRes int text);

    String getPhone();

    void setPhone(String string);

    String getPassword();

    void setPassword(String string);

    void clearPhoneError();

    void setPasswordError(@StringRes int text);

    void clearPasswordError();

    void grantPasswordFocus();

    void showProgress(boolean show);

    void loginSucceed();

    void grantPhoneFocus();

    void severError();

    void showPasswordForm();

    void showTvAskIfRegister(boolean show);

    void nopeIfRegister();
}
