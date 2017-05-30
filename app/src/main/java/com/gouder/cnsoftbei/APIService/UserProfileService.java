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

package com.gouder.cnsoftbei.APIService;


import com.gouder.cnsoftbei.Model.GetAllCategoriesResult;
import com.gouder.cnsoftbei.Model.HttpResponse;
import com.gouder.cnsoftbei.Model.User;
import com.gouder.cnsoftbei.Model.UserPreferenceResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserProfileService {

    @POST("/{phone}")
    Call<HttpResponse> updateProfile(@Path("phone") String phone, @Body User user);

    @GET("/get_preference")
    Call<UserPreferenceResult> getUserPreference(@Query("phone") String phone);

    @GET("/getAllCategories")
    Call<GetAllCategoriesResult> getAllCategories();

}
