/**
 * Created by jason on 2015/1/30.
 */
ecss.baseenergy=(function(){
    'use strict';
    var getUpItems=function(event,level){
            var level=level!=null?String(level-1):String($(this).val()-1);
            var $select=jqueryMap.$form.getElementsByName('parentitemname');
            if($(this).val()-1==0){
                $select.html("<option value=0>最高能耗级别</option>");
                return;
            }
            modelMap.model.post('getparent',function(data){
                if(data!=null){
                    $select.html('');
                    $.each(data,function(i,item){
                        $select.append("<option value='"+item.item_code+","+item.item_name+"'>"+item.item_code+item.item_name+"</option>");
                    });
                }
            },null,level,'json',false);
        },
        configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">能耗基础</li>'
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
                    +'<table class="table table-hover table-bordered" id="DTenergytype"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getallenergytypes.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"分类分项级别","data": "itemlevel" },
                    { title:"分类分项代码","data": "itemcode" },
                    { title:"分类分项名称","data": "itemname" ,"className": 'data-left'},
                    { title:"上级分项代码","data": "parentitemcode" },
                    { title:"上级分项名称","data": "parentitemname" },
                    { title:"能耗类型","data": "itemtype" },
                    { title:"计量单位","data": "itemunit" },
                    { title:"换算公式(千克标准煤)","data": "itemfml" },
                    { title:"启用状态","data": "itemstate" ,
                        render: function(data, type, full){
                            if(data==1)
                                return '<div class="togglebutton"><label> <input type="checkbox" id="'+full.itemcode+'" name="'+full.itemname+'" checked></label> </div>';
                            else
                                return '<div class="togglebutton"><label> <input type="checkbox" id="'+full.itemcode+'" name="'+full.itemname+'"></label> </div>';
                        }
                    }
                ],
                "order": [[ 2, 'asc' ]]
            },
            formOption:[
                [{label:'label',text:'分类分项代码',size:3},{label:'input',type:'text',name:'itemcode',size:8,valid:{notEmpty: {},stringLength:{min:5,max:5}}}],
                [{label:'label',text:'分类分项名称',size:3},{label:'input',type:'text',name:'itemname',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'分类分项级别',size:3},{label:'select',name:'itemlevel',funcs:[{event:'change',func:getUpItems}],options:[{value:1,text:1},{value:2,text:2},{value:3,text:3},{value:4,text:4},{value:5,text:5}],size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'上级分项名称',size:3},{label:'select',name:'parentitemname',options:[{value:0,text:'最高能耗级别'}],size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'能耗类型',size:3},{label:'input',type:'radio',name:'itemtype',options:[{value:'A',text:'A-分类能耗'},{value:'B',text:'B-分项能耗'}],size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'计量单位',size:3},{label:'input',type:'text',name:'itemunit',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'换算公式',size:3},{label:'input',type:'text',name:'itemfml',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'是否启用',size:3},{label:'input',type:'radio',name:'itemstate',options:[{value:1,text:'启用'},{value:0,text:'不启用'}],size:8,valid:{notEmpty: {}}}]
            ]
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.energy.energytype)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addEnergyType,   updateEnergyType, deleteEnergyType;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'能耗类型','green','',configMap.body),
            $dtenergytype    :new ecss.tools.makeTable($container.find('#DTenergytype'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'能耗类型信息'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $confirm     :new ecss.tools.makeModal($container),
            $energytypeadd   :$container.find('#add'),
            $energytypeupdate:$container.find('#update'),
            $energytypedelete:$container.find('#delete'),
            $window     :$(window)
        };
        $('table').delegate('.togglebutton input','click',function(){
            var id=$(this).prop('id');
            var name=$(this).prop('name');
            var check=$(this).prop('checked');
            var togglebutton=$(this);
            jqueryMap.$confirm.confirm('确认'+(check?'开启':'关闭')+'以下能耗类型？',name,function(){
                modelMap.model.post('onoff',function(){
                    jqueryMap.$dtenergytype.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    alert('操作失败！');
                },id+','+(check?1:0));
            },function(){
                togglebutton.prop('checked',!check);
            });
        });
    };
    addEnergyType=function(){
        jqueryMap.$form.add();
        jqueryMap.$form.getElementsByName('parentitemname').html("<option value=0>最高能耗级别</option>");
        jqueryMap.$modal.show(function () {
            modelMap.model.set(jqueryMap.$form.save());
            modelMap.model.post('add',function(){
                jqueryMap.$dtenergytype.reload();
                jqueryMap.$modal.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateEnergyType=function(){
        var data=jqueryMap.$dtenergytype.getSelect();
        data[0].parentitemname=data[0].parentitemcode+','+data[0].parentitemname;//坑爹
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            getUpItems(null,data[0].itemlevel);
            modelMap.model.set(data[0]);
//            console.log(data[0]);
            data[0].itemname=data[0].itemname.replace(/&nbsp/g,"");
            jqueryMap.$form.update(data[0]);
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.post('update',function(){
                    jqueryMap.$dtenergytype.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteEnergyType=function(){
        var data=jqueryMap.$dtenergytype.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.itemcode}).join(",");
            var names=data.map(function(item){return item.itemname}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下区域？',names,function(){
                modelMap.model.post('del',function(){
                    jqueryMap.$dtenergytype.reload();
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
        jqueryMap.$energytypeadd.click(addEnergyType);
        jqueryMap.$energytypeupdate.click(updateEnergyType);
        jqueryMap.$energytypedelete.click(deleteEnergyType);
    };
    return{initModule:initModule};
}());