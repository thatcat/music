$(function() {
    $("td img").click(function() {
        var pic = $(this).attr("src");
        var w = $(window).width();
        var h = $(window).height();
        var pw = w / 2;
        var ph = h / 2;
        $("#showPic").css("width", w);
        $("#showPic").css("height", $(document).height());
        $("#showPic").append("<img src='"+pic+"' style='position:absolute;z-index:1001;width:600px;height:400px;top:"+ph+"px;left:"+pw+"px;margin-left:-300px;margin-top:-200px;'/>");
        $("#showPic").show("slow");
    });
    $("#showPic").click(function() {
        $(this).hide();
    })
})
