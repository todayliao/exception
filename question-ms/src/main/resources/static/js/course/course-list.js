$(document).ready(function () {
    // back to top
    $('.to-top').toTop({
        //以下是选项默认参数，您可以根据自己的需求修改
        autohide: true,  // 是否自动隐藏
        offset: 420,     // 距离顶部多少距离时自动隐藏按钮
        speed: 200,      // 滚动持续时间
        position: true,  // 如果设置为 false，则需要手动在 css 中设置“按钮”的位置
        right: 20,       // 右侧距离
        bottom: 120      // 底部距离
    });

    // header search
    $('#header-search').click(function() {
        $('#header-search').animate({width: '500px'});
    }).blur(function() {
        $('#header-search').animate({width: '300px'});
    });
});

function showLoginModal() {
    $('#loginModal').modal('show');
}

function ajaxLogin() {
    // csrf
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });

    var formParam = $('#login-form').serialize();
    $.ajax({
        type: "post",
        async: true,
        url: "/login",
        data: formParam,
        success: function (response) {
            if (response.success == true) {
                $('#loginModal').modal('hide');
                window.location.reload();
            } else {
                layer.msg(response.errorMessage);
            }
        },
        // http status not 200
        error: function(response) {
            layer.msg('登录失败 !!!');
        }
    });
}

(function(){
    var bp = document.createElement('script');
    var curProtocol = window.location.protocol.split(':')[0];
    if (curProtocol === 'https') {
        bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
    }
    else {
        bp.src = 'http://push.zhanzhang.baidu.com/push.js';
    }
    var s = document.getElementsByTagName("script")[0];
    s.parentNode.insertBefore(bp, s);
})();