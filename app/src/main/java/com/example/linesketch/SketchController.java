package com.example.linesketch;

import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

public class SketchController implements View.OnTouchListener {

    SketchModel model;
    InteractionModel iModel;
    Line Line;
    boolean startPoint;
    private int normX, normY;

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



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        startPoint = false;
        normX = (int)motionEvent.getX();
        normY = (int)motionEvent.getY();

        switch (currentState) {
            case READY:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //if we've touched down on an existing line - select it and switch to SELECTING STATE
                        if(model.lineSelection(model.lines,new PointF(motionEvent.getX(),motionEvent.getY()))){
                            currentState = State.SELECTING;
                        }
                        else{
                            iModel.addStart(new PointF(motionEvent.getX(),motionEvent.getY()));
                            currentState = State.DRAWING;
                        }
                        break;
                }
                break;

            case DRAWING:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        //add dots to view
                        model.addDot(motionEvent.getX(),motionEvent.getY());
                        break;
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //store coordinates of last point
                        iModel.addLast(new PointF(motionEvent.getX(),motionEvent.getY()));
                        //if points create valid line draw the line
                        if((new Line(iModel.start,iModel.end).isValidLine(model.points))){
                            model.addLine(iModel.start,iModel.end);
                        }
                        //else just clear points
                        model.points.clear();
                        currentState = State.READY;
                        break;
                }
                break;

            case SELECTING:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                            //if the touchdown is on the selected line start handle - switch to the ENDPOINTMOVE state
                            if (model.selectedLine.Start.x <= motionEvent.getX() +20 && model.selectedLine.Start.x >= motionEvent.getX() -20 &&
                                    model.selectedLine.Start.y <= motionEvent.getY() +20 && model.selectedLine.Start.y >= motionEvent.getY()-20) {
                                System.out.println("Touched start endpoint");
                                startPoint = true;
                                currentState = State.STARTPOINTMOVE;
                            }

                            //if the touchdown is on the selected line end handle - switch to the ENDPOINTMOVE state
                            if (model.selectedLine.End.x <= motionEvent.getX() + 20 && model.selectedLine.End.x >= motionEvent.getX() - 20 &&
                                    model.selectedLine.End.y <= motionEvent.getY() + 20 && model.selectedLine.End.y >= motionEvent.getY() - 20) {
                                System.out.println("Touched end endpoint");
                                currentState = State.ENDPOINTMOVE;
                                break;
                            }

                            //if the touchdown is on the selected line - switch to the DRAGGING state
                            if (Math.abs(model.selectedLine.distanceFromLine(motionEvent.getX(), motionEvent.getY())) < 0.05) {
                                System.out.println("Touched selected line");
//                                currentState = State.DRAGGING;
                            }

                            //we can assume the touch is on the background or an un-selected line - clear selection and switch to the READY state
                            else {
                                System.out.println("Touched bg or non selected line");
                                        model.clearSelection();
                                        currentState = State.READY;
                                        break;
                            }
                            break;
                        }
                        break;

            case DRAGGING:

            case STARTPOINTMOVE:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        model.startMove(normX,normY);
                        break;
                }
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        currentState = State.SELECTING;
                        break;
                }
                break;

            case ENDPOINTMOVE:
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        model.endMove(normX,normY);
                        break;
                }
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
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
