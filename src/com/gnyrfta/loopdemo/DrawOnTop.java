package com.gnyrfta.loopdemo;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class DrawOnTop extends View 
{
	
	public DrawOnTop(Context activity) 
	{ 
        super(activity);
    }
	public void setDrawingVariables(ArrayList<Long> thisOne,ArrayList<String> thatOne)
	{
		ArrayList<Long> temp = new ArrayList<Long>();
		
	}
    @Override 
    protected void onDraw(Canvas canvas) 
    {
        // put your drawing commands here
    }


}
