package com.invenktion.gravity.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class SpaceObject extends UniverseObject{
	Rect destRect;
	Path path;
	int color = Color.WHITE;
	
	public SpaceObject(Context context, int image) {
		paint = new Paint();//Paint.ANTI_ALIAS_FLAG
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		//paint.setStrokeWidth(2);
		//paint.setMaskFilter(new BlurMaskFilter(5, Blur.OUTER));
		this.drawable = image;
		bitmap = BitmapFactory.decodeResource(context.getResources(), drawable);
		rotation = (float)(Math.random()*180);
		path = new Path();
	}

	public void setColor(int color) {
		this.color = color;
		paint.setColor(color);
	}
	
	@Override
	public void draw(Canvas canvas) {
		// rotate the canvas on center of the text to draw
		canvas.save();
		canvas.rotate(rotation, x, y);

		canvas.drawCircle(x, y, radius, paint);
		canvas.drawLine(x-radius, y, x, y-(radius*4), paint);
		canvas.drawLine(x+radius, y, x, y-(radius*4), paint);
		//canvas.drawCircle(x, y-(radius), radius, paint);
	
		//path.reset();
		//path.moveTo(x, y);
		//path.quadTo(x-30, y-40, x-50, y-30);
		//canvas.drawPath(path, paint);
		
		//canvas.drawCircle(x, y-(radius*2), radius, paint);
		//destRect = new Rect((int)(x-radius),(int)(y-radius),(int)(x+radius),(int)(y+radius));
		//canvas.drawBitmap(bitmap, null,destRect, null);
		
		canvas.restore();
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}
	
}
