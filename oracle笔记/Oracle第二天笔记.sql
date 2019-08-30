select * from bonus;

select * from salgrade;

/*
     多表查询:
       笛卡尔积: 实际上是两张表的乘积,但是在实际开发中没有太大意义
      
     格式: select * from 表1,表2   
*/
select * from emp;
select * from dept;

select * from emp, dept;

select * from emp e1, dept d1 where e1.deptno = d1.deptno;
/*
     内联接:
       隐式内联接: 
           等值内联接:   where e1.deptno = d1.deptno;
           不等值内联接:  where e1.deptno <> d1.deptno;
           自联接: 自己连接自己
       显示内联接:
           select * from 表1 inner join 表2 on 连接条件
           
           inner 关键字可以省略
*/
select * from emp e1, dept d1 where e1.deptno <> d1.deptno;

--查询员工编号,员工姓名,经理的编号,经理的姓名
select e1.empno,e1.ename,e1.mgr,m1.ename
from emp e1, emp m1 where e1.mgr= m1.empno;


--查询员工编号,员工姓名,员工的部门名称,经理的编号,经理的姓名
select e1.empno,e1.ename,d1.dname,e1.mgr,m1.ename
from emp e1, emp m1,dept d1 where e1.mgr= m1.empno and e1.deptno = d1.deptno;

--查询员工编号,员工姓名,员工的部门名称,经理的编号,经理的姓名,经理的部门名称
select e1.empno,e1.ename,d1.dname,e1.mgr,m1.ename,d2.dname
from emp e1, emp m1,dept d1,dept d2 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno 
;

--查询员工编号,员工姓名,员工的部门名称,员工的工资等级,经理的编号,经理的姓名,经理的部门名称
select e1.empno,e1.ename,d1.dname,s1.grade,e1.mgr,m1.ename,d2.dname
from emp e1, emp m1,dept d1,dept d2,salgrade s1 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno
 and e1.sal between s1.losal and s1.hisal 
;

--查询员工编号,员工姓名,员工的部门名称,员工的工资等级,经理的编号,经理的姓名,经理的部门名称,经理的工资等级
select e1.empno,e1.ename,d1.dname,s1.grade,e1.mgr,m1.ename,d2.dname,s2.grade
from emp e1, emp m1,dept d1,dept d2,salgrade s1,salgrade s2 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno
 and e1.sal between s1.losal and s1.hisal 
 and m1.sal between s2.losal and s2.hisal 
;

--查询员工编号,员工姓名,员工的部门名称,员工的工资等级,经理的编号,经理的姓名,经理的部门名称,经理的工资等级
--将工资等级 1,2,3,4 显示成 中文的 一级 二级 三级...

select e1.empno,
       e1.ename,
       d1.dname,
       case s1.grade
         when 1 then '一级'
         when 2 then '二级'
         when 3 then '三级'
         when 4 then '四级'
         else
             '五级'
         end "等级",
       e1.mgr,
       m1.ename,
       d2.dname,
       decode(s2.grade,1,'一级',2,'二级',3,'三级',4,'四级','五级') "等级"
from emp e1, emp m1,dept d1,dept d2,salgrade s1,salgrade s2 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno
 and e1.sal between s1.losal and s1.hisal 
 and m1.sal between s2.losal and s2.hisal 
;

--查询员工姓名和员工部门所处的位置
select e1.ename,d1.loc from emp e1,dept d1 where e1.deptno = d1.deptno;

select * from emp e1 inner join dept d1 on e1.deptno = d1.deptno;


/*
    外连接: (标准,通用写法)
       左外连接: left outer join 左表中所有的记录,如果右表没有对应记录,就显示空
       右外连接: right outer join 右表中的所有记录,如果左表没有对应记录,就显示空
       outer 关键字可以省略  
       
    Oracle中的外连接: (+) 实际上是如果没有对应的记录就加上空值
          select * from emp e1,dept d1 where e1.deptno = d1.deptno(+);            
*/
select * from emp e1 left outer join dept d1 on e1.deptno = d1.deptno;
insert into emp(empno,ename) values(9527,'HUAAN');
select * from emp e1,dept d1 where e1.deptno = d1.deptno(+);

select * from emp e1 right outer join dept d1 on e1.deptno = d1.deptno;
select * from emp e1,dept d1 where e1.deptno(+) = d1.deptno;



