package org.mengyun.tcctransaction.sample.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.mengyun.tcctransaction.sample.dao.CapitalAccountDao;
import org.mengyun.tcctransaction.sample.entity.CapitalAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Component
public class CapitalAccountDomain extends ServiceImpl<CapitalAccountDao, CapitalAccount> implements IService<CapitalAccount> {

    @Autowired
    private CapitalAccountDao capitalAccountDao;

    public CapitalAccount findByUserId(long userId) {
        return capitalAccountDao.findByUserId(userId);
    }

    public int update(CapitalAccount capitalAccount) {
        return capitalAccountDao.update(capitalAccount);
    }
}
