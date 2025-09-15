package com.xiaoyu.controller.search;

import com.xiaoyu.dto.search.SearchDTO;
import com.xiaoyu.result.Result;
import com.xiaoyu.service.SearchService;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@Slf4j
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping
    public Result search(SearchDTO searchDTO) {
        log.info("search");

        return Result.success();
    }

}
