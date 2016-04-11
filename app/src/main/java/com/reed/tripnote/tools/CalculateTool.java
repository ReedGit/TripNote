package com.reed.tripnote.tools;

import java.util.Date;

/**
 * 与计算相关工具类
 * Created by 伟 on 2016/4/11.
 */
public class CalculateTool {

    /**
     * 计算两个日期间的天数（包含计算日期当天，如2016-04-11和2016-04-12结果便是2）
     *
     * @param foreDate 较前的日期
     * @param lastDate 较后的日期
     * @return 两日期相差天数
     */
    public static long calculateDay(Date foreDate, Date lastDate) {
        if (foreDate.after(lastDate)) {
            throw new IllegalArgumentException("the lastDate is before the foreDate!");
        }
        return (lastDate.getTime() - foreDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
    }
}
