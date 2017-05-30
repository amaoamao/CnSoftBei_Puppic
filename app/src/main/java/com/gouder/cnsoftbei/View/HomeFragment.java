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

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gouder.cnsoftbei.GouderApplication;
import com.gouder.cnsoftbei.Model.User;
import com.gouder.cnsoftbei.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements IHomeView {
    @BindView(R.id.iv_avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_gender)
    ImageView ivGender;
    @BindView(R.id.rl_user)
    RelativeLayout rlUser;
    @BindView(R.id.tv_history)
    TextView tvHistory;

    Unbinder unbinder;

    @Inject
    User user;
    private View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, rootView);
        GouderApplication.getApplicationComponent().inject(this);
        refresh(user);
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refresh(User user) {
        tvName.setText(user.getName());
        tvPhone.setText(user.getPhone());
        ivGender.setImageTintList(ColorStateList.valueOf(GouderApplication.getContext().getResources().getColor(
                user.getGender().equals("male") ? R.color.colorPrimary : R.color.colorPink)));
        Glide.with(this).load(user.getAvatar()).into(ivAvatar);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh(user);
    }

    @OnClick(R.id.rl_user)
    public void onUserClicked() {
        startActivity(new Intent(getActivity(), UserActivity.class));
    }
}
