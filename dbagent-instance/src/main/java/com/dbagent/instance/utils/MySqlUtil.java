package com.dbagent.instance.utils;

import com.dbagent.common.utils.StringUtils;
import com.dbagent.instance.domain.Instance;
import com.dbagent.instance.domain.MySqlInstanceConfig;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Base64;

/**
 * @Classname MySqlUtil
 * @Description TODO
 * @Date 2025/8/10 20:04
 * @Created by xxx
 */

@Slf4j
public class MySqlUtil {

    // 默认密钥
    public static final String DEFAULT_KEY = "DBAgentKey123456";

    /**
     * AES加密
     *
     * @param content  需要加密的内容
     * @param password 加密密码
     * @return 加密后的字符串
     */
    public static String aesEncrypt(String content, String password) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            byte[] result = cipher.doFinal(byteContent);
            return Base64.getEncoder().encodeToString(result);
        } catch (Exception e) {
            log.error("AES加密异常:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return 解密后的字符串
     */
    public static String aesDecrypt(String content, String password) {
        try {
            byte[] contentBytes = Base64.getDecoder().decode(content);
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(contentBytes);
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密异常:{}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 根据密码创建密钥
     *
     * @param password 密码
     * @return SecretKeySpec
     */
    private static SecretKeySpec createKey(String password) {
        byte[] keyBytes = password.getBytes();
        // 使用SHA-256摘要算法确保密钥长度为128位
        java.security.MessageDigest sha = null;
        try {
            sha = java.security.MessageDigest.getInstance("SHA-256");
            keyBytes = sha.digest(keyBytes);
            keyBytes = java.util.Arrays.copyOf(keyBytes, 16); // 使用前128位
        } catch (Exception e) {
            log.error("SHA-256摘要算法异常:{}", e.getMessage(), e);
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static Integer ping(String host, String port, String username, String password) {
        return ping(getUrl(host, port), username, password);
    }

    public static Integer ping(MySqlInstanceConfig config) {
        return ping(config.getHost(), config.getPort().toString(), config.getUsername(), config.getPassword());
    }

    //测试连接性
    public static Integer ping(String url, String username, String password) {
        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return 0;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            java.sql.Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
                java.sql.Statement stmt = conn.createStatement();
                java.sql.ResultSet rs = stmt.executeQuery("select 1");
                if (rs.next()) {
                    int value = rs.getInt(1);
                    if (value == 1) {
                        conn.close();
                        return 1;
                    }
                }
                return 0;
            }
        } catch (Exception e) {
            log.error("数据库连接失败:{}\n{}\n{}", url, username, password);
            return 0;
        }
        return 0;
    }

    public static String getUrl(String host, String port) {
        return "jdbc:mysql://" + host + ":" + port + "/";
    }

    //实例信息补充
    public static void InstanceInfoSupplement(Instance instance) {
        String url = getUrl(instance.getHost(), instance.getPort().toString());
        String username = instance.getUsername();
        String password = instance.getPasswordEnc();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            DatabaseMetaData metaData = conn.getMetaData();
            String instanceType = metaData.getDatabaseProductName();
            String version = metaData.getDatabaseProductVersion();
            instance.setInstanceType(instanceType);
            instance.setVersion(version);
        } catch (SQLException e) {
            log.error("数据库连接失败:{}", e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        System.out.println(ping("jdbc:mysql://127.0.0.1/", "root", "1234"));
    }
}