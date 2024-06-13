package org.mengyun.tcctransaction.sample.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by pktczwd on 2016/12/16.
 */
@Data
@EqualsAndHashCode
@TableName("ord_shop")
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private long ownerUserId;
}
