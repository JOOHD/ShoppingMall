/**
1.데이터베이스 테이블 생성 및 VO 생성
-회원가입 기능 구현을 위해서 기본적으로 필요로 회원 테이블 (book_member)와 테이블의 VO(Value Object)
 를 생성하고 Oracle과 MySQL 각각 모두 사용햇 진행, 순서는 Oracle 테이블 생성, MySQL 테이블 생성, VO생성 순으로 진행
 회원테이블의 이름은 'book_member'이고, VO의 이름은 'MemberVO' 로 하였다.

2.MySQL 테이블 생성
 - 아이디(memberId), 비밀번호(memberPw), 회원 이름(memberName), 회원 메일(memberMail), 
   우편번호(memberAddr1), 사는 지역(memberAddr2), 상세주소(memberAddr3), 관리자 여부(adminCk), 
   등록 날짜(regDate), 회원 돈(money), 회원 포인트(point)로 구성하였습니다.

 - 관리자 여부는 int형으로 하였으며 0은 일반사용자, 1은 관리자로 하여서 로그인한 회원이
    일반 회원인지 관리자인지 구분하기 위하여 삽입하였다.
 
 - 몇몇 데이터 정보는 필수 입력이 아니라고 생각하지만 편의를 위해서 모든 정보가 NULL이 불가능하도록 하였다. 
 */
CREATE TABLE BOOK_MEMBER(
  memberId VARCHAR(50),
  memberPw VARCHAR(100) NOT NULL,
  memberName VARCHAR(30) NOT NULL,
  memberMail VARCHAR(100) NOT NULL,
  memberAddr1 VARCHAR(100) NOT NULL,
  memberAddr2 VARCHAR(100) NOT NULL,
  memberAddr3 VARCHAR(100) NOT NULL,
  adminCk int NOT NULL,
  regDate DATE NOT NULL,
  money int NOT NULL,
  point int NOT NULL,
  PRIMARY KEY(memberId)
);
insert into book_member values('admin', 'admin', 'admin', 'admin', 'admin', 'admin', 'admin', 1, sysdate(), 1000000, 1000000);

/**로그인 기능 구현(쿼리문, mapper,service)
  * 목표
    1.아이디, 비번 입력 후 서버 제출
    2.db요청하여 아이디, 비번 일치 여부 확인
    3.일치시 메인 페이지 이동
    4.불일치시 로그인 페이지 이동
    이번 포스팅에선 "2.db 요청하여 아이디 비번 일치 여부 확인"을 위해 사용될 쿼리문, mapper 인터페이스, service 인터페이스, 클래스 작성
  * 순서
    1.쿼리문 작성 & MemberMapper.xml 작성
    2.MemberMapper.java 인터페이스 작성
    3.mapper 로그인 메서드 테스트
    4.MemberService.java 인터페이스, 클래스 작성  

    로그인에 사용할 쿼리문은 제출받은 로그인, 비밀번호 데이터와 비교하여 일치하는 데이터가 있을 시 해당 아이디의 정보를 반환하고, 없을 시 null
    반환하도록 할 것. 쿼리문은 MemberMapper.xml에 작성해야 하는데 하기 전 쿼리문이 정상적으로 실행이 되는지 sqldeveloper(Oracle DB)에서 먼저
    작성하여 실행해보자
  */

SELECT MEMBERID, MEMBERNAME, ADMINCK, MONEY, POINT FROM book_member where MEMBERID = "테스트할 아이디" AND MEMBERPW = "테스트할 비밀번호";

/**
 테스트를 한 쿼리문을 사용하여 MemberMapper.xml에 쿼리문을 작성한다. 해당 쿼리 문의 아아디, 비밀번호 파라미터는 MemberVO 클래스의 memberId, memberPw
 변수를 삽입한다. 해당 쿼리문을 요청하는 MemberMapper.java의 로그인 메서드가 아이디, 비밀번호를 담고 있는 MemberVO 객체를 파라미터로 사용하기 때문
 

