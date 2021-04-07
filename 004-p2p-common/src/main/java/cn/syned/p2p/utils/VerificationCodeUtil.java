package cn.syned.p2p.utils;

import java.util.Random;

public class VerificationCodeUtil {

    /**
     * 生成验证码
     *
     * @param number 生成随机数的位数
     * @return 验证码
     */
    public static String getVerificationCode(int number) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
