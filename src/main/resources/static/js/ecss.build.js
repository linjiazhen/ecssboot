/**
 * Created by jason on 2015/1/30.
 */
ecss.build=(function(){
    'use strict';
    var makeFloors=function(){
            var data=modelMap.model.get();
            jqueryMap.$buildinfo.find('h3').html(data.name+'楼内信息');
            jqueryMap.$buildinfo.find(".floors").html('');
            modelMap.model.post('getfloors',function(data){
                $.each(data,function(i,item){
                    if(jqueryMap.$buildinfo.find(".floor").size()==0){
                        jqueryMap.$buildinfo.find(".floors").html('<div class="floor text-center" id="'+item.id+'" code="'+item.code+'"> '
                            +'<div class="title">'+item.name+'<i class="mdi-action-settings text-warning updatefloor" style="cursor: pointer;"></i></div> '
                            +'<div class="rooms tile"><div class="checkbox" ><label><input type="checkbox" id="chooseall"></label>全选<i class="mdi-content-add-circle fa-lg addrooms text-success" style="cursor: pointer;"></i><i class="mdi-content-remove-circle fa-lg deleterooms text-danger" style="cursor: pointer;"></i></div></div> '
                        +'</div>');
                    }
                    else
                        jqueryMap.$buildinfo.find(".floor:last").after('<div class="floor text-center" id="'+item.id+'" code="'+item.code+'"> '
                            +'<div class="title">'+item.name+'<i class="mdi-action-settings text-warning updatefloor" style="cursor: pointer;"></i></div> '
                            +'<div class="rooms tile"><div class="checkbox" ><label><input type="checkbox" id="chooseall"></label>全选<i class="mdi-content-add-circle fa-lg addrooms text-success" style="cursor: pointer;"></i><i class="mdi-content-remove-circle fa-lg deleterooms text-danger" style="cursor: pointer;"></i></div></div> '
                        +'</div>');
                    modelMap.roommodel.post('getrooms',function(data){
                        $.each(data,function(i,item){
                            jqueryMap.$buildinfo.find("#"+item.floorid+" .rooms").append("<div><input type='button' class='room btn btn-success inline' id="+item.id+" value="+item.name+"></div>");
                        });
                    },function(){},item.id,'json');
                });
                $.material.init();
            },function(){},data.id,'json');

        },
        initform=function(){
            var $groups=jqueryMap.$form.getElementsByName('groupid');
            modelMap.model.post('getallgroups',function(data){
                if(data!=null){
                    $groups.html('');
                    $.each(data.data,function(i,item){
                        $groups.append("<option value='"+item.id+"'>"+item.code+item.name+"</option>");
                    });
                }
            },null,null,'json');
            var $buildtype=jqueryMap.$form.getElementsByName('buildtypeid');
            modelMap.model.post('getallbuildtypes',function(data){
                if(data!=null){
                    $buildtype.html('');
                    $.each(data.data,function(i,item){
                        $buildtype.append("<option value='"+item.id+"'>"+item.id+item.name+"</option>");
                    });
                }
            },null,null,'json');
            var $roomtype=jqueryMap.$roomform.getElementsByName('roomtypeid');
            modelMap.roommodel.post('getallroomtypes',function(data){
                if(data!=null){
                    $roomtype.html('');
                    $.each(data.data,function(i,item){
                        $roomtype.append("<option value='"+item.id+"'>"+item.id+item.name+"</option>");
                    });
                }
            },null,null,'json');

        },
        configMap={
            main_html:String()
                +'<div class="col-md-12" id="buildinfo" style="display:none">'
                    +'<h3 class="text-center"></h3>'
                    +'<div class="col-md-12">'
                        +'<div class="btn-group">'
                            +'<button type="button" class="btn btn-success btn-sm dropdown-toggle" data-toggle="dropdown">添加 <span class="caret"></span></button>'
                            +'<ul class="dropdown-menu" role="menu">'
                                +'<li><a class="addupfloor">地上层</a></li>'
                                +'<li><a class="adddownfloor">地下层</a></li>'
                                +'<li><a class="addzerofloor">架空层</a></li>'
                            +'</ul>'
                        +'</div>'
                        +'<div class="btn-group">'
                            +'<button type="button" class="btn btn-danger btn-sm dropdown-toggle" data-toggle="dropdown">删除 <span class="caret"></span></button>'
                            +'<ul class="dropdown-menu" role="menu">'
                                +' <li><a class="deleteupfloor">地上层</a></li>'
                                +'<li><a class="deletedownfloor">地下层</a></li>'
                                +'<li><a class="deletezerofloor">架空层</a></li>'
                            +'</ul>'
                        +'</div>'
                        +'<button class="btn btn-info btn-sm returnbuild ">返回</button>'
                    +' </div>'
                    +' <div class="floors">'
                    +' </div>'
                +'</div>'
                +'<div class="col-md-12" id="build">'
                    +'<div class="page-title">'
                        +'<h4 class="pull-right strong" id="datetime"></h4>'
                        +'<ol class="breadcrumb">'
                            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                            +'<li class="active">建筑档案</li>'
                        +'</ol>'
                    +'</div>'
                    +'<div id="portlet"></div>'
                +'</div>',
                body:String()
                    +'<div class="form-group">'
                        +'<lable class="col-md-1 control-label">条件筛选:</lable>'
                        +'<div class="col-md-2"><select name="group_choice" class="form-control"></select></div><div class="col-md-2"><select name="buildtype_choice" class="form-control"></select></div>'
                    +'</div>'
                    +'<div class="tablecontent" style="margin-top:45px">'
                        +'<div class="buttons">'
                            +'<button class="btn btn-success btn-sm" id="add">添加</button>'
                            +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
                            +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                        +'</div>'
                        +'<table class="table table-hover table-bordered" id="DTbuild"></table>'
                    +'</div>',
            tableOption:{
                "ajax": { "url": "getallbuilds.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"建筑编号","data": "code" },
                    { title:"建筑名称","data": "name" },
                    { title:"所属区域","data": "group"},
                    { title:"功能类型","data": "buildtype" },
                    { title:"总面积","data": "area" },
                    { title:"总人数","data": "people" },
                    { title:"空调面积","data": "airarea" },
                    { title:"建设年代","data": "year" },
                    { title:"地上层数","data": "upfloor" },
                    { title:"地下层数","data": "downfloor" },
                    { title:"架空层","data": "zerofloor" }
                ],
                "order": [[ 1, 'asc' ]],
                "initComplete":function(){
                    var api=this.api();
                    jqueryMap.$groupchoice.html('<option value="">所属区域</option>');
                    api.column(3).data().unique().sort().each( function ( data ) {
                        jqueryMap.$groupchoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$groupchoice.on( 'change', function () {
                        api.column(3).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$bdtypechoice.html('<option value="">功能类型</option>');
                    api.column(4).data().unique().sort().each( function ( data ) {
                        jqueryMap.$bdtypechoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$bdtypechoice.on( 'change', function () {
                        api.column(4).search( $(this).val() ).draw();
                    } );
                    initform();
                }
            },
            formOption:[
                [{label:'label',text:'所属区域',size:3},{label:'select',name:'groupid',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'建筑名称',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'建筑编号',size:3},{label:'input',type:'text',name:'code',size:8,valid:{notEmpty: {},stringLength:{min:10,max:10}}}],
                [{label:'label',text:'功能类型',size:3},{label:'select',name:'buildtypeid',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'总面积',size:3},{label:'input',type:'text',name:'area',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'总人数',size:3},{label:'input',type:'text',name:'people',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'空调面积',size:3},{label:'input',type:'text',name:'airarea',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'建设年代',size:3},{label:'input',type:'text',name:'year',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'架空层',size:3},{label:'input',type:'radio',name:'zerofloor',options:[{value:1,text:'有'},{value:0,text:'无'}],size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'地上层数',size:3},{label:'input',type:'text',name:'upfloor',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'地下层数',size:3},{label:'input',type:'text',name:'downfloor',size:8,valid:{notEmpty: {}}}]
            ],
            floorformOption:[
                [{label:'label',text:'面积',size:3},{label:'input',type:'text',name:'area',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'人数',size:3},{label:'input',type:'text',name:'people',size:8,valid:{notEmpty: {}}}]
            ],
            roomformOption:[
                [{label:'label',text:'房间名称',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'房间类型',size:3},{label:'select',name:'roomtypeid',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'人数',size:3},{label:'input',type:'text',name:'people',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'面积',size:3},{label:'input',type:'text',name:'area',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'创建房间数',size:3},{label:'input',type:'text',name:'roomnum',size:8,valid:{notEmpty: {}}}]
            ]
        },
        stateMap={
            $container:null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.build.build),
            roommodel:new ecss.tools.makeModel(ecss.model.build.room)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addBuild,   updateBuild, deleteBuild,  addEvent;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'建筑列表','green','',configMap.body),
            $dtbuild    :new ecss.tools.makeTable($container.find('#DTbuild'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'建筑楼栋信息'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $floormodal      :new ecss.tools.makeModal($container,'楼层信息'),
            $floorform       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.floorformOption),
            $roommodal      :new ecss.tools.makeModal($container,'房间信息'),
            $roomform       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.roomformOption),
            $confirm     :new ecss.tools.makeModal($container),
            $groupchoice:$container.find('select[name=group_choice]'),
            $bdtypechoice:$container.find('select[name=buildtype_choice]'),
            $build      :$container.find('#build'),
            $buildinfo  :$container.find('#buildinfo'),
            $buildadd   :$container.find('#add'),
            $buildupdate:$container.find('#update'),
            $builddelete:$container.find('#delete'),
            $window     :$(window)
        };
    };
    addEvent=function(){
        var data=modelMap.model.get();
        jqueryMap.$container.find('#DTbuild').delegate('tbody tr','dblclick',function(){
            modelMap.model.set(jqueryMap.$dtbuild.getData($(this))[0]);
            jqueryMap.$build.hide();
            makeFloors();
            jqueryMap.$buildinfo.show();
        });
        jqueryMap.$buildinfo.find('.returnbuild').click(function () {
            jqueryMap.$buildinfo.hide();
            jqueryMap.$build.show();
        });
        jqueryMap.$buildinfo.find(".addupfloor").click(function(){
            var code=jqueryMap.$buildinfo.find('.floor:first').attr('code');
            if(code==null||code<0) code=0;
            modelMap.model.post('addfloor',function(){
                makeFloors();
            },function(){},{code:parseInt(code)+1,name:(parseInt(code)+1)+'层',buildid:data.id});
        });
        jqueryMap.$buildinfo.find(".adddownfloor").click(function(){
            var code=jqueryMap.$buildinfo.find('.floor:last').attr('code');
            if(code==null||code>0) code=0;
            modelMap.model.post('addfloor',function(){
                makeFloors();
            },function(){},{code:parseInt(code)-1,name:'负'+(1-parseInt(code))+'层',buildid:data.id});
        });
        jqueryMap.$buildinfo.find(".addzerofloor").click(function(){
            if(jqueryMap.$buildinfo.find('.floor[code=0]').length==0)
                modelMap.model.post('addfloor',function(){
                    makeFloors();
                },function(){},{code:0,name:'架空层',buildid:data.id});
            else{
                ZENG.msgbox.show('有架空层！',1,3000);
            }
        });
        jqueryMap.$buildinfo.find(".deleteupfloor").click(function(){
            var id=jqueryMap.$buildinfo.find('.floor:first').attr('id');
            var name=jqueryMap.$buildinfo.find('.floor:first').find('.title').text();
            var code=jqueryMap.$buildinfo.find('.floor:first').attr('code');
            if(code<=0){
                ZENG.msgbox.show('无地上层！',1,3000);
            }
            else
                jqueryMap.$confirm.confirm('确认删除以下楼层？',name,function(){
                    modelMap.model.post('deletefloor',function(){
                        jqueryMap.$buildinfo.find('.floor:first').remove();
                        jqueryMap.$confirm.hide();
                    },function(){
                        ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                    },id+',upfloor');
                });
        });
        jqueryMap.$buildinfo.find(".deletedownfloor").click(function(){
            var id=jqueryMap.$buildinfo.find('.floor:last').attr('id');
            var name=jqueryMap.$buildinfo.find('.floor:last').find('.title').text();
            var code=jqueryMap.$buildinfo.find('.floor:last').attr('code');
            if(code>=0){
                ZENG.msgbox.show('无地下层！',1,3000);
            }
            else
                jqueryMap.$confirm.confirm('确认删除以下楼层？',name,function(){
                    modelMap.model.post('deletefloor',function(){
                        jqueryMap.$buildinfo.find('.floor:last').remove();
                        jqueryMap.$confirm.hide();
                    },function(){
                        ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                    },id+',downfloor');
                });
        });
        jqueryMap.$buildinfo.find(".deletezerofloor").click(function(){
            var id=jqueryMap.$buildinfo.find('.floor[code=0]').prop('id');
            if(jqueryMap.$buildinfo.find('.floor[code=0]').length==0){
                ZENG.msgbox.show('无架空层！',1,3000);
            }
            else
                jqueryMap.$confirm.confirm('确认删除以下楼层？','架空层',function(){
                    modelMap.model.post('deletefloor',function(){
                        jqueryMap.$buildinfo.find('.floor[code=0]').remove();
                        jqueryMap.$confirm.hide();
                    },function(){
                        ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                    },id+',zerofloor');
                });
        });
        jqueryMap.$buildinfo.find(".floors").delegate('.addrooms','click',function(){
            jqueryMap.$roomform.add();
            jqueryMap.$roomform.showItemByName('roomnum');
            var id=$(this).parents('.floor').attr('id');
            modelMap.roommodel.set({floorid:id});
            jqueryMap.$roommodal.show(function () {
                modelMap.roommodel.set(jqueryMap.$roomform.save());
                modelMap.roommodel.post('addrooms',function(){
                    makeFloors();
                    jqueryMap.$roommodal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$roommodal.getElementsById('save'),'添加失败！请刷新重试！');
                });
            });
        });
        jqueryMap.$buildinfo.find(".floors").delegate('.updatefloor','click',function(){
            var id=$(this).parents('.floor').prop('id');
            modelMap.model.post('getfloor',function(data){
                console.log(data);
                jqueryMap.$floorform.update(data);
                jqueryMap.$floormodal.show(function () {
                    var para=jqueryMap.$floorform.save();
                    para.id=id;
                    modelMap.model.post('updatefloor',function(){
                        jqueryMap.$floormodal.hide();
                    },function(){
                        ecss.tools.popover.show(jqueryMap.$floormodal.getElementsById('save'),'修改失败！请刷新重试！');
                    },para);
                });
            },function(){
                ecss.tools.popover.show($(this),'获取房间失败！');
            },id,'json');
        });
        jqueryMap.$buildinfo.find(".floors").delegate('#chooseall','click',function(){
            if($(this).prop('checked'))
                $(this).parents('.floor').find('.btn-success').removeClass('btn-success').addClass('btn-danger');
            else
                $(this).parents('.floor').find('.btn-danger').removeClass('btn-danger').addClass('btn-success');
        });
        jqueryMap.$buildinfo.find(".floors").delegate('.room','click',function(){
            $(this).toggleClass('btn-success');
            $(this).toggleClass('btn-danger');
        })
        jqueryMap.$buildinfo.find(".floors").delegate('.room','dblclick',function(){
            modelMap.roommodel.post('getroom',function(data){
                modelMap.roommodel.set(data);
                jqueryMap.$roomform.update(data);
                jqueryMap.$roomform.hideItemByName('roomnum');
                jqueryMap.$roommodal.show(function () {
                    modelMap.roommodel.set(jqueryMap.$roomform.save());
                    modelMap.roommodel.post('updateroom',function(){
                        makeFloors();
                        jqueryMap.$roommodal.hide();
                    },function(){
                        ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                    });
                });
            },function(){
                ecss.tools.popover.show($(this),'获取房间失败！');
            },$(this).attr('id'),'json');
        });
        jqueryMap.$buildinfo.find(".floors").delegate('.deleterooms','click',function(){
            var data=$(this).parents('.floor').find('.room.btn-danger');
            if(data.length==0)
                ecss.tools.popover.show($(this),'请至少选择一个房间！');
            else {
                var ids=[],names=[];
                $.each(data,function(index,item){
                    ids.push($(item).attr('id'));
                    names.push($(item).attr('value'));
                });
                jqueryMap.$confirm.confirm('确认删除以下房间？',names.join(','),function(){
                    modelMap.roommodel.post('deleterooms',function(){
                        makeFloors();
                        jqueryMap.$confirm.hide();
                    },function(){
                        ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                    },ids.join(','));
                });
            }
        });

    };
    addBuild=function(){
        jqueryMap.$form.showItemByName('upfloor');
        jqueryMap.$form.showItemByName('zerofloor');
        jqueryMap.$form.showItemByName('downfloor');
        jqueryMap.$form.add();
        jqueryMap.$modal.show(function () {
            modelMap.model.set(jqueryMap.$form.save());
            modelMap.model.post('add',function(){
                jqueryMap.$dtbuild.reload();
                jqueryMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateBuild=function(){
        var data=jqueryMap.$dtbuild.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            jqueryMap.$form.hideItemByName('upfloor');
            jqueryMap.$form.hideItemByName('zerofloor');
            jqueryMap.$form.hideItemByName('downfloor');
            modelMap.model.set(data[0]);
            jqueryMap.$form.update(data[0]);
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtbuild.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteBuild=function(){
        var data=jqueryMap.$dtbuild.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.id}).join(",");
            var names=data.map(function(item){return item.name}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下楼栋？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtbuild.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        jqueryMap.$buildadd.click(addBuild);
        jqueryMap.$buildupdate.click(updateBuild);
        jqueryMap.$builddelete.click(deleteBuild);
        if(ecss.menu.menus.length==7)
            $('.tablecontent .buttons').hide();
    };
    return{initModule:initModule};
}());