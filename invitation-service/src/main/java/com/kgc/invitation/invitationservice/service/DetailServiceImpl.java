package com.kgc.invitation.invitationservice.service;

import com.alibaba.fastjson.JSON;
import com.kgc.invitation.bean.Detail;
import com.kgc.invitation.bean.DetailExample;
import com.kgc.invitation.bean.Invitation;
import com.kgc.invitation.invitationservice.mapper.DetailMapper;
import com.kgc.invitation.service.DetailService;
import com.kgc.invitation.util.RedisUtil;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RedissonClient;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;

@Service
public class DetailServiceImpl implements DetailService {
    @Resource
    DetailMapper detailMapper;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RedissonClient redissonClient;
    //根据帖子编号查询回复，分页
    @Override
    public List<Detail> DetailListById(Integer invid) {
        DetailExample example=new DetailExample();
        DetailExample.Criteria criteria=example.createCriteria();
        criteria.andInvidEqualTo((long)invid);

        //redis
        String detkey="invid:"+invid+":info";
        Jedis jedis=redisUtil.getJedis();
        String detInfoJson=jedis.get(detkey);

        List<Detail> list=null;

        if(detInfoJson!=null){//redis有缓存
            if(detInfoJson.equals("empty")){
                return null;
            }
            list= JSON.parseArray(jedis.get(detkey),Detail.class);
        }else{//无缓存

            Lock lock = redissonClient.getLock("lock");// 声明锁
            lock.lock();//上锁
            //查询sku
            list=detailMapper.selectByExample(example);
            if(list!=null){
                //随机时间，防止缓存雪崩
                Random random=new Random();
                int i = random.nextInt(10);
                jedis.setex(detkey,i*60*10,JSON.toJSONString(list));
            }else{
                //空数据，存储5分钟，防止缓存穿透
                jedis.setex(detkey,5*6*1,"empty");
            }
            // 删除分布式锁
            lock.unlock();
        }
        jedis.close();

        return list;
    }

    //添加帖子
    @Override
    public int DetailAdd(Detail detail) {
        Integer invid=detail.getInvid().intValue();

        DetailExample example=new DetailExample();
        DetailExample.Criteria criteria=example.createCriteria();
        criteria.andInvidEqualTo((long)invid);

        int i = detailMapper.insert(detail);
        List<Detail> details=detailMapper.selectByExample(example);
        if(i>0){
            //redis
            String detkey="invid:"+invid+":info";
            Jedis jedis=redisUtil.getJedis();
            jedis.del(detkey);
            jedis.setex(detkey,1*60*10,JSON.toJSONString(details));
        }
        return i;
    }

    @Override
    public int DetailDel(Integer invid) {
        DetailExample example=new DetailExample();
        DetailExample.Criteria criteria=example.createCriteria();
        criteria.andInvidEqualTo((long)invid);


        int i = detailMapper.deleteByExample(example);
        List<Detail> details=detailMapper.selectByExample(example);
        if(i>0){
            //redis
            String detkey="invid:"+invid+":info";
            Jedis jedis=redisUtil.getJedis();
            jedis.del(detkey);
            jedis.setex(detkey,i*60*10,JSON.toJSONString(details));
        }
        return i;

    }

    @Override
    public List<Detail> pageList(Long invid, Integer pageNum, Integer pageSize) {
        DetailExample example=new DetailExample();
        DetailExample.Criteria criteria=example.createCriteria();
        criteria.andInvidEqualTo(invid);
        List<Detail> details=detailMapper.pagelist(invid,(pageNum-1)*pageSize,pageSize);
        return details;
    }

}
