/**
 * Created by jason on 2015/1/30.
 */
ecss.shell=(function(){
    'use strict';
    var configMap={
            main_html:String()
                +'<div class="head"></div>'
                +'<div class="sidebarleft"></div>'
                +'<div class="pagecontent"></div>'
        },
        stateMap={
            $container: null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.user.log)
        },
    jqueryMap={},
    setJqueryMap,
    initModule,
        logtext,
    login,
    logout,
    loadpage;

    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $head       :$container.find('.head'),
            $sidebarleft:$container.find('.sidebarleft'),
            $pagecontent:$container.find('.pagecontent'),
            $window     :$(window)
        };
    };

    login=function(){
        ecss.menu.initBoardMenu(jqueryMap.$sidebarleft);
        ecss.dashboard.initModule(jqueryMap.$pagecontent);
    };
    logout=function(){
        ecss.menu.initIndexMenu(jqueryMap.$sidebarleft);
        ecss.map.initModule(jqueryMap.$pagecontent);
    };
    loadpage=function(event,name){
        var code;
        ecss[name].initModule(jqueryMap.$pagecontent);
        $('.tablecontent .buttons .btn:not(.tablecontent .dt-buttons .btn)').hide();
        $.each(ecss.menu.menus,function(i,menu){
            $.each(menu.submenu, function (j, item) {
                    if(item.id==name)
                    code=item.code.substring(0,2);
                else
                if (item.code.substring(0, 2) ==code)
                    $('#' + item.id).show();
            });
        });
        //$('.input-group-addon').show();
        $.material.init();
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        ecss.head.initModule(jqueryMap.$head);
        ecss.menu.initIndexMenu(jqueryMap.$sidebarleft);
        ecss.map.initModule(jqueryMap.$pagecontent);
        $.gevent.subscribe(jqueryMap.$container,'login',login);
        $.gevent.subscribe(jqueryMap.$container,'logout',logout);
        $.gevent.subscribe(jqueryMap.$container,'loadpage',loadpage);
        $('.pagecontent').delegate('.btn:not(.modal .btn)','click',function(){
            logtext=$(this).parents('.pagecontent').find('.breadcrumb .active').text()+'-'+$(this).text();
        });
        $('.pagecontent').delegate('.modal #save','click',function(){
            if(logtext)
            modelMap.model.post('add',null,function(){
                console.log('添加日志出错！');
            },logtext);
        });
    };
    return{initModule:initModule};
}());