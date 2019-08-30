select * from bonus;

select * from salgrade;

/*
     ����ѯ:
       �ѿ�����: ʵ���������ű�ĳ˻�,������ʵ�ʿ�����û��̫������
      
     ��ʽ: select * from ��1,��2   
*/
select * from emp;
select * from dept;

select * from emp, dept;

select * from emp e1, dept d1 where e1.deptno = d1.deptno;
/*
     ������:
       ��ʽ������: 
           ��ֵ������:   where e1.deptno = d1.deptno;
           ����ֵ������:  where e1.deptno <> d1.deptno;
           ������: �Լ������Լ�
       ��ʾ������:
           select * from ��1 inner join ��2 on ��������
           
           inner �ؼ��ֿ���ʡ��
*/
select * from emp e1, dept d1 where e1.deptno <> d1.deptno;

--��ѯԱ�����,Ա������,����ı��,���������
select e1.empno,e1.ename,e1.mgr,m1.ename
from emp e1, emp m1 where e1.mgr= m1.empno;


--��ѯԱ�����,Ա������,Ա���Ĳ�������,����ı��,���������
select e1.empno,e1.ename,d1.dname,e1.mgr,m1.ename
from emp e1, emp m1,dept d1 where e1.mgr= m1.empno and e1.deptno = d1.deptno;

--��ѯԱ�����,Ա������,Ա���Ĳ�������,����ı��,���������,����Ĳ�������
select e1.empno,e1.ename,d1.dname,e1.mgr,m1.ename,d2.dname
from emp e1, emp m1,dept d1,dept d2 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno 
;

--��ѯԱ�����,Ա������,Ա���Ĳ�������,Ա���Ĺ��ʵȼ�,����ı��,���������,����Ĳ�������
select e1.empno,e1.ename,d1.dname,s1.grade,e1.mgr,m1.ename,d2.dname
from emp e1, emp m1,dept d1,dept d2,salgrade s1 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno
 and e1.sal between s1.losal and s1.hisal 
;

--��ѯԱ�����,Ա������,Ա���Ĳ�������,Ա���Ĺ��ʵȼ�,����ı��,���������,����Ĳ�������,����Ĺ��ʵȼ�
select e1.empno,e1.ename,d1.dname,s1.grade,e1.mgr,m1.ename,d2.dname,s2.grade
from emp e1, emp m1,dept d1,dept d2,salgrade s1,salgrade s2 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno
 and e1.sal between s1.losal and s1.hisal 
 and m1.sal between s2.losal and s2.hisal 
;

--��ѯԱ�����,Ա������,Ա���Ĳ�������,Ա���Ĺ��ʵȼ�,����ı��,���������,����Ĳ�������,����Ĺ��ʵȼ�
--�����ʵȼ� 1,2,3,4 ��ʾ�� ���ĵ� һ�� ���� ����...

select e1.empno,
       e1.ename,
       d1.dname,
       case s1.grade
         when 1 then 'һ��'
         when 2 then '����'
         when 3 then '����'
         when 4 then '�ļ�'
         else
             '�弶'
         end "�ȼ�",
       e1.mgr,
       m1.ename,
       d2.dname,
       decode(s2.grade,1,'һ��',2,'����',3,'����',4,'�ļ�','�弶') "�ȼ�"
from emp e1, emp m1,dept d1,dept d2,salgrade s1,salgrade s2 
where 
     e1.mgr= m1.empno 
 and e1.deptno = d1.deptno
 and m1.deptno = d2.deptno
 and e1.sal between s1.losal and s1.hisal 
 and m1.sal between s2.losal and s2.hisal 
;

--��ѯԱ��������Ա������������λ��
select e1.ename,d1.loc from emp e1,dept d1 where e1.deptno = d1.deptno;

select * from emp e1 inner join dept d1 on e1.deptno = d1.deptno;


/*
    ������: (��׼,ͨ��д��)
       ��������: left outer join ��������еļ�¼,����ұ�û�ж�Ӧ��¼,����ʾ��
       ��������: right outer join �ұ��е����м�¼,������û�ж�Ӧ��¼,����ʾ��
       outer �ؼ��ֿ���ʡ��  
       
    Oracle�е�������: (+) ʵ���������û�ж�Ӧ�ļ�¼�ͼ��Ͽ�ֵ
          select * from emp e1,dept d1 where e1.deptno = d1.deptno(+);            
*/
select * from emp e1 left outer join dept d1 on e1.deptno = d1.deptno;
insert into emp(empno,ename) values(9527,'HUAAN');
select * from emp e1,dept d1 where e1.deptno = d1.deptno(+);

select * from emp e1 right outer join dept d1 on e1.deptno = d1.deptno;
select * from emp e1,dept d1 where e1.deptno(+) = d1.deptno;



