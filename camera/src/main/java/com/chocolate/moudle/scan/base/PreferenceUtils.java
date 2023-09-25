/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chocolate.moudle.scan.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraSelector;

import com.google.common.base.Preconditions;
import com.google.mlkit.vision.face.FaceDetectorOptions;

/** Utility class to retrieve shared preferences. */
public class PreferenceUtils {

  //是否隐藏人脸检测的debug数据显示
  public static boolean shouldHideDetectionInfo(Context context) {
    return true;
  }

  public static FaceDetectorOptions getFaceDetectorOptions(Context context) {
    int landmarkMode = FaceDetectorOptions.LANDMARK_MODE_NONE;
    int contourMode = FaceDetectorOptions.CONTOUR_MODE_ALL;
    int classificationMode = FaceDetectorOptions.CLASSIFICATION_MODE_ALL;
    int performanceMode = FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE;
    boolean enableFaceTracking = false;
    float minFaceSize = 0.1f;

    FaceDetectorOptions.Builder optionsBuilder =
        new FaceDetectorOptions.Builder()
            .setLandmarkMode(landmarkMode)
            .setContourMode(contourMode)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setMinFaceSize(minFaceSize);

    if (enableFaceTracking) {
      optionsBuilder.enableTracking();
    }
    return optionsBuilder.build();
  }

  public static boolean isCameraLiveViewportEnabled(Context context) {
    return false;
  }

  @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
  @Nullable
  public static android.util.Size getCameraXTargetResolution(Context context, int lensfacing) {
    Preconditions.checkArgument(
            lensfacing == CameraSelector.LENS_FACING_BACK
                    || lensfacing == CameraSelector.LENS_FACING_FRONT);;
    try {
      return android.util.Size.parseSize("600x600");
    } catch (Exception e) {
      return null;
    }
  }

  private PreferenceUtils() {}
}
