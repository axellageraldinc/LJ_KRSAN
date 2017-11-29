package pap.ta.lj_krsan.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by axellageraldinc on 27/11/17.
 */

public class custom_font_1 extends TextView{
    public custom_font_1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/yoster.ttf"));
    }
}
