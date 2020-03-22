package com.cafjo.carjo.api.all;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import com.google.auth.oauth2.GoogleCredentials;
import java.util.concurrent.ConcurrentHashMap;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Builder;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;

/**
 * @Auther: IT实战联盟Line
 * @Date: 2019-11-23 14:02
 * @Description:  Google_FireBase推送工具类
 */
public class FireBaseUtil {
    //存放多个实例的Map
    private static Map<String, FirebaseApp> firebaseAppMap = new ConcurrentHashMap<>();
    //获取AndroidConfig.Builder对象
    private static com.google.firebase.messaging.AndroidConfig.Builder androidConfigBuilder=AndroidConfig.builder();
    //获取AndroidNotification.Builder对象
    private static AndroidNotification.Builder androidNotifiBuilder=AndroidNotification.builder();
    FirebaseApp myapp;
    /**
     * 判断SDK是否初始化
     * @param appName
     * @return
     */
    public static boolean isInit(String appName) {
        return firebaseAppMap.get(appName) != null;
    }

    /**
     * 初始化SDK
     * @param jsonPath      JSON路径
     * @param dataUrl       firebase数据库
     * @param appName       APP名字
     * @throws IOException
     */
    public static void initSDK(String jsonPath, String dataUrl,String appName) throws IOException {
        InputStream serviceAccount = Thread.currentThread().getContextClassLoader().getResourceAsStream(jsonPath);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(dataUrl).build();
        //初始化firebaseApp
        FirebaseApp firebaseApp = FirebaseApp.initializeApp(options);
        //存放
        firebaseAppMap.put(appName,firebaseApp);
    }

    /**
     * 单设备推送
     * @param appName      应用的名字
     * @param token        注册token
     * @param title        推送题目
     * @param body         推送内容
     * @return
     * @throws IOException
     * @throws FirebaseMessagingException
     */
    public static void pushSingle(String appName, String token, String title, String body) throws IOException, FirebaseMessagingException{
        FirebaseApp firebaseApp = firebaseAppMap.get(appName);
        if (firebaseApp == null) {
            return;
        }
        Message message = Message.builder().setNotification(new Notification(title,body))
                .setToken(token)
                .build();
        String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
        System.out.println("单个设备推送成功 : "+response);
    }

    /**
     * 给设备订阅主题
     * @param appName     应用的名字
     * @param tokens      设备的token,最大1000个
     * @param topic       要添加的主题
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    public static void registrationTopic(String appName, List<String> tokens, String topic) throws FirebaseMessagingException, IOException {
        //获取实例
        FirebaseApp firebaseApp = firebaseAppMap.get(appName);
        //实例不存在的情况
        if(firebaseApp == null) {
            return;
        }
        //订阅，返回主题管理结果对象。
        TopicManagementResponse response = FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(tokens, topic);
        System.out.println("添加设备主题，成功：" + response.getSuccessCount() + ",失败：" + response.getFailureCount());
    }
    /**
     * 按主题推送
     * @param appName      应用的名字
     * @param topic        主题的名字
     * @param title        消息题目
     * @param body         消息体
     * @return
     * @throws FirebaseMessagingException
     * @throws IOException
     */
    public static void sendTopicMes(String appName, String topic, String title, String body) throws FirebaseMessagingException, IOException {
        //获取实例
        FirebaseApp firebaseApp = firebaseAppMap.get(appName);
        //实例不存在的情况
        if(firebaseApp == null) {
            return;
        }
        //构建消息
        Message message = Message.builder()
                .setNotification(new Notification(title,body))
                .setTopic(topic)
                .build();
        //发送后，返回messageID
        String response = FirebaseMessaging.getInstance(firebaseApp).send(message);
        System.out.println("主题推送成功: " + response);
    }
}