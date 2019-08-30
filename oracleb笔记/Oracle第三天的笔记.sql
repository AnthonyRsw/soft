/*
  多表查询:
    内联接
      等值联接
      inner join
    外连接
      left outer join
      right outer join
      Oracle特有的写法(+)
      
    子查询:
      
   
*/
--内联接的结果
select * from emp e1,emp t1 where e1.mgr = t1.empno;
--t1表里面的所有记录都会显示出来, 如果t1.empno 在e1.mgr中没有对应的记录,就加空值
select * from emp e1,emp t1 where e1.mgr(+) = t1.empno;
--查询不是领导的员工编号
select empno from emp where empno not in (select mgr from emp where mgr is not null);

--获取员工的名字和部门的名字
select e.ename,d.dname from emp e,dept d where e.deptno = d.deptno;
----使用子查询的方式来完成
select ename,deptno from emp;

select ename,deptno,deptno from emp;
--关联子查询, 子查询依赖外部查询的条件
select e.ename,e.deptno,(select d.dname from dept d where d.deptno = e.deptno ) aa from emp e;


--统计薪资大于薪资最高的员工所在部门的平均工资和薪资最低的员工所在部门的平均工资的平均工资的员工信息。


/*
     Oracle体系结构:
      数据库 ---> 数据库实例ORCL ---> 表空间 (用户里面的创建表) ---> 数据文件 
      地球   ---> 中国           ---> 省份  (人民)              ---> 土地山川河流
     
     雄安新区 ---> 人(开发荒地,种地)
                
     
     创建表空间: 逻辑单位, 通常我们新建一个项目,就会去新建表空间,在表空间中创建用户来创建表
           语法:
               create tablespace 表空间的名称
               datafile '文件的路径(服务器上)'
               size 大小
               autoextend on  自动扩展
               next 每次扩展的大小
*/
--切换到system帐号下创建
--创建一个表空间 --- 汉东
create tablespace handong
datafile 'c:\handong.dbf'
size 100m
autoextend on
next 10m;


--删除表空间
drop tablespace handong;


/*
   创建用户 
     create user 用户名
     identified by 密码
     default tablespace 表空间的名称
*/
create user dakang
identified by dakang
default tablespace handong;



/*
授权   grant 角色 | 权限  to 用户
*/
grant connect to dakang;

--授予 dba的角色
grant dba to dakang;


select * from scott.emp;


/*
      创建表:
         create table 表名(
           列名  列的类型 [列的约束],
           列名  列的类型  [列的约束]      
         );
      列的类型:
         varchar ,在Oracle中,目前是支持的, 但是不保证以后还支持
         
         varchar2(长度) 可变字符长度    varchar2(10)  hello  占5个字符
         char(长度)   固定长度字符      char(10)      hello  占10个字符,用空格填充
         number(总长度,小数长度)     数字类型 --小数长度不能大于等于总长度
         
         date                   年月日时分秒 2017/4/13 9:43:49
         timestamp              时间戳, 比date类型更加精确 13-APR-17 09.44.08.272000 AM +08:00
         
         LONG/CLOB : 存放一本小说
         BLOB      : 存放电影  java 存进去,  再读取出来
     

    使用子查询的方式创建表
         
         create table 表名 as 查询语句; 
         
           注意: 只会复制表结构和表中的数据,不会复制列的约束     
                 如果查询语句有结果, 就是复制 表结构和数据
                 如果查询语句没有结果, 就是复制 表结构   
         
*/
create table test1(
   name1 varchar2(10),
   name2 char(10),
   age   number(2,3)    
);

insert into test1(name1,name2) values('hello','hello');

select * from test1 where name1 like 'hello'; --可以查询数据
select * from test1 where name2 like 'hello'; --查不出数据

insert into test1(age) values(2);

select current_date from dual;
select current_timestamp from dual;

select * from test1;

--  create table 表名 as 查询语句; 复制表     
select * from scott.emp;

create table emp as select * from scott.emp;

--如果查询语句是没有任何的结果的
select * from scott.emp where 1=2;
create table emp1 as select * from scott.emp where 1=2;


select * from emp1;


/*
    修改表:
       添加列
       修改列 vharchar2(10)
       删除列
       修改列名
       
       重命名表   
       
  SQL分类:
       DDL : 数据定义语言, 修改的结构  alter create drop truncate
       DML :　数据操纵语言 , 操作表中数据 insert update delete
       DCL : 数据控制语言 , grant     
       DQL : select
*/
create table stu(
    stuid number,
    sname varchar(10)   
);

