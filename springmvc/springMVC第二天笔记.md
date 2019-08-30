## SpringMVC第二天

### 一、教学目标

1、ModelAttribute和SessionAttribute注解的使用

2、RestFul风格

3、控制器方法的返回值

4、交互JSON数据

5、springMVC实现文件上传

6、springMVC异常处理

### 二、ModelAttribute和SessionAttribute注解的使用

```
a. @ModelAttribute标记在方法上
	特点：当执行控制器中任何一个方法时，都会先执行@ModelAttribute标记的方法
	缺点：慎重使用,因为执行控制器中任何一个方法时，都会先执行@ModelAttribute标记的方法， 效率太低
	UserUpdateController
	UserQueryController
b. 用法一
 	@ModelAttribute
    public User findById(Integer id){
        User user = new User();
        user.setUsername("王五");
        user.setSex("女");
        return user;
    }

    /**
     * update user set username = ? ,sex = ? where id = ?
     * @ModelAttribute:在执行控制器中任何一个方法时，都会先执行
     *      findById方法返回一个User对象
     *      判断参数user中的属性是否为null，如果为null，则使用findById的方法返回值中的数据覆盖
     * @param user
     * @return
     */
    @RequestMapping(value = "/testUpdate",method = RequestMethod.POST)
    public String testUpdate(User user){
        System.out.println(user);
        System.out.println("更新");
        return "show";
    }
c.方法二：
 @ModelAttribute
    public void findById(Integer id, Map<String ,Object> map){
        User user = new User();
        user.setUsername("王五");
        user.setSex("女");

        map.put("aa",user);
    }

    @RequestMapping(value = "/testUpdate",method = RequestMethod.POST)
    public String testUpdate(@ModelAttribute("aa") User user){
        System.out.println(user);
        System.out.println("更新");
        return "show";
    }
d. 注解@SessionAttributes:在session范围内存储对象
	1) @SessionAttributes({"username","password"}) 在类上标记该注解
		在session范围可以存储这两个变量名
	2) /**
     * 把用户名和密码存入到Session范围内
     */
    @RequestMapping("/testPut")
    public void testPut(Model model){
        model.addAttribute("username","zhangsan");
        model.addAttribute("password","123456");
    }

   3)  /**
     * 获取session范围内的对象
     */
    @RequestMapping("/testGet")
    public void testGet(ModelMap modelMap){
        Object username = modelMap.get("username");
        System.out.println(username);
        Object password = modelMap.get("password");
        System.out.println(password);
    }

    4) /**
     * 清空session
     * @param sessionStatus
     */
    @RequestMapping("/testClear")
    public void testClear(SessionStatus sessionStatus){ 
   	 	sessionStatus.setComplete();
    }

```

### 三、RestFul风格

