package com.example.onik.view

import android.graphics.*
import com.squareup.picasso.Transformation


class GradientTransformation(private var startColor: Int) : Transformation {
    private var endColor: Int = Color.TRANSPARENT

    override fun transform(source: Bitmap): Bitmap? {
        val x = source.width
        val y = source.height
        val grandientBitmap = source.copy(source.config, true)
        val canvas = Canvas(grandientBitmap)
        //left-top == (0,0) , right-bottom(x,y);
        val grad = LinearGradient(
            (x / 2).toFloat(),
            (y / 1.1).toFloat(),
            (x / 2).toFloat(),
            (y / 5).toFloat(),
            startColor,
            endColor,
            Shader.TileMode.CLAMP)
        val p = Paint(Paint.DITHER_FLAG)
        p.setShader(null)
        p.setDither(true)
        p.setFilterBitmap(true)
        p.setShader(grad)
        canvas.drawPaint(p)
        source.recycle()
        return grandientBitmap
    }


    override fun key(): String? {
        return "Gradient"
    }

}