--添加一列  
alter table stu add phone varchar2(11);

alter table stu add (
                          mobile varchar2(11),
                          sex    varchar2(2)
                     );

--修改列的类型
alter table stu modify sex varchar2(4);

--修改列名 sex --- gender
alter table stu rename column sex to gender;

--删除列
alter table stu drop column gender;

--修改表名
rename stu to student;



--删除表
drop table student;


/*
   表的五大约束
   列的约束: 约束主要是用来约束表中数据的规则
     主键约束: primary key 不能为空, 必须唯一
     非空约束
     唯一约束
     检查约束 check(条件)  在mysql中是可以写的,但是mysql直接忽略了检查约束
     
     外键约束:
          主要是用来约束从表A中的记录,必须是存在于主表B中
*/
--男,女,人妖
create table student(
    stuid number primary key,
    sname varchar2(10) unique,
    age   varchar2(10) not null,
    gender varchar2(4) check( gender in ('男','女','人妖'))
);
--主键约束违反
insert into student values(1,'张三','31','男');
insert into student values(1,'李四','31','男');
--唯一约束违反
insert into student values(1,'徐立','31','男');
insert into student values(2,'徐立','31','男');
--非空约束
insert into student values(1,'徐立',null,'男');
--检查约束
insert into student values(1,'徐立','31','男');

insert into student values(1,'徐立','31','妖');

select * from student;

/*
     商品分类,商品表
     
*/
--商品分类表
create table category(
       cid number primary key,
       cname varchar2(20)
);

--创建一个商品表
create table product(
       pid number primary key,
       pname varchar2(20),
       cno number
);

insert into category values(1,'手机数码');

insert into product values(10,'锤子',11);



--添加外键约束
alter table product add foreign key(cno) references category(cid);
insert into product values(10,'锤子',11);--插入失败

--1.首先主表中必须存在11号, 先往主表中插入数据,再往从表中插入数据
insert into category values(2,'电脑办公');
insert into product values(11,'外星人',2);


--删除Category
drop table category; --表中记录被外键关联无法删除

--强制删除表(不建议使用) : 先删除外键关联表的外键约束,然后再删除自己, 先删除product的外键约束,再删除category
drop table category cascade constraint;


--级联删除
----添加外键约束,使用级联约束  ,在删除的时候,使用级联删除
alter table product add foreign key(cno) references category(cid) on delete cascade;


insert into category values(2,'电脑办公');
insert into product values(11,'外星人',2);

--级联删除 : 首先去从表中找有没有 关联数据, 如果在从表中找到关联数据,先删除从表中关联数据,然后再删除表中的数据
delete from category where cid = 2;


select * from category;
select * from product;

drop table product;
truncate table product;
truncate table category;

/*
     插入数据:
         insert into 表名 values(所有列的值都要对应写上)
         insert into 表名(列1,列2) values(值1,值2);
         
     使用子查询插入数据
         insert into 表名 查询语句
*/
select * from emp1;

select * from emp;
--将emp中10号部门的员工信息,插入到emp1中
insert into emp1 select * from emp where deptno = 10;


/*
     更新数据
       update 表名 set 列名 = 列的值  [where 条件]
*/
update emp1 set ename='HUAAN' where ename = 'KING';
select * from emp1;

/*
     删除数据:
       delete from 表名  [where 条件]
       
       delete和truncate 区别
        
       delete:                 truncate:
        DML                    DDL
        逐条删除               先删除表再创建表
        支持事务操作           不支持事务操作,
                               执行效率要高  
       
       
*/
delete from emp1 where empno=7839;

/*
   事务: 就是一系列的操作,要么都成功,要么都失败
       四大特性: 原子性,隔离性,持久性,一致性
          
       如果不考虑隔离级别: 脏读,虚读,不可重复读
            MYSQL隔离级别: READ UNCOMMITTED , READ COMMITTED, REPEATABLE READ, SERIALIAZABLE
            ORACLE隔离级别: READ COMMITTED SERIALIZABLE READ ONLY 
                        默认隔离级别: READ COMMITTED
                        
      提交 : commit
      事务的保存点/回滚点: savepoint 保存点的名称
      回滚: rollback
*/
create table louti(
   lou number primary key    
);