/*
       �Ӳ�ѯ: ��ѯ�����Ƕ�ײ�ѯ���; ����������ӵĲ�ѯ���
            ��ѯ��߹��ʵ�Ա����Ϣ     
         �����Ӳ�ѯ: > >= = < <= <> !=
            
         �����Ӳ�ѯ: in not in  >any >all exists not exists
               
               ��ѯ�쵼��Ϣ
*/
--��ѯ��߹��ʵ�Ա����Ϣ 
--1.��ѯ����߹��� --5000
select max(sal) from emp;
--2. ���ʵ�����߹���
select * from emp where sal = (select max(sal) from emp);


--��ѯ���ȹ�Ա7654�Ĺ��ʸ�,ͬʱ��7788������ͬ������Ա����Ϣ
--1.��Ա7654�Ĺ��� 1250
select sal from emp where empno = 7654;
--2.7788���µĹ��� ANALYST
select job from emp where empno = 7788;
--3.���������ϲ�
select * from emp where sal > 1250 and job = 'ANALYST';

select * from emp where sal > (select sal from emp where empno = 7654) and job = (select job from emp where empno = 7788);

--��ѯÿ��������͹��ʵ�Ա����Ϣ�������ڵĲ�����Ϣ
--1.��ѯÿ�����ŵ���͹���,����ͳ��
select deptno,min(sal) minsal from emp group by deptno;
--2.Ա�����ʵ������������ŵ���͹���
select * 
from emp e1,
     (select deptno,min(sal) minsal from emp group by deptno) t1 
where e1.deptno = t1.deptno and e1.sal = t1.minsal; 
--3.��ѯ���������Ϣ
select * 
from emp e1,
     (select deptno,min(sal) minsal from emp group by deptno) t1,
     dept d1 
where e1.deptno = t1.deptno and e1.sal = t1.minsal and e1.deptno = d1.deptno; 


/*
      ������, �����Ӳ�ѯ, �����Ӳ�ѯ
      
      in 
      not in
      any 
      all
      exists 
      
      ͨ�������, ���ݿ��в�Ҫ����null  ��õ���������Not null
      nullֵ��������ռ�ռ�, char(100) null 100���ַ�
*/
--��ѯ�쵼��Ϣ
--1.��ѯ���о���ı��
select mgr from emp;
select distinct mgr from emp;
--2.���
select * from emp where empno in (select mgr from emp);

--��ѯ�����쵼����Ϣ
select * from emp where empno not in (select mgr from emp);
select * from emp where empno <>all(select mgr from emp);
--��ȷ��д��
select * from emp where empno not in (select mgr from emp where mgr is not null);

--��ѯ����10�Ų�������һ��Ա��н�ʸߵ�Ա����Ϣ  10 20 30
select * from emp where sal >any (select sal from emp where deptno = 10);


--��ѯ����20�Ų�������Ա��н�ʸߵ�Ա����Ϣ 10 20 30
--1.20����߹��� 5000
select max(sal) from emp where deptno =20;
--2.Ա����Ϣ
select * from emp where sal > (select max(sal) from emp where deptno =20); 

-----ʹ�ö����Ӳ�ѯ�����������
---------20�Ų�������Ա��н�� (800 2975 ...)
select sal from emp where deptno = 20;
---------���ڼ������е�
select * from emp where sal >all(select sal from emp where deptno = 20);


/*
     exists(��ѯ���) : ���ڵ���˼,�ж�һ�ű�����ļ�¼�Ƿ����������һ�ű���
                ��������ֵ������:
                    ����ѯ����н����ʱ��, ���Ƿ���true
                                            ���򷵻ص���false
     �������Ƚϴ��ʱ���Ƿǳ���Ч��   
*/
select * from emp where exists(select * from emp where deptno = 1234567);
select * from emp where 3=4;

select * from emp where exists(select * from emp where deptno = 20);

--��ѯ��Ա���Ĳ��ŵ���Ϣ
select * from dept d1 where exists(select * from emp e1 where e1.deptno = d1.deptno );






--�ҵ�Ա�����й�����ߵ�ǰ����(��������)
select * from emp order by sal desc;
/*
       rownum : α��, ϵͳ�Զ����ɵ�һ��, ������ʾ�к�
       
       rownum��Oracle�����е�������ʾ�кŵ�, Ĭ��ֵ/��ʼֵ�� 1 ,��ÿ��ѯ�����֮��,�����1
       
       rownum��ò��������ں��ж�,������С�ں��ж�
       
       SQLִ��˳��
       from .. where ..group by..having .. select..rownum..order by
*/
Select rownum,e1.* from emp e1;

--��ѯrownum����2�����м�¼ ,
select rownum,e1.* from emp e1 where rownum > 2;  --û���κμ�¼

--��ѯrownum���ڵ���1�����м�¼ 
select rownum,e1.* from emp e1 where rownum >=1;
 
--��ѯrownum < 6 �����м�¼
select rownum,e1.* from emp e1 where rownum < 6;

--rownum ����
Select rownum,e1.* from emp e1 order by sal;

