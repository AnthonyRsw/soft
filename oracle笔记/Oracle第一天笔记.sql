select * from tab;

/*
数据库  ---> 数据库实例  --->  表空间(逻辑单位)(用户)  ---> 数据文件(物理单位)

地球    ---> 一个国家    --->  省份(逻辑单位)(公民)    ---> 山川河流(物理单位)



通常情况下,ORacle数据库只会有一个实例ORCL,

新建一个项目:
     MYSQL : 创建一个数据库,创建相应的表
     Oracle: 创建一个表空间,创建用户,用户去创建表
     
Oracle和MYSQL的差别

Oracle是多用户的, MYSQL是多数据库的

1. 遵循SQL标准
2. 不同厂商,不同的数据库产品,但是有自己的方言
3. 使用自己的方言,也能够完成相同的功能
4. Oracle安全级别要高,MYSQL开源免费

*/
/*
基本查询:
   SQL : 结构化查询语言
   
   请听题: 请说一下SQL的分类以及每类常见的操作符都有哪些
   四类:
        DDL : 数据定义语言 create alter drop truncate
        DML : 数据操纵语言 insert update delete
        DCL : 数据控制语言 安全 授权 grant revoke
        DQL : 数据查询语言 select from子句 where子句
        
   查询语句的结构:
   
   select [列名] [*] from 表名 [where 条件] [group by 分组条件] [having 过滤] [order by 排序]
*/
select * from emp;

select 1+1;  --在Oracle等于报错 ,在MYSQL中输出结果是2

/*
     dual : oracle中的虚表 ,伪表, 主要是用来补齐语法结构
     
*/
select 1+1 from dual;

select * from dual;

select 1 from emp;
--直接写一个常量比写 * 要高效
select count(1) from emp;
select count(*) from emp;

/*
       别名查询: 使用as 关键字, 可以省略
       别名中不能有特殊字符或者关键字, 如果有就加双引号
       
*/
select ename 姓名, sal 工资 from emp;

select ename "姓       名", sal 工资 from emp;

/*
      去除重复数据 distinct
      多列去除重复: 每一列都一样才能够算作是重复
*/
--单列去除重复
select distinct job from emp;

--多列去除重复的
select distinct job,deptno from emp;


--查询中四则运算
select 1+1 from dual;

--查询员工年薪  = 月薪* 12
select sal*12 from emp;


--查询员工年薪+奖金
select sal*12 + comm from emp;
--nvl 函数 : 如果参数1为null  就返回参数2
select sal*12 + nvl(comm,0) from emp;

/*
    注意: null值 , 代表不确定的 不可预知的内容 , 不可以做四则运算
*/


/*
字符串拼接:
    java : + 号拼接
    Oracle 特有的连接符: || 拼接
    
    在Oracle 中 ,双引号主要是别名的时候使用, 单引号是使用的值, 是字符
    
    concat(str1,str2) 函数, 在mysql和Oracle中都有
*/
--查询员工姓名 :  姓名:SCOTT
select ename from emp;
--使用拼接符
select '姓名:' || ename from emp;

--使用函数拼接
select concat('姓名:',ename) from emp;


/*
    条件查询 : [where后面的写法]   
        关系运算符: > >= = < <= != <>
        逻辑运算符: and or not
        其它运算符:
               like 模糊查询
               in(set) 在某个集合内
               between..and.. 在某个区间内
               is null  判断为空
               is not null 判断不为空
*/
--查询每月能得到奖金的员工信息
select * from emp where comm is not null;


--查询工资在1500--3000之间的员工信息
select * from emp where sal between 1500 and 3000;

select * from emp where sal >= 1500 and sal <= 3000;

--查询名字在某个范围的员工信息 ('JONES','SCOTT','FORD') in
select * from emp where ename in ('JONES','SCOTT','FORD');


        _   匹配单个字符
        
        如果有特殊字符, 需要使用escape转义
*/
/*
    模糊查询: like
        %   匹配多个字符
--查询员工姓名第三个字符是O的员工信息
select * from emp where ename like '__O%';

--查询员工姓名中,包含%的员工信息
select * from emp where ename like '%\%%' escape '\';

select * from emp where ename like '%#%%' escape '#';


/*
       排序 : order by 
          升序: asc    ascend
          降序: desc   descend
          
          排序注意null问题 : nulls first | last
          
          同时排列多列, 用逗号隔开
*/
--查询员工信息,按照奖金由高到低排序
select * from emp order by comm desc nulls last;

--查询部门编号和按照工资  按照部门升序排序, 工资降序排序
select deptno, sal from emp order by deptno asc, sal desc;