insert into louti values(1);
insert into louti values(2);
insert into louti values(3);
insert into louti values(4);
insert into louti values(5);
savepoint dangban;
insert into louti values(5); --主键约束会发生异常
insert into louti values(6);
rollback to dangban
commit;


declare

begin
  insert into louti values(1);
  insert into louti values(2);
  insert into louti values(3);
  insert into louti values(4);
  insert into louti values(5);
  savepoint dangban;
  insert into louti values(5);  --这行代码会发生异常
  insert into louti values(6);
  commit;
exception  --捕获异常
  when others then
     rollback to dangban;
     commit;
end;

select * from louti;

/*
      视图: 是对查询结果的一个封装
              视图里面所有的数据,都是来自于它查询的那张表,视图本身不存储任何数据
          1.能够封装复杂的查询结果
          2.屏蔽表中的细节
       语法: 
          create [or replace] view 视图的名称 as 查询语句 [ with read only]
          
       注意: 通常不要通过视图去修改,视图创建的时候,通常要加上with read only
*/
select * from emp;

--创建一个视图
create or replace view view_test1 as select ename,job,mgr from emp;

--通过视图修改数据
update view_test1 set ename='SMITH2' where ename = 'SMITH';

--创建一个只读视图
create or replace view view_test2 as select ename,job,mgr from emp with read only;

update view_test2 set ename='SMITH3' where ename = 'SMITH2';

--视图封装复杂的查询语句
create view view_test3 as select
      sum(cc) "TOTAL",
      sum(case yy when '1980' then cc end) "1980",
      sum(case yy when '1981' then cc end) "1981",
      sum(case yy when '1982' then cc end) "1982",
      sum(case yy when '1987' then cc end) "1987"
