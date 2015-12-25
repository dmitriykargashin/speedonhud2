package com.example.vimage.speedonhud2;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;


public class Speedometer {



    private float verticesScale[] = {
// шакала спидометра
            0.27026019f, 0.73492773262f, 0f,
            0.41561957f, 0.73492773262f, 0f,
            0.41568207f, 0.73999023262f, 0f,
            0.41230707f, 0.76261523262f, 0f,
            0.40543207f, 0.78445898262f, 0f,
            0.39524457f, 0.80492773262f, 0f,
            0.38196332f, 0.82355273262f, 0f,
            0.36593207f, 0.83989648262f, 0f,
            0.34755707f, 0.85349023262f, 0f,
            0.32724457f, 0.86408398262f, 0f,
            0.30555707f, 0.87136523262f, 0f,
            0.28299457f, 0.87514648262f, 0f,
            0.26011957f, 0.87533398262f, 0f,
            0.23749457f, 0.87199023262f, 0f,
            0.21565082f, 0.86511523262f, 0f,
            0.19518207f, 0.85489648262f, 0f,
            0.17655707f, 0.84161523262f, 0f,
            0.16021332f, 0.82561523262f, 0f,
            0.14658832f, 0.80720898262f, 0f,
            0.13602582f, 0.78692773262f, 0f,
            0.12874457f, 0.76524023262f, 0f,
            0.12496332f, 0.74267773262f, 0f,
            0.12490082f, 0.73492773262f, 0f,
            // стрелка
            -0.05f, 0f, 0f,
            -0.01f, 0.7f, 0f,
            0.05f, 0f, 0f,
            0.01f, 0.7f, 0f

    };

// порядок рисования вершин
    private short[] indices =
            {0, 1, 2, 0, 3, 4, 0, 5, 6, 0, 7, 8, 0, 9, 10, 0, 11, 12, 0, 13, 14, 0, 15, 16, 0,
                    17, 18, 0, 19, 20, 0, 21, 22,
                    23, 24, 25, 26};// это стрелка


    private FloatBuffer vertexBuffer; // массив вершин
    private ShortBuffer indexBuffer; // массив индексов
    private Float speed=0f; // скорость
    private Float path=0f; // пройденное расстояние
    Boolean hudMode; // режим на стекло
    Boolean analogMode; // аналоговый режим
    private GLText glText; // объект для выводя текста



    public Speedometer() {

      // заполним массив вершин
        ByteBuffer vbb = ByteBuffer.allocateDirect(verticesScale.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(verticesScale);

        vertexBuffer.position(0);

        // заполним массив индексов
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
   }


    public void draw(GL10 gl, GLText fglText, Integer width, Integer height, float ratio) {

       glText = fglText;

        if (analogMode) { // аналоговый режим
            indexBuffer.position(0);// установим с нуля
            // рисуем спидометр
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
            gl.glPushMatrix();
            gl.glScalef(5f, 5f, 0f);

            if (hudMode) {
                gl.glRotatef(180f, 1f, 0f, 0f);// перевернём
            }

            gl.glTranslatef(-0.27026019f, -0.73492773262f, 0.2f); // установим в центр координат
            gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

            gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length - 4, // нарисуем шкалу
                    GL10.GL_UNSIGNED_SHORT, indexBuffer);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glScalef(0.5f, 0.5f, 0f);

            if (hudMode) {
                gl.glRotatef(180f, 1f, 0f, 0f);// перевернём
            }

            gl.glTranslatef(-0.27026019f, -0.73492773262f, 0.0f); // установим в центр координат
            gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
            gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length - 4, //нарисуем мелкий центр
                    GL10.GL_UNSIGNED_SHORT, indexBuffer);

            gl.glPopMatrix();
            drawArrow(gl, speed); // тут рисуем стрелку с нужным углом

            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
// закончили
        }


// изобразим текст
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glOrthof(                                    // Set Ortho Projection (Left,Right,Bottom,Top,Front,Back)
                0, width,
                0, height,
                1.0f, -1.0f
        );

