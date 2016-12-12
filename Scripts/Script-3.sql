-- 제품정보
CREATE TABLE product (
	code VARCHAR(4)  NOT NULL,
	name VARCHAR(20) null,
	primary key (code)
);

-- 판매정보
CREATE TABLE sale (
	code       VARCHAR(4) NOT NULL, -- 제품코드
	price      INT(11)    NULL, -- 제품단가
	saleCnt    INT(11)    NULL, -- 판매수량
	marginRate INT(11)    null, -- 마진율
	primary key (code),
	FOREIGN KEY (code) REFERENCES product (code)
);

-- 판매세부정보
CREATE TABLE sale_detail (
	code         VARCHAR(4)  NULL , -- 제품코드
	sale_price   INT(11) NULL, -- 판매금액
	addTax       INT(11) NULL, -- 부가세액
	supply_price INT(11) NULL, -- 공급가액
	marginPrice  INT(11) null, -- 마진액
	FOREIGN KEY (code) REFERENCES product (code)
	on delete cascade
);


insert into coffee.product(code, name) values('A001', '아메리카노');
insert into coffee.product(code, name) values('A002', '카푸치노');
insert into coffee.product(code, name) values('A003', '헤이즐넛');
insert into coffee.product(code, name) values('A004', '에스프레소');
insert into coffee.product(code, name) values('B001', '딸기쉐이크');
insert into coffee.product(code, name) values('B002', '후르츠와인');
insert into coffee.product(code, name) values('B003', '팥빙수');
insert into coffee.product(code, name) values('B004', '아이스초코');


CREATE TRIGGER tri_sale_after_insert_detail
AFTER INSERT
ON coffee.sale
FOR EACH ROW
begin
	set @saleprice=NEW.price*new.salecnt,
	@addtax=ceil(NEW.price*new.salecnt/11),
	@supPrice=@saleprice - @addtax,
	@marPrice=@supPrice*(new.marginRate/100);
    INSERT INTO sale_detail(code, sale_price, addTax, supply_price, marginPrice) 
    values (new.code, @saleprice, @addtax, @supPrice, @marPrice);
END;



select code 제품코드, price 제품단가, saleCnt 판매수량, marginRate 마진율 from sale;
select code 제품코드, sale_price 판매금액, addTax 부가세액, supply_price 공급가액, marginPrice 마진액 from sale_detail;

insert into coffee.sale values('A001', 4500, 150, 10);
insert into coffee.sale values('A002', 3800, 140, 15);
insert into coffee.sale values('B001', 5200, 250, 12);
insert into coffee.sale values('B002', 4300, 110, 11);

delete from sale;

select * from sale_detail;