```
a. rest  是一种编程风格
b. 满足了rest风格的网站就是restful风格
c. 只是一种规范，不是规则
d. 根据id获取一个用户： /user/findById?id=1				restful风格：/user/operate/1  使用get方式提交
	 根据id删除一个用户：/user/delById?id=1					restful风格：/user/operate/1 使用delete方式提交
	 更新一个用户：/user/update?id=1&username=zzz 	 restful风格：/user/operate/1    使用put方式提交
	 添加一个用户：/user/save?id=1&username=zzz  	 restful风格：/user/operate      使用post方式提交
e. 根据id获取
	页面
		http://localhost:8080/user/operate/1
	方法
		/**
     * 根据id查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/operate/{id}",method = RequestMethod.GET)
    public String findById(@PathVariable("id") Integer id){
        System.out.println("findById:"+id);
        return "show";
    }
f. 添加一个用户
	页面
	 <%--请求保存用户--%>
    <form action="${pageContext.request.contextPath}/user/operate/1" method="post">
        <input type="submit" value="提交">
    </form>
	方法
		/**
     * 保存用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/operate/{id}" , method = RequestMethod.POST)
    public String save(@PathVariable("id") Integer id){
        System.out.println("save:" + id);
        return "show";
    }
 h. 更新用户
 	页面：
 	<%--请求更新用户--%>
    <form action="${pageContext.request.contextPath}/user/operate/1" method="post">
        <%--要使用put，delete提交方式
                 1) 在web.xml开启put和delete提交方式
                 2） 表单的提交方式必须post
                 3) 表单中必须设置一个隐藏域 name=_method value=PUT
                 4) 请求的方法返回值必须以流的形式返回，在方法上标记注解：@ResponseBody
                       流的形式返回： response.getWriter().print()
                --%>
        <input type="hidden" name="_method" value="PUT">
        <input type="submit" value="更新">
    </form>
    方法
     /**
     * 更新用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/operate/{id}" , method = RequestMethod.PUT)
    @ResponseBody
    public String update(@PathVariable("id") Integer id){
        System.out.println("update:" + id);
        return "show";
    }
 i. 删除用户
 	页面
 	<%--请求删除用户--%>
    <form action="${pageContext.request.contextPath}/user/operate/1" method="post">
        <input type="hidden" name="_method" value="DELETE">
        <input type="submit" value="删除">
    </form>
    方法
     /**
     * 删除用户
     * @param id
     * @return
     */
    @RequestMapping(value = "/operate/{id}" , method = RequestMethod.DELETE)
    @ResponseBody
    public String delById(@PathVariable("id") Integer id){
        System.out.println("delById:" + id);
        return "show";
    }
j， web.xml
	<!--开启另外两种提交方式:put, delete-->
  <filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
```

### 四、控制器方法的返回值

##### 1、返回值为void类型

```
a. 方法一：
	@RequestMapping("/testVoid")
   public void testVoid(){
        System.out.println("测试没有返回值");
        //因为没有指定返回值页面，会自动截取请求路径，进入视图解析器，拼接完整的路径
        //可以对应的路径下创建对应的jsp页面
   }
b. 方法二：response重定向
@RequestMapping("/testVoid2")
    public void testVoid2(HttpServletResponse response){
        System.out.println("测试没有返回值");
        try {
            //重定向不能进入web-inf路径
            //转发可以进入web-inf路径
            response.sendRedirect("/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
c. 方法二：servlet转发
	@RequestMapping("/testVoid3")
    public void testVoid3(HttpServletResponse response, HttpServletRequest request){
        System.out.println("测试没有返回值");
        try {
            request.getRequestDispatcher("/WEB-INF/show.jsp").forward(request,response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
```

##### 2、返回值为String类型（常用）

```
a. 默认的情况：转发请求，返回值直接进入视图解析器,拼接前缀和后缀，完整的路径
	/**
     * 方法的返回值为String类型
     *      返回值直接进入视图解析器,拼接前缀和后缀，完整的路径
     * @return
     */
    @RequestMapping("/testReturnString")
    public String testReturnString(){
        return "show";
    }
 b. 请求重定向
 	/**
     * 执行保存操作-- 重新查询 : save -- redirect:findAll
     *
     * 方法的返回值为String类型
     *      返回值直接进入视图解析器,拼接前缀和后缀，完整的路径
 *      redirect:重定向, 不能进入web-inf
     *   redirect:添加了redirect后则不会进入视图解析器，需要配置完整的路径
     * @return
     */
    @RequestMapping("/testReturnString2")
    public String testReturnString2(){
        return "redirect:/index.jsp";
    }
 c. 请求转发
 /**
     * forward: 转发，不会进入视图解析器，需要配置完整的路径
     * @return
     */
    @RequestMapping("/testReturnString3")
    public String testReturnString3(){
        return "forward:/index.jsp";
    }
```

##### 3、返回值为ModelAndView类型（常用）

