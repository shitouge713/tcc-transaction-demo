package org.mengyun.tcctransaction.sample.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mengyun.tcctransaction.sample.dao.CapitalAccountDao;
import org.mengyun.tcctransaction.sample.dao.RedPacketAccountDao;
import org.mengyun.tcctransaction.sample.entity.CapitalAccount;
import org.mengyun.tcctransaction.sample.entity.RedPacketAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Component
public class RedPacketAccountDomain extends ServiceImpl<RedPacketAccountDao, RedPacketAccount> implements IService<RedPacketAccount> {

    @Autowired
    private RedPacketAccountDao redPacketAccountDao;

    public RedPacketAccount findByUserId(long userId) {
       return redPacketAccountDao.findByUserId(userId);
    }

    public int update(RedPacketAccount redPacketAccount) {
        return redPacketAccountDao.update(redPacketAccount);
    }


}
