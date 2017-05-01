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


import com.gouder.cnsoftbei.GouderApplication;
import com.gouder.cnsoftbei.Model.User;
import com.gouder.cnsoftbei.R;
import com.gouder.cnsoftbei.View.IUserView;

import javax.inject.Inject;

public class UserPresenter implements IUserPresenter {

    @Inject
    User user;
    private boolean editMode = false;
    private IUserView view;

    public UserPresenter(IUserView view) {
        this.view = view;
        GouderApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void fabClicked() {
        if (editMode) {
            view.setFabIcon(R.drawable.ic_mode_edit_black_24dp);
        } else {
            view.setFabIcon(R.drawable.ic_done_black_24dp);
        }
        editMode = !editMode;
        view.enableEditTexts(editMode);
    }

    @Override
    public void init() {
        view.setAvatar(user.getAvatar());
        view.setPhone(user.getPhone());
        view.setUsername(user.getName());
    }
}
