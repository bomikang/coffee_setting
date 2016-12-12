
DROP DATABASE IF EXISTS coffee;

CREATE DATABASE IF not exists coffee;

-- 제품코드
CREATE TABLE coffee.product (
	code VARCHAR(4)  NOT NULL, -- 제품코드
	name VARCHAR(20) NULL,      -- 제품명
	PRIMARY KEY (code)
);

-- 제품판매
CREATE TABLE coffee.sale (
	code      VARCHAR(4) NOT NULL, -- 제품코드
	price      INTEGER    NULL,     -- 제품단가
	saleCnt    INTEGER    NULL,     -- 판매수량
	marginRate INTEGER    NULL,      -- 마진율
	PRIMARY KEY (code),
	FOREIGN KEY (code) REFERENCES product (code)
);

insert into coffee.product(code, name) values('A001', '아메리카노');
insert into coffee.product(code, name) values('A002', '카푸치노');
insert into coffee.product(code, name) values('A003', '헤이즐넛');
insert into coffee.product(code, name) values('A004', '에스프레소');
insert into coffee.product(code, name) values('B001', '딸기쉐이크');
insert into coffee.product(code, name) values('B002', '후르츠와인');
insert into coffee.product(code, name) values('B003', '팥빙수');
insert into coffee.product(code, name) values('B004', '아이스초코');

insert into coffee.sale values('A001', 4500, 150, 10);
insert into coffee.sale values('A002', 3800, 154, 15);
insert into coffee.sale values('B001', 5200, 250, 12);
insert into coffee.sale values('B002', 4300, 110, 11);

/*
판매금액 = 제품단가 * 판매수량
부가세액 = 판매금액/ 11(소수점첫자리에서 올림)
공급가액 = 판매금액-부가세액
마진액 = 공급가액*마진율
*/
select p.code 제품코드, name 제품명, price 제품단가, saleCnt 판매수량, marginRate 마진율
from product p, sale s
where p.code = s.code;

/* view table */
create view v_output as
select p.code, name, price, saleCnt, marginRate, 
		price*saleCnt salePrice, 
		round(price*saleCnt/11, 0) addTax,
		price*saleCnt-round(price*saleCnt/11, 0) supplyPrice,
		round(( (price*saleCnt-round(price*saleCnt/11, 0)) * (marginRate/100)),0) marginPrice
from product p, sale s 
where p.code = s.code
order by salePrice;


drop view v_output;


select (select count(*)+1 from v_saleprice where salePrice > p1.salePrice) as rank, p1.*
from v_output p1
order by rank;

select (select count(*)+1 from v_saleprice where marginPrice > p1.marginPrice) as rank, p1.*
from v_output p1
order by rank;


-- 계정 추가
-- create user 계정명 identified by '비밀번호';
create user choi identified by 'choi123';

-- 계정 권한추가
-- grant 권한,.. on 데이터베이스명.* to 계정@localhost;

grant select, insert, update, delete 
on haksa.*
to lee@localhost;

-- 사용자 삭제
-- mysql.user, mysql.db 에 삭제
drop user lee@localhost;

--계정과 권한 한번에
grant select, insert, update, delete 
on haksa.* to 'choi123' identified by 'rootroot';


-- export
select * 
into outfile 'D:/workspace_skill/CoffeeProject_Setting/DataFiles/product.txt' 
character set 'UTF8' 
fields TERMINATED by ',' 
LINES TERMINATED by '\n' 
from product;

select * 
into outfile 'D:/workspace_skill/CoffeeProject_Setting/DataFiles/sale.txt' 
character set 'UTF8' 
fields TERMINATED by ',' 
LINES TERMINATED by '\n' 
from sale;


-- import
LOAD DATA
INFILE 'D:/workspace_skill/CoffeeProject_Setting/DataFiles/product.txt'
INTO TABLE product
character set 'UTF8' 
fields TERMINATED by ',';

LOAD DATA
INFILE 'D:/workspace_skill/CoffeeProject_Setting/DataFiles/sale.txt'
INTO TABLE sale
character set 'UTF8' 
fields TERMINATED by ',';

