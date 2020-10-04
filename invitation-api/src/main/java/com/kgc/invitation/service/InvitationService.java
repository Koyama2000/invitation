package com.kgc.invitation.service;

import com.github.pagehelper.PageInfo;
import com.kgc.invitation.bean.Invitation;

import java.util.List;

public interface InvitationService {
    //根据帖子标题模糊查询，分页
    public List<Invitation> InvitationList(String title);
    //根据Id删除
    public int InvivationDelById(Integer id,String title);

    //分页
    public List<Invitation> pageList(String title,Integer pageNum, Integer pageSize);
}
