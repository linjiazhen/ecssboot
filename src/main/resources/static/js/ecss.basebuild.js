/**
 * Created by jason on 2015/1/30.
 */
ecss.basebuild=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">建筑基础</li>'
                     +'</ol>'
                +'</div>'
                +'<div class=" form-group">'
                    +'<div class="radio radio-success radio-inline">'
                    +'<label>'
                        +'<input type="radio" name="build" value="group" checked="">建筑区域'
                    +'</label>'
                    +'<label>'
                        +'<input type="radio" name="build" value="buildtype">建筑类型'
                    +'</label>'
                    +'<label>'
                        +'<input type="radio" name="build" value="roomtype">房间类型'
                    +'</label>'
                    +'</div>'
                +'</div>'
                +'<div id="portlet"></div>'
            +'</div>',
            body:String()
                +'<div class="tablecontent">'
                    +'<div class="buttons">'
                        +'<button class="btn btn-success btn-sm" id="add">添加</button>'
                        +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
                        +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                    +'</div>'
                    +'<table class="table table-hover table-bordered" id="DTgroup"></table>'
                    +'<table class="table table-hover table-bordered" id="DTbdtype"></table>'
                    +'<table class="table table-hover table-bordered" id="DTrmtype"></table>'
                +'</div>',
            tableGroupOption:{
                "ajax": { "url": "getallgroups.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"区域代码","data": "code" },
                    { title:"区域名","data": "name" },
                    { title:"字母别名","data": "aliasname" },
                    { title:"区域面积","data": "area" },
                    { title:"描述","data": "desc" }
                ]
            },
            formGroupOption:[
                [{label:'label',text:'区域代码',size:3},{label:'input',type:'text',name:'code',size:8,valid:{notEmpty: {},stringLength:{min:10,max:10}}}],
                [{label:'label',text:'区域名',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'字母别称',size:3},{label:'input',type:'text',name:'aliasname',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'区域面积',size:3},{label:'input',type:'text',name:'area',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'描述',size:3},{label:'textarea',row:3,name:'desc',size:8,valid:{stringLength:{min:0}}}]
            ],
            tableBdypeOption:{
                "ajax": { "url": "getallbuildtypes.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"编码","data": "id" },
                    { title:"功能类型","data": "name" },
                    { title:"附加项","data": "extra" }
                ]
            },
            formDdtypeOption:[
                [{label:'label',text:'编码',size:3},{label:'input',type:'text',name:'id',size:8,valid:{notEmpty: {},stringLength:{min:1,max:1}}}],
                [{label:'label',text:'功能类型',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'附加项',size:3},{label:'textarea',type:'tag',row:3,name:'extra',size:8}]
            ],
            tableRmtypeOption:{
                "ajax": { "url": "getallroomtypes.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"编码","data": "id" },
                    { title:"类型","data": "name" },
                    { title:"描述","data": "desc" }
                ]
            },
            formRmtypeOption:[
                [{label:'label',text:'编码',size:3},{label:'input',type:'text',name:'id',size:8,valid:{notEmpty: {},stringLength:{min:1,max:1}}}],
                [{label:'label',text:'类型',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'描述',size:3},{label:'textarea',row:3,name:'desc',size:8,valid:{stringLength:{min:0}}}]
            ]
        },
        modelMap={
            groupmodel:new ecss.tools.makeModel(ecss.model.build.group),
            bdtypemodel:new ecss.tools.makeModel(ecss.model.build.buildtype),
            rmtypemodel:new ecss.tools.makeModel(ecss.model.build.roomtype)
        },
        stateMap={
            $container: null,
            $modal    : null,
            $form     : null,
            $dttable  : null,
            model    : null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addGroup,   updateGroup, deleteGroup,switchPage;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'建筑区域','green','',configMap.body),
            $dtgroup    :new ecss.tools.makeTable($container.find('#DTgroup'),configMap.tableGroupOption),
            $modalgroup :new ecss.tools.makeModal($container,'建筑区域信息'),
            $formgroup  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formGroupOption),
            $dtbdtype   :new ecss.tools.makeTable($container.find('#DTbdtype'),configMap.tableBdypeOption),
            $modalbdtype:new ecss.tools.makeModal($container,'建筑类型信息'),
            $formbdtype :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formDdtypeOption),
            $dtrmtype   :new ecss.tools.makeTable($container.find('#DTrmtype'),configMap.tableRmtypeOption),
            $modalrmtype:new ecss.tools.makeModal($container,'房间类型信息'),
            $formrmtype :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formRmtypeOption),
            $confirm    :new ecss.tools.makeModal($container),
            $groupadd   :$container.find('#add'),
            $groupupdate:$container.find('#update'),
            $groupdelete:$container.find('#delete'),
            $radio      :$container.find('input[type=radio]'),
            $window     :$(window)
        };
        stateMap.$dttable=jqueryMap.$dtgroup;
        stateMap.$form=jqueryMap.$formgroup;
        stateMap.$modal=jqueryMap.$modalgroup;
        stateMap.model=modelMap.groupmodel;
        jqueryMap.$dtbdtype.hide();
        jqueryMap.$dtrmtype.hide();
    };
    addGroup=function(){
        stateMap.$form.add();
        stateMap.$modal.show(function () {
            stateMap.model.set(stateMap.$form.save());
            stateMap.model.post('add',function(){
                stateMap.$dttable.reload();
                stateMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(stateMap.$modal.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateGroup=function(){
        var data=stateMap.$dttable.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            stateMap.model.set(data[0]);
            stateMap.$form.update(data[0]);
            stateMap.$modal.show(function () {
                stateMap.model.set(stateMap.$form.save());
                stateMap.model.post('update',function(){
                    stateMap.$dttable.reload();
                    stateMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(stateMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteGroup=function(){
        var data=stateMap.$dttable.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.id}).join(",");
            var names=data.map(function(item){return item.name}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下区域？',names,function(){
                stateMap.model.post('del',function(){
                    stateMap.$dttable.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(stateMap.$modal.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            })
        }
    };
    switchPage=function(){
        if($(this).val()=='group'){
            stateMap.$dttable=jqueryMap.$dtgroup;
            stateMap.$form=jqueryMap.$formgroup;
            stateMap.$modal=jqueryMap.$modalgroup;
            stateMap.model=modelMap.groupmodel;
            jqueryMap.$dtbdtype.hide();
            jqueryMap.$dtrmtype.hide();
            jqueryMap.$portlet.setTitle('建筑区域');
        }
        else
            if($(this).val()=='buildtype'){
                stateMap.$dttable=jqueryMap.$dtbdtype;
                stateMap.$form=jqueryMap.$formbdtype;
                stateMap.$modal=jqueryMap.$modalbdtype;
                stateMap.model=modelMap.bdtypemodel;
                jqueryMap.$dtgroup.hide();
                jqueryMap.$dtrmtype.hide();
                jqueryMap.$portlet.setTitle('建筑类型');
            }
            else{
                stateMap.$dttable=jqueryMap.$dtrmtype;
                stateMap.$form=jqueryMap.$formrmtype;
                stateMap.$modal=jqueryMap.$modalrmtype;
                stateMap.model=modelMap.rmtypemodel;
                jqueryMap.$dtbdtype.hide();
                jqueryMap.$dtgroup.hide();
                jqueryMap.$portlet.setTitle('房间类型');
            }
        stateMap.$dttable.show();
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$groupadd.click(addGroup);
        jqueryMap.$groupupdate.click(updateGroup);
        jqueryMap.$groupdelete.click(deleteGroup);
        jqueryMap.$radio.click(switchPage);
    };
    return{initModule:initModule};
}());