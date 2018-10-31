<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>爬虫结果-${str}</title>
</head>
<body>
<div>
    <h2 style="color: darkred;">获得 ${str} 爬取结果${count}条，显示前20条记录</h2>
        <#list links as link>
            <h3>
                <a href="${link.URL}">${link.keyword}</a>
            </h3>
        </#list>
</div>
</body>
