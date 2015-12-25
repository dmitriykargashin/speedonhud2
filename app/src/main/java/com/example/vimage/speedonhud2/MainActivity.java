package com.example.vimage.speedonhud2;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;


public class MainActivity extends Activity implements LocationListener {

    private LocationManager myManager;
    private GLSurfaceView view; // тут будем рисовать
    private Speedometer speedometer; // это наш спидометр
    private float path = 0; // пройденный путь
    private Location prevLocation; // пре
    Boolean hudMode = false; // флаг режиа на стекло
    Boolean analogMode = true; // флаг режиа аналаговый
    Boolean preventScreenOff = false; // флаг запрета выключения экрана


    @Override
    protected void onSaveInstanceState(Bundle outState)
    // тут сохраним параметры после переворота экрана
    {
        outState.putFloat("path", path);
        outState.putBoolean("hudMode", hudMode);
        outState.putBoolean("analogMode", analogMode);
        outState.putBoolean("preventScreenOff", preventScreenOff);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // обработаем результат от активити настроек
        if (resultCode == RESULT_OK) {
            hudMode = data.getBooleanExtra("HudMode", false);
            analogMode = data.getBooleanExtra("AnalogMode", false);
            preventScreenOff = data.getBooleanExtra("PreventScreenOff", false);
            speedometer.SetSettings(hudMode, analogMode);
            ApplyScreenOffSettings();
        }
        // если вернулось не ОК
        else

        {
            Toast.makeText(this, "Wrong result", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // получим сохранённые данные, если есть
        if (savedInstanceState != null) {
            path = savedInstanceState.getFloat("path", 0);
            hudMode = savedInstanceState.getBoolean("hudMode", false);
            analogMode = savedInstanceState.getBoolean("analogMode", true);
            preventScreenOff = savedInstanceState.getBoolean("preventScreenOff", false);
        }
// включим полноэкранный режим
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        speedometer = new Speedometer();
        speedometer.SetSettings(hudMode, analogMode);

        Intent intent = new Intent(this, ActivitySettings.class);
        view = new MainSurfaceView(this, speedometer, intent);

        view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);// обновляем по запросу
        setContentView(view);

        // настроим провайдер gps
        myManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        myManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 5, this); // дистанция каждые пять метров, опрос раз в треть секунды

        // применить настройки блокироки экрана
        ApplyScreenOffSettings();


    }

    private void ApplyScreenOffSettings() {
        // настройка затухания экрана
        if (preventScreenOff)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void onLocationChanged(Location location) {


        if (prevLocation == null) prevLocation = location;

        path += location.distanceTo(prevLocation); // считаем расстояние между предыдущей точкой и текущей
        // (конечно могут быть погрешности...) по уму надо сглаживать траекторию.

        prevLocation = location;

        // передадим в спидометр настройки отображения
        speedometer.SetSettings(hudMode, analogMode);
        // передадим в спидометр скорость (т.к. получается в метрах/сек, то переведём в км/час)
        speedometer.SetSpeed(location.getSpeed() * 3.6f);
        // передадим в спидометр пройденное расстояние
        speedometer.SetPath(path);

        // перерисуем сцену
        view.requestRender();

    }
   // отключать определение положения при сворачивании приложения не буду, т.к. считается расстояние.
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}

class MainSurfaceView extends GLSurfaceView {

    private Intent intent;
    private MainActivity mainActivity;
    private Context context;

    public MainSurfaceView(Context fContext, Speedometer speedometer, Intent fIntent) {
        super(fContext);
        intent = fIntent;
        context = fContext;
        mainActivity = (MainActivity) fContext;

        setRenderer(new OpenGLRenderer(speedometer, context));
    }


    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // обработка нажатия на glSurface_!!
        if (MotionEvent.ACTION_DOWN == e.getAction()) {
            // передадим параметры в активити настроек
            intent.putExtra("HudMode", mainActivity.hudMode);
            intent.putExtra("AnalogMode", mainActivity.analogMode);
            intent.putExtra("PreventScreenOff", mainActivity.preventScreenOff);

            // фиктивно вводим
            Integer rslt;
            rslt = 1;

            mainActivity.startActivityForResult(intent, rslt);

            return true;
        } else
            return false;


    }


}


