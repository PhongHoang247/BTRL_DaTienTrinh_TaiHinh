package com.phong.btrl_datientrinh_taihinh;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView imgHinh;
    CheckBox chkTuDong;
    Button btnTruoc, btnSau;
    int soGiay = 0;
    int position = 0;

    TimerTask timerTask;
    Timer timer;

    ArrayList<String> dsLink = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
        taiHinh();
    }

    //Tải hình ở vị trí nào đó
    private void taiHinh(){
        ImageTask task = new ImageTask();
        String link = dsLink.get(position);
        task.execute(link);
    }

    private void addEvents() {
        chkTuDong.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    btnTruoc.setEnabled(false);
                    btnSau.setEnabled(false);
                    moCuaSoCauHinh();
                }
                else {
                    btnTruoc.setEnabled(true);
                    btnSau.setEnabled(true);
                    if (timer != null){
                        timer.cancel();
                        timer = null;
                    }
                }
            }
        });
        btnTruoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position--;
                if (position < 0){
                    position = dsLink.size() - 1;
                }
                taiHinh();
            }
        });
        btnSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                if (position >= dsLink.size()){
                    position = 0;
                }
                taiHinh();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void moCuaSoCauHinh() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.cauhinh);

        final EditText edtGiay = dialog.findViewById(R.id.edtGiay);
        Button btnLuu = dialog.findViewById(R.id.btnLuu);
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Xử lý lưu giây
                soGiay = Integer.parseInt(edtGiay.getText().toString());
                dialog.dismiss();
                xuLyHenGioTai();
            }
        });
        dialog.show();
    }

    private void xuLyHenGioTai() {
        if (timer != null){
            timer.cancel();
            timer = null;
            timerTask = null;
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        position++;
                        if (position >= dsLink.size()){
                            position = 0;
                        }
                        taiHinh();
                    }
                });
            }
        };
        timer = new Timer();
        timer.schedule(timerTask,0,soGiay * 1000);
    }

    private void addControls() {
        imgHinh = findViewById(R.id.imgHinh);
        chkTuDong = findViewById(R.id.chkTuDong);
        btnTruoc = findViewById(R.id.btnTruoc);
        btnSau = findViewById(R.id.btnSau);
        dsLink.add("https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Hailee_Steinfeld_by_Gage_Skidmore.jpg/1200px-Hailee_Steinfeld_by_Gage_Skidmore.jpg");
        dsLink.add("https://t.a4vn.com/2019/05/7/hailee-steinfeld-tham-gia-bo-phim-hai-lang-man-voicemails-for-is-3af.jpg");
        dsLink.add("https://www.cheatsheet.com/wp-content/uploads/2019/11/Hailee-Steinfeld.jpg");
        dsLink.add("https://pbs.twimg.com/media/D2D-eHQUYAAK_Yv.jpg");
        dsLink.add("https://i.pinimg.com/originals/d9/5f/76/d95f7641707948bc50dd120720e7e563.jpg");
        dsLink.add("https://i.pinimg.com/originals/1b/a5/6a/1ba56a1dee8ef6667d4f62d1484c3a8e.jpg");
        dsLink.add("https://cdn.inquisitr.com/wp-content/uploads/2019/04/Amanda-Cerny-.jpg");
        dsLink.add("https://media.gq.com/photos/558473fd3655c24c6c987e9f/master/pass/women-2011-10-elizabeth-olsen-elizabeth-olsen-300x430.jpg");
        dsLink.add("https://image2.tienphong.vn/w665/Uploaded/2020/xpiutqvp/2016_06_30/36_gazp.jpg");
        dsLink.add("https://znews-photo.zadn.vn/w1024/Uploaded/neg_yslewlx/2019_08_26/190715scarlettjohanssoncs143p_d65c2614276482c17315a8c4e4544dc3.jpg");
    }
    class ImageTask extends AsyncTask<String,Void, Bitmap>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgHinh.setImageBitmap(bitmap);
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.xoay);
            imgHinh.startAnimation(animation);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());
                return  bitmap;
            } catch (Exception ex){
                Log.e("Lỗi", ex.toString());
            }
            return null;
        }
    }
}
