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

package com.gouder.cnsoftbei.Helper;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.gouder.cnsoftbei.R;


public class AnimateHelper {
    public static void nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.spacing_medium);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500).start();
    }

    public static void slideUp(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(),
                R.anim.slid_up));
    }
}
