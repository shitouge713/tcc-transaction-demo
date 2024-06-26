package org.mengyun.tcctransaction.sample.controller;

import org.mengyun.tcctransaction.sample.service.CapitalTradeOrderService;
import org.mengyun.tcctransaction.sample.request.CapitalTradeOrderRecordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by pktczwd on 2016/12/19.
 */
@Controller
public class CapitalController {

    @Autowired
    private CapitalTradeOrderService capitalTradeOrderService;

    @RequestMapping(value = "/recordCap", method = RequestMethod.POST)
    @ResponseBody
    public String recordCap(@RequestBody CapitalTradeOrderRecordRequest request) {
        return capitalTradeOrderService.record(request.getTransactionContext(), request.getCapitalTradeOrderDto());
    }
}
