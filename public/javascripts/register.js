$(function() {
    var isUsername = false;
    var isEmail = false;
    var isPasswd = false;
    var isConfirm = false;
    $("#username").blur(function() {
        var username = $(this).val();
        if (username.length < 2) {
            $(this).next(".flag").html("");
            $("#name-msg").text("昵称不小于2个字符");
            isUsername = false;
        } else {
            $("#name-msg").text("");
            $(this).next(".flag").html("<img src='public/images/yes.png' />");
            isUsername = true;
        }
    });
    $("#email").blur(function() {
        var email = $(this).val();
        var reg = /^[a-z0-9]+@[a-z0-9]+\.[a-z0-9]{2,3}$/;
        if (reg.test(email)) {
            $("#email-msg").text("");
            $(this).next(".flag").html("<img src='public/images/yes.png' />");
            isEmail = true;
        } else {
            $(this).next(".flag").html("");
            $("#email-msg").text("邮箱格式错误");
            isEmail = false;
        }
    });
    $("#passwd").blur(function() {
        var passwd = $(this).val();
        if (passwd.length >= 5) {
            $("#passwd-msg").text("");
            $(this).next(".flag").html("<img src='public/images/yes.png' />");
            isPasswd = true;
        } else {
            $(this).next(".flag").html("");
            $("#passwd-msg").text("密码不小于5个字符");
            isPasswd = false;
        }
    });
    $("#passwdconfirm").blur(function() {
        var passwd = $("#passwd").val();
        var confirm = $(this).val();
        var len = confirm.length;
        if (passwd == confirm && len != 0) {
            $("#confirm-msg").text("");
            $(this).next(".flag").html("<img src='public/images/yes.png' />");
            isConfirm = true;
        } else if(len != 0) {
            $(this).next(".flag").html("");
            $("#confirm-msg").text("密码不一致");
            isConfirm = false;
        } else {
            isConfirm = false;
        }
    });
    $("#registerBtn").click(function() {
        if (isUsername && isEmail && isPasswd && isConfirm) {
            return true;
        } else {
            alert("请正确填写注册信息!");
            return false;
        }
    });
})