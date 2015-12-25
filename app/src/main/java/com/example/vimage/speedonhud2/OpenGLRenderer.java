package com.example.vimage.speedonhud2;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;


public class OpenGLRenderer implements Renderer {

    private Speedometer speedometer;
    public GLText glText;         // A GLText Instance
    public GLText glTextMirror; // для зеркального отображения
    private Context context;    // Context (from Activity)

    private int width = 200;
    private int height = 200;
    final Integer baseResMin = 768; // базововое разрешение
    public float ratio; //кэффициент разрешения

    public OpenGLRenderer(Speedometer fspeedometer, Context acontext) {
        super();
        speedometer = fspeedometer;
        context = acontext;
    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // цвет офна черный
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // создадим объекты для вывода текста
        glText = new GLText(gl, context.getAssets());
        glTextMirror = new GLText(gl, context.getAssets());

    }


    public void onDrawFrame(GL10 gl) {
        // Clears the screen and depth buffer.
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
      // если режим на стекло, то используем зеркальный шрифт
        if (speedometer.hudMode)
            speedometer.draw(gl, glTextMirror, width, height, ratio);
        else
            speedometer.draw(gl, glText, width, height, ratio);


    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {

      // тут считаем размер области вывода в зависимости от разрешения экрана
        Integer w, h, left, top;
        h = height;
        w = width;
        left = 0;
        top = 0;

        if (w < h) {
            h = w;
            top = (height - width) / 2;

        } else {
            w = h;
            left = (width - height) / 2;

        }

        this.width = w;
        this.height = h;

        ratio = (float) w / baseResMin; // поправочный коэффициент

        // загрузим шрифты с правильными пропорциями
        glText.load("Roboto-Regular.ttf", (int) (130 * ratio), 2, 2, false);  // Create Font
        glTextMirror.load("Roboto-Regular.ttf", (int) (130 * ratio), 2, 2, true);  // Create Font

// сбросим матрицу проекции
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        // сбросим модельно-видовую матрицу
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        // укажем область вывода (квадратное изображение)
        gl.glViewport(left, top, w, h); //

    }
}
