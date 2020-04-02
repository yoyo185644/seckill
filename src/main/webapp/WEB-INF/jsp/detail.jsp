<%--
  Created by IntelliJ IDEA.
  User: yoyo
  Date: 2020/3/30
  Time: 10:23 上午
  To change this template use File | Settings | File Templates.
--%>
<%--
  Created by IntelliJ IDEA.
  User: yoyo
  Date: 2020/3/30
  Time: 10:23 上午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--引入jstl--%>
<%@include file="common/tag.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>秒杀详情页</title>
    <%@include file="common/head.jsp"%>
</head>
<body>
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">${seckill.name}</div>
        </div>
        <div class="panel-body">
            <h2 class="text-danger text-center">
<%--                显示time图标--%>
                <span class="glyphicon glyphicon-time"></span>
<%--                显示倒计时--%>
                <span class="glyphicon" id="seckill-box"></span>
            </h2>

        </div>

    </div>
<%--    登陆弹出框，输入电话--%>
    <div id="killPhoneModal" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone">秒杀电话</span>
                    </h3>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhoneKey"
                        placeholder="填手机号" class="from-control"/>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
<%--                验证信息--%>
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success">
                        <span class="glyphicon glyphicon-phone"></span>
                    submit
                    </button>
                </div>
        </div>
        </div>

    </div>

<!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
<script src="https://cdn.jsdelivr.net/npm/jquery@1.12.4/dist/jquery.min.js"></script>
<!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"></script>
<%--jQuery cookie操作插件--%>
<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
<%--jQuery countDown倒计时插件--%>
<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.min.js"></script>
<%--开始编写交互逻辑--%>
<script src="/resources/script/seckill.js" type="text/javascript"></script>
<script type="text/javascript">
    $(function(){
        seckill.detail.init({
            // 使用EL表达式传入参数
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},//毫秒时间
            endTime : ${seckill.endTime.time}
        });
    });
</script>
</html>
