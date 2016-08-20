package com.gz.family.application;

/**
 * Created by host on 2016/1/29.
 */
public class Constant {
    public class Code {

    }

    public class URL {
        public static final String HOST = "139.129.18.117";
        public static final int PORT = 8080;
        public static final String Get = "http://" + HOST + ":" + PORT + "/Family/";
        public static final String Login = Get + "login.html";
        public static final String Register = Get + "register.html";
        public static final String Relatives = Get + "relatives.html";
        public static final String MQTT = "http://139.129.18.117/PhpMQTTClient/pub.php";
    }

    public class DataKey {
        public static final String SESS = "session:";
    }
}
