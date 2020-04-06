package com.example.linesketch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

public class SketchView extends View implements SketchListener {
    SketchController controller;
    SketchModel model;
    InteractionModel iModel;
    Paint myPaint;
    Paint linePaint;
    Paint selectPaint;
    Paint linePaintTranslucent;
    Paint selectPaintTranslucent;
    float radius;

    public SketchView(Context context) {
        super(context);
        myPaint = new Paint();
        linePaint = new Paint();
        selectPaint = new Paint();
        linePaintTranslucent = new Paint();
        selectPaintTranslucent = new Paint();
        setBackgroundColor(Color.DKGRAY);
        radius = 10;
    }

    public void setModel(SketchModel model) {
        this.model =model;
    }

    public void setiModel(InteractionModel iModel) {
        this.iModel = iModel;
    }

    public void setController(SketchController newController) {
        controller = newController;
        this.setOnTouchListener(controller);
    }

    public void onDraw(Canvas c) {
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(Color.GRAY);
        for (PointF p : model.points) {
            c.drawOval(p.x - radius, p.y - radius, p.x + radius, p.y + radius, myPaint);
        }

        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(10);

        linePaintTranslucent.setStyle(Paint.Style.FILL);
        linePaintTranslucent.setColor(Color.GREEN);
        linePaintTranslucent.setAlpha(150);
        linePaintTranslucent.setStrokeWidth(35);

        selectPaint.setStyle(Paint.Style.FILL);
        selectPaint.setColor(Color.YELLOW);
        selectPaint.setStrokeWidth(10);

        selectPaintTranslucent.setStyle(Paint.Style.FILL);
        selectPaintTranslucent.setColor(Color.YELLOW);
        selectPaintTranslucent.setAlpha(150);
        selectPaintTranslucent.setStrokeWidth(35);

        for (Line l : model.lines) {
            if(l == model.selectedLine){
                c.drawLine(l.Start.x,l.Start.y,l.End.x,l.End.y,selectPaint);
                c.drawLine(l.Start.x,l.Start.y,l.End.x,l.End.y,selectPaintTranslucent);
                c.drawCircle(l.Start.x,l.Start.y,21,selectPaint);
                c.drawCircle(l.End.x,l.End.y,21,selectPaint);
            }
            else{
                c.drawLine(l.Start.x,l.Start.y,l.End.x,l.End.y,linePaintTranslucent);
                c.drawLine(l.Start.x,l.Start.y,l.End.x,l.End.y,linePaint);}
        }
    }

    @Override
    public void modelChanged() {
        this.invalidate();
    }
}
