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
package com.example.myanmarlensandroid.Managers;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.SparseArray;

import com.example.myanmarlensandroid.Scan.Camera.GraphicOverlay;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;

//import org.w3c.dom.Text;

import java.util.List;

/**
 * A very simple Processor which receives detected TextBlocks and adds them to the overlay
 * as OcrGraphics.
 */
public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    private GraphicOverlay<OcrGraphic> mGraphicOverlay;

    OcrDetectorProcessor(GraphicOverlay<OcrGraphic> ocrGraphicOverlay) {
        mGraphicOverlay = ocrGraphicOverlay;
    }

    /**
     * Called by the detector to deliver detection results.
     * If your application called for it, this could be a place to check for
     * equivalent detections by tracking TextBlocks that are similar in location and content from
     * previous frames, or reduce noise by eliminating TextBlocks that have not persisted through
     * multiple detections.
     */
    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        mGraphicOverlay.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            OcrGraphic graphic = new OcrGraphic(mGraphicOverlay, item);
            mGraphicOverlay.add(graphic);
        }
    }

    /**
     * Frees the resources associated with this detection processor.
     */
    @Override
    public void release() {
        mGraphicOverlay.clear();
    }

    /**
     * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
     * overlay view.
     */
    public static class OcrGraphic extends GraphicOverlay.Graphic {

        private int mId;

        private static final int TEXT_COLOR = Color.WHITE;

        private static Paint sRectPaint;
        private static Paint sTextPaint;
        private final TextBlock mText;

        OcrGraphic(GraphicOverlay overlay, TextBlock text) {
            super(overlay);

            mText = text;

            if (sRectPaint == null) {
                sRectPaint = new Paint();
                sRectPaint.setColor(TEXT_COLOR);
                sRectPaint.setStyle(Paint.Style.STROKE);
                sRectPaint.setStrokeWidth(4.0f);
            }

            if (sTextPaint == null) {
                sTextPaint = new Paint();
                sTextPaint.setColor(TEXT_COLOR);
                sTextPaint.setTextSize(54.0f);
            }
            // Redraw the overlay, as this graphic has been added.
            postInvalidate();
        }

        public int getId() {
            return mId;
        }

        public void setId(int id) {
            this.mId = id;
        }

        public TextBlock getTextBlock() {
            return mText;
        }

        /**
         * Checks whether a point is within the bounding box of this graphic.
         * The provided point should be relative to this graphic's containing overlay.
         * @param x An x parameter in the relative context of the canvas.
         * @param y A y parameter in the relative context of the canvas.
         * @return True if the provided point is contained within this graphic's bounding box.
         */
        public boolean contains(float x, float y) {
            TextBlock text = mText;
            if (text == null) {
                return false;
            }
            RectF rect = new RectF(text.getBoundingBox());
            rect.left = translateX(rect.left);
            rect.top = translateY(rect.top);
            rect.right = translateX(rect.right);
            rect.bottom = translateY(rect.bottom);
            return (rect.left < x && rect.right > x && rect.top < y && rect.bottom > y);
        }

        /**
         * Draws the text block annotations for position, size, and raw value on the supplied canvas.
         */
        @Override
        public void draw(Canvas canvas) {
            TextBlock text = mText;
            if (text == null) {
                return;
            }

            // Draws the bounding box around the TextBlock.
            RectF rect = new RectF(text.getBoundingBox());
            rect.left = translateX(rect.left);
            rect.top = translateY(rect.top);
            rect.right = translateX(rect.right);
            rect.bottom = translateY(rect.bottom);
            canvas.drawRect(rect, sRectPaint);

            // Break the text into multiple lines and draw each one according to its own bounding box.

            List<? extends Text> textComponents = text.getComponents();

            for(Text currentText : textComponents) {
                float left = translateX(currentText.getBoundingBox().left);
                float bottom = translateY(currentText.getBoundingBox().bottom);
                canvas.drawText(currentText.getValue(), left, bottom, sTextPaint);
            }
        }
    }
}
