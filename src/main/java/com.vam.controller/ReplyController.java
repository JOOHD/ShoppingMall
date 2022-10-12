package com.vam.controller;

@RestController // 댓글 요청 처리의 경우 전부 뷰를 만들지 않고 http body 바로 데이터를 담아 반환하려고
@RequestMapping("/reply")
public class ReplyController {
    
    @Autowired  // ReplyService 객체를 의존성 주입
    private ReplyService replyService;

    /**댓글 등록 요청을 처리하는 URL 매핑 메서드를 작성한다.
        -URL의 경로는 "/enroll"로 지정
        -반환 값이 없도록 void를 지정하였댜.(성공 여부를 알리는 반환 값을 주는 게 더 좋은 판단이겠지만 단순한 구현을 위해 void지정)
        -댓글 등록 관련 데이터를 모두 속성으로 가지고 있는 ReplyDTO를 파라미터로 지정
        -댓글 등록 Service 메서드를 구현부에 호출한다.
     */

    /* 댓글등록 */
    @PostMapping("/enroll")
    public void enrollReplyPOST(ReplyDTO dto) {
        replyService.enrollReply(dto);
    }

}
