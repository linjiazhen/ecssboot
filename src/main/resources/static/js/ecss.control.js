/**
 * Created by jason on 2015/1/30.
 */
ecss.control=(function(){
    'use strict';
    var makeFloors=function(id){
            var data=modelMap.model.get();
            jqueryMap.$buildinfo.find(".floors").html('');
            modelMap.model.post('getfloors',function(data){
                $.each(data,function(i,item){
                    if(jqueryMap.$buildinfo.find(".floor").size()==0){
                        jqueryMap.$buildinfo.find(".floors").html('<div class="floor text-center" id="'+item.id+'" code="'+item.code+'"> '
                            +'<div class="title">'+item.name+'</div> '
                            +'<div class="rooms tile"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div> '
                        +'</div>');
                    }
                    else
                        jqueryMap.$buildinfo.find(".floor:last").after('<div class="floor text-center" id="'+item.id+'" code="'+item.code+'"> '
                            +'<div class="title">'+item.name+'</div> '
                            +'<div class="rooms tile"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div> '
                        +'</div>');
                    modelMap.roommodel.post('getrooms',function(data){
                        $.each(data,function(i,item){
                            jqueryMap.$buildinfo.find("#"+item.floorid+" .rooms").append('<div id='+item.id+' class="col-lg-3"></div>');
                            ecss.tools.makePortlet(jqueryMap.$buildinfo.find("#"+item.id),item.name,'green','','<div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div>');
                        });
                        $.material.init();
                    },function(){},item.id,'json');
                });
                $.material.init();
            },function(){},id,'json');
        },
        makeGroups=function(){
            var data=modelMap.model.get();
            jqueryMap.$buildinfo.find(".floors").html('');
            modelMap.modeltools.post('getgroup',function(data){
                $.each(data,function(i,item){
                    if(jqueryMap.$buildinfo.find(".floor").size()==0){
                        jqueryMap.$buildinfo.find(".floors").html('<div class="floor text-center" id="'+item.id+'" > '
                            +'<div class="title">'+item.name+'</div> '
                            +'<div class="rooms tile"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div> '
                            +'</div>');
                    }
                    else
                        jqueryMap.$buildinfo.find(".floor:last").after('<div class="floor text-center" id="'+item.id+'"> '
                            +'<div class="title">'+item.name+'</div> '
                            +'<div class="rooms tile"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div> '
                            +'</div>');
                    modelMap.modeltools.post('getbuild',function(data){
                        $.each(data,function(i,item1){
                            jqueryMap.$buildinfo.find("#"+item.id+" .rooms").append('<div id='+item1.id+' class="col-lg-3"></div>');
                            ecss.tools.makePortlet(jqueryMap.$buildinfo.find("#"+item1.id),item1.name,'green','','<div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div>');
                        });
                        $.material.init();
                    },function(){},item.id,'json');
                });
                $.material.init();
            },function(){},null,'json');
        },
        configMap={
            main_html:String()
                +'<div class="col-md-12" id="build">'
                    +'<div class="page-title">'
                        +'<ol class="breadcrumb">'
                            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                            +'<li class="active">水电控制</li>'
                        +'</ol>'
                    +'</div>'
                    +'<div style="height: 40px" class="form-control">'
                        +'<lable class="col-md-1 control-label">楼宇选择:</lable>'
                        +'<div class="col-md-2"><select name="group" class="form-control"></select></div><div class="col-md-2"><select name="build" class="form-control"></select></div>'
                    +'</div>'
                    +'<div id="portlet"></div>'
                +'</div>',
            body:'<div id="buildinfo">'
            +' <div class="floors">'
            +' </div>'
            +'</div>',
            roomformOption:[
                [{label:'label',text:'房间名称',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'房间类型',size:3},{label:'select',name:'roomtypeid',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'人数',size:3},{label:'input',type:'text',name:'num',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'面积',size:3},{label:'input',type:'text',name:'area',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'创建房间数',size:3},{label:'input',type:'text',name:'roomnum',size:8,valid:{notEmpty: {}}}],
            ]
        },
        stateMap={
            $container:null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.build.build),
            roommodel:new ecss.tools.makeModel(ecss.model.build.room),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addBuild,   updateBuild, deleteBuild,  addEvent,addOptions;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'全校','green','<div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div>',configMap.body),
            $confirm     :new ecss.tools.makeModal($container),
            $buildinfo  :$container.find('#buildinfo'),
            $group:$container.find('select[name=group]'),
            $build:$container.find('select[name=build]'),
            $window     :$(window)
        };
    };
    addOptions=function(url,para,$select,text){
        modelMap.modeltools.post(url,function(data){
            if(data!=null){
                $select.html(text);
                $.each(data,function(i,item){
                    $select.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,para,'json');
    };
    addEvent=function(){
        addOptions('getgroup',null,jqueryMap.$group, '<option value="#">选择区域</option>');
        jqueryMap.$build.html('<option value="#">全区</option>');
        jqueryMap.$group.change(function(){
            if($(this).val()=='#'){
                makeGroups();
                jqueryMap.$portlet.setTitle('全校');
                jqueryMap.$build.html('<option value="#">全区</option>');
            }
            else
                addOptions('getbuild',jqueryMap.$group.val(),jqueryMap.$build,'<option value="#">选择建筑</option>');
        });
        jqueryMap.$build.change(function(){
            makeFloors($(this).val());
            jqueryMap.$portlet.setTitle($(this).find("option:selected").text());
        });
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        makeGroups();
    };
    return{initModule:initModule};
}());