```
a. ModelAndView:   Model 模型:封装数据  view 视图: 指定页面  ---> 模型和视图
b. /**
     * 返回值类型为ModelAndView,包含数据和视图页面
     * @return
     */
    @RequestMapping("/testReturnModelAndView")
    public ModelAndView testReturnModelAndView(){
        //准备数据--数据库查询
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUsername("zhangsan");
        user.setSex("男");
        user.setId(2);

        User user1 = new User();
        user1.setUsername("zhangsan1");
        user1.setSex("女");
        user1.setId(1);

        userList.add(user);
        userList.add(user1);
        ModelAndView modelAndView = new ModelAndView();
        //添加数据
        modelAndView.addObject("userList",userList);
        //指定页面
        modelAndView.setViewName("show");
        return  modelAndView;
    }
```

### 五、交互JSON数据

```xml
1. 引入依赖
<!--引入json的依赖-->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-core</artifactId>
      <version>2.9.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.9.0</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-annotations</artifactId>
      <version>2.9.0</version>
    </dependency>
b. ReqeustBody注解
 	@RequestMapping("/testRequestJson")
    public void testRequestJson(String username ,Integer age){
        System.out.println(username);
        System.out.println(age);
    }

    /**
     * @RequestBody： 可以把所有参数转换为字符串
     * @param body
     */
    @RequestMapping("/testRequestJson2")
    public void testRequestJson2(@RequestBody  String body){
        System.out.println(body);
    }

		$.ajax({
            url:"${pageContext.request.contextPath}/user/testRequestJson2",
            data:{"username":"zhangsan","age":20},
            type:"post",
            dataType:"json",
            success:function(data){
               
            }
    });
c. ResponseBody注解
/**
     * @ResponseBody ：标记了@ResponseBody注解的方法，数据会以流的方式返回
     * @return
     */
    @RequestMapping("/testResponseBody")
    @ResponseBody
    public List<User> testResponseBody(){
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setUsername("zhangsan");
        user.setSex("男");
        user.setId(2);

        User user1 = new User();
        user1.setUsername("zhangsan1");
        user1.setSex("女");
        user1.setId(1);

        userList.add(user);
        userList.add(user1);

        return userList;
    }
      
      $.ajax({
            url:"${pageContext.request.contextPath}/user/testResponseBody",
            data:{},
            type:"post",
            dataType:"json",
            success:function(data){
                alert(data[0].username);
                alert(data[1].username);
            }
        });
d. 引入静态资源后，必须静态资源放行
		<!--对静态资源放行
        把js下的静态资源映射到js目录下
    -->
    <mvc:resources mapping="/js/*" location="/js/"></mvc:resources>
```

### 六、SpringMVC实现文件上传

#### 1、文件上传

##### a、引入依赖

```
	引入fileUpload会自动依赖commons-io
<dependency>
      <groupId>commons-fileupload</groupId>
      <artifactId>commons-fileupload</artifactId>
      <version>1.3.1</version>
    </dependency>
```

##### b、spring-mvc.xml 配置文件

```
		<!-- 配置文件上传解析器 -->
    <!-- id的值是固定的-->
    <bean id="multipartResolver"
          class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <!-- 设置上传文件的最大尺寸为5MB -->
        <property name="maxUploadSize">
            <value>5242880</value>
        </property>
    </bean>
```

##### c、页面配置

```
<%--
        上传文件的表单前提
            1) 提交方式必须是post
            2) 表单的类型必须：multipart/form-data， 多功能表单数据
            3) 必须有一个type=file的表单元素
    --%>
    <form action="${pageContext.request.contextPath}/user/upload" method="post" enctype="multipart/form-data">
        <input type="text" name="username"> <br>
        <input type="file" name="upload"><br>
        <input type="submit" value="上传">
    </form>
```

##### d、controller代码

