package org.mengyun.tcctransaction.sample.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.pankai.tcctransaction.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = "健康监测", consumes = "application/json")
public class HealthController {

    @GetMapping("/health")
    @ApiOperation("健康监测")
    public Result health() {
        log.info("cap-health-successfully");
        return Result.success("cap-health-successfully");
    }

}
