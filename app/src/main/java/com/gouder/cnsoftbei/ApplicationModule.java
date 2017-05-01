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

package com.gouder.cnsoftbei;

import com.gouder.cnsoftbei.APIService.APIUrl;
import com.gouder.cnsoftbei.APIService.ImgurService;
import com.gouder.cnsoftbei.APIService.LogIn.LogInService;
import com.gouder.cnsoftbei.APIService.SignUp.SignUpService;
import com.gouder.cnsoftbei.Model.User;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    @Provides
    @Singleton
    LogInService provideLogInService(Retrofit.Builder retrofit) {
        return retrofit.build().create(LogInService.class);
    }

    @Provides
    @Singleton
    SignUpService provideSignUpService(Retrofit.Builder retrofit) {
        return retrofit.build().create(SignUpService.class);
    }

    @Provides
    @Singleton
    ImgurService provideImgurService(Retrofit.Builder retrofit) {
        return retrofit.baseUrl("https://api.imgur.com/3/").build().create(ImgurService.class);
    }

    @Provides
    @Singleton
    Interceptor provideInterceptor() {
        return new LoggingInterceptor();
    }

    @Provides
    @Singleton
    OkHttpClient provideClient(Interceptor interceptor) {
        return new OkHttpClient.Builder().addNetworkInterceptor(interceptor).build();
    }

    @Provides
    @Singleton
    Retrofit.Builder provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder().client(client).baseUrl(APIUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create());
    }

    @Provides
    @Singleton
    User provideUser() {
//        return new User("amaoamao", "17761302891", "123123", "male");
        return new User();
    }

}
