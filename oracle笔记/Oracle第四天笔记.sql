/*
  序列: ORACLE使用来模拟ID自动增长的
  
*/
create sequence seq_test4;

create table test2(
   tid number primary key,
   tname varchar2(10)    
);

insert into test2 values(seq_test4.nextval,'张三');
select * from test2;

/*
       PLSQL编程: 过程语言,编写一些复杂业务逻辑  
       
       输出星号:
                abs(y) + abs(x) <= m   
                
       vsal  emp.sal%type --引用型变量
       row  emp%rowtype  --记录型变量
       
       select sal into vsal from emp where empno=7788;
*/
declare
   m number := 3;             
begin
   for y in -m..m loop
     for x in -m..m loop
         if abs(y) + abs(x) <= m then
            dbms_output.put('*');
         else  
            dbms_output.put(' ');  
         end if;
     end loop;
     dbms_output.new_line();
   end loop;  
end;


/*
   游标(光标): 是用来操作查询结果集,相当于是JDBC中ResultSet
       
       语法: cursor 游标名[(参数名 参数类型)] is 查询结果集
       
       开发步骤:
           1. 声明游标
           2. 打开游标       open 游标名
           3. 从游标中取数据  fetch 游标名 into 变量
                         游标名%found :找到数据
                         游标名%notfound : 没有找到数据 
           4. 关闭游标       close 游标名
           
      系统引用游标
           1. 声明游标 : 游标名 sys_refcursor
           2. 打开游标: open 游标名 for 结果集
           3. 从游标中取数据
           4. 关闭游标
                
     for循环遍历游标:
           不需要声明额外变量
           不需要打开游标
           不需要关闭游标      
*/
--输出员工表中所有的员工姓名和工资(不带参数游标)
/*
   游标:所有员工
   声明一个变量,用来记录一行数据  %rowtype
*/
declare
   --游标
   cursor vrows is select * from emp;
   --s声明变量,记录一行数据
   vrow emp%rowtype;
begin
   --1.打开游标  
   open vrows;
   --2.从游标提取数据
   --循环取数据
   loop
       fetch vrows into vrow; 
       exit when vrows%notfound;  
       dbms_output.put_line('姓名:'||vrow.ename ||' 工资: ' || vrow.sal);
   end loop;
   --3.关闭游标
   close vrows;
end;

--输出指定部门下的员工姓名和工资
/*
   游标: 指定部门的所有员工
   声明一个变量记录一行数据
*/
declare
   --声明游标
   cursor vrows(dno number) is select * from emp where deptno = dno;
   --声明变量
   vrow emp%rowtype;
begin
  --1.打开游标 , 指定10号部门
  open vrows(10);
  --2. 循环遍历,取数据
  loop
     fetch vrows into vrow;
     exit when vrows%notfound;    
      dbms_output.put_line('姓名:'||vrow.ename ||' 工资: ' || vrow.sal);
  end loop;
  close vrows;
end;

--系统引用游标
--输出员工表中所有的员工姓名和工资
declare
  --声明系统引用游标
  vrows sys_refcursor;
  --声明一个变量
  vrow emp%rowtype;
begin
  --1.打开游标
  open vrows for select * from emp;
  --2.取数据
  loop
    fetch vrows into vrow;
    exit when vrows%notfound;
     dbms_output.put_line('姓名:'||vrow.ename ||' 工资: ' || vrow.sal);
  end loop;
  close vrows;
end;

--扩展内容----使用for循环遍历游标
declare
  --声明一个游标
  cursor vrows is select * from emp;
begin
  for vrow in vrows loop
     dbms_output.put_line('姓名:'||vrow.ename ||' 工资: ' || vrow.sal || '工作:'|| vrow.job);
  end loop;
end;

select * from emp;



--按照员工工作给所有员工涨工资,总裁涨1000,经理涨800,其他人涨400
/*
    游标 : 所有员工
    声明一个记录一行数据   
*/
declare
   --声明游标
   cursor vrows is select * from emp;
   --声明一个变量
   vrow emp%rowtype; 
begin
  --1.打开游标
  open vrows;
  --2.循环取数据
  loop
       --取数据
       fetch vrows into vrow;
       --退出条件
       exit when vrows%notfound;  
       --根据不同的职位,涨工资 总裁涨1000,经理涨800,其他人涨400
       if vrow.job = 'PRESIDENT' then
          update emp set sal = sal + 1000 where empno = vrow.empno;
       elsif vrow.job = 'MANAGER' then
          update emp set sal = sal + 800 where empno = vrow.empno;
       else
          update emp set sal = sal + 400 where empno = vrow.empno; 
       end if;       
  end loop;
  --3.关闭游标
  close vrows;
  --4.提交事务
  commit;
end;


select * from emp;


