/**
 * Created by jason on 2015/1/30.
 */
ecss.organ=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">机构档案</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div class="col-lg-4" id="organlist"></div>'
            +'<div class="col-lg-4" id="organdetail"></div>'
            +'<div class="col-lg-4" id="buildlist"></div>',
            organlist:'<div id="organtree"><div class="btn btn-success" id="addorgan">添加</div><div class="btn btn-danger" id="deleteorgan">删除</div></div>',
            organdetail:'<div id="organform"><div class="btn btn-warning col-md-offset-6" id="update">编辑</div><div class="btn btn-success" id="saveorgan">保存</div></div>',
            buildlist:'<div id="connecttree"><div class="btn btn-warning" id="updatetree">编辑</div><div class="btn btn-success" id="savetree">保存</div></div>',
            organTreeOption : {
                core:{
                    multiple:false,
                    check_callback:true,
                    data:{
                        url:'getallorgans.do'
                    }
                },
                contextmenu:{
                    items:function(){
                        return {
                            add:{
                                label:'添加',
                                action:addOrgan,
                                icon:'pages/images/details_open.png'
                            },
                            delete:{
                                label:'删除',
                                action:deleteOrgan,
                                icon:'pages/images/details_close.png'
                            }
                        }
                    }
                },
                checkbox:{
                    three_state:false
                },
                plugins:['checkbox','contextmenu','search']
            },
            connectTreeOption : {
                core:{
                    check_callback : true,
                    data:{
                        url:'getschooltree.do'
                    }
                },
                contextmenu:{
                    items:function(){
                        return {
                            update:{
                                label:'调整比例',
                                action:updaterange
                                //icon:'pages/images/details_open.png'
                            }
                        }
                    }
                },
                plugins:['checkbox','contextmenu','search']
            },
            formOption:[
                [{label:'label',text:'机构名称',size:3},{label:'input',type:'text',name:'text',size:8,disable:true,valid:{notEmpty: {}}}],
                [{label:'label',text:'机构类别',size:3},{label:'select',name:'type',size:8,disable:true,valid:{notEmpty: {}}}],
                [{label:'label',text:'机构人数',size:3},{label:'input',name:'people',size:8,disable:true,valid:{notEmpty: {}}}],
                [{label:'label',text:'机构面积',size:3},{label:'input',name:'area',size:8,disable:true,valid:{notEmpty: {}}}],
                [{label:'label',text:'负责人',size:3},{label:'input',type:'text',name:'manager',size:8,disable:true,valid:{}}],
                [{label:'label',text:'电话',size:3},{label:'input',type:'text',name:'tel',size:8,disable:true,valid:{}}],
                [{label:'label',text:'邮箱',size:3},{label:'input',type:'text',name:'email',size:8,disable:true,valid:{}}],
                [{label:'label',text:'备注',size:3},{label:'input',type:'text',name:'remark',size:8,disable:true,valid:{}}]
            ],
            rangeformOption:[
                [{label:'label',text:'比例',size:3},{label:'input',type:'text',name:'text',size:8,valid:{notEmpty: {}}}]
            ]
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.organ.organ),
            parconnect:[],
            connect:[]
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap,  setInitEvent,initform, initModule,addEvent,addOrgan,deleteOrgan,updateOrgan,setTreeSelect,updateConnect,saveConnect,updaterange;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet1    :new ecss.tools.makePortlet($container.find('#organlist'),'机构列表','green','',configMap.organlist),
            $portlet2    :new ecss.tools.makePortlet($container.find('#organdetail'),'机构详情','red','',configMap.organdetail),
            $portlet3    :new ecss.tools.makePortlet($container.find('#buildlist'),'关联建筑','blue','',configMap.buildlist),
            $organtree  :new ecss.tools.makeTree($container.find('#organtree'),configMap.organTreeOption),
            $connecttree:new ecss.tools.makeTree($container.find('#connecttree'),configMap.connectTreeOption),
            $form       :new ecss.tools.makeForm($container.find('#organform'),configMap.formOption),
            $rangemodal :new ecss.tools.makeModal($container,'机构占比调整'),
            $rangeform  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.rangeformOption),
            $confirm    :new ecss.tools.makeModal($container),
            $update     :$container.find('#update'),
            $add        :$container.find('#addorgan'),
            $delete     :$container.find('#deleteorgan'),
            $save       :$container.find('#saveorgan'),
            $updatetree :$container.find('#updatetree'),
            $savetree   :$container.find('#savetree'),
            $window     :$(window)
        };
    };
    initform=function(){
        var $typecode=jqueryMap.$form.getElementsByName('type');
        modelMap.model.post('getallorgantypes',function(data){
            console.log(data);
            if(data!=null){
                $.each(data.data,function(i,item){
                    $typecode.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,null,'json');
    };
    setInitEvent=function(){
        jqueryMap.$connecttree.disable_node();
    };
    setTreeSelect=function(e,data){
        jqueryMap.$connecttree.disable_node();
        jqueryMap.$connecttree.deselect_node();
        jqueryMap.$connecttree.close_all();
        modelMap.model.post('getorganbyid',function(data){
            modelMap.model.set(data);
            jqueryMap.$form.update(data);
        },function(){
            ZENG.msgbox.show('获取机构详情失败！',5,3000);
        },data.node.id,'json');
        modelMap.model.post('getbuildbyorgan',function(data){
            modelMap.parconnect=data;
        },function(){
            ZENG.msgbox.show('获取机构关联失败！',5,3000);
        },data.node.parent,'json');
        modelMap.model.post('getbuildbyorgan',function(data){
            modelMap.connect=data;
            jqueryMap.$connecttree.select_node(data);
        },function(){
            ZENG.msgbox.show('获取机构关联失败！',5,3000);
        },data.node.id,'json');
    };
    addOrgan=function(){
        var par=jqueryMap.$organtree.get_selected()[0];
        if(par==null)
            par='#';
        jqueryMap.$organtree.create_node(par,'新机构',function(e,data){
            modelMap.model.post('add',function(){
                ZENG.msgbox.show('添加成功！',4,3000);
            },function(){
                jqueryMap.$organtree.delete_node(data.node);
                ZENG.msgbox.show('添加失败！',5,3000);
            },data.node);
        });
    };
    deleteOrgan=function(){
        var node=jqueryMap.$organtree.get_selected()[0];
        jqueryMap.$confirm.confirm('确认删除以下部门？',node.text,function() {
            modelMap.model.post('del', function () {
                ZENG.msgbox.show('机构删除成功！', 4, 3000);
                jqueryMap.$organtree.delete_node(node);
                jqueryMap.$confirm.hide();
            }, function () {
                ZENG.msgbox.show('机构删除失败！', 5, 3000);
            }, node.id);
        });
    };
    updateOrgan=function(){
        modelMap.model.set(jqueryMap.$form.save());
        modelMap.model.post('update',function(){
            ZENG.msgbox.show('机构更新成功！',4,3000);
            jqueryMap.$container.find('#organform [name]').prop('disabled',true);
            jqueryMap.$organtree.refresh();
        },function(){
           ZENG.msgbox.show('机构更新失败！',5,3000);
        });
    };
    updateConnect=function(){
        if(modelMap.parconnect.length==0){
            jqueryMap.$connecttree.enable_node();
            jqueryMap.$connecttree.open_node('#');
        }
        else{
            $.each(modelMap.parconnect,function(i,item){
                jqueryMap.$connecttree.enable_node(item);
                jqueryMap.$connecttree.enable_node(jqueryMap.$connecttree.get_node(item).children_d);
                jqueryMap.$connecttree.select_node(item);
                jqueryMap.$connecttree.deselect_node(item);
            });
            jqueryMap.$connecttree.select_node(modelMap.connect);
        }
    };
    updaterange=function(){
        jqueryMap.$rangemodal.show(function(){

        });
    };
    saveConnect=function(){
        var organid=modelMap.model.get().id;
        var buildid=jqueryMap.$connecttree.get_checked('top').map(function(item){return item.id}).join(',');
        modelMap.model.post('updateconnect',function(){
            jqueryMap.$connecttree.disable_node();
            ZENG.msgbox.show('关联更新成功！',4,3000);
        },function(){
            ZENG.msgbox.show('关联更新失败！',5,3000);
        },{
            organid:organid,
            buildid:buildid
        });
    };
    addEvent=function(){
        jqueryMap.$connecttree.setInitEvent(setInitEvent);
        //jqueryMap.$connecttree.setSelectEvent(function (e,data){
        //    jqueryMap.$connecttree.rename_node(data.node,data.node.text+'<input name="'+data.node.id+'"/>%')
        //});
        jqueryMap.$organtree.setSelectEvent(setTreeSelect);
        jqueryMap.$update.click(function(){jqueryMap.$container.find('#organform [name]').prop('disabled',false);});
        jqueryMap.$save.click(updateOrgan);
        jqueryMap.$add.click(addOrgan);
        jqueryMap.$delete.click(deleteOrgan);
        jqueryMap.$updatetree.click(updateConnect);
        jqueryMap.$savetree.click(saveConnect);
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        initform();
    };
    return{initModule:initModule};
}());