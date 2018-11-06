<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>爬虫结果-${str}</title>
</head>
<body>
<div>
    <#assign ctx = request.contextPath />
    ${ctx}
    <h2 style="color: darkred;">获得 ${str} 爬取结果${count}条，显示前20条记录! <p style="font-size:16px">点击下载完整记录:</p><a style="font-size:16px" href="${ctx}/files/${tablename}.csv" >CSV</a>|<a style="font-size:16px" href="${ctx}/files/${tablename}.xls" >EXCEL</a></h2>
        <#list links as link>
            <h3>
                <a href="${link.URL}">${link.keyword}</a>
            </h3>
        </#list>
</div>
</body>
