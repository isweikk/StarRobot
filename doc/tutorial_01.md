# Tutorial_01_构建基础环境

## S01.1、新建Helloworld工程
本过程略

## S01.2、简单修改：横屏、全屏、常亮、图标

- 在AndroidManifest.xml中，设置activity的属性

        android:screenOrientation="landscape"  
        android:configChanges="orientation|keyboardHidden"  
        注：landscape强制横屏，keyboardHidden防止APP切换导致activity被重载
    
- 在MainActivity.java中，onCreate方法，设置全屏、常亮

        注：必须在setContentView方法之前设置
        //全屏  
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏系统标题栏  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //设置常亮

- 在res/values/style.xml中，设置隐藏APP菜单栏

        <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        更改为
        <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
- 在res/mipmap-***中，放入固定尺寸的图标，命名ic_launcher.png

到此，达到我想要的效果：启动即横屏，全屏显示，并常亮

