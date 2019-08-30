select * from tab;

/*
���ݿ�  ---> ���ݿ�ʵ��  --->  ��ռ�(�߼���λ)(�û�)  ---> �����ļ�(����λ)

����    ---> һ������    --->  ʡ��(�߼���λ)(����)    ---> ɽ������(����λ)



ͨ�������,ORacle���ݿ�ֻ����һ��ʵ��ORCL,

�½�һ����Ŀ:
     MYSQL : ����һ�����ݿ�,������Ӧ�ı�
     Oracle: ����һ����ռ�,�����û�,�û�ȥ������
     
Oracle��MYSQL�Ĳ��

Oracle�Ƕ��û���, MYSQL�Ƕ����ݿ��

1. ��ѭSQL��׼
2. ��ͬ����,��ͬ�����ݿ��Ʒ,�������Լ��ķ���
3. ʹ���Լ��ķ���,Ҳ�ܹ������ͬ�Ĺ���
4. Oracle��ȫ����Ҫ��,MYSQL��Դ���

*/
/*
������ѯ:
   SQL : �ṹ����ѯ����
   
   ������: ��˵һ��SQL�ķ����Լ�ÿ�ೣ���Ĳ�����������Щ
   ����:
        DDL : ���ݶ������� create alter drop truncate
        DML : ���ݲ������� insert update delete
        DCL : ���ݿ������� ��ȫ ��Ȩ grant revoke
        DQL : ���ݲ�ѯ���� select from�Ӿ� where�Ӿ�
        
   ��ѯ���Ľṹ:
   
   select [����] [*] from ���� [where ����] [group by ��������] [having ����] [order by ����]
*/
select * from emp;

select 1+1;  --��Oracle���ڱ��� ,��MYSQL����������2

/*
     dual : oracle�е���� ,α��, ��Ҫ�����������﷨�ṹ
     
*/
select 1+1 from dual;

select * from dual;

select 1 from emp;
--ֱ��дһ��������д * Ҫ��Ч
select count(1) from emp;
select count(*) from emp;

/*
       ������ѯ: ʹ��as �ؼ���, ����ʡ��
       �����в����������ַ����߹ؼ���, ����оͼ�˫����
       
*/
select ename ����, sal ���� from emp;

select ename "��       ��", sal ���� from emp;

/*
      ȥ���ظ����� distinct
      ����ȥ���ظ�: ÿһ�ж�һ�����ܹ��������ظ�
*/
--����ȥ���ظ�
select distinct job from emp;

--����ȥ���ظ���
select distinct job,deptno from emp;


--��ѯ����������
select 1+1 from dual;

--��ѯԱ����н  = ��н* 12
select sal*12 from emp;


--��ѯԱ����н+����
select sal*12 + comm from emp;
--nvl ���� : �������1Ϊnull  �ͷ��ز���2
select sal*12 + nvl(comm,0) from emp;

/*
    ע��: nullֵ , ����ȷ���� ����Ԥ֪������ , ����������������
*/


/*
�ַ���ƴ��:
    java : + ��ƴ��
    Oracle ���е����ӷ�: || ƴ��
    
    ��Oracle �� ,˫������Ҫ�Ǳ�����ʱ��ʹ��, ��������ʹ�õ�ֵ, ���ַ�
    
    concat(str1,str2) ����, ��mysql��Oracle�ж���
*/
--��ѯԱ������ :  ����:SCOTT
select ename from emp;
--ʹ��ƴ�ӷ�
select '����:' || ename from emp;

--ʹ�ú���ƴ��
select concat('����:',ename) from emp;


/*
    ������ѯ : [where�����д��]   
        ��ϵ�����: > >= = < <= != <>
        �߼������: and or not
        ���������:
               like ģ����ѯ
               in(set) ��ĳ��������
               between..and.. ��ĳ��������
               is null  �ж�Ϊ��
               is not null �жϲ�Ϊ��
*/
--��ѯÿ���ܵõ������Ա����Ϣ
select * from emp where comm is not null;


--��ѯ������1500--3000֮���Ա����Ϣ
select * from emp where sal between 1500 and 3000;

select * from emp where sal >= 1500 and sal <= 3000;

--��ѯ������ĳ����Χ��Ա����Ϣ ('JONES','SCOTT','FORD') in
select * from emp where ename in ('JONES','SCOTT','FORD');


        _   ƥ�䵥���ַ�
        
        ����������ַ�, ��Ҫʹ��escapeת��
*/
/*
    ģ����ѯ: like
        %   ƥ�����ַ�
--��ѯԱ�������������ַ���O��Ա����Ϣ
select * from emp where ename like '__O%';

--��ѯԱ��������,����%��Ա����Ϣ
select * from emp where ename like '%\%%' escape '\';

select * from emp where ename like '%#%%' escape '#';


/*
       ���� : order by 
          ����: asc    ascend
          ����: desc   descend
          
          ����ע��null���� : nulls first | last
          
          ͬʱ���ж���, �ö��Ÿ���
*/
--��ѯԱ����Ϣ,���ս����ɸߵ�������
select * from emp order by comm desc nulls last;