/*
   例外:(意外)程序运行的过程发生异常,相当于是JAVA中的异常
   
   declare
       --声明变量
   begin
       --业务逻辑
   exception
       --处理异常
       when 异常1 then
         ...
       when 异常2 then
         ...
       when others then
         ...处理其它异常
   end;
   
   zero_divide : 除零异常
   value_error : 类型转换异常
   too_many_rows : 查询出多行记录,但是赋值给了rowtype记录一行数据变量
   no_data_found : 没有找到数据
       
   
   自定义异常:
       异常名  exception;
       raise 异常名          
*/
declare
   vi number;
   vrow emp%rowtype;
begin
   --vi := 8/0;  
   --vi := 'aaa';
   --select * into vrow from emp;
   select * into vrow from emp where empno=1234567;
exception
  when zero_divide then
    dbms_output.put_line('发生了除零异常');
  when value_error then
     dbms_output.put_line('发生了类型转换异常');
  when too_many_rows then
    dbms_output.put_line(' 查询出多行记录,但是赋值给了rowtype记录一行数据变量');
  when no_data_found then
    dbms_output.put_line('没有找到数据异常');
  when others then
     dbms_output.put_line('发生了其它异常' || sqlerrm);     
end;

--查询指定编号的员工,如果没有找到,则抛出自定义的异常
/*
     --错误的演示
     
     1.声明一个变量 %rowtype
     2.查询员工信息,保存起来
     3.判断员工信息是否为空
     4. 如果是 则抛出异常
*/
declare
  --   1.声明一个变量 %rowtype
  vrow emp%rowtype;
  --2 .声明一个自定义的异常
  no_emp exception;  
begin
  --查询员工信息,保存起来
  select * into vrow from emp where empno = 8888;   --抛出异常
  
  if vrow.sal is null then
    raise no_emp; --抛出自定义的异常
  end if;
exception
  when no_emp then
     dbms_output.put_line('输出了自定义的异常');  
  when others then
     dbms_output.put_line('输出了其它异常'||sqlerrm);  
end;

--查询指定编号的员工,如果没有找到,则抛出自定义的异常
/*
     游标来判断
       %found %notfound
    声明一个游标
    声明一个变量,记录数据
    从游标中取记录
       如果有,则不管它
       如果没有就抛出自定义的异常
*/
declare
  --声明游标
  cursor vrows is select * from emp where empno=8888;   
  --声明一个记录型变量
  vrow emp%rowtype;
  --声明一个自定义异常
  no_emp exception;  
begin
  --1.打开游标
  open vrows;
  --2.取数据
  fetch vrows into vrow;
  --3.判断游标是否有数据
  if vrows%notfound then
    raise no_emp;
  end if;
  close vrows;
exception
  when no_emp then
    dbms_output.put_line('发生了自定义的异常');
end;



/*
    存储过程: 实际上是封装在服务器上一段PLSQL代码片断,已经编译好了的代码
              1.客户端取调用存储过程,执行效率就会非常高效
         语法:
              create [or replace] procedure 存储过程的名称(参数名 in|out 参数类型,参数名 in|out 参数类型)
              is | as
               --声明部分
              begin
               --业务逻辑 
              end; 
             
              
*/
--给指定员工涨薪,并打印涨薪前和涨薪后的工资
/*
    参数 : in 员工编号
    参数 : in 涨多少
    
    声明一个变量 : 存储涨工资前的工资
    
    查询出当前是多少
    打印涨薪前的工资
    更新工资
    打印涨薪后的工资          
*/
create or replace procedure proc_updatesal(vempno in number,vnum in number)
is
   --声明变量.记录当前工资
   vsal number;    
begin
  --查询当前的工资
  select sal into vsal from emp where empno = vempno;
  --输出涨薪前的工资
  dbms_output.put_line('涨薪前:'||vsal);
  --更新工资
  update emp set sal = vsal + vnum where empno = vempno;
  --输出涨薪后的工资
  dbms_output.put_line('涨薪后:'||(vsal+vnum));
  --提交
  commit;
end;

--方式1
call proc_updatesal(7788,10);

--方式2 用的最多的方式
declare

begin
  proc_updatesal(7788,-100);
end;


/*
  存储函数: 实际上是一段封装是Oracle服务器中的一段PLSQL代码片断,它是已经编译好了的代码片段
        
        语法: 
             create [or replace] function 存储函数的名称(参数名 in|out 参数类型,参数名 in|out 参数类型) return 参数类型
             is | as
             
             begin
               
             end;
        存储过程和函数的区别:
             1.它们本质上没有区别
             2.函数存在的意义是给过程调用   存储过程里面调用存储函数
             3.函数可以在sql语句里面直接调用
             4.存储过程能实现的,存储函数也能实现,存储函数能实现的,过程也能实现
             
        默认是 in       
*/
--查询指定员工的年薪
/*
    参数 : 员工的编号
    返回 : 年薪          
*/
create or replace function func_getsal(vempno number) return number
is
  --声明变量.保存年薪
  vtotalsal number;     
