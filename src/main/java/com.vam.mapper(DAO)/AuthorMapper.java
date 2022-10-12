package com.vam.mapper;

import java.util.List;

import com.vam.model.AuthorVO;
import com.vam.model.Criteria;

/*  작가 정보 관련 쿼리 메서드만 분리하여 관리하기 위해서 com.vam.mapper. 패키지에 
    AuthorMapper.java 인터페이스를 생성하였다. 해당 인터페이스에 작가 등록 쿼리를 실행하는
    메서드 작성

	작가 데이터 목록 쿼리를 실행하는 메서드를 선언한다.

	여러 작가의 데이터를 반환받아야 하기 때문에 리턴 타입으로 List 자료 구조를 지정하였습니다.

	vam_author 테이블의 총 행의 개수를 구하는 쿼리를 호출하는 메서드를 선언한다. 
	총 개수 값을 반환받아야 하기 때문에 메서드 선언부의 리턴 타입은 int이다.
*/

public interface AuthorMapper {

	/* 작가 등록 */
	public void authorEnroll(AuthorVO author);
	
	/* 작가 목록 */
	//List에 저장될 데이터가 AuthorVO(작가 정보) 임을 명시하기 위해 AuthorVO 제네릭을 선언하였습니다.
	public List<AuthorVO> authorGetList(Criteria cri);
	
	/* 작가 총 수 */
	// 그리고 조건문에 사용할 keyword 데이터를 전달받기 위해 파라미터로 Criteria 클래스를 부여한다.
	public int authorGetTotal(Criteria cri);
	
	/* 작가 상세 */
	public AuthorVO authorGetDetail(int authorId);	
	
	/* 작가 정보 수정 */
	public int authorModify(AuthorVO author);	
	
	/* 작가 정보 삭제 */
	public int authorDelete(int authorId);	
	
}