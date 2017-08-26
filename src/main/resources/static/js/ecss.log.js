/**
 * Created by jason on 2015/1/30.
 */
ecss.log=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">操作日志</li>'
                    +'</ol>'
                +'</div>'
                +'<div id="portlet"></div>'
            +'</div>',
            body:String()
                +'<div class="tablecontent">'
                    +'<div class="buttons">'
                    +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                    +'</div>'
                    +'<table class="table table-hover table-bordered" id="DTlog"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getlogs.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"用户ID","data": "userid" },
                    { title:"用户名","data": "username" },
                    { title:"操作","data": "operate" },
                    { title:"操作时间","data": "time" },
                    { title:"IP","data": "ip" }
                ],
                "order": [[ 4, 'desc' ]]
            }
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.user.log)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule, deleteLog;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'操作日志','green','',configMap.body),
            $dtlog    :new ecss.tools.makeTable($container.find('#DTlog'),configMap.tableOption),
            $confirm     :new ecss.tools.makeModal($container),
            $logdelete  :$container.find('#delete'),
            $window     :$(window)
        };
    };
    deleteLog=function(){
        var data=jqueryMap.$dtlog.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.id}).join(",");
            var names=data.map(function(item){return item.userid}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下记录？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtlog.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$logdelete.click(deleteLog);
    };
    return{initModule:initModule};
}());