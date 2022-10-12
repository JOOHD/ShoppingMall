-- Oracle --
-- 국가 테이블 생성
create table vam_nation(
    nationId varchar2(2) primary key,
    nationName varchar2(50)
);

-- 국가 태이블 데이터 삽입
insert into vam_nation values ('01', '국내');
insert into vam_nation values ('02', '국외');

-- 작가 테이블 생성
create tablel vam_author(
    authorId number generated as identity (start with 1) primary key,
    authorName varchar2(50),
    authorId varchar2(2),
    authorIntro long,
    foreign key (nationId) references vam_nation(nationId)

    -- 행을 추가한 날짜와 수정한 날짜를 자동으로 기록하기 위해서이다.
    alter table vam_author modify regDate date default sysdate;
    alter table vam_author add updateDate date default sysdate;
    
);

commit;

-- test
delete from vam_author;
 
ALTER TABLE vam_author MODIFY(authorId GENERATED AS IDENTITY (START WITH 1));

insert into vam_author(authorName, nationId, authorIntro) values('유홍준', '01', '작가 소개입니다' );
insert into vam_author(authorName, nationId, authorIntro) values('김난도', '01', '작가 소개입니다' );
insert into vam_author(authorName, nationId, authorIntro) values('폴크루그먼', '02', '작가 소개입니다' );

insert into vam_author(authorName, nationId, authorIntro) values('작가이름', '나라코드', '작가소개' );

select * from vam_author;


-- MySQL
-- 국가 테이블 생성
create table vam_nation(
    nationId varchar(2) primary key,
    nationName varchar(50)
);
 
-- 국가 테이블 데이터 삽입
insert into vam_nation values ('01', '국내');
insert into vam_nation values ('02', '국외');
 
-- 작가 테이블 생성
create table vam_author(
    authorId int auto_increment primary key,
    authorName varchar(50),
    nationId varchar(2),
    authorIntro text,
    foreign key (nationId) references vam_nation(nationId)
    
    alter table vam_author add regDate timestamp default now();
    alter table vam_author add updateDate timestamp default now();
);

-- test
delete from vam_author;

alter table vam_author auto_increment = 1;

insert into vam_author(authorName, nationId, authorIntro) values('유홍준', '01', '작가 소개입니다' );
insert into vam_author(authorName, nationId, authorIntro) values('김난도', '01', '작가 소개입니다' );
insert into vam_author(authorName, nationId, authorIntro) values('폴크루그먼', '02', '작가 소개입니다' );    

select * from vam_author;