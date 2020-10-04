package com.kgc.invitation.invitationservice.service;

import com.alibaba.fastjson.JSON;
import com.kgc.invitation.bean.Invitation;
import com.kgc.invitation.bean.InvitationExample;
import com.kgc.invitation.invitationservice.mapper.InvitationMapper;
import com.kgc.invitation.service.InvitationService;
import com.kgc.invitation.util.RedisUtil;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.Lock;

@Service
public class InvitationServiceImpl implements InvitationService {
    @Resource
    InvitationMapper invitationMapper;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RedissonClient redissonClient;
    @Resource
    JestClient jestClient;
    @Resource
    EsService esService;
    //根据帖子标题模糊查询，分页
    @Override
    public List<Invitation> InvitationList(String title) {
        List<Invitation> list=new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        if(title!=null&&title.isEmpty()==false){
            MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("title",title);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("createdate",SortOrder.DESC);

        //高亮
//        HighlightBuilder highlightBuilder=new HighlightBuilder();
//        highlightBuilder.field("assetname");
//        highlightBuilder.preTags("<span style='color:red;'>");
//        highlightBuilder.postTags("</span>");
//        searchSourceBuilder.highlighter(highlightBuilder);

        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("invitation").addType("invitationinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Invitation,Void>> hits=searchResult.getHits(Invitation.class);
            for (SearchResult.Hit<Invitation,Void> hit: hits){

                Invitation invi=hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                if (highlight!=null){
//                    String assetname = highlight.get("assetname").get(0);
                    //使用高亮的skuName替换原来的skuName
//                    assets.setAssetname(assetname);
                }
                list.add(invi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    //根据Id删除
    @Override
    public int InvivationDelById(Integer id,String title) {
        int i = invitationMapper.deleteByPrimaryKey((long) id);
        String index=String.valueOf(id);
        if(i>0){
            try {
                esService.deleteData(index,"invitation","invitationinfo");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    /**
     * es分页
     * @param title
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Invitation> pageList(String title,Integer pageNum, Integer pageSize) {
        List<Invitation> list=new ArrayList<>();

        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();

        if(title!=null&&title.isEmpty()==false){
            MatchQueryBuilder matchQueryBuilder=new MatchQueryBuilder("title",title);
            boolQueryBuilder.must(matchQueryBuilder);
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("createdate",SortOrder.DESC);
        searchSourceBuilder.from((pageNum-1)*pageSize);
        searchSourceBuilder.size(pageSize);
        //高亮
//        HighlightBuilder highlightBuilder=new HighlightBuilder();
//        highlightBuilder.field("assetname");
//        highlightBuilder.preTags("<span style='color:red;'>");
//        highlightBuilder.postTags("</span>");
//        searchSourceBuilder.highlighter(highlightBuilder);

        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("invitation").addType("invitationinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Invitation,Void>> hits=searchResult.getHits(Invitation.class);
            for (SearchResult.Hit<Invitation,Void> hit: hits){

                Invitation invi=hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                if (highlight!=null){
//                    String assetname = highlight.get("assetname").get(0);
                    //使用高亮的skuName替换原来的skuName
//                    assets.setAssetname(assetname);
                }
                list.add(invi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public  List<Invitation> getEs(){
        List<Invitation> Invitationlist=new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder=new BoolQueryBuilder();
        String dsl=searchSourceBuilder.toString();
        Search search=new Search.Builder(dsl).addIndex("invitation").addType("invitationinfo").build();
        try {
            SearchResult searchResult=jestClient.execute(search);
            List<SearchResult.Hit<Invitation,Void>> hits=searchResult.getHits(Invitation.class);
            for (SearchResult.Hit<Invitation,Void> hit: hits){
                Invitation InvitationInfo=hit.source;
                Invitationlist.add(InvitationInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Invitationlist;
    }

    public void setEs(){
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
