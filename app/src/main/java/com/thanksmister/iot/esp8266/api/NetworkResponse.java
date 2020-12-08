/*
 * Copyright (c) 2018 ThanksMister LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thanksmister.iot.esp8266.api;


import android.support.annotation.NonNull;

import javax.annotation.Nullable;

import static com.thanksmister.iot.esp8266.api.Status.ERROR;
import static com.thanksmister.iot.esp8266.api.Status.LOADING;
import static com.thanksmister.iot.esp8266.api.Status.SUCCESS;

/**
 * Response holder provided to the UI
 * https://proandroiddev.com/mvvm-architecture-using-livedata-rxjava-and-new-dagger-android-injection-639837b1eb6c
 */
public class NetworkResponse<T> {

    public  Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private NetworkResponse(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> NetworkResponse<T> loading() {
        return new NetworkResponse<T>(LOADING, null, null);
    }

    public static <T> NetworkResponse<T> success(@NonNull T data) {
        return new NetworkResponse<T>(SUCCESS, data, null);
    }

    public static <T> NetworkResponse<T> error(@NonNull Throwable error) {
        return new NetworkResponse<T>(ERROR, null, error);
    }
}