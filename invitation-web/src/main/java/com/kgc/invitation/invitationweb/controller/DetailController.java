package com.kgc.invitation.invitationweb.controller;


import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.kgc.invitation.bean.Invitation;
import com.kgc.invitation.service.DetailService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kgc.invitation.bean.Detail;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class DetailController {
    @Reference
    DetailService detailService;

    //查询分页
    @GetMapping("/det/list")
    @ResponseBody
    public Map<String,Object> DetailPageInfo(HttpSession session, @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                           @RequestParam(value = "pageSize",required = false,defaultValue = "3") Integer pageSize){
        Integer invid= (Integer) session.getAttribute("invid");
        Map<String,Object> map=new HashMap<>();
        List<Detail> detlist=detailService.DetailListById(invid);
        if(detlist.size()==0){
            map.put("pages",0);
            map.put("prePage",0);
            map.put("nextPage",0);
            map.put("count",0);
            map.put("navigatepageNums",null);
            map.put("hasPrePage",0);
            map.put("hasNextPage",0);
            map.put("pageinfo",null);
            return map;
        }
        Integer count=detlist.size();
        Integer pages=count%pageSize==0?count/pageSize:count/pageSize+1;
        if(pageNum<1){
            pageNum=1;
        }
        if(pageNum>pages){
            pageNum=pages;
        }

        List<Detail> list=detailService.pageList((long)invid,pageNum,pageSize);
        map.put("pages",pages);
        map.put("prePage",pageNum-1);
        map.put("nextPage",pageNum+1);
        map.put("count",count);
        List<Integer> navigatepageNums=new ArrayList<>();
        for (int i=1;i<=pages;i++){
            navigatepageNums.add(i);
        }
        map.put("navigatepageNums",navigatepageNums);
        map.put("hasPrePage",pageNum>1);
        map.put("hasNextPage",pageNum<pages);
        map.put("pageinfo",list);
        return map;
    }


    //添加回复
    @PostMapping("/det/add")
    @ResponseBody
    public String detadd(@RequestBody Detail detail, HttpSession session){
        Map<String,Object> map=new HashMap<>();
        Integer invid=(Integer) session.getAttribute("invid");
        if(invid==null){
            return "index";
        }
        detail.setCreatedate(new Date());
        detail.setInvid((long)invid);
        int i = detailService.DetailAdd(detail);
        if(i>0) {//如果添加成功
            map.put("detail",detail);
            return JSON.toJSONString(map);
        }
        return null;
    }

}
