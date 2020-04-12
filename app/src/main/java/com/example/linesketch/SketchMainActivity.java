package com.example.linesketch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class SketchMainActivity extends AppCompatActivity {

    private SketchModel model;
    private InteractionModel iModel;
    private SketchView sketchView;
    private SketchController controller;
    private SeekBar rotateBar;
    private SeekBar scaleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LinearLayout root = findViewById(R.id.root);

        // MVC
        model = new SketchModel();
        iModel = new InteractionModel();
        sketchView = new SketchView(this);
        controller = new SketchController();

        // Connect MVC //
        controller.setModel(model);
        controller.setIModel(iModel);

        //chartView
        sketchView.setModel(model);
        sketchView.setiModel(iModel);
        sketchView.setController(controller);

        //add model subscribers
        model.addSubscribers(sketchView);
        iModel.addSubscribers(sketchView);

        //add view to root
        root.addView(sketchView);

        //adding seekbars
        rotateBar = findViewById(R.id.RotateBar);
        scaleBar = findViewById(R.id.ScaleBar);

        //seek bar listeners
        rotateBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                controller.rotateBar(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        scaleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                controller.scaleBar(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        // return true so that the main_menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.cut:
                System.out.println("Cut");
                return true;

            case R.id.copy:
                System.out.println("Copy");
                return true;

            case R.id.paste:
                System.out.println("Paste");
                return true;

            case R.id.group:
                System.out.println("Group");
                return true;

            case R.id.ungroup:
                System.out.println("Ungroup");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
