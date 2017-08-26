/**
 * Created by jason on 2015/3/2.
 */
ecss.waternet=(function(){
    'use strict';
    var initform=function(){
            var $gateway=jqueryMap.$formadd.getElementsByName('gateway');
            $gateway.html("<option value='#'>请选择网关</option>");
            modelMap.modeltools.post('getgatewayinwater',function(data){
                console.log(data);
                $.each(data,function(i,item){
                    $gateway.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            },null,null,'json');
            $gateway.change(function(){
                var $equiplist=jqueryMap.$formadd.getElementsByName('equiplist');
                if($(this).val()=='#'){
                    $equiplist.html('');
                    jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
                }
                else{
                    $equiplist.html('');
                    jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
                    modelMap.modeltools.post('getequipbygateway',function(data){
                        console.log(data);
                        $.each(data,function(i,item){
                            $equiplist.append("<option value='"+item.id+"'>"+item.name+"</option>");
                        });
                        jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
                    },null,$(this).val(),'json');
                }
            });
        },
        configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">水产销差</li>'
                    +'</ol>'
                +'</div>'
                +'<div id="info" style="display: none;margin-bottom: 60px" ><div class="btn btn-info col-md-1" id="return" style="margin-top: -10px">返回</div><div class="uuid hidden"></div>' +
                '<div class="col-md-2">编号:<strong class="code"></strong></div><div class="col-md-2">水网名称:<strong class="name"></strong></div><div class="col-md-1">级别:<strong class="level"></strong></div></div>'
                +'<div id="waternet"></div>'
                +'<div id="list" style="display: none">'
                    +'<div id="produce"></div>'
                    +'<div id="consume"></div>'
                +'</div>'
            +'</div>',
            waternet:String()
                    +'<div class="tablecontent" >'
                        +'<div class="buttons">'
                            +'<button class="btn btn-success btn-sm" id="add">添加</button>'
                            +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
                            +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                        +'</div>'
                        +'<table class="table table-hover table-bordered" id="DTwaternet"></table>'
                    +'</div>',
            produce: '<div class="tablecontent0" >'
                        +'<div class="buttons">'
                            +'<button class="btn btn-success btn-sm" id="addproduce">添加</button>'
                            +'<button class="btn btn-danger btn-sm" id="deleteproduce">删除</button>'
                        +'</div>'
                        +'<table class="table table-hover table-bordered" id="DTproduce"></table>'
                    +'</div>',
            consume: '<div class="tablecontent0" >'
                        +'<div class="buttons">'
                        +'<button class="btn btn-success btn-sm" id="addconsume">添加</button>'
                        +'<button class="btn btn-danger btn-sm" id="deleteconsume">删除</button>'
                        +'</div>'
                        +'<table class="table table-hover table-bordered" id="DTconsume"></table>'
                    +'</div>',
            tableWaternetOption:{
                "ajax": { "url": "getallwaternets.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"编号","data": "code" },
                    { title:"水网名称","data": "name" },
                    { title:"级别","data": "level"},
                    { title:"生产表数","data": "pronum" },
                    { title:"消费表数","data": "connum" }
                ],
                "order": [[ 0, 'asc' ]]
            },
            formWaternetOption:[
                [{label:'label',text:'编号',size:3},{label:'input',type:'text',name:'code',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'水网名称',size:3},{label:'input',type:'text',name:'name',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'级别',size:3},{label:'input',type:'text',name:'level',size:8,valid:{notEmpty: {}}}]
            ],
            tableProduceOption: {
                "columns": [{"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    {title: "编号", "data": "equipid"},
                    {title: "批号", "data": "batch"},
                    {title: "类型", "data": "type"},
                    {title: "子类", "data": "subtype"},
                    {title: "型号", "data": "model"},
                    {title: "参数", "data": "paras"},
                    {title: "安装类型", "data": "installtype", "render":function ( data) {
                            if(data==0) return '户外';
                            else if(data == 1) return '户内';
                            else return '未装配';
                        }},
                    {title: "安装位置", "data": "installtype", "render":function ( data , type, full) {
                        if(data==0) return ("经度："+(full.longitude ? full.longitude:'')+"   纬度："+(full.latitude ? full.latitude:''));
                        else if(data == 1) return (full.groupid?full.groupid:'')+(full.buildid?('-'+full.buildid):'')+(full.floorid?('-'+full.floorid):'')+(full.roomid?('-'+full.roomid):'');
                        else return '';
                    }},
                    {title: "备注", "data": "remark"}
                ],
                "order": [[1, 'asc']]
            },
            tableConsumeOption: {
                "columns": [{"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    {title: "编号", "data": "equipid"},
                    {title: "批号", "data": "batch"},
                    {title: "类型", "data": "type"},
                    {title: "子类", "data": "subtype"},
                    {title: "型号", "data": "model"},
                    {title: "参数", "data": "paras"},
                    {title: "安装类型", "data": "installtype", "render":function ( data) {
                        if(data==0) return '户外';
                        else if(data == 1) return '户内';
                        else return '未装配';
                    }},
                    {title: "安装位置", "data": "installtype", "render":function ( data , type, full) {
                        if(data==0) return ("经度："+(full.longitude ? full.longitude:'')+"   纬度："+(full.latitude ? full.latitude:''));
                        else if(data == 1) return (full.groupid?full.groupid:'')+(full.buildid?('-'+full.buildid):'')+(full.floorid?('-'+full.floorid):'')+(full.roomid?('-'+full.roomid):'');
                        else return '';
                    }},
                    {title: "备注", "data": "remark"}
                ],
                "order": [[1, 'asc']]
            },
            formAddOption:[
                [{label:'label',text:'网关',size:3},{label:'select',name:'gateway',size:8,valid:{}}],
                [{label:'select',name:'equiplist',multiple:'multiple',row:10,size:12}]
            ],
            dualListOption:{
                nonSelectedListLabel: '未选择',
                selectedListLabel: '已选择',
                filterPlaceHolder:'筛选',
                infoTextEmpty:'无设备',
                preserveSelectionOnMove: 'moved',
                moveOnSelect: false,
                selectorMinimalHeight:350
            }
        },
        stateMap={
            $container:null
        },
        modelMap={
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option),
            modelwaternet:new ecss.tools.makeModel(ecss.model.waterleak.waternet)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addBatch,   updateBatch, deleteBatch, addEvent,toDtlist,returnBatch, addPro,delPro,addCon,delCon,getleak,initDetail,getpro,getcon,setTime,timeFunc,getEnergy,returnDisplay,setWaterNet;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet1    :new ecss.tools.makePortlet($container.find('#waternet'),'水网列表','green','',configMap.waternet),
            $portlet2    :new ecss.tools.makePortlet($container.find('#produce'),'生产水表','green','表数：<strong id="producenum"></strong>',configMap.produce),
            $portlet3    :new ecss.tools.makePortlet($container.find('#consume'),'消费水表','green','表数：<strong id="consumenum"></strong>',configMap.consume),
            $dtwaternet  :new ecss.tools.makeTable($container.find('#DTwaternet'),configMap.tableWaternetOption),
            $modalwaternet :new ecss.tools.makeModal($container,'水网信息'),
            $formwaternet  :new ecss.tools.makeForm($container.find('.modal-body:last'),configMap.formWaternetOption),
            $dtproduce    :new ecss.tools.makeTable($container.find('#DTproduce'),configMap.tableProduceOption),
            $dtconsume    :new ecss.tools.makeTable($container.find('#DTconsume'),configMap.tableConsumeOption),
            $modaladd   :new ecss.tools.makeModal($container,'添加设备'),
            $formadd    :new ecss.tools.makeForm($container.find('.modal-body:last'),configMap.formAddOption),
            $confirm    :new ecss.tools.makeModal($container),
            $duallist   :$container.find('[name=equiplist]').bootstrapDualListbox(configMap.dualListOption),
            $waternetadd   :$container.find('#add'),
            $waternetupdate:$container.find('#update'),
            $waternetdelete:$container.find('#delete'),
            $return     :$container.find('#return'),
            $divwaternet:$container.find('#waternet'),
            $divlist    :$container.find('#list'),
            $info       :$container.find('#info'),
            $addpro     :$container.find('#addproduce'),
            $delpro     :$container.find('#deleteproduce'),
            $addcon     :$container.find('#addconsume'),
            $delcon     :$container.find('#deleteconsume'),
            $window     :$(window)
        };
    };
    getpro=function(id){
        modelMap.modelwaternet.post('getpro',function(data){
            jqueryMap.$container.find('#producenum').html(data.length);
            jqueryMap.$dtproduce.setData(data,true);
        }, function () {
            ZENG.msgbox.show('获取生产水表信息失败！',5,3000);
        },id,'json');
    };
    getcon=function(id){
        modelMap.modelwaternet.post('getcon',function(data){
            jqueryMap.$container.find('#consumenum').html(data.length);
            jqueryMap.$dtconsume.setData(data,true);
        }, function () {
            ZENG.msgbox.show('获取消费水表信息失败！',5,3000);
        },id,'json');
    };
    toDtlist=function(data){
        jqueryMap.$divwaternet.hide();
        jqueryMap.$divlist.show();
        jqueryMap.$info.show();
        jqueryMap.$info.find('.uuid').html(data.uuid);
        jqueryMap.$info.find('.code').html(data.code);
        jqueryMap.$info.find('.name').html(data.name);
        jqueryMap.$info.find('.level').html(data.level);
        getpro(data.uuid);
        getcon(data.uuid);
    };
    returnBatch=function(){
        jqueryMap.$divwaternet.show();
        jqueryMap.$divlist.hide();
        jqueryMap.$info.hide();
    };
    addEvent=function(){
        jqueryMap.$dtwaternet.setDblClick(toDtlist);
        jqueryMap.$waternetadd.click(addBatch);
        jqueryMap.$waternetupdate.click(updateBatch);
        jqueryMap.$waternetdelete.click(deleteBatch);
        jqueryMap.$return.click(returnBatch);
        jqueryMap.$addpro.click(addPro);
        jqueryMap.$delpro.click(delPro);
        jqueryMap.$addcon.click(addCon);
        jqueryMap.$delcon.click(delCon);
    };
    addBatch=function(){
        jqueryMap.$formwaternet.add();
        jqueryMap.$modalwaternet.show(function () {
            modelMap.modelwaternet.set(jqueryMap.$formwaternet.save());
            modelMap.modelwaternet.post('add',function(){
                jqueryMap.$dtwaternet.reload();
                jqueryMap.$modalwaternet.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modalwaternet.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateBatch=function(){
        var data=jqueryMap.$dtwaternet.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            modelMap.modelwaternet.set(data[0]);
            jqueryMap.$formwaternet.update(data[0]);
            jqueryMap.$modalwaternet.show(function () {
                modelMap.modelwaternet.set(jqueryMap.$formwaternet.save());
                modelMap.modelwaternet.post('update',function(){
                    jqueryMap.$dtwaternet.reload();
                    jqueryMap.$modalwaternet.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modalwaternet.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteBatch=function(){
        var data=jqueryMap.$dtwaternet.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.name}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下水网信息？',names,function(){
                modelMap.modelwaternet.post('del',function(){
                    jqueryMap.$dtwaternet.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    addPro=function(){
        jqueryMap.$formadd.getElementsByName('equiplist').html('');
        jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
        jqueryMap.$formadd.add();
        jqueryMap.$modaladd.show(function () {
            modelMap.modelwaternet.post('addpro',function(){
                getpro(jqueryMap.$info.find('.uuid').html());
                jqueryMap.$modaladd.hide();
            },function(){
                ZENG.msgbox.show('添加失败请重试！',5,3000);
            },jqueryMap.$info.find('.uuid').html()+':'+jqueryMap.$formadd.getElementsByName('equiplist').val().join(','));
        });
    };
    delPro=function(){
        var data=jqueryMap.$dtproduce.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.equipid}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下生产表？',names,function(){
                modelMap.modelwaternet.post('delpro',function(){
                    getpro(jqueryMap.$info.find('.uuid').html());
                    jqueryMap.$confirm.hide();
                },function(){
                    ZENG.msgbox.show('删除失败请重试！',5,3000);
                },ids);
            });
        }
    };
    addCon=function(){
        jqueryMap.$formadd.getElementsByName('equiplist').html('');
        jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
        jqueryMap.$formadd.add();
        jqueryMap.$modaladd.show(function () {
            modelMap.modelwaternet.post('addcon',function(){
                getcon(jqueryMap.$info.find('.uuid').html());
                jqueryMap.$modaladd.hide();
            },function(){
                ZENG.msgbox.show('添加失败请重试！',5,3000);
            },jqueryMap.$info.find('.uuid').html()+':'+jqueryMap.$formadd.getElementsByName('equiplist').val().join(','));
        });
    };
    delCon=function(){
        var data=jqueryMap.$dtconsume.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.equipid}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下消费表？',names,function(){
                modelMap.modelwaternet.post('delcon',function(){
                    getcon(jqueryMap.$info.find('.uuid').html());
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
        initform();
    };
    return{initModule:initModule};
}());