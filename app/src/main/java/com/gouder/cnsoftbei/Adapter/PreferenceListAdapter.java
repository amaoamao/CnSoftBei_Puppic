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

package com.gouder.cnsoftbei.Adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.gouder.cnsoftbei.R;

import java.util.List;

public class PreferenceListAdapter extends RecyclerView.Adapter<PreferenceListAdapter.MyViewHolder> {
    private List<String> preferList;


    private List<String> selected;

    private boolean enabled = false;

    public PreferenceListAdapter(List<String> preferList, List<String> selected) {
        this.preferList = preferList;
        this.selected = selected;
    }

    public List<String> getPreferList() {
        return preferList;
    }

    public void setPreferList(List<String> preferList) {
        this.preferList = preferList;
        this.notifyDataSetChanged();
    }

    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
        this.notifyDataSetChanged();
    }

    @Override
    public PreferenceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_checkbox, parent, false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(PreferenceListAdapter.MyViewHolder holder, int position) {
        holder.cbPrefer.setText(preferList.get(position));
        holder.cbPrefer.setSelected(selected.contains(preferList.get(position)));
        holder.cbPrefer.setEnabled(enabled);

    }


    @Override
    public int getItemCount() {
        return preferList.size();
    }

    public void setCheckBoxEnabled(boolean enabled) {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox cbPrefer;

        public MyViewHolder(View itemView) {
            super(itemView);

            cbPrefer = (CheckBox) itemView.findViewById(R.id.cb_preference);
            this.setIsRecyclable(false);
        }
    }
}
