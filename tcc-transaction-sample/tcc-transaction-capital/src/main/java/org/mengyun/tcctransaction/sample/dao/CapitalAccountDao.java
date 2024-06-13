package org.mengyun.tcctransaction.sample.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mengyun.tcctransaction.sample.entity.CapitalAccount;

/**
 * Created by changming.xie on 4/2/16.
 */
@Mapper
public interface CapitalAccountDao extends BaseMapper<CapitalAccount> {

    CapitalAccount findByUserId(long userId);

    int update(CapitalAccount capitalAccount);

    int insert(CapitalAccount capitalAccount);
}
