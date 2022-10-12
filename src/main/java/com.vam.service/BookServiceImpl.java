package com.vam.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AdminMapper;
import com.vam.mapper.AttachMapper;
import com.vam.mapper.BookMapper;
import com.vam.model.AttachImageVO;
import com.vam.model.BookVO;
import com.vam.model.CateFilterDTO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;
import com.vam.model.SelectDTO;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BookServiceImpl implements BookService {
    
    @Autowired
    private BookMapper BookMapper;

    @Autowired
    private AttachMapper attachMapper;

    /**
     * BookServiceImpl.java에 인터페이스에서 선언한 메서드를 오버라이딩 하기앞서서 AdimnMapper 클래스의
       메서드를 사용해야 하기 때문에 아래의 코드를 추가하여 'AdminMapper' 객체를 의존성 주입해주었다.
     */
    @Autowired
    private AdminMappier adminMapper;

    /* 상품 검색 */
    @Override
    public List<BookVO> getGoodList(Criteria cri) {

        Log.info("getGoodList()......");

        String type = cri.getType();
        String[] typeArr = type.split("");
        String[] authorArr = bookMapper.getAuthorIdList(cri.getKeyword());

        if(type.equals("A") || type.equals("AT") || type.equals("ACT")) {
            if(authorArr.length == 0) {
                    return new ArrayList();
            }
        }

        for(String t : typeArr) {
            if(t.equals("A")) {
                 cri.setAuthorArr(authorArr);
            }
        }

        List<BookVO> list = bookMapper.getGoodList(cri);

        list.forEach(book -> {

            int bookId = book.getBookId();

            List<AttachImageVO> imageList = attachMapper.getAttachList(bookId);

            book.setImageList(impageList);

        });

        return list;
    }

    /* 상품 총 갯수 */
    @Override
    public int goodGetTotal(Criteria cri) {

        log.info("goodGetTotal()......");

        return bookMapper.goodsGetTotal(cri);
        
    }
}