        // enable texture + alpha blending
        // NOTE: this is required for text rendering! we could incorporate it into
        // the GLText class, but then it would be called multiple times (which impacts performance).

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


        glText.begin(1.0f, 1.0f, 1.0f, 1.0f);            // начали вывод текста с белым цветом

        if (analogMode) {// аналоговый
            if (hudMode) {// режим на панели
                glText.draw("100", (width - glText.getLength("100")) / 2, 0-25 * ratio);
                glText.draw("0", 20 * ratio, height / 2);
                glText.draw("200", (width - glText.getLength("200")), height / 2);
                glText.draw(path.toString() + " Km", (width - glText.getLength(path.toString() + " Km")) / 2, height - glText.getCharHeight() + 30 * ratio);    // пройденный путь
                glText.end();
            } else {
                glText.draw("100", (width - glText.getLength("100")) / 2, height - glText.getCharHeight()+45*ratio);
                glText.draw("0", 20 * ratio, height / 2 - glText.getCharHeight() + 30 * ratio);
                glText.draw("200", (width - glText.getLength("200")), height / 2 - glText.getCharHeight() + 30 * ratio);
                glText.draw(path.toString() + " Km", (width - glText.getLength(path.toString() + " Km")) / 2, 10 * ratio);    // пройденный путь
                glText.end();
            }
        } // цифровой режим
        else {
            if (hudMode) {// режим на панели
                // рисуем цифровой спидометр
                gl.glPushMatrix();
                glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
                gl.glScalef(4f, 4f, 1);
                glText.drawC(String.valueOf(speed.intValue()), (width) / 8, height / 8);
                glText.end();
                gl.glPopMatrix();
                glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
                glText.draw( path + " Km", (width - glText.getLength(path.toString() + " Km")) / 2, height - glText.getCharHeight() + 30 * ratio);               // пройденный путь
                glText.end();
            } else {// режим обычный
                // рисуем цифровой спидометр

                gl.glPushMatrix();
                //gl.glTranslatex(2,2,2);
                glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
                gl.glScalef(4f, 4f, 1);
                glText.drawC(String.valueOf(speed.intValue()), (width) / 8, height / 8);
                glText.end();
                gl.glPopMatrix();
                glText.begin(1.0f, 1.0f, 1.0f, 1.0f);
                glText.draw( path + " Km", (width - glText.getLength(path.toString() + " Km")) / 2, 0 - 30 * ratio);                // пройденный путь
                glText.end();
            }
        }


        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);


    }

    public void SetSpeed(Float fSpeed) {
        // передаём скорость
        speed = fSpeed;
    }

    public void SetPath(Float fPath) {
        // пройденный путь, переводим из метров в километры, с одной значащей цифрой после запятой
        fPath = fPath / 1000;
        Integer tmpVar;
        tmpVar = Math.round(fPath * 10);

        path = tmpVar.floatValue() / 10;
    }

    public void drawArrow(GL10 gl, Float fSpeed) {
        // рисуем подвижную стрелку

        Float angle;

        //fSpeed = interpolate(fSpeed);
        // скорость переводим в угол
        angle = fSpeed * 0.9f;
        angle = -angle + 90;

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        indexBuffer.position(33); // начало индексов стрелки

        gl.glPushMatrix();
        if (hudMode)
        gl.glRotatef(-(angle+180), 0f, 0f, 1f);
         else
        gl.glRotatef(angle, 0f, 0f, 1f);

        gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 4,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        gl.glPopMatrix();
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    private Integer interpolate(Integer input) {
        // заготовка для сглаживания показаний датчиков
        return input;

    }

    public void SetSettings(Boolean fhudMode, Boolean fanalogMode) {
        // установка параметров отображения
        hudMode = fhudMode;
        analogMode = fanalogMode;


    }


}
