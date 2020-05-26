/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.myanmarlensandroid.Scan.Activities;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myanmarlensandroid.R;
import com.example.myanmarlensandroid.Scan.Views.Camera.GraphicOverlay;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

import org.json.JSONObject;

/**
 * A very simple Processor which gets detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor extends AppCompatActivity implements Detector.Processor<TextBlock> {

    SparseArray<TextBlock> items;
    TextBlock item;
    private GraphicOverlay<OcrGraphic> graphicOverlay;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        graphicOverlay = ocrGraphicOverlay;
    }

    public OcrDetectorProcessor( SparseArray<TextBlock> items, TextBlock item){
        items = this.items;
        item = this.item;

    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        graphicOverlay.clear();
         items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            // this one
            item = items.valueAt(i);
            if (item != null && item.getValue() != null) {

                //here is the part where text is detected.
                Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());

                String URL = "https://api.mymemory.translated.net/get?q=Hello World!&langpair=en|my";
                Log.d ("sending req to the URL","success" );

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                Log.d ("sending req to the URL","sending" );

                JsonObjectRequest objReq = new JsonObjectRequest(

                        Request.Method.GET,
                        URL, null,
                        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Rest Response:", response.toString());
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Rest Response:", error.toString());
                                error.printStackTrace();
                            }
                        });

                requestQueue.add(objReq);
                OcrGraphic graphic = new OcrGraphic(graphicOverlay, item);
                graphicOverlay.add(graphic);
            }

        }
    }

    /*protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // String[] array= {"myanmar", "hello", "hi"};

        for(int i=0; i< items.size(); i++) {

            String URL = "https://api.mymemory.translated.net/get?q="+ item.getValue() + "&langpair=en|my";
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
                    if (item != null && item.getValue() != null) {
                        Log.d("OcrDetectorProcessor", "Text detected! " + item.getValue());
                        OcrGraphic graphic = new OcrGraphic(graphicOverlay, item);
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


    }*/



    @Override
    public void release() {
        graphicOverlay.clear();
    }
}