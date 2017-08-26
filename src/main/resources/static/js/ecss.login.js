/**
 * Created by jason on 2015/1/30.
 */
ecss.login=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<form class="form-horizontal row">'
            +'<fieldset class="col-lg-6 col-lg-offset-3">'
            +'<legend class="col-lg-offset-1"><img src="images/logo2.png"></legend>'
            +'<div class="form-group">'
            +'<label for="inputEmail" class="col-md-2 control-label">账号</label>'

            +'<div class="col-md-10">'
            +'<input type="email" class="form-control" id="id" placeholder="账号">'
            +'</div>'
            +'</div>'
            +'<div class="form-group">'
            +'<label for="inputPassword" class="col-md-2 control-label">密码</label>'

            +'<div class="col-md-10">'
            +'<input type="password" class="form-control" id="passwd" placeholder="密码">'
            +'</div>'
            +'</div>'
            +'</fieldset>'
            +'</form>'
        },
        stateMap={
            $container: null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.home.loginout)
        },
    jqueryMap={},
    setJqueryMap,
    initModule, getCheckImg,
    login;

    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $from       :$container.find('.login-form'),
            $login      :$container.find('.login-button'),
            $window     :$(window)
        };
    };

    login=function () {

        modelMap.model.set(jqueryMap.$form.save());
        modelMap.model.post('login', function (data) {
            console.log(data);
            if (data == '0') {
                $.gevent.publish('login');
            }
            else
            if(data=='1'){
                ZENG.msgbox.show('验证码错误！', 5, 3000);
                getCheckImg();
            }
            else{
                ZENG.msgbox.show('密码错误！', 5, 3000);
                getCheckImg();
            }
        })

    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function() {
            $(this).removeClass('input-error');
        });
        jqueryMap.$login.click(login);
        $.material.init();
    };
    return{initModule:initModule};
}());