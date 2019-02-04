$(document).ready(function () {
    var navSelector = "#toc";
    var $myNav = $(navSelector);
    Toc.init($myNav);
    $("body").scrollspy({
        target: navSelector
    });

    // 代码高亮
    hljs.initHighlightingOnLoad();
    // $("pre").addClass("prettyprint");
    // prettyPrint();

    // 图片放大
    $('p img').zoomify();
});