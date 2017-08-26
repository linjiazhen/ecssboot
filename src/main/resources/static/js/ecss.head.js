/**
 * Created by jason on 2015/1/30.
 */
ecss.head=(function(){
    'use strict';
    var configMap={
            main_html:String()
                +'<div class="logo"></div>'
                +'<div class="login"><span class="glyphicon glyphicon-log-in" ></span>登陆</div>'
                +'<div class="details" >'
                +'<div class="dropdown">'
                +'<a href="#" data-target="#" class="dropdown-toggle" data-toggle="dropdown"><Strong id="name" style="color: #000">欢迎使用</Strong><b class="caret"></b></a>'
                +'<ul class="dropdown-menu">'
                +'<li><a href="javascript:void(0)">修改密码</a></li>'
                +'<li class="logout"><a href="javascript:void(0)">退出登录</a></li>'
                +'</ul>'
                +'</div></div>'
                +'<div class="gomap"><span class="glyphicon glyphicon-globe" ></span>返回地图</div>'
                +'<div class="godashboard"><span class="glyphicon glyphicon-dashboard" ></span>返回后台</div>',
            formOption:[
                [{label:'label',text:'账号',size:3},{label:'input',type:'text',name:'id',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'密码',size:3},{label:'input',type:'password',name:'passwd',size:8,valid:{notEmpty: {}}}]
                //[{label:'label',text:'验证码',size:3},{label:'input',type:'text',name:'check',size:4,valid:{notEmpty: {}}},{label:'image',class:'check',src:''},{label:'label',text:'看不清请点击图片'}]
            ]
        },
        stateMap={
            $container: null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.home.loginout)
        },
        jqueryMap={
        },
        setJqueryMap,   initModule,getCheckImg,login,logout,checklogin,gomap,godashboard;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $modal      :new ecss.tools.makeModal($container,'登陆系统'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm    :new ecss.tools.makeModal($container),
//            $atm        :new ecss.tools.makeAtm('test.do',message),
            $logo       :$container.find('.logo'),
            $login      :$container.find('.login'),
            $logout      :$container.find('.logout'),
            $window     :$(window)
        };
    };
    getCheckImg=function(){
        // modelMap.model.post('createpic',function(data){
        //     jqueryMap.$form.getElementsByClass('check').prop('src','images/check/'+data+'.jpeg');
        // });
    };
    login=function () {
        getCheckImg();
        jqueryMap.$modal.show(function () {
            modelMap.model.set(jqueryMap.$form.save());
            modelMap.model.post('login', function (data) {
                console.log(data);
                if (data == '0') {
                   // $.gevent.publish('login');
                    jqueryMap.$login.hide();
                    $('.details').show();
                    $('.godashboard').show();
                    jqueryMap.$modal.hide();
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
        });
    };
    logout=function () {
        jqueryMap.$confirm.confirm('确认退出？','谢谢使用！',function(){
            modelMap.model.post('logout',function(){
                ecss.menu.menus=null;
                $.gevent.publish('logout');
                jqueryMap.$login.show();
                $('.details,.gomap,.godashboard').hide();
                jqueryMap.$confirm.hide();
            });
        });
    };
    checklogin=function(){
        modelMap.model.post('checklogin', function (data) {
            if (data == 'yes') {
                //$.gevent.publish('login');
                jqueryMap.$login.hide();
                $('.details').show();
                $('.godashboard').show();
                jqueryMap.$modal.hide();
            }
        })
    };
    gomap=function(){
        $('.godashboard').show();
        $('.gomap').hide();
        $.gevent.publish('logout');
    };
    godashboard=function(){
        $('.gomap').show();
        $('.godashboard').hide();
        $.gevent.publish('login');
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        $('.details,.gomap,.godashboard').hide();
        jqueryMap.$login.click(login);
        jqueryMap.$container.find('img').click(getCheckImg);
        jqueryMap.$logout.click(logout);
        $('.gomap').click(gomap);
        $('.godashboard').click(godashboard);
        checklogin();
    };
    return{initModule:initModule};
}());