/*
       子查询: 查询语句中嵌套查询语句; 用来解决复杂的查询语句
            查询最高工资的员工信息     
         单行子查询: > >= = < <= <> !=
            
         多行子查询: in not in  >any >all exists not exists
               
               查询领导信息
*/
--查询最高工资的员工信息 
--1.查询出最高工资 --5000
select max(sal) from emp;
--2. 工资等于最高工资
select * from emp where sal = (select max(sal) from emp);


--查询出比雇员7654的工资高,同时和7788从事相同工作的员工信息
--1.雇员7654的工资 1250
select sal from emp where empno = 7654;
--2.7788从事的工作 ANALYST
select job from emp where empno = 7788;
--3.两个条件合并
select * from emp where sal > 1250 and job = 'ANALYST';

select * from emp where sal > (select sal from emp where empno = 7654) and job = (select job from emp where empno = 7788);

--查询每个部门最低工资的员工信息和他所在的部门信息
--1.查询每个部门的最低工资,分组统计
select deptno,min(sal) minsal from emp group by deptno;
--2.员工工资等于他所处部门的最低工资
select * 
from emp e1,
     (select deptno,min(sal) minsal from emp group by deptno) t1 
where e1.deptno = t1.deptno and e1.sal = t1.minsal; 
--3.查询部门相关信息
select * 
from emp e1,
     (select deptno,min(sal) minsal from emp group by deptno) t1,
     dept d1 
where e1.deptno = t1.deptno and e1.sal = t1.minsal and e1.deptno = d1.deptno; 


/*
      内联接, 单行子查询, 多行子查询
      
      in 
      not in
      any 
      all
      exists 
      
      通常情况下, 数据库中不要出现null  最好的做法加上Not null
      null值并不代表不占空间, char(100) null 100个字符
*/
--查询领导信息
--1.查询所有经理的编号
select mgr from emp;
select distinct mgr from emp;
--2.结果
select * from emp where empno in (select mgr from emp);

--查询不是领导的信息
select * from emp where empno not in (select mgr from emp);
select * from emp where empno <>all(select mgr from emp);
--正确的写法
select * from emp where empno not in (select mgr from emp where mgr is not null);

--查询出比10号部门任意一个员工薪资高的员工信息  10 20 30
select * from emp where sal >any (select sal from emp where deptno = 10);


--查询出比20号部门所有员工薪资高的员工信息 10 20 30
--1.20号最高工资 5000
select max(sal) from emp where deptno =20;
--2.员工信息
select * from emp where sal > (select max(sal) from emp where deptno =20); 

-----使用多行子查询完成上面这题
---------20号部门所有员工薪资 (800 2975 ...)
select sal from emp where deptno = 20;
---------大于集合所有的
select * from emp where sal >all(select sal from emp where deptno = 20);


/*
     exists(查询语句) : 存在的意思,判断一张表里面的记录是否存在与另外一张表中
                当作布尔值来处理:
                    当查询语句有结果的时候, 就是返回true
                                            否则返回的是false
     数据量比较大的时候是非常高效的   
*/
select * from emp where exists(select * from emp where deptno = 1234567);
select * from emp where 3=4;

select * from emp where exists(select * from emp where deptno = 20);

--查询有员工的部门的信息
select * from dept d1 where exists(select * from emp e1 where e1.deptno = d1.deptno );






--找到员工表中工资最高的前三名(降序排序)
select * from emp order by sal desc;
/*
       rownum : 伪列, 系统自动生成的一列, 用来表示行号
       
       rownum是Oracle中特有的用来表示行号的, 默认值/起始值是 1 ,在每查询出结果之后,再添加1
       
       rownum最好不能做大于号判断,可以做小于号判断
       
       SQL执行顺序
       from .. where ..group by..having .. select..rownum..order by
*/
Select rownum,e1.* from emp e1;

--查询rownum大于2的所有记录 ,
select rownum,e1.* from emp e1 where rownum > 2;  --没有任何记录

--查询rownum大于等于1的所有记录 
select rownum,e1.* from emp e1 where rownum >=1;
 
--查询rownum < 6 的所有记录
select rownum,e1.* from emp e1 where rownum < 6;

--rownum 排序
Select rownum,e1.* from emp e1 order by sal;

--找到员工表中工资最高的前三名
select e1.* from emp e1 order by sal desc;
--将上面的结果当作一张表处理,再查询
select rownum, t1.* from (select e1.* from emp e1 order by sal desc) t1;

--只要显示前三条记录
select rownum, t1.* from (select e1.* from emp e1 order by sal desc) t1 where rownum < 4;


