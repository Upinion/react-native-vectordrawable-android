package com.upinion.VectorDrawableAndroid;

import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.BaseViewPropertyApplicator;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.text.ReactTextShadowNode;

import java.util.Arrays;
import java.util.List;

public class ReactVectorDrawableAndroidManager extends SimpleViewManager<ImageView> {
    public static final String REACT_CLASS = "RCTVectorDrawableView";

    @UIProp(UIProp.Type.NUMBER)
    public static final String PROP_VALUE = "value";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public ImageView createViewInstance(ThemedReactContext context) {
        ImageView img = new ImageView(context);
        Drawable draw = context.getResources().getDrawable(R.drawable.test_drawable);
        img.setImageDrawable(draw);
        return img;
    }

    @Override
    public void updateView(final ImageView view,
                           final CatalystStylesDiffMap props) {

        BaseViewPropertyApplicator.applyCommonViewProperties(view, props);

        super.updateView(view, props);
    }
}