--�ҵ�Ա�����й�����ߵ�ǰ����
select e1.* from emp e1 order by sal desc;
--������Ľ������һ�ű���,�ٲ�ѯ
select rownum, t1.* from (select e1.* from emp e1 order by sal desc) t1;

--ֻҪ��ʾǰ������¼
select rownum, t1.* from (select e1.* from emp e1 order by sal desc) t1 where rownum < 4;


--�ҵ�Ա������нˮ���ڱ�����ƽ��нˮ��Ա��
--1.����ͳ�Ʋ���ƽ��нˮ
select deptno,avg(sal) avgsal from emp group by deptno;
--2.Ա������ > ������ƽ������
select * from emp e1,(select deptno,avg(sal) avgsal from emp group by deptno) t1 
where e1.deptno = t1.deptno and e1.sal > t1.avgsal;
/*
      �����Ӳ�ѯ , �ǹ����Ӳ�ѯ
*/
select * from emp e where sal > (select avg(sal) from emp e2 group by deptno having e.deptno=e2.deptno);



/*
   ͳ��ÿ����ְ��Ա������
*/
select hiredate from emp;
--ֻ��ʾ��
select to_char(hiredate,'yyyy') from emp;
--����ͳ��
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

--ȥ���м�¼�еĿ�ֵ
select sum(case yy when '1987' then cc end) "1987"
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--ͳ��Ա��������
select sum(cc) "TOTAL"
from 
(select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--��1987 ��TOTAL �ϲ���һ��
select
      sum(cc) "TOTAL",
      sum(case yy when '1987' then cc end) "1987"
from
      (select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

--��ʾ������ݵĽ��
select
      sum(cc) "TOTAL",
      sum(case yy when '1980' then cc end) "1980",
      sum(case yy when '1981' then cc end) "1981",
      sum(case yy when '1982' then cc end) "1982",
      sum(case yy when '1987' then cc end) "1987"
from
      (select  to_char(hiredate,'yyyy') yy,count(1) cc from emp group by  to_char(hiredate,'yyyy')) tt;

/*
    rowid : α��  ÿ�м�¼����ŵ���ʵ�����ַ
    rownum : �к� , ÿ��ѯ����¼֮��,�ͻ����һ���к�
*/
select rowid,e.* from emp e;

--ȥ�������ظ���¼
create table p(
       name varchar2(10)
);

insert into p values('��ΰ��');
insert into p values('�Ժ�');
insert into p values('�');

delete from p where 

select rowid,p.* from p;
select distinct * from p;

delete from p p1 where rowid > (select min(rowid) from p p2 where p1.name = p2.name);


/*
       rownum : ��ҳ��ѯ 
       ��oracle��ֻ��ʹ���Ӳ�ѯ������ҳ��ѯ  
*/
--��ѯ��6 - ��10 ��¼
select rownum, emp.* from emp;

select rownum hanghao, emp.* from emp;

select * from (select rownum hanghao, emp.* from emp) tt where tt.hanghao between 6 and 10;



/*
       ��������: 
         ����: ��������ѯ������кϲ�
         ����
         �
       
       ���еĲ�ѯ������ܲ�������ͬһ�ű�,  
         emp  2000��
              2017�� �ֻ� ��ϸ��Ϣ emp2017
         
*/
--���ʴ���1500,����20�Ų����µ�Ա��
select * from emp where sal > 1500 or deptno = 20;

--���ʴ���1500
select * from emp where sal > 1500;
--20�Ų����µ�Ա��
select * from emp where deptno = 20;

--��������: union  union all
/*
        union : ȥ���ظ���,��������
        union all : ����ȥ���ظ���
*/
select * from emp where sal > 1500
union
select * from emp where deptno = 20;

select * from emp where sal > 1500
union all
select * from emp where deptno = 20;

/*
    ��������: intersect
       
*/
--���ʴ���1500,����20�Ų����µ�Ա��
select * from emp where sal > 1500;
select * from emp where deptno = 20;

select * from emp where sal > 1500
intersect
select * from emp where deptno = 20;


/*
   �����: ����������
*/
--1981����ְԱ��(�������ܲú;���)
--1981����ְԱ��
select * from emp where to_char(hiredate,'yyyy')='1981';

--�ܲú;���
select * from emp where job = 'PRESIDENT' or job = 'MANAGER';


select * from emp where to_char(hiredate,'yyyy')='1981'
minus
select * from emp where job = 'PRESIDENT' or job = 'MANAGER';



/*
       ���������е�ע������:
         1.�е�����Ҫһ��
         2.����˳��д
         3.�е�����Ҫһ��,�������,�ÿ�ֵ���
*/
select ename,sal from emp where sal > 1500
union
select ename,sal from emp where deptno = 20;
--�е����Ͳ�ƥ��
select ename,sal from emp where sal > 1500
union
select sal,ename from emp where deptno = 20;

--�е�������ƥ��
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
