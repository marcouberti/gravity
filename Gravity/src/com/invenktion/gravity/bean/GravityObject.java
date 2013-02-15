package com.invenktion.gravity.bean;

import com.invenktion.gravity.utils.Math2DUtils;

import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

public class GravityObject extends UniverseObject{
	public float power;
	
	public GravityObject() {
		paint = new Paint();//Paint.ANTI_ALIAS_FLAG
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.FILL);
		//paint.setStrokeWidth(10);
		paint.setMaskFilter(new BlurMaskFilter(15, Blur.OUTER));
	}
	
	public float computeAttraction(Point p) {
		float distance = Math2DUtils.computeDistance(p, new Point((int)x,(int)y));
		return (float)(power*(1/Math.pow(distance, 2)));
	}
	

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(x, y, power, paint);
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
		
	}
}
