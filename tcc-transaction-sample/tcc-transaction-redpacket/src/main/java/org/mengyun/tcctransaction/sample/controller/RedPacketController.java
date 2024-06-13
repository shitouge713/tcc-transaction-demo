package org.mengyun.tcctransaction.sample.controller;

import org.mengyun.tcctransaction.sample.service.RedPacketTradeOrderService;
import org.mengyun.tcctransaction.sample.request.RedPacketTradeOrderRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pktczwd on 2016/12/26.
 */
@Controller
public class RedPacketController {

    @Autowired
    private RedPacketTradeOrderService redPacketTradeOrderService;

    @RequestMapping(value = "/recordRed", method = RequestMethod.POST)
    @ResponseBody
    public String recordRed(@RequestBody RedPacketTradeOrderRecordRequest request) {
        return redPacketTradeOrderService.record(request.getTransactionContext(), request.getRedPacketTradeOrderDto());
    }
}
