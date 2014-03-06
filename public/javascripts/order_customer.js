$(function() {
    $("#sq").click(function() {
        $("#sql").addClass("actived");
        $("#xgl").removeClass();
        $("#uploadPhoto").show();
        $("#change_password").hide();
    });
    $("#xg").click(function() {
        $("#xgl").addClass("actived");
        $("#sql").removeClass();
        $("#change_password").show();
        $("#uploadPhoto").hide();
    })
})
