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

package com.gouder.cnsoftbei.Singleton;

import com.gouder.cnsoftbei.API.APIUrl;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitSingleton {

    private volatile static RetrofitSingleton ourInstance;
    private Retrofit retrofit;

    private RetrofitSingleton() {
        retrofit = new Retrofit.Builder().baseUrl(APIUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static Retrofit getInstance() {
        if (ourInstance == null) {
            synchronized (RetrofitSingleton.class) {
                if (ourInstance == null) {
                    ourInstance = new RetrofitSingleton();
                }
            }
        }
        return ourInstance.retrofit;
    }
}