--找到员工表中薪水大于本部门平均薪水的员工
--1.分组统计部门平均薪水
select deptno,avg(sal) avgsal from emp group by deptno;
--2.员工工资 > 本部门平均工资
select * from emp e1,(select deptno,avg(sal) avgsal from emp group by deptno) t1 
where e1.deptno = t1.deptno and e1.sal > t1.avgsal;
/*
      关联子查询 , 非关联子查询
*/
select * from emp e where sal > (select avg(sal) from emp e2 group by deptno having e.deptno=e2.deptno);



/*
   统计每年入职的员工个数
*/
select hiredate from emp;
--只显示年
select to_char(hiredate,'yyyy') from emp;
--分组统计
select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy');

select yy
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

select case yy when '1987' then cc end
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

select case yy when '1987' then cc end "1987"
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--去除行记录中的空值
select sum(case yy when '1987' then cc end) "1987"
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--统计员工的总数
select sum(cc) "TOTAL"
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--将1987 和TOTAL 合并在一起
select
      sum(cc) "TOTAL",
      sum(case yy when '1987' then cc end) "1987"
from
      (select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--显示所有年份的结果
select
      sum(cc) "TOTAL",
      sum(case yy when '1980' then cc end) "1980",
      sum(case yy when '1981' then cc end) "1981",
      sum(case yy when '1982' then cc end) "1982",
      sum(case yy when '1987' then cc end) "1987"
from
      (select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

/*
    rowid : 伪列  每行记录所存放的真实物理地址
    rownum : 行号 , 每查询出记录之后,就会添加一个行号
*/
select rowid,e.* from emp e;

--去除表中重复记录
create table p(
       name varchar2(10)
);

insert into p values('黄伟福');
insert into p values('赵洪');
insert into p values('杨华');

delete from p where 

select rowid,p.* from p;
select distinct * from p;

delete from p p1 where rowid > (select min(rowid) from p p2 where p1.name = p2.name);


/*
       rownum : 分页查询 
       在oracle中只能使用子查询来做分页查询  
*/
--查询第6 - 第10 记录
select rownum, emp.* from emp;

select rownum hanghao, emp.* from emp;

select * from (select rownum hanghao, emp.* from emp) tt where tt.hanghao between 6 and 10;



/*
       集合运算: 
         并集: 将两个查询结果进行合并
         交集
         差集
       
       所有的查询结果可能不是来自同一张表,  
         emp  2000年
              2017年 手机 详细信息 emp2017
         
*/
--工资大于1500,或者20号部门下的员工
select * from emp where sal > 1500 or deptno = 20;

--工资大于1500
select * from emp where sal > 1500;
--20号部门下的员工
select * from emp where deptno = 20;

--并集运算: union  union all
/*
        union : 去除重复的,并且排序
        union all : 不会去除重复的
*/
select * from emp where sal > 1500
union
select * from emp where deptno = 20;

select * from emp where sal > 1500
union all
select * from emp where deptno = 20;

/*
    交集运算: intersect
       
*/
--工资大于1500,并且20号部门下的员工
select * from emp where sal > 1500;
select * from emp where deptno = 20;

select * from emp where sal > 1500
intersect
select * from emp where deptno = 20;


/*
   差集运算: 两个结果相减
*/
--1981年入职员工(不包括总裁和经理)
--1981年入职员工
select * from emp where to_char(hiredate,'yyyy')='1981';

--总裁和经理
select * from emp where job = 'PRESIDENT' or job = 'MANAGER';


select * from emp where to_char(hiredate,'yyyy')='1981'
minus
select * from emp where job = 'PRESIDENT' or job = 'MANAGER';



/*
       集合运算中的注意事项:
         1.列的类型要一致
         2.按照顺序写
         3.列的数量要一致,如果不足,用空值填充
*/
select ename,sal from emp where sal > 1500
union
select ename,sal from emp where deptno = 20;
--列的类型不匹配
select ename,sal from emp where sal > 1500
union
select sal,ename from emp where deptno = 20;

--列的数量不匹配
select ename,sal,deptno from emp where sal > 1500
union
select ename,sal from emp where deptno = 20;

select ename,sal,deptno from emp where sal > 1500
union
select ename,sal,null from emp where deptno = 20;

select ename,sal,deptno from emp where sal > 1500
union
select ename,sal,66 from emp where deptno = 20;

select * from emp;
select * from dept;
