package com.example.linesketch;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class SketchController implements View.OnTouchListener {

    SketchModel model;
    InteractionModel iModel;
    Line Line;
    boolean startPoint;
    private float normX, normY;
    private float normDX, normDY;
    private float prevNormX, prevNormY;

    public SketchController(){

    }

    private enum State {READY, DRAWING, SELECTING, DRAGGING, STARTPOINTMOVE, ENDPOINTMOVE}

    private State currentState = State.READY;

    public void setModel(SketchModel sModel) {
        model = sModel;
    }

    public void setIModel(InteractionModel im) {
        iModel = im;
    }

    public void setLines() {
        model.lines = iModel.lines;
    }

    public void setPoints() {
        model.points = iModel.points;
    }

    public void setSelected() {model.selectedLine = iModel.selectedLine;}

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        startPoint = false;
        normX = (int)motionEvent.getX();
        normY = (int)motionEvent.getY();
        normDX = normX - prevNormX;
        normDY = normY - prevNormY;
        prevNormX = normX;
        prevNormY = normY;

        switch (currentState) {
            case READY:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //if we've touched down on an existing line - select it and switch to SELECTING STATE
                        if(iModel.lineSelection(iModel.lines,new PointF(motionEvent.getX(),motionEvent.getY()))){
                            setSelected();
                            currentState = State.SELECTING;
                        }
                        else{
                            iModel.addStart(new PointF(motionEvent.getX(),motionEvent.getY()));
                            setPoints();
                            currentState = State.DRAWING;
                        }
                        break;
                }
                break;

            case DRAWING:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //add dots to view
                        iModel.addDot(motionEvent.getX(),motionEvent.getY());
                        setPoints();
                        break;
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //store coordinates of last point
                        iModel.addLast(new PointF(motionEvent.getX(),motionEvent.getY()));
                        //if points create valid line draw the line
                        if((new Line(iModel.start,iModel.end).isValidLine(iModel.points))){
                            setLines();
                            iModel.addLine(iModel.start,iModel.end);
                        }
                        //else just clear points
                        iModel.points.clear();
                        currentState = State.READY;
                        break;
                }
                break;

            case SELECTING:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                            //if the touchdown is on the selected line start handle - switch to the ENDPOINTMOVE state
                            if (iModel.selectedLine.Start.x <= motionEvent.getX() +20 && iModel.selectedLine.Start.x >= motionEvent.getX() -20 &&
                                    iModel.selectedLine.Start.y <= motionEvent.getY() +20 && iModel.selectedLine.Start.y >= motionEvent.getY()-20) {
                                System.out.println("Touched start endpoint");
                                startPoint = true;
                                currentState = State.STARTPOINTMOVE;
                                break;
                            }

                            //if the touchdown is on the selected line end handle - switch to the ENDPOINTMOVE state
                            if (iModel.selectedLine.End.x <= motionEvent.getX() + 20 && iModel.selectedLine.End.x >= motionEvent.getX() - 20 &&
                                    iModel.selectedLine.End.y <= motionEvent.getY() + 20 && iModel.selectedLine.End.y >= motionEvent.getY() - 20) {
                                System.out.println("Touched end endpoint");
                                currentState = State.ENDPOINTMOVE;
                                break;
                            }

                            //if the touchdown is on the selected line - switch to the DRAGGING state
                            if (Math.abs(iModel.selectedLine.distanceFromLine(motionEvent.getX(), motionEvent.getY())) < 0.05) {
                                System.out.println("Touched selected line");
                                currentState = State.DRAGGING;
                            }

                            //we can assume the touch is on the background or an un-selected line - clear selection and switch to the READY state
                            else {
                                System.out.println("Touched bg or non selected line");
                                        iModel.clearSelection();
                                        setSelected();
                                        currentState = State.READY;
                                        break;
                            }
                            break;
                        }
                        break;

            case DRAGGING:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        iModel.translateLine(normDX,normDY);
                        break;
                }
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        iModel.updateLines();
                        setLines();
                        setSelected();
                        currentState = State.SELECTING;
                        break;
                }
                break;

            case STARTPOINTMOVE:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        iModel.startMove(normX,normY);
                        break;
                }
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        iModel.updateLines();
                        setLines();
                        setSelected();
                        currentState = State.SELECTING;
                        break;
                }
                break;

            case ENDPOINTMOVE:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        iModel.endMove(normX,normY);
                        break;
                }
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        iModel.updateLines();
                        setLines();
                        setSelected();
                        currentState = State.SELECTING;
                        break;
                }
                break;
        }
    return true;
    }


    public void rotateBar(int position){
        System.out.println(position);
    };

    public void scaleBar(int position){
        System.out.println(position);
    };
}