begin
  select sal*12 + nvl(comm,0) into vtotalsal from emp where empno = vempno;
  return vtotalsal;
end;

--调用存储函数
declare
  vsal number;
begin
  vsal := func_getsal(7788);
  dbms_output.put_line(vsal);
end;


--查询员工的姓名,和他的年薪
select ename,func_getsal(empno) from emp;
--查询员工的姓名和部门的名称


--查询指定员工的年薪--存储过程来实现
--参数: 员工编号
--输出: 年薪
create or replace procedure proc_gettotalsal(vempno in number,vtotalsal out number)
is
       
begin
  select sal*12 + nvl(comm,0) into vtotalsal from emp where empno = vempno;
end;


declare
  vtotal number;
begin
  proc_gettotalsal(7788,vtotal);
  dbms_output.put_line('年薪:'||vtotal);
end;

select *  from emp where empno = 8888; 

/*
    JAVA调用存储过程
       JDBC的开发步骤:
          1.导入驱动包
          2.注册驱动
          3.获取连接
          4.获取执行SQL的statement
          5.封装参数
          6.执行SQL
          7.获取结果
          8.释放资源   
*/

/*
   封装一个存储过程 : 输出所有表中的记录
   
   输出类型 : 游标  
*/
create or replace procedure proc_getemps(vrows out sys_refcursor)
is

begin
  --1.打开游标, 给游标赋值
  open vrows for select * from emp;
end;



/*
   触发器: 当用户执行了 insert | update | delete 这些操作之后, 可以触发一系列其它的动作/业务逻辑
       作用 : 
            在动作执行之前或者之后,触发业务处理逻辑
            插入数据,做一些校验
            
       语法:
           create [or replace] trigger 触发器的名称
           before | after
           insert | update | delete 
           on 表名
           [for each row]
           declare
           
           begin
             
           end;
           
       触发器的分类:
           语句级触发器:   不管影响多少行, 都只会执行一次
           
           行级触发器:     影响多少行,就触发多少次
                  :old  代表旧的记录, 更新前的记录
                  :new  代表的是新的记录
       
*/
--新员工入职之后,输出一句话: 欢迎加入黑马程序员
create or replace trigger tri_test1
after
insert
on emp
declare

begin
  dbms_output.put_line('欢迎加入黑马程序员');
end;

insert into emp(empno,ename) values(9527,'HUAAN');

--数据校验, 星期六老板不在, 不能办理新员工入职
--在插入数据之前
--判断当前日期是否是周六
--如果是周六,就不能插入
create or replace trigger tri_test2
before
insert 
on emp
declare
 --声明变量
 vday varchar2(10);
begin
  --查询当前
  select trim(to_char(sysdate,'day')) into vday from dual;
  --判断当前日期:
  if vday = 'saturday' then
     dbms_output.put_line('老板不在,不能办理入职');
     --抛出系统异常
     raise_application_error(-20001,'老板不在,不能办理入职');
  end if;
end;

insert into emp(empno,ename) values(9528,'HUAAN2');

--更新所有的工资 输出一句话
create or replace trigger tri_test3
after
update
on emp 
for each row
declare

begin
  dbms_output.put_line('更新了数据');
end;

update emp set sal = sal+10;



--判断员工涨工资后的工资一定要大于涨工资前的工资
/*
   200 --> 100
   触发器 : before
      旧的工资 
      新的工资
      如果旧的工资大于新的工资 , 抛出异常,不让它执行成功   
      
      
   触发器中不能提交事务,也不能回滚事务 
*/
create or replace trigger tri_updatesal
before
update
on emp
for each row
declare

begin
  if :old.sal > :new.sal then
    raise_application_error(-20002,'旧的工资不能大于新的工资');
  end if;
end;

update emp set sal = sal + 10;
select * from emp;

update emp set sal = sal - 100;


/*
   模拟mysql中ID的自增属性 auto_increment 
   insert into person(null,'张三');  
   
   触发器:
   
   pid=1  insert  pid=1
   
   序列 : create sequence seq_person_pid;       
*/
create table person(
    pid number primary key,
    pname varchar2(20)   
);

insert into person values(null,'张三'); 

create sequence seq_person_pid;

--触发器
create or replace trigger tri_add_person_pid
before
insert
on person
for each row
declare

begin
  dbms_output.put_line(:new.pname);
  --给新记录 pid 赋值
  select seq_person_pid.nextval into :new.pid from dual;
end;

insert into person values(null,'张三'); 


select * from person;
