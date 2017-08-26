/**
 * Created by jason on 2015/1/30.
 */
ecss.menu=(function(){
    'use strict';
    var configMap={
            index_html:String()
                +'<div class="list" >' +
                    '<div class="sidebar-nav1"></div>'
                +'</div>',
            board_html:String()
                +'<div class="menus">'
                    +'<div class="sidebar-nav"></div>'
                +'</div>',
            model:{
                indexMenu:[
                    {image:'multiple.png',name:'全部'},
                    {id:'A',image:'handle.png',name:'行政办公'},
                    {id:'B',image:'library.png',name:'图书馆'},
                    {id:'C',image:'teach.png',name:'教学楼'},
                    {id:'D',image:'scientific.png',name:'科研楼'},
                    {id:'E',image:'multiple.png',name:'综合楼'},
                    {id:'F',image:'space.png',name:'场馆类'},
                    {id:'G',image:'restaurant.png',name:'食堂餐厅'},
                    {id:'H',image:'wash.png',name:'集中浴室'},
                    {id:'I',image:'dormitory.png',name:'学生宿舍'},
                    {id:'J',image:'laboratory.png',name:'大型特殊实验室'},
                    {id:'K',image:'hospital.png',name:'医院'},
                    {id:'L',image:'library.png',name:'交流中心'},
                    {id:'M',image:'multiple.png',name:'其它'}
                ],
                indexMenu1:[
                    {id:'build',image:'building.png',name:'建筑',submenu:[
                    {id:'A',images:'handle.png',name:'行政办公'},
                    {id:'B',images:'library.png',name:'图书馆'},
                    {id:'C',images:'teach.png',name:'教学楼'},
                    {id:'D',images:'scientific.png',name:'科研楼'},
                    {id:'E',images:'multiple.png',name:'综合楼'},
                    {id:'F',images:'space.png',name:'场馆类'},
                    {id:'G',images:'restaurant.png',name:'食堂餐厅'},
                    {id:'H',images:'wash.png',name:'集中浴室'},
                    {id:'I',images:'dormitory.png',name:'学生宿舍'},
                    {id:'J',images:'laboratory.png',name:'大型特殊实验室'},
                    {id:'K',images:'hospital.png',name:'医院'},
                    {id:'L',images:'library.png',name:'交流中心'},
                    {id:'M',images:'multiple.png',name:'其它'}
                ]},{id:'organ',image:'organ.png',name:'机构',submenu:[]}]
            }
        },

        stateMap={
            $container: null
        },
        jqueryMap={},
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.home.loginout),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option),
            menu:null
        },
        setJqueryMap,   initIndexMenu, initBoardMenu,  makeIndexMenu,makeIndexMenu1,  makeBoardMenu;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $submenu    :$container.find('.submenu'),
            $menu       :$container.find('.menu'),
            $window     :$(window)
        };
    };

    makeBoardMenu=function($container,menuData){
        console.log(menuData);
        var $nav=$container.find('.sidebar-nav');
        $nav.html('<ul></ul>');
        var $ul=$nav.find('ul');
        var menu=function($ul,menuData) {
            var $newul=$ul;
            $.each(menuData, function (index, data) {
                if(data.code[2]!='0') return;
                $newul.append('<li></li>');
                var $li=$newul.find('li').last();
                if(data.id==null)
                    $li.append('<a></a>');
                else
                    $li.append('<a class="submenu" id='+data.id+'></a>');
                var $a=$li.find('a');
                if(data.image!=null)
                    $a.append('<i class="glyphicon glyphicon-'+data.image+'"></i>');
                $a.append(data.txt);
                if(data.submenu!=null&&data.submenu.length!=0){
                    $a.append('<i class="glyphicon arrow"></i>');
                    $li.append('<ul></ul>');
                    var $ul=$li.find('ul');
                    menu($ul,data.submenu);
                }
            });
        };
        menu($ul,menuData);
        $nav.metisMenu();
    };
    makeIndexMenu1=function($container,menuData){
        var $nav=$container.find('.sidebar-nav1');
        $nav.html('<ul></ul>');
        var $ul=$nav.find('ul');
        var menu=function($ul,menuData) {
            var $newul=$ul;
            $.each(menuData, function (index, data) {
                $newul.append('<li></li>');
                var $li=$newul.find('li').last();
                if(data.id==null)
                    $li.append('<a></a>');
                else
                    $li.append('<a class="submenu" id='+data.id+'></a>');
                var $a=$li.find('a');
                if(data.image!=null)
                    $a.append('<img src=images/'+data.image+'>');
                $a.append(data.name);
                if(data.submenu!=null&&data.submenu.length!=0){
                    $li.append('<ul></ul>');
                    var $ul=$li.find('ul');
                    menu($ul,data.submenu);
                }
            });
        };
        menu($ul,menuData);
        $nav.metisMenu();
    };
    initIndexMenu=function($container){
        stateMap.$container=$container;
        $container.html(configMap.index_html);
        modelMap.modeltools.post('getorgan',function(data){
            configMap.model.indexMenu1[1].submenu=data;
        },null,'j4_2','json',false);
        //makeIndexMenu($container,configMap.model.indexMenu);
        makeIndexMenu1($container,configMap.model.indexMenu1);
        setJqueryMap();
        jqueryMap.$submenu.click(function () {
            $.gevent.publish('getPoints',$(this).attr('id'));
        });
        $(".sidebar-nav1 li:first").toggleClass("active").children("ul").collapse("toggle");
    };
    initBoardMenu=function($container){
        stateMap.$container=$container;
        $container.html(configMap.board_html);
        modelMap.model.post('getusermenus',function(data){
            if(data.length==0){
                $.gevent.publish('logout');
                jqueryMap.$login.show();
                $('.details,.gomap,.godashboard').hide();
            }
            ecss.menu.menus=data;
            makeBoardMenu($container,data);
            setJqueryMap();
            jqueryMap.$submenu.click(function () {
                $.gevent.publish('loadpage',$(this).attr('id'));
            });
        },null,null,'json',false);
    };
    return{
        initIndexMenu:initIndexMenu,
        initBoardMenu:initBoardMenu,
        menus:modelMap.menu
    };
}());