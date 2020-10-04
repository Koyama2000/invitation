package com.kgc.invitation.service;

import com.github.pagehelper.PageInfo;
import com.kgc.invitation.bean.Detail;
import com.kgc.invitation.bean.Invitation;

import java.util.List;

public interface DetailService {
    //根据帖子编号查询回复，分页
    List<Detail> DetailListById(Integer invid);
    //添加回复
    int DetailAdd(Detail detail);
    //删除回复
    int DetailDel(Integer invid);
    //分页
    public List<Detail> pageList(Long invid, Integer pageNum, Integer pageSize);
}
