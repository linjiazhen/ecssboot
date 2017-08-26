/**
 * Created by jason on 2015/1/30.
 */
ecss.baseorgan=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">机构基础</li>'
                    +'</ol>'
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
                    +'<table class="table table-hover table-bordered" id="DTorgantype"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getallorgantypes.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"类型代码","data": "id" },
                    { title:"类型名","data": "name" },
                    { title:"描述","data": "desc" }
                ]
            },
            formOption:[
                [{label:'label',text:'类型代码',size:3},{label:'input',type:'text',name:'id',size:8,valid:{notEmpty: {},stringLength:{min:1,max:1}}}],
                [{label:'label',text:'类型名',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'描述',size:3},{label:'textarea',row:3,name:'desc',size:8,valid:{stringLength:{min:0}}}]
            ]
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.organ.organtype)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addOrganType,   updateOrganType, deleteOrganType;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'机构类型','green','',configMap.body),
            $dtorgantype    :new ecss.tools.makeTable($container.find('#DTorgantype'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'机构类型信息'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm     :new ecss.tools.makeModal($container),
            $organtypeadd   :$container.find('#add'),
            $organtypeupdate:$container.find('#update'),
            $organtypedelete:$container.find('#delete'),
            $window     :$(window)
        };
    };
    addOrganType=function(){
        jqueryMap.$form.add();
        jqueryMap.$modal.show(function () {
            modelMap.model.set(jqueryMap.$form.save());
            modelMap.model.post('add',function(){
                jqueryMap.$dtorgantype.reload();
                jqueryMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateOrganType=function(){
        var data=jqueryMap.$dtorgantype.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            modelMap.model.set(data[0]);
            jqueryMap.$form.update(data[0]);
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtorgantype.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteOrganType=function(){
        var data=jqueryMap.$dtorgantype.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.id}).join(",");
            var names=data.map(function(item){return item.name}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下区域？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtorgantype.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            })
        }
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$organtypeadd.click(addOrganType);
        jqueryMap.$organtypeupdate.click(updateOrganType);
        jqueryMap.$organtypedelete.click(deleteOrganType);
        if(ecss.menu.menus.length==7)
            $('.tablecontent .buttons').hide();
    };
    return{initModule:initModule};
}());