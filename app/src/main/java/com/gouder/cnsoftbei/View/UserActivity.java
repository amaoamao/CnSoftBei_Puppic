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
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gouder.cnsoftbei.ApplicationComponent;
import com.gouder.cnsoftbei.BaseActivity;
import com.gouder.cnsoftbei.Presenter.IUserPresenter;
import com.gouder.cnsoftbei.Presenter.UserPresenter;
import com.gouder.cnsoftbei.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends BaseActivity implements IUserView {
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    IUserPresenter presenter;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.fab)
    FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new UserPresenter(this);
        initBars();
        presenter.init();
    }

    @OnClick(R.id.fab)
    public void onFabClicked() {
        presenter.fabClicked();
    }

    public void initBars() {
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void injectComponent(ApplicationComponent component) {
    }

    @Override
    public void introAnimate() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void setAvatar(String url) {
        Glide.with(this).load(url).into(ivAvatar);
    }

    @Override
    public void enableEditTexts(boolean enabled) {
        etUsername.setEnabled(enabled);
    }

    @Override
    public void setFabIcon(@DrawableRes int res) {
        fab.setImageDrawable(getDrawable(res));
    }

    @Override
    public void setPhone(String phone) {
        etPhone.setText(phone);
    }

    @Override
    public String getUsername() {
        return etUsername.getText().toString();
    }

    @Override
    public void setUsername(String username) {
        etUsername.setText(username);
    }
}
