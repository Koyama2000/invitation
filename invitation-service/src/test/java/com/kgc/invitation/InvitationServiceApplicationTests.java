package com.kgc.invitation;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.kgc.invitation.bean.Detail;
import com.kgc.invitation.bean.DetailExample;
import com.kgc.invitation.bean.Invitation;
import com.kgc.invitation.invitationservice.mapper.DetailMapper;
import com.kgc.invitation.invitationservice.mapper.InvitationMapper;
import com.kgc.invitation.service.DetailService;
import com.kgc.invitation.service.InvitationService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class InvitationServiceApplicationTests {
    @Resource
    InvitationMapper invitationMapper;
    @Reference
    DetailService detailService;
    @Reference
    InvitationService invitationService;
    @Resource
    JestClient jestClient;

    @Test
    void contextLoads() {
        List<Invitation> allinv = invitationMapper.selectByExample(null);
        System.out.println("invlist:"+allinv);
        List<Invitation> invInfos=new ArrayList<>();
        for (Invitation inv : allinv) {
            Invitation invs = new Invitation();
            BeanUtils.copyProperties(inv,invs);
            invInfos.add(invs);
        }
        System.out.println(invInfos);
        for (Invitation inv : invInfos) {
            Index index=new Index.Builder(inv).index("invitation").type("invitationinfo").id(inv.getId()+"").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
