package com.example.myanmarlensandroid.Scan.Views.Camera;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BundleKt;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myanmarlensandroid.R;
import com.example.myanmarlensandroid.Scan.Activities.OcrDetectorProcessor;
import com.example.myanmarlensandroid.Scan.Activities.OcrGraphic;
import com.google.android.gms.vision.text.TextBlock;

import org.json.JSONObject;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class mymemory_req extends AppCompatActivity {

    SparseArray<TextBlock> theitems;
    TextBlock theitem;
    GraphicOverlay<OcrGraphic> graphicOverlay;

    OcrGraphic graphic = new OcrGraphic(graphicOverlay, theitem);

  //OcrDetectorProcessor ocrDetectorProcessor1 = new OcrDetectorProcessor(theitems, theitem);

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // String[] array= {"myanmar", "hello", "hi"};

       for(int i=0; i< theitems.size(); i++) {

           String URL = "https://api.mymemory.translated.net/get?q="+ theitem.getValue() + "&langpair=en|my";
           Log.d ("sending req to the URL","success" );

           RequestQueue requestQueue = Volley.newRequestQueue(this);
           Log.d ("sending req to the URL","sending" );

        JsonObjectRequest objreq = new JsonObjectRequest(

                Request.Method.GET,
                URL,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("Rest Response:", response.toString());
                if (theitem != null && theitem.getValue() != null) {
                      Log.d("OcrDetectorProcessor", "Text detected! " + theitem.getValue());

                    graphicOverlay.add(graphic);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Rest Response:", error.toString());
                    }
                }
        );

       requestQueue.add(objreq);
    }


    }
}