```
/**
     * 声明参数 变量接收数据
     *
     */
    @RequestMapping("/upload")
    public String upload(String username , MultipartFile upload, HttpServletRequest request){
//        System.out.println(username);
        //1. 目标路径
        //获取项目运行的路径
        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        //判断该路径是否存在
        File realFile = new File(realPath);
        if(!realFile.exists()){
            realFile.mkdirs();
        }
        //2. 获取唯一的文件名称(包含扩展名)
        String uuidName = UUID.randomUUID().toString().replace("-", "");
        //获取扩展名: 获取文件名
        //获取真实的文件名
        String originalFilename = upload.getOriginalFilename();
        //截取字符串，获取文件的扩展名
        String extendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println(extendName);
        //唯一的文件名
        String fileName = uuidName + extendName;
        System.out.println(fileName);

        //文件上传
        //transferTo: 执行文件上传
        //参数file：目录文件
        try {
            upload.transferTo(new File(realFile, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "show";
    }
```

#### 2、跨服上传

##### a、引入依赖

```
<!--引入jersey服务器的包-->
    <dependency>
      <groupId>com.sun.jersey</groupId>
      <artifactId>jersey-core</artifactId>
      <version>1.18.1</version>
    </dependency>
    <dependency>
      <groupId>com.sun.jersey</groupId>
      <artifactId>jersey-client</artifactId>
      <version>1.18.1</version>
    </dependency>
```

##### b、修改tomcat配置

```
1. tomcat默认不能跨服上传的
2. tomcat/conf/web.xml 	
		<init-param>
            <param-name>debug</param-name>
            <param-value>0</param-value>
        </init-param>
    <!--需要添加的-->
		<init-param>
    	<param-name>readonly</param-name>
    	<param-value>false</param-value>
    </init-param>
```

##### c、配置图片服务器

```
1. 创建一个web项目
2. 配置一个tomcat，与原来tomcat端口号不一致
3. 在webapp目录下创建一个upload目录，空的文件夹不会编译，需要在upload目录添加（任意）一个文件
```

##### d、修改controller代码

```
/**
     * 声明参数 变量接收数据
     *
     */
    @RequestMapping("/uploadServer")
    public String uploadServer(String username , MultipartFile upload, HttpServletRequest request){
        //1. 配置图片服务器的路径
        String serverPath = "http://localhost:9090/img_server/upload/";
        //2. 获取唯一的文件名称(包含扩展名)
        String uuidName = UUID.randomUUID().toString().replace("-", "");
        //获取扩展名: 获取文件名
        //获取真实的文件名
        String originalFilename = upload.getOriginalFilename();
        //截取字符串，获取文件的扩展名
        String extendName = originalFilename.substring(originalFilename.lastIndexOf("."));
        System.out.println(extendName);
        //唯一的文件名
        String fileName = uuidName + extendName;
        System.out.println(fileName);

        //获取jersey服务器客户端
        Client client = Client.create();
        //配置上传路径的资源对象
        WebResource resource = client.resource(serverPath + fileName);
        //上传
            //参数：资源的类型
            //文件的字节内容
        try {
            resource.put(String.class,upload.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "show";
  }
```

### 七、SpringMVC的统一异常处理

```
1. 自定义异常类
public class CustomException extends  Exception {

    private String message;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
2. 定义异常的统一处理对象--实现接口：HandlerExceptionResolver
/**
 * 自定义的异常统一处理对象
 *
 * @author 黑马程序员
 * @Company http://www.ithiema.com
 * @Version 1.0
 */
//创建该类的对象
@Component
public class MyExceptionResolver implements HandlerExceptionResolver {


    /**
     * 解析异常
     * @param request
     * @param response
     * @param handler
     * @param e 其他模块传过来的异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        //添加数据
        //instanceof: 实例类型判断运算符
        if(e instanceof CustomException){
            //强制转换
            CustomException customException = (CustomException) e;
            modelAndView.addObject("message",customException.getMessage() );
        }else{
            modelAndView.addObject("message","系统错误，请联系管理员!!!");
        }
        //指定页面
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
3. 错误页面
<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    ${message}
</body>
</html>
```