--��ѯ���ű�źͰ��չ���  ���ղ�����������, ���ʽ�������
select deptno, sal from emp order by deptno asc, sal desc;


/*
     ����: ����Ҫ�з���ֵ
     
     ���к���: ��ĳһ���е�ĳ��ֵ���д���
         ��ֵ����
         �ַ�����
         ���ں���
         ת������
         ͨ�ú���
     
     ���к���: ��ĳһ�е������н��д���
           max()  min count sum avg
           
           1.ֱ�Ӻ��Կ�ֵ 
*/
--ͳ��Ա�������ܺ�
select sum(sal) from emp;

--ͳ��Ա�������ܺ�  2200
select sum(comm) from emp;

--ͳ��Ա������ 14
select count(1) from emp;

--ͳ��Ա����ƽ������  550  ����    2200/14 =
select avg(comm) from emp;


--ͳ��Ա����ƽ������ 157.
select sum(comm)/count(1) from emp;
select ceil(sum(comm)/count(1)) from emp;

update emp set ename = 'TUR%NER' where ename = 'TURNER';

select * from emp;



--��ֵ����
select ceil(45.926) from dual;  --46
select floor(45.926) from dual; --45
--��������
select round(45.926,2) from dual; --45.93
select round(45.926,1) from dual; -- 45.9
select round(45.926,0) from dual; --46
select round(45.926,-1) from dual; --50
select round(45.926,-2) from dual; --0
select round(65.926,-2) from dual; --100

--�ض�
select trunc(45.926,2) from dual; --45.92
select trunc(45.926,1) from dual; -- 45.9
select trunc(45.926,0) from dual; --45
select trunc(45.926,-1) from dual; --40
select trunc(45.926,-2) from dual; --0
select trunc(65.926,-2) from dual; --0

--����
select mod(9,3) from dual; --0
select mod(9,4) from dual; --1


--�ַ�����
-- substr(str1,��ʼ����,����) 
--ע��: ��ʼ��������д 0 ���� 1 ���Ǵӵ�һ���ַ���ʼ��ȡ
select substr('abcdefg',0,3) from dual; --abc
select substr('abcdefg',1,3) from dual; --abc

select substr('abcdefg',2,3) from dual; --bcd

--��ȡ�ַ������� 24 28
select length('abcdefg') from dual;

--ȥ���ַ��������ߵĿո�
select trim('  hello  ') from dual;

--�滻�ַ���
Select replace('hello','l','a') from dual;



--���ں���
--��ѯ���������
select sysdate from dual;
--��ѯ3���º�Ľ��������
select add_months(sysdate,3) from dual;
--��ѯ3��������
select sysdate + 3 from dual;


--��ѯԱ����ְ������
select sysdate - hiredate from  emp;

select ceil(sysdate - hiredate) from  emp;

--��ѯԱ����ְ������
select (sysdate - hiredate)/7 from emp;

--��ѯԱ����ְ������
select months_between(sysdate,hiredate) from emp;

--��ѯԱ����ְ�����
select months_between(sysdate,hiredate)/12 from emp;

--ת������  ��ֵת�ַ� �ַ�ת��ֵ  ����
--�ַ�ת��ֵ to_number(str) ����
select 100+'10' from dual;  --110  Ĭ���Ѿ�������ת��
select 100 + to_number('10') from dual; --110

--��ֵת�ַ�
select to_char(sal,'$9,999.99') from emp;

select to_char(sal,'L9,999.99') from emp;
/*
to_char(1210.73, '9999.9') ���� '1210.7' 
to_char(1210.73, '9,999.99') ���� '1,210.73' 
to_char(1210.73, '$9,999.00') ���� '$1,210.73' 
to_char(21, '000099') ���� '000021' 
to_char(852,'xxxx') ����' 354'

*/

--����ת�ַ� to_char()  
select to_char(sysdate,'yyyy-mm-dd hh:mi:ss') from dual;
select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') from dual;
--ֻ��Ҫ��
select to_char(sysdate,'yyyy') from dual;  --2017

--ֻ��Ҫ��
select to_char(sysdate,'d') from dual; --2  ����һ�������еڼ���
select to_char(sysdate,'dd') from dual;  --10  ����һ�����еĵڼ���
select to_char(sysdate,'ddd') from dual; --100 ����һ���еĵڼ���


select to_char(sysdate,'day') from dual;  --monday
select to_char(sysdate,'dy') from dual;   --mon  ���ڵļ�д



--�ַ�ת����
select to_date('2017-04-10','yyyy-mm-dd') from dual;

--��ѯ1981�� -- 1985����ְ��Ա����Ϣ
select * from emp where hiredate between to_date('1981','yyyy') and to_date('1985','yyyy');


/* 
      ͨ�ú���:
       nvl(����1,����2) �������1 = null �ͷ��ز���2
       nvl2(����1,����2,����3) �������1 = null ,�ͷ��ز���3, ���򷵻ز���2
       
       nullif(����1,����2) �������1 = ����2 ��ô�ͷ��� null , ���򷵻ز���1
       
       coalesce: ���ص�һ����Ϊnull��ֵ
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
