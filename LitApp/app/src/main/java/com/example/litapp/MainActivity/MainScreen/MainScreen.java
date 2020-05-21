package com.example.litapp.MainActivity.MainScreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.litapp.MainActivity.Utilities.NetworkUtility;
import com.example.litapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainScreen extends Fragment {
    ImageView im;
    TextView v;
    Context context;

    public MainScreen(Context ct) {
        context = ct;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        im = getView().findViewById(R.id.mp_picture);
        v = getView().findViewById(R.id.cont);
        new GetMainPcontent().execute();
        GetImg getImg = new GetImg(context);
        getImg.execute();
    }

    private class GetMainPcontent extends AsyncTask<String, Void, String> {
        private ProgressDialog pd;
        ConstraintLayout l = getActivity().findViewById(R.id.main_page_layout);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = ProgressDialog.show(context, "", "Loading", true,
                    false);
            l.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            return NetworkUtility.downloadDataFromUrl();
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            v.setText(Html.fromHtml(result, null, null));
            v.setMovementMethod(LinkMovementMethod.getInstance());
            l.setVisibility(View.VISIBLE);
        }
    }

    public class GetImg extends AsyncTask<String, Void, Bitmap> {
        Context mcontext;

        public GetImg(Context context) {
            mcontext = context;
        }

        @Override
        protected Bitmap doInBackground(String... path) {
            try {
                URL url = new URL(NetworkUtility.downloadPictureidFromUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            im.setImageBitmap(result);
        }
    }
}
