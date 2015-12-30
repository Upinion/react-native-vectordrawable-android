# react-native-vectordrawable-android
React native vectordrawable enables you to use vector for android.

### Important Notes
```
In order to work on Android < 5.0 implementation has been changed to use Vector-compat,
instead of native VectorDrawable.

Read carefully about the XML syntax needed for vector-compat compatibility.
```
https://github.com/wnafee/vector-compat

### Installation

```bash
npm install --save react-native-vectordrawable-android

or manually

git clone the directory to [node_modules/react-native-vectordrawable-android]
```

### Add it to your android project

* In `android/setting.gradle`

```gradle
...
include ':reactVectorDrawable', ':app'
project(':reactVectorDrawable').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-vectordrawable-android')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':reactVectorDrawable')
}
```

* register module (in MainActivity.java)

```java
import com.upinion.VectorDrawableAndroid.ReactVectorDrawableAndroidPackage;  // <--- import

public class MainActivity extends Activity implements DefaultHardwareBackBtnHandler {
  ......

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mReactRootView = new ReactRootView(this);

    mReactInstanceManager = ReactInstanceManager.builder()
      .setApplication(getApplication())
      .setBundleAssetName("index.android.bundle")
      .setJSMainModuleName("index.android")
      .addPackage(new MainReactPackage())
      .addPackage(new ReactVectorDrawableAndroidPackage())             // <------ add here
      .setUseDeveloperSupport(BuildConfig.DEBUG)
      .setInitialLifecycleState(LifecycleState.RESUMED)
      .build();

 
  ......

}
```

## Example

Add vectorDrawable to `res/drawable` in your project.

```javascript
var VectorDrawableView = require('react-native-vectordrawable-android');

<VectorDrawableView
    resourceName={'resourceNameofDrawable'}
    style={{height: 180, width: 180}}
>
    <Text>Example</Text>
</VectorDrawableView>
```
Dont forget to add size (will not show up if its not added).

## Experimental Animations
(Make sure that your `Drawable` is an `AnimatedVectorDrawable` first ;))

Create your own animation. (you can add as many as you want)

```javascript
<VectorDrawableView
    resourceName={'resourceNameofDrawable'}
    style={{height: 180, width: 180}}
    animations={[
        {
            targetName: "NameOfTargetElement",
            propertyName: "NameOfPropertyToChange",
            duration: 1000, //miliseconds
            valueFrom: 0, //number
            valueTo: 10 //number
        },
        {
            targetName: "NameOfTargetElement2",
            propertyName: "NameOfPropertyToChange2",
            duration: 1000, //miliseconds
            valueFrom: 0, //number
            valueTo: 10 //number
        }
    ]}
/>
```

Use your own `res/anim` Animation resources.

```javascript
<VectorDrawableView
    resourceName={'resourceNameofDrawable'}
    style={{height: 180, width: 180}}
    animations={[
        {
            targetName: "NameOfTargetElement",
            resourceName: "NameofAnimationResource"
        },
        {
            targetName: "NameOfTargetElement2",
            resourceName: "NameOfAnimationResource2"
        }
    ]}
/>
```
