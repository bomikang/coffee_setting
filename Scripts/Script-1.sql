-- 제품정보
CREATE TABLE product (
	code VARCHAR(4)  NOT NULL COMMENT '제품코드', -- 제품코드
	name VARCHAR(20) NULL     COMMENT '제품명' -- 제품명
)
COMMENT '제품정보';

-- 제품정보
ALTER TABLE product
	ADD CONSTRAINT
		PRIMARY KEY (
			code -- 제품코드
		);

-- 판매정보
CREATE TABLE sale (
	code       VARCHAR(4) NOT NULL COMMENT '제품코드', -- 제품코드
	price      INT(11)    NULL     COMMENT '제품단가', -- 제품단가
	saleCnt    INT(11)    NULL     COMMENT '판매수량', -- 판매수량
	marginRate INT(11)    NULL     COMMENT '마진율' -- 마진율
)
COMMENT '판매정보';

-- 판매정보
ALTER TABLE sale
	ADD CONSTRAINT
		PRIMARY KEY (
			code -- 제품코드
		);

-- 판매세부정보
CREATE TABLE sale_detail (
	code         VARCHAR(4)         NULL COMMENT '제품코드', -- 제품코드
	sale_price   <데이터 타입 없음> NULL COMMENT '판매금액', -- 판매금액
	addTax       <데이터 타입 없음> NULL COMMENT '부가세액', -- 부가세액
	supply_price <데이터 타입 없음> NULL COMMENT '공급가액', -- 공급가액
	marginPrice  <데이터 타입 없음> NULL COMMENT '마진액' -- 마진액
)
COMMENT '판매세부정보';

-- 판매정보
ALTER TABLE sale
	ADD CONSTRAINT sale_ibfk_1 -- sale_ibfk_1
		FOREIGN KEY (
			code -- 제품코드
		)
		REFERENCES product ( -- 제품정보
			code -- 제품코드
		);

-- 판매세부정보
ALTER TABLE sale_detail
	ADD CONSTRAINT FK_sale_TO_sale_detail -- 판매정보 -> 판매세부정보
		FOREIGN KEY (
			code -- 제품코드
		)
		REFERENCES sale ( -- 판매정보
			code -- 제품코드
		);

-- v_output
CREATE
	DEFINER = `root`@`localhost` 
	SQL SECURITY DEFINER 
VIEW v_output (
	`code`,
	`name`,
	`price`,
	`saleCnt`,
	`marginRate`,
	`salePrice`,
	`addTax`,
	`supplyPrice`,
	`marginPrice`
)
AS
	select `p`.`code` AS `code`,`p`.`name` AS `name`,`s`.`price` AS `price`,`s`.`saleCnt` AS `saleCnt`,`s`.`marginRate` AS `marginRate`,(`s`.`price` * `s`.`saleCnt`) AS `salePrice`,round(((`s`.`price` * `s`.`saleCnt`) / 11),0) AS `addTax`,((`s`.`price` * `s`.`saleCnt`) - round(((`s`.`price` * `s`.`saleCnt`) / 11),0)) AS `supplyPrice`,round((((`s`.`price` * `s`.`saleCnt`) - round(((`s`.`price` * `s`.`saleCnt`) / 11),0)) * (`s`.`marginRate` / 100)),0) AS `marginPrice` from `coffee_project`.`product` `p` join `coffee_project`.`sale` `s` where (`p`.`code` = `s`.`code`) order by (`s`.`price` * `s`.`saleCnt`)
	
	
select 