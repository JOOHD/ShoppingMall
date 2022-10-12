package com.vam.mapper(DAO);

import com.vam.model.ReplyDTO;

public interface ReplyMapper {
    
    /* 댓글 등록 
        댓글 테이블에 데이터를 등록하는 Mapper 메서드를 선언부를 추가한다. 
        데이터 등록 쿼리 성공 시 1을 반환하도록 반환 타입을 int를 지정하였다.
        등록할 댓글 정보가 담길 수 있는 ReplyDTO타입을 파라미터로 지정하였다.
    */

    public int enrollReply(ReplyDTO dto);
}
