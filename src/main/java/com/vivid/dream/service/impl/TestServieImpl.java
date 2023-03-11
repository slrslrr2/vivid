package com.vivid.dream.service.impl;

import com.vivid.dream.mapper.TestMapper;
import com.vivid.dream.model.TestVo;
import com.vivid.dream.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServieImpl implements TestService {

    private final TestMapper testMapper;

    @Override
    public List<TestVo> test() {
        List<TestVo> testVos = testMapper.selectTest();
        return testVos;
    }
}
