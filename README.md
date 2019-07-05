YGO2_Android_Library
---------------------
    用于 https://github.com/Unicorn369/ygopro2
    
编译
---------------------
    ./gradlew :library:assembleRelease
    
使用方法
---------------------
    1.创建一个unity项目
    2.将生成的aar文件放到Assets/Plugins/Android/文件夹下
    
    定义：AndroidJavaObject jo = new AndroidJavaObject("cn.unicorn369.library.API");
    调用：jo.Call(methodName, args);
