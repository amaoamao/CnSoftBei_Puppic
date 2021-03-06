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


import android.support.annotation.DrawableRes;

import java.util.List;

public interface IUserView {

    void setAvatar(String url);

    void setEditTextsEnabled(boolean enabled);

    void setFabIcon(@DrawableRes int res);

    void setPhone(String phone);

    String getUsername();

    void setUsername(String username);

    void showProgress(boolean b);

    void setCheckBoxEnabled(boolean enabled);

    void setCheckBoxSelected(List<String> selected);

    void setCheckBoxs(List<String> boxes);
}