from
      (select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;



--同义词的概念
create synonym dept for view_test3;


create synonym yuangong for view_test2;

select * from yuangong;

select * from dept;

select * from view_test3;

select * from view_test2;

/*
    序列: 生成类似于 auto_increment 这种ID自动增长 1,2,3,4,5....
       auto_increment 这个是mysql  
       
       语法:
           create sequence 序列的名称
           start with 从几开始
           increment by 每次增长多少
           maxvalue 最大值 | nomaxvalue
           minvalue 最小值 | nominvalue
           cycle | nocycle  是否循环    1,2,3,1,2,3
           cache 缓存的数量3 | nocache  1,2,3,4,5,6 
           
      如何从序列获取值
          currval : 当前值
          nextval : 下一个值
          
               注意: currval 需要在调用nextval之后才能使用      
               
               永不回头,往下取数据, 无论发生异常, 回滚   
*/
--创建一个 1,3,5,7,9......30 
create sequence seq_test1
start with 1
increment by 2
maxvalue 30
cycle
cache 10;

select seq_test1.nextval from dual;
select seq_test1.currval from dual;

--序列用的最多的一种写法
create sequence seq_test2;
select seq_test2.nextval from dual;


create sequence seq_test3
start with 1
increment by 2
maxvalue 30
minvalue 0
cycle
cache 10;

select seq_test3.nextval from dual;

/*
    索引:相当于是一本书的目录,能够提高我们的查询效率
       如果某一列,你经常用来作为查询条件,那么就有必要创建索引,数据量比较的情况
       
       语法: 
             create index 索引的名称 on 表名(列)   
        
       注意:主键约束自带主键索引, 唯一约束自带唯一索引
       
       索引原理: btree   balance Tree 平衡二叉树
       
             如果某列作为查询条件的时候,可以提高查询效率,但是修改的时候,会变慢
             
             索引创建好之后,过了一段,DBA都会去做重构索引
             
       SQL调优:
             1.查看执行计划F5
             2. 分析里面的cost 和 影响行数, 想办法降低            
*/
--五百万数据测试
create table wubaiwan(
      name varchar2(30),
      address varchar2(20) 
);

insert into wubaiwan values('')

--插入500000万条数据
declare

begin
     for i in 1..5000000 loop
       insert into wubaiwan values('姓名'||i,'地址'||i);
     end loop;
     commit;  
end;

--在没有添加索引的情况下,去查询  name='姓名3000000'  --2.985
select * from wubaiwan where name='姓名3000000';

--创建索引 name 再去查询 name='姓名3000000'
create index ind_wubaiwan on wubaiwan(name);
select * from wubaiwan where name='姓名3000000';  --0.016

--在没有添加复合索引的情况下,再去查询 name='姓名3000000' and '地址3000000'
select * from wubaiwan where name='姓名3000000' and address='地址3000000'; --0.032

--创建复合索引的情况下, 再去查询
create index ind_wubaiwan2 on wubaiwan(name,address);
select * from wubaiwan where name='姓名3000000' and address='地址3000000'; --0.015



/*
     DDL表空间操作
         创建表空间
         创建用户
         授权
         
         创建表
              子查询创建表
         修改表 : 添加列,删除列,修改列,修改列名, 修改表名
         
         约束:
             主键约束,唯一约束,非空约束,检查约束,外键约束
             
             外键约束:
               强制删除
               级联删除
             
     DML表中数据:
         插入数据
             子查询插入数据
         更新数据
         删除数据: delete 和 truncate
         
         事务操作:
               savepoint 保存点
               rollback to 保存点
          ORACLE事务隔离级别  : READ COMMITTED 
          
     视图: 就像窗户一样, 封装查询结果 , 通常视图创建只读视图
     序列: 主要是用来实现ID自增长 
     索引: 相当于是书的目录,能够提高查询效率, 原理 平衡二叉树, 每隔一段时间DBA都需要去重建索引
     同义词: create synonym 名称 for 对象的名称          

*/
/*
     PLSQL编程 : procedure Language 过程语言 Oracle对SQL的一个扩展
             让我们能够像在java中一样写 if else else if 条件, 还可以编写循环逻辑 for while
             
             declare
                --声明变量
                变量名 变量类型;
                变量名 变量类型 := 初始值;
                  vsal emp.sal%type;  --引用型的变量  
                  vrow emp%rowtype;   --声明记录型变量          
             begin
                --业务逻辑
             end;
             
             dbms_output.put_line()相当于java中 syso 
*/
declare
   i varchar2(10) := '张三';          
begin
  dbms_output.put_line(i);
end;

--查询7369的工资,并且打印出来
declare
  vsal emp.sal%type;
begin
  --将查询出的结果赋值给vsal
  select sal into vsal from emp where empno = 7369;
  
  dbms_output.put_line(vsal);
end;

--查询7369的员工信息,并且打印出来
 select * from emp where empno = 7369;

declare
  vrow emp%rowtype;      
begin
  select * into vrow from emp where empno = 7369;
  
  dbms_output.put_line('姓名:'||vrow.ename || '工资'|| vrow.sal);
end;

/*
  PL条件判断
     
     if then
     
     elsif then
       
     else 
     
     end if;
*/
--根据不同年纪,输出相关内容
declare
   age number := &aaa;
begin
  if age < 18 then
     dbms_output.put_line('小屁孩');
  elsif age>=18 and age <=24 then
     dbms_output.put_line('年轻人');
  elsif age>24 and age < 40 then
    dbms_output.put_line('老司机');
  else 
      dbms_output.put_line('老年人');    
  end if;
end;

/*
  循环操作
  while 循环
      while 条件 loop
        
      end loop;
    
  for循环
      for 变量  in [reverse] 起始值..结束值 loop
        
      end loop;
  
  loop循环  
      loop
        exit when 条件
      end loop;
      
*/
--输出1~10
declare
  i number :=1;
begin
  while i<=10 loop
    dbms_output.put_line(i);
    i := i+1;    
  end loop;
end;

--输出1~10
declare

begin
  for i in reverse 1..10 loop
    dbms_output.put_line(i);
  end loop;
end;

--输出1~10
declare
   i number :=1;
begin
   loop
     exit when i>10;
      dbms_output.put_line(i);  
     i := i+1;
   end loop;
end;

/*

   *
  ***
 *****
  ***
   *   
输出 m  
   x : [-m,m]
   y : [-m,m]
   
   输出所有满足条件的 : abs(y)+abs(x) <=m
   
   m取值
*/
--使用PLSQL输出菱形
declare
   m number := 10;
begin
   for x in -m..m loop
     for y in -m..m loop
       if abs(y) + abs(x) <= m then
         dbms_output.put('*');
       else
         dbms_output.put(' ');
       end if;      
     end loop;
     dbms_output.new_line();
   end loop;  
end;

--使用PLSQL输出三角形,只要是三个角
declare
   m number := 10;
begin
   for x in reverse -m..m loop
     for y in -m..m loop
       if abs(y) + abs(x) <= m and x>=0 then
         dbms_output.put('*');
       else
         dbms_output.put(' ');
       end if;      
     end loop;
     dbms_output.new_line();
   end loop;  
end;

