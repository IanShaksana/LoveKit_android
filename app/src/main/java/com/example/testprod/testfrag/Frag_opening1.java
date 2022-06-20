package com.example.testprod.testfrag;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_opening1 extends Fragment {
    @Nullable
    String state;
    private static final float MAX =12, MIN = 1f;
    private static final int random =5;
    View importPanel;
    FrameLayout progressOverlay;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.opening1, container, false);


        final SharedPreferences preferences = this.getActivity().getSharedPreferences("State", MODE_PRIVATE);
        state = preferences.getString("Login_State", "");
        ImageView img = view.findViewById(R.id.img_vis);
        Picasso.get().load(R.drawable.visual3b).centerInside().fit().into(img);

        Button button =  view.findViewById(R.id.start_test);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                        .replace(R.id.main_fragment, new Frag_opening2())
                        .addToBackStack(null)
                        .commit();
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
