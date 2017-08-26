/**
 * Created by jason on 2015/2/9.
 */
ecss.baseequip=(function(){
    'use strict';

    var addAdds=function(){
            jqueryMap.$addsform.add();
            jqueryMap.$addsmodal.show(function(){
                var data=[];
                data.push(jqueryMap.$addsform.save());
                jqueryMap.$dtadds.setData(data);
                jqueryMap.$addsmodal.hide();
            });
        },
        deleteAdds=function(){
            jqueryMap.$dtadds.removeSelect();
        },
        configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">设备基础</li>'
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
                    +'<table class="table table-hover table-bordered" id="DTequiptype"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getallequiptypes.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"设备大类","data": "type" },
                    { title:"子类型","data": "subtype" }
                    //{ title:"参数","data": "parameter" ,
                    //    "render":function ( data ) {
                    //        if(data != null){
                    //            if(data[0]==',') return data;  //////////要修改
                    //            var datas=data.split("@");
                    //            var parameter='';
                    //            for(var i=0;i<datas.length;i++) {
                    //                if(datas[i]!='') {
                    //                    var json = JSON.parse(datas[i]);
                    //                    console.log(json);
                    //                    parameter += i+1 + '、名称：' + json.parametername + '&nbsp&nbsp类型：' + json.parametertype + '&nbsp&nbsp内容：' + json.parameters+'<br/>';
                    //                }
                    //            }
                    //            return parameter;
                    //        }
                    //        else
                    //            return null;
                    //    }
                    //}
                ]
            },
            formOption:[
                [{label:'label',text:'类型编码',size:3},{label:'input',type:'text',name:'uuid',size:8,valid:{notEmpty: {},stringLength:{min:4,max:4}}}],
                [{label:'label',text:'设备类型',size:3},{label:'input',type:'text',name:'type',size:4,valid:{notEmpty: {}}},{label:'input',type:'text',name:'subtype',size:4,valid:{notEmpty: {}}}]
      //          [{label:'table',id:'DTadds',buttons:[{text:'添加',class:'btn-success',funcs:[{event:'click',func:addAdds}]},{text:'删除',class:'btn-danger',funcs:[{event:'click',func:deleteAdds}]}],size:12}]
            ],
            tableAddsOption:{
                "pageLength": 5,
                "columns": [
                    { title:"序号","data": null },
                    { title:"参数名称","data": "parametername" },
                    { title:"类型","data": "parametertype" },
                    { title:"待选参数","data": "parameters" }
                ],
                "dom": 'rtp'
            },
            formAddsOption:[
                [{label:'label',text:'附加参数名称',size:3},{label:'input',type:'text',name:'parametername',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'附加参数类型',size:3},{label:'select',name:'parametertype',options:[{value:'文本',text:'文本'},{value:'单选',text:'单选'},{value:'多选',text:'多选'}],size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'默认参数或选项',size:3},{label:'textarea',row:3,name:'parameters',size:8,valid:{notEmpty: {}}}]
            ]
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.equip.equiptype)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addEquipType,   updateEquipType, deleteEquipType;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'设备类型','green','',configMap.body),
            $dtequiptype    :new ecss.tools.makeTable($container.find('#DTequiptype'),configMap.tableOption),
            $equiptypemodal :new ecss.tools.makeModal($container,'设备类型信息'),
            $equiptypeform  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption,modelMap.model),
            $equiptypeconfirm:new ecss.tools.makeModal($container),
            $equiptypeadd   :$container.find('#add'),
            $equiptypeupdate:$container.find('#update'),
            $equiptypedelete:$container.find('#delete'),
            $dtadds    :new ecss.tools.makeTable($container.find('#DTadds'),configMap.tableAddsOption),
            $addsmodal :new ecss.tools.makeModal($container,'设备类型参数信息'),
            $addsform  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formAddsOption),
            $addsadd   :$container.find('#addsadd'),
            $addsdelete:$container.find('#addsdelete'),
            $window     :$(window)
        };
        jqueryMap.$dtadds.setShowNumber();
    };
    addEquipType=function(){
        jqueryMap.$equiptypeform.add();
        jqueryMap.$equiptypemodal.show(function () {
            modelMap.model.set(jqueryMap.$equiptypeform.save());
            var parameter= '';
            $.each(jqueryMap.$dtadds.getData(),function(index,data){
                parameter+=JSON.stringify(data)+'@';
            });
            modelMap.model.set({parameter:parameter});
            modelMap.model.post('add',function(){
                jqueryMap.$dtequiptype.reload();
                jqueryMap.$equiptypemodal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$equiptypemodal.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateEquipType=function(){
        var data=jqueryMap.$dtequiptype.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            modelMap.model.set(data[0]);
            console.log(data[0]);
            //if(data[0]!=null){
            //    var datas=data[0].parameter.split('@');
            //    for(var i=0;i<datas.length;i++){
            //        if(datas[i]!='')
            //            jqueryMap.$dtadds.setData(JSON.parse(datas[i]));
            //    }
            //}
            jqueryMap.$equiptypeform.update(data[0]);
            jqueryMap.$equiptypemodal.show(function () {
                modelMap.model.set(jqueryMap.$equiptypeform.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtequiptype.reload();
                    jqueryMap.$equiptypemodal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$equiptypemodal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteEquipType=function(){
        var data=jqueryMap.$dtequiptype.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.subtype}).join(",");
            jqueryMap.$equiptypeconfirm.confirm('确认删除以下区域？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtequiptype.reload();
                    jqueryMap.$equiptypeconfirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$equiptypemodal.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$equiptypeadd.click(addEquipType);
        jqueryMap.$equiptypeupdate.click(updateEquipType);
        jqueryMap.$equiptypedelete.click(deleteEquipType);
        if(ecss.menu.menus.length==7)
            $('.tablecontent .buttons').hide();
    };
    return{initModule:initModule};
}());