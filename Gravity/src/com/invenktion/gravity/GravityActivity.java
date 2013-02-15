package com.invenktion.gravity;

import com.invenktion.gravity.bean.GravityObject;
import com.invenktion.gravity.bean.NonAttractedObject;
import com.invenktion.gravity.bean.RepulsorObject;
import com.invenktion.gravity.bean.SpaceObject;
import com.invenktion.gravity.core.ApplicationManager;
import com.invenktion.gravity.core.UniverseManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class GravityActivity extends Activity {
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintBg = new Paint(Paint.FILTER_BITMAP_FLAG);

	private float initX, initY, radius;
	private boolean drawing = false;
	
    public class MySurfaceThread extends Thread {
    	
    	private SurfaceHolder myThreadSurfaceHolder;
    	private MySurfaceView myThreadSurfaceView;
    	private boolean myThreadRun = false;
    	
    	
    	public MySurfaceThread(SurfaceHolder surfaceHolder, MySurfaceView surfaceView) {
    		myThreadSurfaceHolder = surfaceHolder;
    		myThreadSurfaceView = surfaceView;
        }
    	
    	public void setRunning(boolean b) {
    		myThreadRun = b;
   		}

		
		public void run() {
			// TODO Auto-generated method stub
			//super.run();
			while (myThreadRun) {
                Canvas c = null;
                try {
                    c = myThreadSurfaceHolder.lockCanvas(null);
                    synchronized (myThreadSurfaceHolder) {
                    	UniverseManager.updateUniverse();
                        myThreadSurfaceView.onDraw(c);
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                    	myThreadSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
		}

	}

	public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback{
		
		private MySurfaceThread thread;
		private Bitmap buffer;
		private Canvas bufferCanvas;
		private Rect destRect;
		private Bitmap background;
		
		protected void onDraw(Canvas canvas) {
			/*
			paint.setStyle(Style.FILL);
			paint.setColor(Color.BLACK);
			bufferCanvas.drawRect(new Rect(0,0,ApplicationManager.SCREEN_H,ApplicationManager.SCREEN_W), paint);
			paint.setStyle(Style.STROKE);
			paint.setColor(Color.WHITE);
			*/
			try {
				//canvas.drawBitmap(background, null, destRect, paintBg);
				canvas.drawColor(Color.BLACK);
				
				if(drawing){
					//paint.setColor(Color.rgb((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
					canvas.drawCircle(initX, initY, radius, paint);
				}
		
				for(NonAttractedObject nao: UniverseManager.nonAttractedObjects) {
					if(nao.destroyed) continue;
					//bufferCanvas.drawCircle(so.x, so.y, so.radius, paint);
					nao.draw(canvas);
				}
				
				for(GravityObject go: UniverseManager.gravityObjects) {
					if(go.destroyed) continue;
					//canvas.drawCircle(go.x, go.y, go.power, paint);
					go.draw(canvas);
				}
				
				for(SpaceObject so: UniverseManager.spaceObjects) {
					if(so.destroyed) continue;
					//bufferCanvas.drawCircle(so.x, so.y, so.radius, paint);
					so.draw(canvas);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}

		
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			//return super.onTouchEvent(event);
			int action = event.getAction();
			if (action==MotionEvent.ACTION_MOVE){
				float x = event.getX();
				float y = event.getY();
				radius = (float) Math.sqrt(Math.pow(x-initX, 2) + Math.pow(y-initY, 2));
				
				UniverseManager.repulsorX = x;
				UniverseManager.repulsorY = y;
				//UniverseManager.repulsorPower = radius;
				UniverseManager.repulsorActive = true;
				}
			else if (action==MotionEvent.ACTION_DOWN){
				initX = event.getX();
				initY = event.getY();
				radius = 1;
				drawing = false;//true
				UniverseManager.repulsorX = initX;
				UniverseManager.repulsorY = initY;
				UniverseManager.repulsorPower = 50;
				UniverseManager.repulsorActive = true;
				}
			else if (action==MotionEvent.ACTION_UP){
				drawing = false;
				
				UniverseManager.repulsorActive = false;
				/*
				//Aggiungo il GO all'universo
				GravityObject go = new GravityObject();
				go.x = initX;
				go.y = initY;
				go.velx= 0;
				go.vely =0;
				go.power = radius;
				
				UniverseManager.queueGravityObject(go);
				*/
				
			}
			
			return true;
		}

		public MySurfaceView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			buffer = Bitmap.createBitmap(ApplicationManager.SCREEN_H, ApplicationManager.SCREEN_W, Bitmap.Config.ARGB_8888);
			bufferCanvas = new Canvas(buffer);
			destRect = new Rect(0,0,ApplicationManager.SCREEN_H,ApplicationManager.SCREEN_W);
			background = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.nero);

			//paint.setFilterBitmap(true);
			init();
		}

		public MySurfaceView(Context context, AttributeSet attrs) {
			super(context, attrs);
			// TODO Auto-generated constructor stub
			init();
		}

		public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			init();
		}
		
		private void init(){
			getHolder().addCallback(this);
			thread = new MySurfaceThread(getHolder(), this);
			
			setFocusable(true); // make sure we get key events
			
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			paint.setColor(Color.WHITE);
		}

		
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
		}

		
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			thread.setRunning(true);
			thread.start();
		}

		
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			boolean retry = true;
			thread.setRunning(false);
			while (retry) {
				try {
					thread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
	        }
		}
	}

	/** Called when the activity is first created. */
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay(); 
        int width = display.getWidth();
        int height = display.getHeight();
        if(width > height) {
        	ApplicationManager.SCREEN_H = height;
        	ApplicationManager.SCREEN_W = width;
        }else {
        	ApplicationManager.SCREEN_H = width;
        	ApplicationManager.SCREEN_W = height;
        }
        
        Log.d("",""+ApplicationManager.SCREEN_H+"x"+ApplicationManager.SCREEN_W);
   
        UniverseManager.initialize();
        
        for(int i=0; i<500; i++) {
	        SpaceObject s1 = new SpaceObject(getApplicationContext(), R.drawable.bomb);
	        s1.x = (int)(Math.random()*ApplicationManager.SCREEN_H);
	        s1.y = (int)(Math.random()*ApplicationManager.SCREEN_W);
	        s1.velx = 0;
	        s1.vely = 0;
	        s1.radius = 2+(float)(Math.random()*3);
	        UniverseManager.queueSpaceObject(s1);
        }
        
        //Aggiungo un oggetto gravitazionale
        GravityObject go = new GravityObject();
        go.power = 100;
        go.x = 200;
        go.y = 300;
        go.radius = 5;
        go.velx = 0;
        go.vely = 0;
        
        UniverseManager.queueGravityObject(go);
        
        //Aggiungo un ALTRO oggetto gravitazionale
        GravityObject go2 = new GravityObject();
        go2.power = 50;
        go2.x = 300;
        go2.y = 600;
        go2.radius = 7;
        go2.velx = 0;
        go2.vely = 0;
        
        UniverseManager.queueGravityObject(go2);
        
        //Aggiungo l'obiettivo
        /*
        NonAttractedObject s1 = new NonAttractedObject(getApplicationContext(), R.drawable.goal);
        s1.velx = 0;
        s1.vely = 0;
        s1.radius = 50;
        s1.x = ApplicationManager.SCREEN_H-s1.radius;
        s1.y = ApplicationManager.SCREEN_W-s1.radius;
        UniverseManager.queueNonAttractedObject(s1);
        */
        
        //UniverseManager.queueGravityObject(g1);
        
        FrameLayout frameLayout = new FrameLayout(getApplicationContext());
        MySurfaceView mySurfaceView = new MySurfaceView(getApplicationContext());
        //LinearLayout ll = new LinearLayout(getApplicationContext());
        //ll.setBackgroundResource(R.drawable.foreground);
        frameLayout.addView(mySurfaceView);
        //frameLayout.addView(ll);
        setContentView(frameLayout);

    }

}