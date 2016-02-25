package com.upinion.VectordrawableAndroid;

import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.wnafee.vector.compat.*;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.BaseViewPropertyApplicator;
import com.facebook.react.uimanager.CatalystStylesDiffMap;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIProp;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.uimanager.annotations.ReactProp;
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
import javax.annotation.Nullable;

public class ReactVectorDrawableAndroidManager extends ViewGroupManager<RelativeLayout> {
    public static final String REACT_CLASS = "RCTVectorDrawableView";

    @UIProp(UIProp.Type.STRING)
    public static final String PROP_RES = "resourceName";
    @UIProp(UIProp.Type.ARRAY)
    public static final String PROP_VANI = "vectorAnimation";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public RelativeLayout createViewInstance(ThemedReactContext context) {
        RelativeLayout res = new RelativeLayout(context);
        return res;
    }

    @ReactProp(name = PROP_RES)
    public void setResourceName(RelativeLayout view, @Nullable String resourceName) {
        ImageView img = new ImageView(view.getContext());
        Drawable draw = createVectorDrawable(view, resourceName);
        img.setImageDrawable(draw);
        if (img.getDrawable() instanceof AnimatedVectorDrawable){
            throw new JavascriptException("Cant use AnimatedVector in resourceName");
        }
        view.addView(img, 0);
    }

    private Drawable createVectorDrawable(RelativeLayout view, String resourceName) throws JavascriptException{
        int resourceIdent;
        if( (resourceIdent = view.getContext().getResources().getIdentifier( resourceName, "drawable", view.getContext().getPackageName())) == 0 )
            throw new JavascriptException("Invalid resourceName");

        Drawable draw;
        //Fix bug with Animations in Android >= 5.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            draw = ResourcesCompat.getDrawable(view.getContext(), resourceIdent);
        else{
            try {
                draw = VectorDrawable.getDrawable(view.getContext(), resourceIdent);
            } catch (IllegalArgumentException e1) {
                try {
                    draw = AnimatedVectorDrawable.getDrawable(view.getContext(), resourceIdent);
                } catch (IllegalArgumentException e2) {
                    throw new JavascriptException(e2.toString());
                }
            }
        }
        return draw;
    }

    @ReactProp(name = PROP_VANI)
    public void setVectorAnimations(RelativeLayout view, @Nullable ReadableMap arr) {
        if(!arr.hasKey("resourceName") || !arr.hasKey("animations"))
            throw new JavascriptException("Invalid props");

        ReadableArray ani = arr.getArray("animations");
        String resourceName = arr.getString("resourceName");
        ImageView img = new ImageView(view.getContext());
        Drawable draw = createVectorDrawable(view, resourceName);
        img.setImageDrawable(draw);

        if (img != null && img.getDrawable() instanceof AnimatedVectorDrawable && ani.size() > 0) {
            for (int i = 0; i < ani.size(); i++) {
                String targetName = ani.getMap(i).getString("targetName");

                if (ani.getMap(i).hasKey("resourceAnimation") && ani.getMap(i).hasKey("propertyName"))
                    throw new JavascriptException("You cannot use resourceAnimation and propertyName at the same time");

                if (ani.getMap(i).hasKey("resourceAnimation")) {
                    Integer resourceAnimation;
                    if ((resourceAnimation = view.getContext().getResources().getIdentifier(ani.getMap(i).getString("resourceAnimation"), "anim", view.getContext().getPackageName())) == 0)
                        throw new JavascriptException("Invalid resourceAnimation");
                    else {
                        Animator animation;
                        if (((AnimatedVectorDrawable) img.getDrawable()).isPath(targetName)) {
                            Float pathError;
                            try {
                                Field auxf = AnimatedVectorDrawable.class.getDeclaredField("mAnimatedVectorState");
                                auxf.setAccessible(true);
                                Object privateObject = auxf.get((AnimatedVectorDrawable) img.getDrawable());

                                auxf = privateObject.getClass().getDeclaredField("mVectorDrawable");
                                auxf.setAccessible(true);
                                VectorDrawable vdraw = (VectorDrawable) auxf.get(privateObject);
                                pathError = vdraw.getPixelSize();

                            } catch (Exception e) {
                                throw new JavascriptException(e.toString());
                            }

                            animation = PathAnimatorInflater.loadAnimator(view.getContext(), view.getContext().getResources(), null, resourceAnimation, pathError);
                        } else {
                            animation = AnimatorInflater.loadAnimator(view.getContext(), resourceAnimation);
                        }
                        addAnimator((AnimatedVectorDrawable) img.getDrawable(), targetName, animation);
                    }
                } else if (ani.getMap(i).hasKey("propertyName")) {
                    if (!(ani.getMap(i).hasKey("propertyName") &&
                            ani.getMap(i).hasKey("duration") &&
                            ani.getMap(i).hasKey("valueType")))
                        throw new JavascriptException("Missing Props");
                    String propertyName = ani.getMap(i).getString("propertyName");
                    Integer duration = ani.getMap(i).getInt("duration");
                    String valueType = ani.getMap(i).getString("valueType");

                    ObjectAnimator animation;

                    if (valueType.equals("pathType")) {
                        //TODO ? Create ObjectAnimator using path
                    } else {
                        Integer valueFrom = ani.getMap(i).getInt("valueFrom");
                        Integer valueTo = ani.getMap(i).getInt("valueTo");
                        animation = ObjectAnimator.ofFloat(null, propertyName, valueFrom, valueTo);

                        animation.setDuration(duration);
                        animation.setInterpolator(new AccelerateDecelerateInterpolator());

                        addAnimator((AnimatedVectorDrawable) img.getDrawable(), targetName, animation);
                    }
                }
            }
            view.addView(img, 0);
            ((Animatable)img.getDrawable()).start();
        }
    }

    /*
     * Use reflection for adding an animator to an AnimatedVectorDrawable
     */
    private void addAnimator (AnimatedVectorDrawable draw, String targetName, Animator animation) {
        try {
            Method auxM = draw.getClass().getDeclaredMethod("setupAnimatorsForTarget", String.class, Animator.class);
            auxM.setAccessible(true);
            auxM.invoke(draw, targetName, animation);

        } catch (Exception e) {
            throw new JavascriptException(e.toString());
        }
    }
}
