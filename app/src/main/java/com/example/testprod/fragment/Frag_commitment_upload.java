package com.example.testprod.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.testprod.R;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Adrian on 5/18/2018.
 */

public class Frag_commitment_upload extends Fragment {

    private ImageView imageView;
    Button button1;
    Button button2;
    private String id_task;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE_REQUEST =2;
    private Uri mImageUri;
    private Bitmap bitmap_prove;
    private RelativeLayout spinner;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.commitment_upload, container, false);

        final SharedPreferences preferences = getContext().getSharedPreferences("State", MODE_PRIVATE);

        button1 = (Button) view.findViewById(R.id.Open_Camera);
        button2 = (Button) view.findViewById(R.id.send_prove);
        imageView = view.findViewById(R.id.prove_image);
        spinner = view.findViewById(R.id.progress_circular);
        spinner.setVisibility(View.GONE);

        Bundle bundle = getArguments();
        button2.setEnabled(false);
        if (bundle != null) {
            id_task = bundle.getString("id");
            // get_img(id);
        }else{
            // show spinner
        }


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] colors = {"Buka Kamera", "Buka Galeri"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Unggah Bukti Foto");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which == 0) {
                            Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cInt, REQUEST_TAKE_PHOTO);
                        }
                        if (which == 1) {
                            openfile();
                        }
                    }
                });
                builder.show();
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                send_img(id_task);
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                bitmap_prove = bp;
                imageView.setImageBitmap(bp);
                button2.setEnabled(true);
            } else if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            try {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(imageView);
                imageView.setImageURI(mImageUri);
                bitmap_prove = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
                button2.setEnabled(true);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void send_img(String id){
        try {

            spinner.setVisibility(View.VISIBLE);
            http_post post = new http_post(getContext());
            JSONObject JObject = new JSONObject();
            JObject.put("api","prove");
            JObject.put("encodedimage",encodeImage(bitmap_prove));
            JObject.put("id",id);
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if(JObject.getString("statuscode").equals("OK")){
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                            // update();
                            back();

                        }else{
                            Toast.makeText(getContext(),JObject.getString("message"),Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.GONE);
                    }catch(Exception e){

                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openfile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    private void back(){
        getFragmentManager().popBackStack();
    }

    private void update() {
        Fragment current = getFragmentManager().findFragmentById(R.id.main_fragment);
        if (current instanceof Frag_commitment_upload) {
            getFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

}
