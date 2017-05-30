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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.gouder.cnsoftbei.R;
import com.gouder.cnsoftbei.Widget.StackLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DashboardFragment extends Fragment {
    @BindView(R.id.stack_img)
    StackLayout stackImg;
    Unbinder unbinder;


    public DashboardFragment() {
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        final StackAdapter stackAdapter = new StackAdapter(getContext());
        stackAdapter.add("1");
        stackAdapter.add("2");
        stackAdapter.add("3");
        stackAdapter.add("4");
        stackAdapter.add("5");
        stackAdapter.add("6");
        stackAdapter.add("7");
        stackImg.setAdapter(stackAdapter);
        return view;
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

    public class StackAdapter extends ArrayAdapter {

        public StackAdapter(Context context) {
            super(context, R.layout.item_stack);
        }

        @NonNull
        @Override
        public View getView(int position, final View convertView, @NonNull final ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_stack, parent, false);
            }
            final String name = (String) getItem(position);
//            ((TextView) view.findViewById(R.id.name)).setText(name);
            final View completeView = view.findViewById(R.id.complete);
            view.setTag(name);
            completeView.setOnClickListener(v -> stackImg.removeViewWithAnim(convertView, false));
            return view;
        }
    }
}
