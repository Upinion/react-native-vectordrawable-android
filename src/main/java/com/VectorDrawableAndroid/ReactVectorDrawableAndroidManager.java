package com.upinion.VectorDrawableAndroid;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.graphics.drawable.*;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.BaseViewPropertyApplicator;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.text.ReactTextShadowNode;
import com.facebook.react.modules.core.JavascriptException;

import java.lang.Exception;
import java.lang.NoSuchMethodException;
import java.lang.SecurityException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReactVectorDrawableAndroidManager extends ViewGroupManager<RelativeLayout> {
    public static final String REACT_CLASS = "RCTVectorDrawableView";

    @UIProp(UIProp.Type.STRING)
    public static final String PROP_RES = "resourceName";
    @UIProp(UIProp.Type.ARRAY)
    public static final String PROP_ANI = "animations";

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
        BaseViewPropertyApplicator.applyCommonViewProperties(view, props);

        Integer resourceIdent;
        if(!props.hasKey(PROP_RES) || (resourceIdent = view.getContext().getResources().getIdentifier( props.getString(PROP_RES), "drawable", view.getContext().getPackageName())) == 0 )
            throw new JavascriptException("No valid resourceName");

        ImageView img = new ImageView(view.getContext());
        img.setImageResource(resourceIdent);

        if(img.getDrawable() instanceof AnimatedVectorDrawable && props.hasKey(PROP_ANI) && props.getArray(PROP_ANI).size() > 0) {
            for( int i = 0; i < props.getArray(PROP_ANI).size(); i++) {
                String targetName = props.getArray(PROP_ANI).getMap(i).getString("targetName");
                String propertyName = props.getArray(PROP_ANI).getMap(i).getString("propertyName");
                Integer duration = props.getArray(PROP_ANI).getMap(i).getInt("duration");
                String valueType = props.getArray(PROP_ANI).getMap(i).getString("valueType");

                ObjectAnimator animation;

                if (valueType.equals("pathType")){
                    //TODO Create ObjectAnimator using path
                }else {
                    Integer valueFrom = props.getArray(PROP_ANI).getMap(i).getInt("valueFrom");
                    Integer valueTo = props.getArray(PROP_ANI).getMap(i).getInt("valueTo");
                    animation = ObjectAnimator.ofFloat(null, propertyName, valueFrom, valueTo);

                    animation.setDuration(duration);
                    animation.setInterpolator(new AccelerateDecelerateInterpolator());

                    try {
                        Field auxf = AnimatedVectorDrawable.class.getDeclaredField("mAnimatedVectorState");
                        auxf.setAccessible(true);
                        Object privateObject = auxf.get(img.getDrawable());
                        Class privateClass = privateObject.getClass();

                        Method auxM = privateClass.getDeclaredMethod("addTargetAnimator", String.class, Animator.class);
                        auxM.setAccessible(true);
                        auxM.invoke(privateObject, targetName, animation);

                    } catch (Exception e) {
                        throw new JavascriptException(e.getMessage());
                    }
                }
            }
        }
        view.addView(img, 0);
        if(img.getDrawable() instanceof AnimatedVectorDrawable)
            ((Animatable)img.getDrawable()).start();

        super.updateView(view, props);
    }

}
