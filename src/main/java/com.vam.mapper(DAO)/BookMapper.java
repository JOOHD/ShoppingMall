package com.vam.mapper(DAO);

import java.util.List;

import com.vam.model.BookVO;
import com.vam.model.CateFilterDTO;
import com.vam.model.cateVO;
import com.vam.model.Criteria;
import com.vam.model.SelectDTO;

/**
 * 패키지의 BookMapper 인터페이스에 bookId와 bookName 값이 담긴 BookVO 객체를 
   반환해주는 메서드 선언부를 작성한다.
   -bookId를 조건으로 bookName값을 얻을 것이기 떄문에 파라미터로 int 타입의 bookId를 파라미터로 지정
   -작성한 메서드릐 목적은 '상품 이름'을 얻어내는 것이지만 뷰에 bookId, bookName(상품이름)을 한번에 전송하기
    편하도록 BookVO 객체에 담겨서 나오도록 반환값을 지정했다.

 * 클라이언트가 상품 상세 패이자 요청을 하면, 사용자가 지정한 '상품 정보'가 포함된 '상품 상세 페이지'를 전송해주어야한다.
   그렇다면 어떠한 '상품 정보'를 전송해주어야 할지 결정해야 하는데 우리가 앞서 작성해둔 "BookVO" 클래스에서 지정한 필드들의
   값을 채워서 뷰(View)로 전송해주도록 하자. 따라서 'BookVO' 객체의 값을 채워서 반환해주는 Mapper 메서드를 먼저 작성하자.		
 */
public interface BookMapper {
    
    /* 상품 검색 */
	public List<BookVO> getGoodsList(Criteria cri);
	
	/* 상품 총 갯수 */
	public int goodsGetTotal(Criteria cri);		
	
	/* 작가 id 리스트 요청 */
	public String[] getAuthorIdList(String keyword);	
	
	/* 국내 카테고리 리스트 */
	public List<CateVO> getCateCode1();
	
	/* 외국 카테고리 리스트 */
	public List<CateVO> getCateCode2();		
	
	/* 검색 대상 카테고리 리스트 */
	public String[] getCateList(Criteria cri);
	
	/* 카테고리 정보(+검색대상 갯수) */
	public CateFilterDTO getCateInfo(Criteria cri);	
	
	/* 상품 정보 */
	public BookVO getGoodsInfo(int bookId);	
	
	/* 상품 id 이름 */
	public BookVO getBookIdName(int bookId);	
	
	/* 평점순 상품 정보 */
	public List<SelectDTO> likeSelect();	
}
