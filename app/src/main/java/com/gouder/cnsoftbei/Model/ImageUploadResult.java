
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

package com.gouder.cnsoftbei.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageUploadResult {

    @SerializedName("data")
    @Expose
    private UploadedImageInfo uploadedImageInfo;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("status")
    @Expose
    private Integer status;

    public UploadedImageInfo getUploadedImageInfo() {
        return uploadedImageInfo;
    }

    public void setUploadedImageInfo(UploadedImageInfo uploadedImageInfo) {
        this.uploadedImageInfo = uploadedImageInfo;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
