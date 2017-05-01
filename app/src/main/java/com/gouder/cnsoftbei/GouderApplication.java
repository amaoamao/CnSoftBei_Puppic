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


import android.app.Application;
import android.content.Context;

import java.lang.ref.WeakReference;


public class GouderApplication extends Application {
    private static ApplicationComponent applicationComponent;
    private static WeakReference<Context> context;

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    public static Context getContext() {
        return context.get();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule()).build();
        context = new WeakReference<>(this.getApplicationContext());
    }
}
