package com.xiaoyu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoyu.entity.SearchHotwordPO;
import com.xiaoyu.mapper.SearchHotMapper;
import com.xiaoyu.service.SearchService;
import com.xiaoyu.vo.search.SearchStatsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    SearchHotMapper searchHotMapper;

    @Override
    public List<SearchStatsVO> searchHot(){
        List<SearchHotwordPO> poList=searchHotMapper.selectList(
                new LambdaQueryWrapper<SearchHotwordPO>()
                        .eq(SearchHotwordPO::getIsHot,1)
                        .orderByDesc(SearchHotwordPO::getSearchCnt)
                        .last("limit 10")
        );
        List<SearchStatsVO> voList=new ArrayList<SearchStatsVO>();
        if(poList!=null&&poList.size()>0){
            for(SearchHotwordPO po:poList){
                SearchStatsVO vo=new SearchStatsVO();
                BeanUtils.copyProperties(po,vo);
                voList.add(vo);
            }
        }
        return voList;
    }
}
