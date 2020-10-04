package com.kgc.invitation.invitationservice.mapper;

import com.github.pagehelper.PageInfo;
import com.kgc.invitation.bean.Invitation;
import com.kgc.invitation.bean.InvitationExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvitationMapper {
    int countByExample(InvitationExample example);

    int deleteByExample(InvitationExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Invitation record);

    int insertSelective(Invitation record);

    List<Invitation> selectByExample(InvitationExample example);

    Invitation selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Invitation record, @Param("example") InvitationExample example);

    int updateByExample(@Param("record") Invitation record, @Param("example") InvitationExample example);

    int updateByPrimaryKeySelective(Invitation record);

    int updateByPrimaryKey(Invitation record);

    List<Invitation> pagelist(@Param("title") String title,@Param("pageNum") int pageNum,@Param("pageSize") int pageSize);
}