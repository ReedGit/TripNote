package com.reed.tripnote.data;

import com.reed.tripnote.beans.ContentBean;
import com.reed.tripnote.tools.FormatTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 游记内容本地化数据
 * Created by 伟 on 2016/4/6.
 */
public class ContentData {

    public static ContentData contentData;

    public static ContentData getInstance() {
        if (contentData == null) {
            contentData = new ContentData();
        }
        return contentData;
    }

    private List<ContentBean> contents;

    public List<ContentBean> getContents() {
        initData();
        return contents;
    }

    private void initData() {
        contents = new ArrayList<>();
        ContentBean content1 = new ContentBean();
        content1.setContentId(1);
        content1.setArticle("夫子庙好棒\n\n\n\n\n\n\n\n\n\n\n\n\n");
        content1.setDay(1);
        content1.setLocation("江苏省南京市");
        content1.setTime(FormatTool.transformToDate("2016-04-06 11:00:00"));
        content1.setTravelId(1);
        content1.setCoordinate("118.788832,32.020783");
        contents.add(content1);

        ContentBean content2 = new ContentBean();
        content2.setContentId(2);
        content2.setArticle("中山陵不错");
        content2.setDay(1);
        content2.setLocation("江苏省南京市");
        content2.setTime(FormatTool.transformToDate("2016-04-06 12:00:00"));
        content2.setTravelId(1);
        content2.setCoordinate("118.853336,32.059379");
        contents.add(content2);

        ContentBean content3 = new ContentBean();
        content3.setContentId(3);
        content3.setArticle("玄武湖很大");
        content3.setDay(1);
        content3.setLocation("江苏省南京市");
        content3.setTime(FormatTool.transformToDate("2016-04-06 13:00:00"));
        content3.setTravelId(1);
        content3.setCoordinate("118.798908,32.072357");
        contents.add(content3);

        ContentBean content4 = new ContentBean();
        content4.setContentId(4);
        content4.setArticle("鸡鸣寺樱花好美");
        content4.setDay(1);
        content4.setLocation("江苏省南京市");
        content4.setTime(FormatTool.transformToDate("2016-04-06 14:00:00"));
        content4.setTravelId(1);
        content4.setCoordinate("118.79532,32.061038");
        contents.add(content4);

        ContentBean content5 = new ContentBean();
        content5.setContentId(5);
        content5.setArticle("感受总统府");
        content5.setDay(1);
        content5.setLocation("江苏省南京市");
        content5.setTime(FormatTool.transformToDate("2016-04-06 11:00:00"));
        content5.setTravelId(1);
        content5.setCoordinate("118.797403,32.044221");
        contents.add(content5);
    }
}
