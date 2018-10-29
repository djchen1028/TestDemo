<%--
  Created by IntelliJ IDEA.
  User: dj_ch
  Date: 2018/10/26
  Time: 15:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>百度爬虫</title>
    <div style="height:500px" align="center">
        <h1 style="height: 100px;text-align: center;line-height: 100px">百度爬虫</h1>
        <form action="/myCrawler/Crawler" method="post">
        <span style="width: 800px;height: 36px">
        <input name="kw" type="text" align="center" style="width: 500px;height: 36px;font-size: 16px;font-family: Arial" maxlength="255">
            <input type="submit" value="爬一下" style="height: 36px">
        </span>
        </form>
    </div>
</head>
<body>

</body>
</html>
