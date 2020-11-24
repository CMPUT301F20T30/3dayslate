package com.jensen.demo.a3dayslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/* ImageFragment

   Version 1.0.0

   November 22 2020

   Copyright [2020] [Eric Weber]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/** Fragment for expanding an image that the
 * user taps on.
 *
 * @author Eric Weber
 * @version 1.0.0
 * @see EditBookActivity
 * @see ViewBookActivity
 */

public class ImageFragment extends DialogFragment {
    View view;
    byte[] byteArray;

    /** Attaches fragment to activity
     *
     * @param context
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    /** Sets up image in fragment
     *
     * @param savedInstanceState
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_image, null);

        ImageView image = view.findViewById(R.id.image_fragment_image);

        Bundle b = getArguments();
        if(b != null) {
            byteArray = b.getByteArray("image");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Log.d("WIDTH", String.valueOf(image.getWidth()));
            image.setImageBitmap(bmp);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setNegativeButton("Back", null)
                .create();

    }
}
