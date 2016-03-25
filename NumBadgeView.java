package com.xylsh.customview;


import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.xylsh.R;


public class NumBadgeView extends TextView {

	private final int def_bvBackgroundColor = 0xffFF3126;//Color.RED;//#FF3126
	private final int def_text_color = Color.WHITE;
	private final int def_text_size = 11;
	private final int def_offset_radius = 2;
	private final float def_bvCirclePointWidth = 8f;
	private static final int DEFAULT_CORNER_RADIUS_DIP = 8;
	
	
	private int text_color = def_text_color;
	private int text_size = def_text_size;
	private int offset_radius = def_offset_radius;
	
	
	private float bvMinWidth;
	private float bvMinHeight;
	private int bvBackgroundColor = def_bvBackgroundColor;
	private boolean bvIsAlwaysCircleBg = false;
	/**
	 * if true the bvMinWidth, bvMinHeight disValid && not drawText
	 */
	private boolean bvIsCirclePointMode = false;
	private float bvCirclePointWidth = def_bvCirclePointWidth;
	private float bvCirclePointHeight= def_bvCirclePointWidth;
	
	
	/**
	 * if count > 0 && count < 10 draw circle, if count > 9 , drwa rectangle
	 */
	private int count;
	
	
	private Paint mPaint = new Paint();
	private Rect textRect = new Rect();
	
	
	
	public NumBadgeView(Context context) {
		
		this(context,null);
	}
	
	public NumBadgeView(Context context, AttributeSet attrs) {
		
		this(context, attrs, android.R.attr.textViewStyle);
	}

	public NumBadgeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		text_size = sp2px(getContext(), text_size);
		offset_radius = dip2px(getContext(), offset_radius);
		//backgroundColor = getContext().getResources().getColor(R.color.color_palmplay_FF3126);
		bvCirclePointWidth = dip2px(getContext(), bvCirclePointWidth);
		bvCirclePointHeight = dip2px(getContext(), bvCirclePointHeight);
		
		TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.NumBadgeView, defStyle, 0);
		
		bvMinWidth = a.getDimension(R.styleable.NumBadgeView_bvMinWidth, bvMinWidth);
		bvMinHeight = a.getDimension(R.styleable.NumBadgeView_bvMinHeight, bvMinHeight);
		bvBackgroundColor = a.getColor(R.styleable.NumBadgeView_bvBackgroundColor, bvBackgroundColor);
		bvIsAlwaysCircleBg = a.getBoolean(R.styleable.NumBadgeView_bvIsAlwaysCircleBg, bvIsAlwaysCircleBg);
		bvIsCirclePointMode = a.getBoolean(R.styleable.NumBadgeView_bvIsCirclePointMode, bvIsCirclePointMode);
		bvCirclePointWidth = a.getDimension(R.styleable.NumBadgeView_bvCirclePointWidth, bvCirclePointWidth);
		bvCirclePointHeight = a.getDimension(R.styleable.NumBadgeView_bvCirclePointHeight, bvCirclePointHeight);
		
		a.recycle();
	}
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
	    int width;  
	    int height ;  
	    
	    if (widthMode == MeasureSpec.EXACTLY){
	    	
	    	width = widthSize; 
	    	
	    } else {
	    	
	    	mPaint.reset();
	    	mPaint.setTextSize(text_size);  
	        mPaint.getTextBounds(String.valueOf(count), 0, String.valueOf(count).length(), textRect);  
	        float textWidth = mPaint.measureText(String.valueOf(count));
	        int desiredWidth = (int) (getPaddingLeft() + textWidth + def_offset_radius * 2 + getPaddingRight()); 
	        width = desiredWidth; 
	    }
	    
	    if (heightMode == MeasureSpec.EXACTLY) { 
	    	
	        height = heightSize;  
	        
	    } else {  
	    	
	    	mPaint.reset();
	        mPaint.setTextSize(text_size);  
	        mPaint.getTextBounds(String.valueOf(count), 0, String.valueOf(count).length(), textRect);  
	        float textHeight = textRect.height();  
	        int desiredHeight = (int) (getPaddingTop() + textHeight + def_offset_radius * 2  + getPaddingBottom());  
	        height = desiredHeight;  
	    }  
	    
	    
	    width = Math.max((int)bvMinWidth, width);
	    height = Math.max((int)bvMinHeight, height);
	    
	    if(widthMode != MeasureSpec.EXACTLY && width==height && count > 9){
	    	width = (int) (width + mPaint.measureText(String.valueOf(count))/3);
	    }
	    
	    
	    if(bvIsCirclePointMode){
	    	
	    	if(widthMode == MeasureSpec.EXACTLY){
	    		width = Math.min((int)bvCirclePointWidth, width);
	    		
	    	} else {
	    		width = (int) bvCirclePointWidth;
	    	}
	    	
	    	if (heightMode == MeasureSpec.EXACTLY) {
	    		height = Math.min((int)bvCirclePointHeight, height);
	    		
	    	} else {
	    		height = (int) bvCirclePointHeight;
	    	}
	    	
	    	width = height = Math.min(width, height);
	    }
	    
	    setMeasuredDimension(width, height);  
	    
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		
		int viewWidth = getWidth();
		int viewHeight = getHeight();
		
		int minRadius = Math.min(viewWidth, viewHeight)/2;

		
		if(bvIsCirclePointMode){
			canvas.drawCircle(viewWidth/2, viewHeight/2, minRadius /*+offset_radius*/ , getDrawBackgroundAttrPaint());
			
		} else if(count > 0){
			
			String draw_text = "";
			float text_draw_x = viewWidth/2, text_draw_y = viewHeight/2;
			
			draw_text = String.valueOf(count);
			
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setTextSize(text_size);
			mPaint.getTextBounds(String.valueOf(count), 0, String.valueOf(count).length(), textRect);
			
			
			int textWidth = (int) mPaint.measureText(String.valueOf(count));
			int textHeight = textRect.height();
			
			
			text_draw_x = viewWidth/2 - textWidth/2;
			text_draw_y = viewHeight/2 + textHeight/2;
			
			
			if(count > 9 && !bvIsAlwaysCircleBg && !bvIsCirclePointMode){
				
				int left = 0;
				int top = 0;
				int right = viewWidth;
				int bottom = viewHeight;
				
				RectF rect = new RectF(left, top, right, bottom); 
				canvas.drawRoundRect(rect,    
						viewWidth * 2 / 5,     
						viewHeight * 2 / 5,    
						getDrawBackgroundAttrPaint()); 
				
				
			} else {
				
				canvas.drawCircle(viewWidth/2, viewHeight/2, minRadius /*+offset_radius*/ , getDrawBackgroundAttrPaint());
				
			}
			
			canvas.drawText(draw_text, text_draw_x , text_draw_y, getDrawTextPaint());
		}
			
	}
	
	
	private Paint getDrawBackgroundAttrPaint(){
		mPaint.reset();
		mPaint.setAntiAlias(true);
		//mPaint.setStyle(Style.FILL);
		mPaint.setColor(bvBackgroundColor);
		
		return mPaint;
	}
	
	private Paint getDrawTextPaint(){
		mPaint.reset();
		mPaint.setAntiAlias(true);
		//mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setColor(text_color);
		mPaint.setTextSize(text_size);
		
		return mPaint;
	}
	
	
	public void setTextSizeSP(int sp_textSize, boolean invalidate){
		int temp_text_size = sp2px(getContext(), sp_textSize);
		if(temp_text_size!= text_size){
			text_size = temp_text_size;
			if(invalidate){
				postInvalidate();
			}
		}
	}
	
	public void setBvIsAlwaysCircleBg(boolean _bvIsAlwaysCircleBg){
		if(_bvIsAlwaysCircleBg!=bvIsAlwaysCircleBg){
			bvIsAlwaysCircleBg = _bvIsAlwaysCircleBg;
			postInvalidate();
		}
	}
	
	public void setBvIsCirclePointMode(boolean _bvIsCirclePointMode){
		if(_bvIsCirclePointMode!=bvIsCirclePointMode){
			bvIsCirclePointMode = _bvIsCirclePointMode;
			postInvalidate();
		}
	}
	
	public void setBvIsCirclePointMode(boolean _bvIsCirclePointMode, float dp_bvCirclePointWidth, float dp_bvCirclePointHeight){
		if(_bvIsCirclePointMode!=bvIsCirclePointMode){
			bvIsCirclePointMode = _bvIsCirclePointMode;
			bvCirclePointWidth = dip2px(getContext(), dp_bvCirclePointWidth);
			bvCirclePointHeight = dip2px(getContext(), dp_bvCirclePointHeight);
			postInvalidate();
		}
	}
	
	public void setBvIsCirclePointMode(float dp_bvCirclePointWidth, float dp_bvCirclePointHeight){
		bvCirclePointWidth = dip2px(getContext(), dp_bvCirclePointWidth);
		bvCirclePointHeight = dip2px(getContext(), dp_bvCirclePointHeight);
		if(bvIsCirclePointMode){
			postInvalidate();
		}
	}
	
	public void setAttr(boolean _bvIsCirclePointMode, boolean _bvIsAlwaysCircleBg, int num, int sp_textSize){
		bvIsCirclePointMode = _bvIsCirclePointMode;
		bvIsAlwaysCircleBg = _bvIsAlwaysCircleBg;
		count = num;
		setTextSizeSP(sp_textSize, false);
		postInvalidate();
	}
	
	public void setCount(int num){
		if(num != count){
			count = num;
			postInvalidate();
		}
		
	}
	

	/**
	 * use set drawable
	 * @return
	 */
	public ShapeDrawable getDefaultBackground() {
		
		int r = dipToPixels(DEFAULT_CORNER_RADIUS_DIP);
		float[] outerR = new float[] {r, r, r, r, r, r, r, r};
        
		RoundRectShape rr = new RoundRectShape(outerR, null, null);
		ShapeDrawable drawable = new ShapeDrawable(rr);
		drawable.getPaint().setColor(bvBackgroundColor);
		
		return drawable;
		
	}

	public Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable != null) {
			int w = drawable.getIntrinsicWidth();
			int h = drawable.getIntrinsicHeight();
			Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
			Bitmap bitmap = Bitmap.createBitmap(w, h, config);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, w, h);
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}
	
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
	
	private int dipToPixels(int dip) {
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		return (int) px;
	}
	
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

}

