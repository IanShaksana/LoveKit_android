package com.example.testprod.testfrag;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.activity.LoginActivity;
import com.example.testprod.activity.TestActivity;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_opening2 extends Fragment {
    @Nullable
    String state;
    private static final float MAX =12, MIN = 1f;
    private static final int random =5;
    View importPanel;
    FrameLayout progressOverlay;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.opening2, container, false);
        final Integer first = getActivity().getIntent().getIntExtra("first",0);
        Log.i("first",""+first);

        final SharedPreferences preferences = this.getActivity().getSharedPreferences("State", MODE_PRIVATE);
        state = preferences.getString("Login_State", "");
        ImageView img = view.findViewById(R.id.img_vis);
        Picasso.get().load(R.drawable.visual3c).centerInside().fit().into(img);

        Button button =  view.findViewById(R.id.start_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(first == 1){
                    getActivity().startActivityForResult(new Intent(getActivity(), TestActivity.class).putExtra("first",1),200);
                    getActivity().setResult(Activity.RESULT_OK);
                }else{
                    getActivity().startActivityForResult(new Intent(getActivity(), TestActivity.class).putExtra("first",0),200);
                    getActivity().setResult(Activity.RESULT_OK);
                }
                getActivity().finish();
            }
        });


        return view;
    }

    public Drawable resizeImage(int imageResource) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        double deviceWidth = display.getWidth();

        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(
                imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = deviceWidth / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), imageResource);
        Drawable drawable = new BitmapDrawable(this.getResources(),
                getResizedBitmap(bMap, newImageHeight, (int) deviceWidth));

        return drawable;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }


}