/*
     函数: 必须要有返回值
     
     单行函数: 对某一行中的某个值进行处理
         数值函数
         字符函数
         日期函数
         转换函数
         通用函数
     
     多行函数: 对某一列的所有行进行处理
           max()  min count sum avg
           
           1.直接忽略空值 
*/
--统计员工工资总和
select sum(sal) from emp;

--统计员工奖金总和  2200
select sum(comm) from emp;

--统计员工人数 14
select count(1) from emp;

--统计员工的平均奖金  550  错误    2200/14 =
select avg(comm) from emp;


--统计员工的平均奖金 157.
select sum(comm)/count(1) from emp;
select ceil(sum(comm)/count(1)) from emp;

update emp set ename = 'TUR%NER' where ename = 'TURNER';

select * from emp;



--数值函数
select ceil(45.926) from dual;  --46
select floor(45.926) from dual; --45
--四舍五入
select round(45.926,2) from dual; --45.93
select round(45.926,1) from dual; -- 45.9
select round(45.926,0) from dual; --46
select round(45.926,-1) from dual; --50
select round(45.926,-2) from dual; --0
select round(65.926,-2) from dual; --100

--截断
select trunc(45.926,2) from dual; --45.92
select trunc(45.926,1) from dual; -- 45.9
select trunc(45.926,0) from dual; --45
select trunc(45.926,-1) from dual; --40
select trunc(45.926,-2) from dual; --0
select trunc(65.926,-2) from dual; --0

--求余
select mod(9,3) from dual; --0
select mod(9,4) from dual; --1


--字符函数
-- substr(str1,起始索引,长度) 
--注意: 起始索引不管写 0 还是 1 都是从第一个字符开始截取
select substr('abcdefg',0,3) from dual; --abc
select substr('abcdefg',1,3) from dual; --abc

select substr('abcdefg',2,3) from dual; --bcd

--获取字符串长度 24 28
select length('abcdefg') from dual;

--去除字符左右两边的空格
select trim('  hello  ') from dual;

--替换字符串
Select replace('hello','l','a') from dual;



--日期函数
--查询今天的日期
select sysdate from dual;
--查询3个月后的今天的日期
select add_months(sysdate,3) from dual;
--查询3天后的日期
select sysdate + 3 from dual;


--查询员工入职的天数
select sysdate - hiredate from  emp;

select ceil(sysdate - hiredate) from  emp;

--查询员工入职的周数
select (sysdate - hiredate)/7 from emp;

--查询员工入职的月数
select months_between(sysdate,hiredate) from emp;

--查询员工入职的年份
select months_between(sysdate,hiredate)/12 from emp;

--转换函数  数值转字符 字符转数值  日期
--字符转数值 to_number(str) 鸡肋
select 100+'10' from dual;  --110  默认已经帮我们转换
select 100 + to_number('10') from dual; --110

--数值转字符
select to_char(sal,'$9,999.99') from emp;

select to_char(sal,'L9,999.99') from emp;
/*
to_char(1210.73, '9999.9') 返回 '1210.7' 
to_char(1210.73, '9,999.99') 返回 '1,210.73' 
to_char(1210.73, '$9,999.00') 返回 '$1,210.73' 
to_char(21, '000099') 返回 '000021' 
to_char(852,'xxxx') 返回' 354'

*/

--日期转字符 to_char()  
select to_char(sysdate,'yyyy-mm-dd hh:mi:ss') from dual;
select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual;
--只想要年
select to_char(sysdate,'yyyy') from dual;  --2017

--只想要日
select to_char(sysdate,'d') from dual; --2  代表一个星期中第几天
select to_char(sysdate,'dd') from dual;  --10  代表一个月中的第几天
select to_char(sysdate,'ddd') from dual; --100 代表一年中的第几天


select to_char(sysdate,'day') from dual;  --monday
select to_char(sysdate,'dy') from dual;   --mon  星期的简写



--字符转日期
select to_date('2017-04-10','yyyy-mm-dd') from dual;

--查询1981年 -- 1985年入职的员工信息
select * from emp where hiredate between to_date('1981','yyyy') and to_date('1985','yyyy');


/* 
      通用函数:
       nvl(参数1,参数2) 如果参数1 = null 就返回参数2
       nvl2(参数1,参数2,参数3) 如果参数1 = null ,就返回参数3, 否则返回参数2
       
       nullif(参数1,参数2) 如果参数1 = 参数2 那么就返回 null , 否则返回参数1
       
       coalesce: 返回第一个不为null的值
*/
select nvl2(null,5,6) from dual; --6;

select nvl2(1,5,6) from dual; --5;

select nullif(5,6) from dual; --5
select nullif(6,6) from dual; --null

select coalesce(null,null,3,5,6) from dual;  --3




select ceil(-12.5) from dual; --12
select floor(12.5) from dual; --12


select '  hello  ' from dual;
select * from emp;
