package com.vivid.dream.mapper;

import com.vivid.dream.model.TestVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TestMapper {
    List<TestVo> selectTest();
}
