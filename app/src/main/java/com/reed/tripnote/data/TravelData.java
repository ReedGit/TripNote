package com.reed.tripnote.data;

import com.reed.tripnote.beans.TravelBean;
import com.reed.tripnote.tools.FormatTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地模拟数据
 * Created by 伟 on 2016/4/6.
 */
public class TravelData {

    public static TravelData travelData;

    public static TravelData getInstance() {
        if (travelData == null) {
            travelData = new TravelData();
        }
        return travelData;
    }

    private List<TravelBean> travels;

    public List<TravelBean> getTravels() {
        initList();
        return travels;
    }

    private void initList() {
        travels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TravelBean travelBean = new TravelBean();
            travelBean.setUserId(4);
            travelBean.setTravelId(1);
            travelBean.setTitle("南京一日游");
            travelBean.setStartTime(FormatTool.transformToDate("2016-04-06 10:00:00"));
            travelBean.setEndTime(FormatTool.transformToDate("2016-04-06 20:00:00"));
            travelBean.setIntroduction("感受金陵之美");
            travelBean.setNickname("Reed");
            travelBean.setUserImage("/upload/4/da92f128-42ed-4424-8336-d57c830723f1.jpg");
            travels.add(travelBean);
        }
    }
}
