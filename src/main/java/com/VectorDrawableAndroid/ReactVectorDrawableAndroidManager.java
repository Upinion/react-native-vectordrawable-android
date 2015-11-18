package com.upinion.VectorDrawableAndroid;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.drawable.Drawable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.BaseViewPropertyApplicator;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.text.ReactTextShadowNode;
import com.facebook.react.modules.core.JavascriptException;

import java.util.Arrays;
import java.util.List;

public class ReactVectorDrawableAndroidManager extends ViewGroupManager<RelativeLayout> {
    public static final String REACT_CLASS = "RCTVectorDrawableView";

    @UIProp(UIProp.Type.STRING)
    public static final String PROP_RES = "resourceName";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public RelativeLayout createViewInstance(ThemedReactContext context) {
        RelativeLayout res = new RelativeLayout(context);
        return res;
    }

    @Override
    public void updateView(final RelativeLayout view,
                           final CatalystStylesDiffMap props) {

        Integer resourceIdent;
        if(!props.hasKey(PROP_RES) || (resourceIdent = view.getContext().getResources().getIdentifier( props.getString(PROP_RES), "drawable", view.getContext().getPackageName())) == 0 )
            throw new JavascriptException("No valid resourceName");

        Drawable draw = view.getContext().getResources().getDrawable( resourceIdent);
        ImageView img = new ImageView(view.getContext());
        img.setImageDrawable(draw);
        view.addView(img, 0);

        BaseViewPropertyApplicator.applyCommonViewProperties(view, props);

        super.updateView(view, props);
    }
}
