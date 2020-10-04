package com.kgc.invitation.invitationweb.controller;


import com.github.pagehelper.PageInfo;
import com.kgc.invitation.bean.Invitation;
import com.kgc.invitation.service.DetailService;
import com.kgc.invitation.service.InvitationService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Controller
public class InvitationController {
    @Reference
    InvitationService invitationService;
    @Reference
    DetailService detailService;

    //查询分页
    @GetMapping("/inv/list")
    @ResponseBody
    public Map<String,Object> invitationPageInfo(HttpSession session,@RequestParam(value = "title",required = false,defaultValue = "") String title,
                                  @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize",required = false,defaultValue = "3") Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        List<Invitation> invlist=invitationService.InvitationList(title);
        if(invlist.size()==0){
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
        Integer count=invlist.size();
        Integer pages=count%pageSize==0?count/pageSize:count/pageSize+1;
        if(pageNum<1){
            pageNum=1;
        }
        if(pageNum>pages){
            pageNum=pages;
        }
        List<Invitation> list=invitationService.pageList(title,pageNum,pageSize);
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
        session.setAttribute("title",title);
        map.put("pageinfo",list);
        return map;
    }
    //删除
    @PostMapping("/inv/del")
    @ResponseBody
    public int inbDel(Integer id,HttpSession session){
        String title=(String)session.getAttribute("title");
        int i = invitationService.InvivationDelById(id,title);
        if(i>0) {
            detailService.DetailDel(id);
            return 1; //如果删除成功
        }
        else{
            return 0;  //删除失败
        }
    }

    @RequestMapping(value = "/inv/id",method = RequestMethod.POST)
    @ResponseBody
    public String invid(Integer invid, HttpSession session){
        session.setAttribute("invid",invid);
        return "detail";
    }

    //查询分页
    @GetMapping("/inv/from")
    @ResponseBody
    public List<Invitation> invi_from(HttpSession session,@RequestParam(value = "title",required = false,defaultValue = "") String title,
                                                 @RequestParam(value = "pageNum",required = false,defaultValue = "1") Integer pageNum,
                                                 @RequestParam(value = "pageSize",required = false,defaultValue = "3") Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        List<Invitation> list=invitationService.pageList(title,pageNum,pageSize);
        return list;
    }
}
