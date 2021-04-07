//校验姓名
function checkRealName() {
    let realName = $.trim($('#realName').val());
    if (realName === undefined || realName === '' || realName === null) {
        showError('realName', '姓名不能为空');
        return false;
    }
    if (realName.length === 1) {
        showError('realName', '非法姓名');
        return false;
    }
    if (!/^[\u4e00-\u9fa5]{0,}$/.test(realName)) {
        showError('realName', '姓名必须为中文');
        return false;
    }
    showSuccess('realName');
    return true;
}

//校验身份证信息
function checkIdCard() {
    let IdCard = $.trim($('#idCard').val());
    if (IdCard === undefined || IdCard === '' || IdCard === null) {
        showError('idCard', '身份证号码不能为空');
        return false;
    }
    if (IdCard.length !== 15 && IdCard.length !== 18) {
        showError('idCard', '身份证号码位数不正确');
        return false;
    }
    if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(IdCard)) {
        showError('idCard', '身份证号码不正确');
        return false;
    }
    showSuccess('idCard');
    return true;
}

//校验验证码
function checkCode() {
    let messageCode = $.trim($('#messageCode').val());
    if (messageCode === undefined || messageCode === '' || messageCode === null) {
        showError('messageCode', '验证码不能为空');
        return false;
    }
    if (/^\d{6}$/.test(messageCode)) {
        showSuccess("messageCode");
        return true;
    } else {
        showError('messageCode', '验证码只能是6位数字');
    }
    return false;
}

$('#realName').blur(function () {
    checkRealName();
});

$('#idCard').blur(function () {
    checkIdCard();
});

//验证码输入框失去焦点时校验
$('#messageCode').blur(function () {
    checkCode();
});

//获取验证码
$('#messageCodeBtn').click(function () {
    let that = $(this);
    $.ajax({
        url: 'sendCode',
        type: 'post',
    }).then(function (data) {
        if (data.code === 202) {
            //获取验证码成功后禁用获取按钮
            let leftTime = 60;
            that.attr("disabled", true);
            that.css('background', 'gray');
            that.html('重发(' + leftTime + '秒)');
            let promise = new Promise((resolve, reject) => {
                let setTimer = setInterval(
                    () => {
                        leftTime -= 1;
                        that.html('重发(' + leftTime + '秒)');
                        if (leftTime <= 0) {
                            that.html('获取验证码');
                            resolve(setTimer)
                        }
                    }
                    , 1000);
            })
            promise.then((setTimer) => {
                clearInterval(setTimer);
                that.attr("disabled", false);
                that.css('background', '#4185F4');
            })
            alert(data.data);
        } else {
            alert(data.message);
        }
    });
});

//校验身份证和真实姓名
$('#btnRegist').click(function () {
    let cRealName = checkRealName();
    let cIdCard = checkIdCard();
    let cCode = checkCode();
    console.log(cRealName)
    console.log(cIdCard)
    console.log(cCode)
    if (cRealName && cIdCard && cCode) {
        $.ajax({
            url: '/loan/page/realName',
            type: 'post',
            data: {
                name: $('#realName').val(),
                idCard: $('#idCard').val(),
                code: $('#messageCode').val()
            },
            success: function (data) {
                if (data.code === 203){
                    window.location.href = 'login';
                }else{
                    alert('实名失败：' + data.message);
                }
            }
        })
    }
})