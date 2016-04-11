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
            travelBean.setIntroduction("南京，山环水抱，葱笼毓秀，山水城林融为一体，自然风貌久负盛名。自然界厚赐于南京龙蟠虎踞的山川形胜，历史在此又遗存下灿烂的文化。孙中山先生曾有名言概括南京之美：“此地有高山，有平原，有深水，在世界三大城市中亦诚难觅此佳境”。位于城东紫金山麓的中山陵、明孝陵，掩映在绿色葱葱的紫金山中，布局宏伟。太平天国遗址，殿阙巍峨。数十处南朝陵墓刻，硕大洗炼，堪称一代巨制，国之瑰宝。栖霞寺内舍利塔，造型雄健，比例匀称，装饰华丽，是我国现存石塔中不可多得的佳品。总统府、雨花台烈士陵园、侵华日军南京大屠杀遇难同胞纪念馆、梅园新村纪念馆、渡江胜利纪念碑是我国民主革命的历史见证。玄武湖，三面环水，一面临城，秀峰塔影，碧波荡漾，堪称“金陵明珠”。莫愁湖，蕴含动人凄丽的传说，湖光倩影，花团锦蔟，典雅、幽秀而豁达、奔放。");
            travelBean.setNickname("Reed");
            travelBean.setUserImage("/upload/4/da92f128-42ed-4424-8336-d57c830723f1.jpg");
            travels.add(travelBean);
        }
    }
}
