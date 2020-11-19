package com.jensen.demo.a3dayslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.nio.file.ClosedFileSystemException;

public class ImageFragment extends DialogFragment {
    View view;
    byte[] byteArray;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        view = LayoutInflater.from(getActivity()).inflate(R.layout.image_fragment, null);

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
