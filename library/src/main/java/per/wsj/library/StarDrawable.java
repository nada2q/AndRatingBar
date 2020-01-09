package per.wsj.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;

import androidx.appcompat.content.res.AppCompatResources;

public class StarDrawable extends LayerDrawable {

    public StarDrawable(Context context, int starDrawable, int bgDrawable) {
        super(new Drawable[]{
                createLayerDrawableWithTintAttrRes(bgDrawable, R.attr.colorControlHighlight, context),
                createClippedLayerDrawableWithTintColor(starDrawable, Color.TRANSPARENT, context),
                createClippedLayerDrawableWithTintAttrRes(starDrawable, R.attr.colorControlActivated, context)
        });

        setId(0, android.R.id.background);
        setId(1, android.R.id.secondaryProgress);
        setId(2, android.R.id.progress);
    }

    private static Drawable createLayerDrawableWithTintAttrRes(int tileRes, int tintAttrRes,
                                                               Context context) {
        int tintColor = getColorFromAttrRes(tintAttrRes, context);
        return createLayerDrawableWithTintColor(tileRes, tintColor, context);
    }

    @SuppressLint("RtlHardcoded")
    private static Drawable createClippedLayerDrawableWithTintColor(int tileResId, int tintColor,
                                                                    Context context) {
        return new ClipDrawable(createLayerDrawableWithTintColor(tileResId, tintColor,
                context), Gravity.LEFT, ClipDrawable.HORIZONTAL);
    }

    private static Drawable createLayerDrawableWithTintColor(int tileRes, int tintColor,
                                                             Context context) {
        TileDrawable drawable = new TileDrawable(AppCompatResources.getDrawable(context,
                tileRes));
        drawable.mutate();
        drawable.setTint(tintColor);
        return drawable;
    }

    @SuppressLint("RtlHardcoded")
    private static Drawable createClippedLayerDrawableWithTintAttrRes(int tileResId,
                                                                      int tintAttrRes,
                                                                      Context context) {
        return new ClipDrawable(createLayerDrawableWithTintAttrRes(tileResId, tintAttrRes,
                context), Gravity.LEFT, ClipDrawable.HORIZONTAL);
    }

    public float getTileRatio() {
        Drawable drawable = getTileDrawableByLayerId(android.R.id.progress).getDrawable();
        return (float) drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
    }

    public void setStarCount(int count) {
        getTileDrawableByLayerId(android.R.id.background).setTileCount(count);
        getTileDrawableByLayerId(android.R.id.secondaryProgress).setTileCount(count);
        getTileDrawableByLayerId(android.R.id.progress).setTileCount(count);
    }

    @SuppressLint("NewApi")
    private TileDrawable getTileDrawableByLayerId(int id) {
        Drawable layerDrawable = findDrawableByLayerId(id);
        switch (id) {
            case android.R.id.background:
                return (TileDrawable) layerDrawable;
            case android.R.id.secondaryProgress:
            case android.R.id.progress: {
                ClipDrawable clipDrawable = (ClipDrawable) layerDrawable;
                return (TileDrawable) clipDrawable.getDrawable();
            }
            default:
                // Should never reach here.
                throw new RuntimeException();
        }
    }

    private static int getColorFromAttrRes(int attrRes, Context context) {
        TypedArray a = context.obtainStyledAttributes(new int[] { attrRes });
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }
}
