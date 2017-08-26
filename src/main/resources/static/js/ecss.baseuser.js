/**
 * Created by jason on 2015/1/30.
 */
ecss.baseuser=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">用户信息</li>'
                     +'</ol>'
                +'</div>'
                +'<div class=" form-group">'
                    +'<div class="radio radio-success radio-inline">'
                    +'<label>'
                        +'<input type="radio" name="user" value="userlist" checked="">用户列表'
                    +'</label>'
                    +'<label>'
                        +'<input type="radio" name="user" value="rolelist">用户角色'
                    +'</label>'
                    +'<label>'
                        +'<input type="radio" name="user" value="menulist">菜单设置'
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
                    +'<table class="table table-hover table-bordered" id="DTuser"></table>'
                    +'<table class="table table-hover table-bordered" id="DTrole"></table>'
                    +'<table class="table table-hover table-bordered" id="DTmenu"></table>'
                +'</div>',
            tableUserOption:{
                "ajax": { "url": "getallusers.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"账号","data": "account" },
                    { title:"角色","data": "role" },
                    { title:"姓名","data": "name" },
                    { title:"电话","data": "phone" },
                    { title:"邮箱","data": "email" },
                    { title:"备注","data": "remark" }
                ]
            },
            formUserOption:[
                [{label:'label',text:'账号',size:3},{label:'input',type:'text',name:'account',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'密码',size:3},{label:'input',type:'password',name:'passwd',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'角色',size:3},{label:'select',name:'roleid',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'姓名',size:3},{label:'input',type:'text',name:'name',size:8,valid:{}}],
                [{label:'label',text:'电话',size:3},{label:'input',type:'text',name:'phone',size:8,valid:{}}],
                [{label:'label',text:'邮箱',size:3},{label:'input',type:'text',name:'email',size:8,valid:{}}],
                [{label:'label',text:'备注',size:3},{label:'input',type:'text',name:'remark',size:8,valid:{}}]
            ],
            tableRoleOption:{
                "ajax": { "url": "getallroles.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"角色名","data": "name" },
                    { title:"菜单权限","data": "uuid" ,
                        render: function(data){
                            return '<div class="disclick"><i class="glyphicon glyphicon-cog setright" id="'+data+'" style="cursor: pointer"></i></div>';
                        }}
                ],
                "initComplete":function(){
                    $('.setright').click(updateRight);
                }
            },
            formRoleOption:[
                [{label:'label',text:'角色名',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}]
            ],
            tableMenuOption:{
                "ajax": { "url": "getallmenus.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"菜单编码","data": "code" },
                    { title:"菜单名","data": "name" },
                    { title:"级别","data": "levels" },
                    { title:"上级菜单","data": "upmenu",
                        render: function(data){
                            if(data==null)
                            return '无';
                            else
                            return data;
                    } },
                    { title:"功能名","data": "pagename" ,
                        render: function(data){
                            if(data==null)
                                return '无';
                            else
                            return data;
                        } },
                    { title:"图标","data": "image",
                        render: function(data){
                            return '<i class="glyphicon glyphicon-'+data+'"></i>';
                        }}
                ]
            },
            formMenuOption:[
                [{label:'label',text:'菜单编码',size:3},{label:'input',type:'text',name:'code',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'菜单名',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'级别',size:3},{label:'select',name:'levels',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'上级菜单',size:3},{label:'select',name:'upmenuid',size:8,valid:{}}],
                [{label:'label',text:'功能名',size:3},{label:'input',type:'text',name:'pagename',size:8,valid:{}}],
                [{label:'label',text:'图标',size:3},{label:'input',name:'image',size:8,valid:{}}]
            ],
            menuTreeOption : {
                core:{
                    data:{
                        url:'getmenutree.do'
                    }
                },
                checkbox:{
                    three_state:false
                },
                plugins:['checkbox','search']
            }
        },
        modelMap={
            usermodel:new ecss.tools.makeModel(ecss.model.user.user),
            rolemodel:new ecss.tools.makeModel(ecss.model.user.role),
            menumodel:new ecss.tools.makeModel(ecss.model.user.menu)
        },
        stateMap={
            $container: null,
            $modal    : null,
            $form     : null,
            $dttable  : null,
            model    : null
        },
        jqueryMap={},
        setJqueryMap,   initModule,updateRight,  addGroup,   updateGroup, deleteGroup,switchPage,initSelect;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'用户信息','green','',configMap.body),
            $dtuser    :new ecss.tools.makeTable($container.find('#DTuser'),configMap.tableUserOption),
            $modaluser :new ecss.tools.makeModal($container,'用户信息'),
            $formuser  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formUserOption),
            $dtrole   :new ecss.tools.makeTable($container.find('#DTrole'),configMap.tableRoleOption),
            $modalrole:new ecss.tools.makeModal($container,'角色信息'),
            $formrole :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formRoleOption),
            $dtmenu   :new ecss.tools.makeTable($container.find('#DTmenu'),configMap.tableMenuOption),
            $modalmenu:new ecss.tools.makeModal($container,'菜单信息'),
            $formmenu :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formMenuOption),
            $modalright :new ecss.tools.makeModal($container,'权限管理'),
            $righttree :new ecss.tools.makeTree($container.find('.modal-body').last(),configMap.menuTreeOption,540),
            $confirm    :new ecss.tools.makeModal($container),
            $useradd   :$container.find('#add'),
            $userupdate:$container.find('#update'),
            $userdelete:$container.find('#delete'),
            $radio      :$container.find('input[type=radio]'),
            $window     :$(window)
        };
        stateMap.$dttable=jqueryMap.$dtuser;
        stateMap.$form=jqueryMap.$formuser;
        stateMap.$modal=jqueryMap.$modaluser;
        stateMap.model=modelMap.usermodel;
        jqueryMap.$dtrole.hide();
        jqueryMap.$dtmenu.hide();
    };
    updateRight=function(){
        var id=$(this).prop('id');
        modelMap.rolemodel.post('getright',function(data){
            jqueryMap.$righttree.deselect_node();
            jqueryMap.$righttree.select_node(data);
        },function(){
            ZENG.msgbox.show('获取权限失败！请刷新重试！',5,3000);
        },id,'json');
        jqueryMap.$modalright.show(function () {
            var ids=id+'#'+jqueryMap.$righttree.get_selected().map(function(item){return item.id}).join(',');
            modelMap.rolemodel.post('updateright',function(){
                jqueryMap.$modalright.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modalright.getElementsById('save'),'更新权限失败！请刷新重试！');
            },ids);
        });
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
        if(stateMap.$dttable==jqueryMap.$dtmenu)
            modelMap.menumodel.post('getmenusbylevel',function(data){
                $.each(data,function(i,item){
                    jqueryMap.$formmenu.getElementsByName('upmenuid').append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            },null,(data[0].levels-1).toString(),'json',false);
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
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.name}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下信息？',names,function(){
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
        if($(this).val()=='userlist'){
            stateMap.$dttable=jqueryMap.$dtuser;
            stateMap.$form=jqueryMap.$formuser;
            stateMap.$modal=jqueryMap.$modaluser;
            stateMap.model=modelMap.usermodel;
            jqueryMap.$dtrole.hide();
            jqueryMap.$dtmenu.hide();
            jqueryMap.$portlet.setTitle('用户列表');
        }
        else
            if($(this).val()=='rolelist'){
                stateMap.$dttable=jqueryMap.$dtrole;
                stateMap.$form=jqueryMap.$formrole;
                stateMap.$modal=jqueryMap.$modalrole;
                stateMap.model=modelMap.rolemodel;
                jqueryMap.$dtuser.hide();
                jqueryMap.$dtmenu.hide();
                jqueryMap.$portlet.setTitle('角色列表');
            }
            else{
                stateMap.$dttable=jqueryMap.$dtmenu;
                stateMap.$form=jqueryMap.$formmenu;
                stateMap.$modal=jqueryMap.$modalmenu;
                stateMap.model=modelMap.menumodel;
                jqueryMap.$dtrole.hide();
                jqueryMap.$dtuser.hide();
                jqueryMap.$portlet.setTitle('菜单管理');
            }
        stateMap.$dttable.show();
    };
    initSelect=function(){
        jqueryMap.$formmenu.getElementsByName('levels').html('<option value=1>1</option><option value=2>2</option><option value=3>3</option>');
        jqueryMap.$formmenu.getElementsByName('upmenuid').html("<option value=''>无</option>");
        modelMap.rolemodel.post('getroles',function(data){
            $.each(data,function(i,item){
                jqueryMap.$formuser.getElementsByName('roleid').append("<option value='"+item.id+"'>"+item.name+"</option>");
            });
        },null,null,'json');
        jqueryMap.$formmenu.getElementsByName('levels').change(function(){
            if($(this).val()!=1){
                modelMap.menumodel.post('getmenusbylevel',function(data){
                    jqueryMap.$formmenu.getElementsByName('upmenuid').html("<option value=''>无</option>");
                    $.each(data,function(i,item){
                        jqueryMap.$formmenu.getElementsByName('upmenuid').append("<option value='"+item.id+"'>"+item.name+"</option>");
                    });
                },null,($(this).val()-1).toString(),'json');
            }
            else{
                jqueryMap.$formmenu.getElementsByName('upmenu').html("<option value=''>无</option>");
            }
        });
        //jqueryMap.$formmenu.getElementsByName('pagename').html("<option value=''>无</option>");
        //$.each(ecss,function(key,item){
        //    jqueryMap.$formmenu.getElementsByName('pagename').append("<option value='"+key+"'>"+key+"</option>");
        //});
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        initSelect();
        jqueryMap.$useradd.click(addGroup);
        jqueryMap.$userupdate.click(updateGroup);
        jqueryMap.$userdelete.click(deleteGroup);
        jqueryMap.$radio.click(switchPage);
    };
    return{initModule:initModule};
}());