package com.example.vimage.speedonhud2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Switch;


public class ActivitySettings extends ActionBarActivity {

    Boolean hudMode;
    Boolean analogMode;
    Boolean preventScreenOff;

    Switch switchHuds;
    Switch switchAnalogs;
    Switch switchPreventScreenOff;

    private void ApplySettings() {
   // передаём параметры обратно
        Intent intent = new Intent();
        intent.putExtra("HudMode", switchHuds.isChecked());
        intent.putExtra("AnalogMode", switchAnalogs.isChecked());
        intent.putExtra("PreventScreenOff", switchPreventScreenOff.isChecked());

        setResult(RESULT_OK, intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// получаем парамеры
        Intent intent = getIntent();
        hudMode = intent.getBooleanExtra("HudMode", false);
        analogMode = intent.getBooleanExtra("AnalogMode", false);
        preventScreenOff = intent.getBooleanExtra("PreventScreenOff", false);
        setContentView(R.layout.activity_activity_settings);

        // устанвливаем параметры для переключателй
        switchHuds = (Switch) this.findViewById(R.id.switchHud);
        switchHuds.setChecked(hudMode);

        switchAnalogs = (Switch) this.findViewById(R.id.switchAnalog);
        switchAnalogs.setChecked(analogMode);

        switchPreventScreenOff = (Switch) this.findViewById(R.id.switchScreenOff);
        switchPreventScreenOff.setChecked(preventScreenOff);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
   // по свайпу выходим
        if (MotionEvent.ACTION_MOVE == event.getAction()) {
            ApplySettings();
            finish();

            return true;
        } else
            return false;

    }

    @Override
    protected void onStop() {
        // на закрытие применяем параметры
        ApplySettings();
        super.onStop();
    }


    @Override
    public void onBackPressed() {
         // по кнопке назад сохраняем парамерты
         ApplySettings();
         finish();
    }


}
