package com.vam.service;

import org.springframework.beans.factory.anntation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.ReplyMapper;
import com.vam.model.ReplyDTO;

@Service
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyMapper replyMapper;

    /* 댓글 등록 
        인터페이스에서 선언한 메서드를 오버라이딩하여 구현부를 완성해준다. 
        구현부에는 댓글 등록, Mapper 메서드를 호출하여 반환하는 코드를 작성한다.
    */
    @Override
    public int enrollReply(ReplyDTO dto) {

        int result = replyMapper.enrollReply(dto);

        return result;
    }
}
