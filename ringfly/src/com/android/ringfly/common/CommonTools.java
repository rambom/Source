package com.android.ringfly.common;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class CommonTools {
	public static Label getLabel(String value, Color tint) {
		Label label = new Label(value, Assets.uiSkin);
		label.setWrap(false);
		label.setAlignment(Align.BOTTOM | Align.LEFT);
		label.setColor(tint);
		return label;
	}
	
	public static Integer getCoin(String strKy,String strOneNum,String strTwoNum,String strThreeNum)
	{
		Integer returnValue=0;
		for(int i=0;i<strOneNum.length();i++)
		{
			for(int j=0;j<strTwoNum.length();j++)
			{
				for(int k=0;k<strThreeNum.length();k++)
				{
					returnValue+=isZJ(strKy,strOneNum.substring(i,i+1)+
							strTwoNum.substring(j,j+1)+strThreeNum.substring(k,k+1));
				}
			}
		}
		return returnValue;
	}
	
	private static Integer isZJ(String strKy,String strKy2)
	{
		int returnValue=0;
		if(strKy==strKy2)
		{			
			returnValue= 2000;
		}
		if(strKy==strKy2.substring(0,1)+strKy2.substring(2,3)+strKy2.substring(1,2) ||
		strKy==strKy2.substring(1,2)+strKy2.substring(0,1)+strKy2.substring(2,3) ||
		strKy==strKy2.substring(1,2)+strKy2.substring(2,3)+strKy2.substring(0,1) ||
		strKy==strKy2.substring(2,3)+strKy2.substring(0,1)+strKy2.substring(1,2) ||
		strKy==strKy2.substring(2,3)+strKy2.substring(1,2)+strKy2.substring(0,1))
		{
			returnValue=330;
		}
		if(strKy.substring(0,1)==strKy.substring(1,2) || strKy.substring(0,1)==strKy.substring(2,3)
				|| strKy.substring(1,2)==strKy.substring(2,3))
				{
			returnValue= 660;
				}
		return returnValue;
	}
}
