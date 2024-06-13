package org.mengyun.tcctransaction.sample.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.RedPacketAccount;

/**
 * Created by changming.xie on 4/2/16.
 */
@Mapper
public interface RedPacketAccountDao extends BaseMapper<RedPacketAccount> {

    RedPacketAccount findByUserId(long userId);

    int update(RedPacketAccount redPacketAccount);
}
