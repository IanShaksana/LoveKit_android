package com.example.testprod.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testprod.R;
import com.example.testprod.background.http_get;
import com.example.testprod.background.http_post;
import com.example.testprod.general.utility;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Frag_profile extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE_REQUEST = 2;
    private Uri mImageUri;
    private Bitmap bitmap_prove;

    private TextView text0;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TextView text7;
    private Button button1;
    private Button button2;
    private Button button3;
    private View view;
    private ImageView imageView;
    private RelativeLayout spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        text0 = view.findViewById(R.id.title_profile);
        text1 = view.findViewById(R.id.profile_name);
        text2 = view.findViewById(R.id.profile_detail1);
        text3 = view.findViewById(R.id.profile_detail2);
        text4 = view.findViewById(R.id.profile_detail3);
        text5 = view.findViewById(R.id.profile_detail4);
        text6 = view.findViewById(R.id.profile_detail5);
        text7 = view.findViewById(R.id.profile_detail6);
        button1 = view.findViewById(R.id.edit_profile);
        button2 = view.findViewById(R.id.change_password);
        button3 = view.findViewById(R.id.change_pic);
        imageView = view.findViewById(R.id.propic);
        spinner = view.findViewById(R.id.progress_circular);
        spinner.setVisibility(View.GONE);

        Picasso.get().load(R.drawable.placeholder_img).centerInside().fit().into(imageView);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String id = bundle.getString("id");
            Boolean local_or_not = bundle.getBoolean("local_or_not");
            if (!local_or_not) {
                text0.setText("Tentang Partnerku");
                getProfile_Partner(id);
                button1.setVisibility(view.GONE);
                button2.setVisibility(view.GONE);
                button3.setVisibility(view.GONE);
            } else {
                final utility gu = new utility(getContext());
                getProfile_Local(gu.get_data("Login_State"));

                button3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // dialog upload yes or no

                        String[] colors = {"Buka Kamera", "Buka Galeri"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Ubah Foto Profil");
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



                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle newBundle = new Bundle();
                        newBundle.putString("choice", "profile");
                        newBundle.putString("old_profile", gu.get_data_string(gu.get_data("Login_State"), "detail"));
                        Frag_register frag_register = new Frag_register();
                        frag_register.setArguments(newBundle);
                        open_frag(frag_register);
                    }
                });

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle newBundle = new Bundle();
                        newBundle.putString("choice", "password");
                        Frag_register frag_register = new Frag_register();
                        frag_register.setArguments(newBundle);
                        open_frag(frag_register);
                    }
                });
            }
            get_img(id);
        }
        return view;
    }

    private void open_frag(Fragment Frag) {
        try {
            assert getFragmentManager() != null;
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.ani1, R.anim.ani2, R.animator.popenter, R.animator.popexit)
                    .replace(R.id.main_fragment, Frag)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getProfile_Partner(String id) {
        http_get get = new http_get(getContext());
        get.execute("profile", id);
        get.getListener(new http_get.OnUpdateListener() {
            @Override
            public void onUpdate(JSONObject JObject) {
                try {
                    if (JObject.getString("statuscode").equals("OK")) {
                        Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject data_from_server = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0);
                        JSONObject detail = new JSONObject(data_from_server.getString("detail"));
                        text1.setText(detail.getString("name"));
                        text2.setText(detail.getString("username"));
                        text3.setText(detail.getString("gender"));
                        text4.setText(detail.getString("DOB"));
                        if (data_from_server.getString("testDetail").equals("time")) {
                            text5.setText("Waktu Berkualitas");
                        }
                        if (data_from_server.getString("testDetail").equals("word")) {
                            text5.setText("Kata-Kata Pendukung");
                        }
                        if (data_from_server.getString("testDetail").equals("gift")) {
                            text5.setText("Hadiah");
                        }
                        if (data_from_server.getString("testDetail").equals("touch")) {
                            text5.setText("Sentuhan Fisik");
                        }
                        if (data_from_server.getString("testDetail").equals("service")) {
                            text5.setText("Pelayanan");
                        }
                        if (data_from_server.getString("testDetail").equals("placeholder")) {
                            text5.setText("Akun Demo");
                        }

                        text6.setText(detail.getString("telepon"));
                        text7.setText(data_from_server.getString("email"));

                    } else {
                        Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }
        });
    }

    private void getProfile_Local(String data) {
        try {
            JSONObject JObject = new JSONObject(data);
            JSONObject detail = new JSONObject(JObject.getString("detail"));
            text1.setText(detail.getString("name"));
            text2.setText(detail.getString("username"));
            text3.setText(detail.getString("gender"));
            text4.setText(detail.getString("DOB"));
            if (JObject.getString("testDetail").equals("time")) {
                text5.setText("Waktu Berkualitas");
            }
            if (JObject.getString("testDetail").equals("word")) {
                text5.setText("Kata-Kata Pendukung");
            }
            if (JObject.getString("testDetail").equals("gift")) {
                text5.setText("Hadiah");
            }
            if (JObject.getString("testDetail").equals("touch")) {
                text5.setText("Sentuhan Fisik");
            }
            if (JObject.getString("testDetail").equals("service")) {
                text5.setText("Pelayanan");
            }
            if (JObject.getString("testDetail").equals("placeholder")) {
                text5.setText("Akun Demo");
            }
            text6.setText(detail.getString("telepon"));
            text7.setText(JObject.getString("email"));
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

    }

    private void get_img(String id) {
        try {
            http_get get = new http_get(getContext());
            get.execute("profile_pic", id);
            get.getListener(new http_get.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if (JObject.getString("statuscode").equals("OK")) {
                            JSONObject relation_state = new JSONObject(JObject.toString()).getJSONArray("data").getJSONObject(0);
                            byte[] decodedString = Base64.decode(relation_state.getString("encodedimage"), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                            imageView.invalidate();
                        } else {
                            Toast.makeText(getContext(), "Tidak Ada Foto Profil", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                bitmap_prove = bp;
                imageView.setImageBitmap(bp);
                spinner.setVisibility(View.VISIBLE);
                send_img();
            } else if (resultCode == RESULT_CANCELED) {
                //Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                mImageUri = data.getData();
                Picasso.get().load(mImageUri).into(imageView);
                imageView.setImageURI(mImageUri);
                spinner.setVisibility(View.VISIBLE);
                bitmap_prove = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mImageUri);
                send_img();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void send_img() {
        try {
            utility gu = new utility(getContext());
            http_post post = new http_post(getContext());
            JSONObject JObject = new JSONObject();
            JObject.put("api", "profile_pic");
            JObject.put("encodedimage", encodeImage(bitmap_prove));
            JObject.put("id", gu.get_data_string(gu.get_data("Login_State"), "id"));
            post.execute(JObject);
            post.getListener(new http_post.OnUpdateListener() {
                @Override
                public void onUpdate(JSONObject JObject) {
                    try {
                        if (JObject.getString("statuscode").equals("OK")) {
                            Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), JObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                        spinner.setVisibility(View.VISIBLE);
                        update();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {
        Fragment current = getFragmentManager().findFragmentById(R.id.main_fragment);
        if (current instanceof Frag_profile) {
            getFragmentManager().beginTransaction().detach(current).attach(current).commit();
        }
    }

    public String StringToDate(String x) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
            SimpleDateFormat formatter2 = new SimpleDateFormat("MMM d, yyyy");
            Date date = formatter.parse(x);

            Calendar wad = Calendar.getInstance();
            wad.setTime(date);
            date = wad.getTime();
            // System.out.println(formatter.format(date));
            // return formatter.format(date);
            return formatter2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void openfile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRefresh() {
        update();
    }
}
