package com.vam.service;

import java.lang.System.LoggerFinder;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AuthorMapper;
import com.vam.model.AuthorVO;
import com.vam.model.Criteria;

/*
  Service 인터페이스에서 선언한 메서드를 오버라이딩 하여 구현한다. 해당 메서드는 Mapper의 authorGetList()
  메서드를 호출하고 List를 반환받아야 하기 때문에 return에 Mapper의 메서드를 호출한다. 구현부에는 해당 메서드가 
  사용되는 것을 확인할 수 있도록 콘솔에 메세지를 출력하는 Log.info() 메서드를 작성하였따.

  log.info() 메서드를 사용하기 위해 클래스 상단에 아래의 변수를 선언 및 초기화하였다.
  (Lombok을 사용하는 경우 @log4j 어노테잇뎐을 선언하면 된다.)
 */
@Service
public class AuthorServiceImpl implements AuthorService {

    private static final Logger log = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Autowired
    AuthorMapper authorMapper;

    /* 작가 등록 */
    @Override
    public void authorEnroll(AuthorVO author) throws Exception {

        authorMapper.authorEnroll(author);

    }

    /* 작가 목록 */
    @Override
    public List<AuthorVO> authorGetList(Criteria cri) throws Exception {

        log.info("(service)authorGetList()....." + cri);

        return authorMapper.authorGetList(cri);
    }

    /* 작가 총 수 */
    @Override
    public int authorGetTotal(Criteria cri) throws Exception {

        log.info("(service)authorGetTotal()....." + cri);

        return authorMapper.authorGetTotal(cri);
    }

    /* 작가 상세 페이지 */
    @Override
    public AuthorVO authorGetDetail(int authorId) throws Exception {

        log.info("authorGetDetail....." + authorId);

        return authorMapper.authorGetDetail(authorId);
    }

    /* 작가 정보 수정 */
    @Override
    public int authorModify(AuthorVO author) throws Exception {

        log.info("(service) authorModify....." + author);

        return authorMapper.authorModify(author);
    }

    /* 작가 정보 삭제 */
    @Override
    public int authorDelete(int authorId) {

        log.info("authorDelete....");

        return authorMapper.authorDelete(authorId);
    }
}