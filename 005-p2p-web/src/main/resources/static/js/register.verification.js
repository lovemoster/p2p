let cPhone = false;
let cPassword = false;
let cCode = false;

//校验手机号函数
function checkPhone() {
    //获取手机号码
    let phone = $.trim($('#phone').val());
    //判断手机号码是否为空
    if (phone === undefined || phone === '' || phone === null) {
        showError('phone', '手机号不能为空');
        return;
    }
    //验证手机号码格式是否正确
    if (/^1[1-9]\d{9}$/.test(phone)) {
    } else {
        showError('phone', '手机号格式不正确');
        return;
    }
    //判断改手机号是否被注册
    $.ajax({
        url: 'checkPhone',
        data: {
            phone
        },
        type: 'get'
    }).then(function (data) {
        if (data.code === 200) {
            showSuccess('phone');
            cPhone = true;
        } else {
            showError('phone', data.message);
            cPhone = false;
        }
    });
}

//校验密码函数
function checkLoginPassword() {
    cPassword = false;
    //获取手机号码
    let loginPassword = $.trim($('#loginPassword').val());
    //判断密码是否为空
    if (loginPassword === undefined || loginPassword === '' || loginPassword === null) {
        showError('loginPassword', '密码不能为空');
        return;
    }
    //判断密码是否在6-20位之间
    if (loginPassword.length >= 6 && loginPassword.length <= 20) {
    } else {
        showError('loginPassword', '请输入6-20位英文和数字混合密码');
        return;
    }
    //密码字符只可使用数字和大小写英文字母
    if (/^[0-9a-zA-Z]+$/.test(loginPassword)) {
    } else {
        showError('loginPassword', '密码字符只可使用数字和大小写英文字母');
        return;
    }
    //密码应同时包含英文和数字
    if (/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)) {
    } else {
        showError('loginPassword', '密码应同时包含英文和数字');
        return;
    }
    showSuccess('loginPassword');
    cPassword = true;
}

//校验验证码
function checkCode() {
    cCode = false;
    let messageCode = $.trim($('#messageCode').val());
    if (messageCode === undefined || messageCode === '' || messageCode === null) {
        showError('messageCode', '验证码不能为空');
        return;
    }
    if (/^\d{6}$/.test(messageCode)) {
        showSuccess("messageCode");
        cCode = true;
    } else {
        showError('messageCode', '验证码只能是6位数字');
    }
}

//手机号输入框失去焦点时校验手机号
$('#phone').blur(function () {
    checkPhone();
});

//密码输入框失去焦点时校验密码
$('#loginPassword').blur(function () {
    checkLoginPassword();
});

//验证码输入框失去焦点时校验
$('#messageCode').blur(function () {
    checkCode();
});

//获取验证码
$('#messageCodeBtn').click(function () {
    let that = $(this);
    if (cPhone) {
        $.ajax({
            url: 'sendCode',
            type: 'post',
            data: {
                phone: $.trim($('#phone').val()),
            }
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
    }

});

//校验提交数据是否正确
$('#btnRegist').click(function () {
    //验证手机号、密码、验证码合法性
    if (cPhone && cPassword && cCode) {
        $.ajax({
            url: 'register',
            type: 'post',
            data: {
                phone: $.trim($('#phone').val()),
                loginPassword: $.md5($.trim($('#loginPassword').val())),
                code: $.trim($('#messageCode').val()),
            },
            success: function (data) {
                if (data.code === 201) {
                    window.location.href = 'realName';
                } else {
                    alert('注册失败！')
                }
            }
        });
    }
})