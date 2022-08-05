package com.android.operatorapp.View;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class ConsoleView {
   private LinearLayout consoleLayout;
   private Context context;

    public ConsoleView(LinearLayout consoleLayout, Context context) {
        this.consoleLayout = consoleLayout;
        this.context = context;
    }

    public void e(String message){
        TextView tv=new TextView(context);
        tv.setTextColor(Color.RED);
        tv.setText(message);
        tv.setTypeface(Typeface.MONOSPACE);
        consoleLayout.addView(tv);
    }

    public void w(String message){
        TextView tv=new TextView(context);
        tv.setTextColor(Color.YELLOW);
        tv.setText(message);
        tv.setTypeface(Typeface.MONOSPACE);
        consoleLayout.addView(tv);
    }

    public void s(String message){
        TextView tv=new TextView(context);
        tv.setTextColor(Color.GREEN);
        tv.setText(message);
        tv.setTypeface(Typeface.MONOSPACE);
        consoleLayout.addView(tv);
    }

    public void d(String message){
        TextView tv=new TextView(context);
        tv.setTextColor(Color.BLUE);
        tv.setText(message);
        tv.setTypeface(Typeface.MONOSPACE);
        consoleLayout.addView(tv);
    }

}
