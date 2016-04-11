package com.reed.tripnote.data;

import com.reed.tripnote.beans.CommentBean;
import com.reed.tripnote.tools.FormatTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论的本地化数据
 * Created by 伟 on 2016/4/11.
 */
public class CommentData {

    private static CommentData commentData;

    public static CommentData getInstance() {
        if (commentData == null) {
            commentData = new CommentData();
        }
        return commentData;
    }

    private List<CommentBean> comments;

    public List<CommentBean> getComments() {
        initData();
        return comments;
    }

    private void initData() {
        comments = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CommentBean commentBean = new CommentBean();
            commentBean.setCommentId(1);
            commentBean.setNickname("Reed");
            commentBean.setRemark("写的真好");
            commentBean.setTime(FormatTool.transformToDate("2016-04-11 10:10:00"));
            comments.add(commentBean);
        }
    }
}
