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


import com.gouder.cnsoftbei.APIService.UserProfileService;
import com.gouder.cnsoftbei.GouderApplication;
import com.gouder.cnsoftbei.Model.GetAllCategoriesResult;
import com.gouder.cnsoftbei.Model.HttpResponse;
import com.gouder.cnsoftbei.Model.User;
import com.gouder.cnsoftbei.Model.UserPreferenceResult;
import com.gouder.cnsoftbei.R;
import com.gouder.cnsoftbei.View.IUserView;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresenter implements IUserPresenter {

    @Inject
    User user;

    @Inject
    UserProfileService service;
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
            view.showProgress(true);

            service.updateProfile(user.getPhone(), user).enqueue(new Callback<HttpResponse>() {
                @Override
                public void onResponse(Call<HttpResponse> call, Response<HttpResponse> response) {
                    System.out.println(response.body().getError().getCode());
                    view.showProgress(false);
                }

                @Override
                public void onFailure(Call<HttpResponse> call, Throwable t) {
                    view.showProgress(false);
                    t.printStackTrace();
                }
            });
        } else {
            view.setFabIcon(R.drawable.ic_done_black_24dp);

        }
        editMode = !editMode;
        view.setEditTextsEnabled(editMode);
        view.setCheckBoxEnabled(editMode);
    }

    @Override
    public void init() {
        view.setAvatar(user.getAvatar());
        view.setPhone(user.getPhone());
        view.setUsername(user.getName());
        view.setCheckBoxEnabled(false);
        service.getAllCategories().enqueue(new Callback<GetAllCategoriesResult>() {
            @Override
            public void onResponse(Call<GetAllCategoriesResult> call, Response<GetAllCategoriesResult> response) {
                List<String> cat_list = response.body().getCat_list();
                view.setCheckBoxs(cat_list);
                service.getUserPreference(user.getPhone()).enqueue(new Callback<UserPreferenceResult>() {
                    @Override
                    public void onResponse(Call<UserPreferenceResult> call, Response<UserPreferenceResult> response) {
                        List<String> pref_list = response.body().getPref_list();
                        view.setCheckBoxSelected(pref_list);
                    }

                    @Override
                    public void onFailure(Call<UserPreferenceResult> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }

            @Override
            public void onFailure(Call<GetAllCategoriesResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
