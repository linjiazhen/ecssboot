/**
 * Created by jason on 2015/3/2.
 */
ecss.equip=(function(){
    'use strict' ;
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">设备档案</li>'
                    +'</ol>'
                +'</div>'
                +'<div id="batch"></div>'
                +'<div id="list" style="display: none"></div>'
            +'</div>',
            batch:String()
                    +'<div class="form-group">'
                        +'<lable class="col-md-1 control-label">选择类型:</lable>'
                        +'<div class="col-md-2"><select name="type_choice" class="form-control"></select></div><div class="col-md-2"><select name="subtype_choice" class="form-control"></select></div>'
                    +'</div>'
                    +'<div class="tablecontent" style="margin-top:45px" >'
                        +'<div class="buttons">'
                            +'<button class="btn btn-success btn-sm" id="add">添加</button>'
                            +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
                            +'<button class="btn btn-danger btn-sm" id="delete">删除</button>'
                            +'<button class="btn btn-info btn-sm" id="install">装配</button>'
                        +'</div>'
                        +'<table class="table table-hover table-bordered" id="DTequipbatch"></table>'
                    +'</div>',
            list: '<div class="tablecontent" >'
                        +'<div class="buttons">'
                            +'<button class="btn btn-success btn-sm" id="deleteinstall">解除安装</button>'
                            //+'<button class="btn btn-danger btn-sm" id="deleteequip">删除设备</button>'
                            +'<button class="btn btn-info btn-sm" id="returnbatch">返回批次</button>'
                        +'</div>'
                        +'<table class="table table-hover table-bordered" id="DTequiplist"></table>'
                +'</div>',
            tableBatchOption:{
                "ajax": { "url": "getallequipbatchs.do", "type": "POST" },
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"批号","data": "batch" },
                    { title:"类型","data": "type" },
                    { title:"子类","data": "subtype"},
                    { title:"型号","data": "model" },
                    { title:"数量","data": "amount" },
                    { title:"库存量","data": "stock" },
                    { title:"供应商","data": "supplier" },
                    { title:"生产商","data": "product" },
                    { title:"采购单价","data": "price" },
                    { title:"采购日期","data": "buydate" },
                    { title:"保修年限","data": "warranty" },
                    { title:"联系人","data": "contact" },
                    { title:"联系电话","data": "phone" }
                ],
                "order": [[ 1, 'asc' ]],
                "initComplete":function(){
                    var api=this.api();
                    jqueryMap.$typechoice.html('<option value="">类型</option>');
                    api.column(2).data().unique().sort().each( function ( data ) {
                        jqueryMap.$typechoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$typechoice.on( 'change', function () {
                        var txt=$(this).val();
                        api.column(2).search( txt ).draw();
                        api.column(3).search( '').draw();
                        jqueryMap.$subtypechoice.html('<option value="">子类</option>');
                        var subtype=[];
                        api.rows().data().each( function ( data ) {
                            if(data.type==txt)
                                subtype.push(data.subtype);
                        } );
                        $.each(subtype.unique(),function(i,data){
                            jqueryMap.$subtypechoice.append( '<option value="'+data+'">'+data+'</option>' );
                        });
                    } );
                    jqueryMap.$subtypechoice.html('<option value="">子类</option>');
                    jqueryMap.$subtypechoice.on( 'change', function () {
                        api.column(3).search( $(this).val() ).draw();
                    } );
                }
            },
            tableParaOption:{
                "dom": "rtp",
                "columns":[
                    {title:'参数',orderable:false},
                    {title:'参数信息',orderable:false}
                ]
            },
            tableMeasureOption:{
                "dom": "rt",
                "columns":[
                    {title:'电表级别',data:'level',"render":function ( data) {
                        if(data=='0') return '间表';
                        else
                        if(data=='1') return '层表';
                        else
                        if(data=='2') return '楼栋表';
                        else
                        if(data=='3') return '区域表';
                        else
                        if(data=='4') return '校级表';
                        else
                            return '市政/电司表';
                    }},
                    {title:'供给目标',data:'uuid',"render":function ( data , type, full) {
                        return (full.group?full.group:'全校')+(full.build?('-'+full.build):'')+(full.floor?('-'+full.floor):'')+(full.room?('-'+full.room):'');
                    }},
                    {title:'分项 ',data:'energyitem'},
                    {title:'占比系数',data:'percent'},
                    {title:'运算方式',data:'plusminus',"render":function ( data) {
                        if(data=='1') return '加运算';else return '减运算';
                    }},
                    {title:'备注',data:'remark'}
                ],
                multiple:false
            },
            formBatchOption:[
                [{label:'label',text:'批次编号',size:3},{label:'input',type:'text',name:'batch',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'设备类型',size:3},{label:'select',name:'type',size:4,valid:{notEmpty: {}}},{label:'select',name:'typeid',size:4,valid:{notEmpty: {}}}],
                [{label:'label',text:'设备型号',size:3},{label:'input',type:'text',name:'model',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'数量',size:3},{label:'input',type:'text',name:'amount',size:8,disable:true,valid:{notEmpty: {}}}],
                [{label:'label',text:'供应商',size:3},{label:'input',type:'text',name:'supplier',size:8,valid:{}}],
                [{label:'label',text:'生产商',size:3},{label:'input',type:'text',name:'product',size:8,valid:{}}],
                [{label:'label',text:'采购单价',size:3},{label:'input',type:'text',name:'price',size:8,valid:{}}],
                [{label:'label',text:'采购日期',size:3},{label:'input',type:'date',name:'buydate',size:8,valid:{}}],
                [{label:'label',text:'保修期',size:3},{label:'input',type:'text',name:'warranty',size:8,valid:{}}],
                [{label:'label',text:'联系人',size:3},{label:'input',type:'text',name:'contact',size:8,valid:{}}],
                [{label:'label',text:'联系电话',size:3},{label:'input',type:'text',name:'phone',size:8,valid:{}}]
            ],
            tableListOption: {
                "columns": [{ "title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>","className": 'details-control',
                    "data":   null, orderable: false, searchable:false,sortable:false,
                    "defaultContent": ''},
                    {title: "编号", "data": "equipid"},
                    {title: "批号", "data": "batch"},
                    {title: "参数编码", "data": "paras"},
                    {title: "类型", "data": "type"},
                    {title: "子类", "data": "subtype"},
                    {title: "型号", "data": "model"},
                   // {title: "测量数", "data": "connectnum"},
                    {title: "安装类型", "data": "installtype", "render":function ( data) {
                            if(data==0) return '户外';
                            else if(data == 1) return '户内';
                            else return '未装配';
                        }},
                    //{title: "安装位置", "data": "installtype", "render":function ( data , type, full) {
                    //    if(data==0) return ("经度："+(full.longitude ? full.longitude:'')+"   纬度："+(full.latitude ? full.latitude:''));
                    //    else if(data == 1) return (full.groupid?full.groupid:'')+(full.buildid?('-'+full.buildid):'')+(full.floorid?('-'+full.floorid):'')+(full.roomid?('-'+full.roomid):'');
                    //    else return '';
                    //}},
                    {title: "安装位置", "data": "remarkinfo","render":function ( data , type, full) {

                            return (full.groupid?full.groupid:data)+(full.buildid?('-'+full.buildid):'')+(full.floorid?('-'+full.floorid):'')+(full.roomid?('-'+full.roomid):'');

                        }}
                ],
                "order": [[1, 'asc']],
                multiple:false
            },
            formMeasureOption:[
                [{label:'label',text:'表级别',size:3},{label:'select',name:'level',size:8,options:[{value:5,text:'市政/电司表'},
                    {value:4,text:'校级表'},{value:3,text:'区域表'},{value:2,text:'楼栋表'},{value:1,text:'层表'},{value:0,text:'间表'}],valid:{notEmpty: {}}}],
                [{label:'label',text:'区域',size:3},{label:'select',name:'group',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'楼栋',size:3},{label:'select',name:'build',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'楼层',size:3},{label:'select',name:'floor',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'房间',size:3},{label:'select',name:'room',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'上级表级别',size:3},{label:'select',name:'superior_meter_level',size:8,options:[{value:5,text:'市政/电司表'},
                    {value:4,text:'校级表'},{value:3,text:'区域表'},{value:2,text:'楼栋表'},{value:1,text:'层表'},{value:0,text:'间表'}],valid:{notEmpty: {}}}],
                [{label:'label',text:'上级表编码',size:3},{label:'select',name:'superior_meter',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'能耗分项',size:3},{label:'select',name:'energyitemcode',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'运算方式',size:3},{label:'input',type:'radio',name:'plusminus',size:8,options:[{value:1,text:'加运算',checked:true},{value:-1,text:'减运算'}],valid:{notEmpty: {}}}],
                [{label:'label',text:'占比系数',size:3},{label:'input',type:'text',name:'percent',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'备注',size:3},{label:'textarea',name:'remark',row:2,size:8,valid:{notEmpty: {}}}]
            ],
            formInstallOption:[
                [{label:'label',text:'设备类型',size:3},{label:'select',name:'type',size:4,valid:{}},{label:'select',name:'typeid',size:4,valid:{}}],
                [{label:'label',text:'批次编号',size:3},{label:'select',type:'text',name:'batch',size:8,valid:{}}],
                [{label:'select',name:'equiplist',multiple:'multiple',row:10,size:12}]
            ],
            inTreeOption : {
                core:{
                    check_callback:true,
                    data:{
                        url:'getschooltree.do'
                    },
                    multiple:false,
                    dblclick_toggle:false
                },
                checkbox:{
                    three_state:false
                },
                plugins:['checkbox','search']
            },
            outTreeOption : {
                core:{
                    check_callback:true,
                    data:{
                        url:'getbuildtree.do'
                    },
                    multiple:false,
                    dblclick_toggle:false
                },
                checkbox:{
                    three_state:false
                },
                plugins:['checkbox','search']
            },
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
            modelbatch:new ecss.tools.makeModel(ecss.model.equip.batch),
            modellist:new ecss.tools.makeModel(ecss.model.equip.list),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option),
            installdata:{},
            batchdata:{}
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addBatch,   updateBatch, deleteBatch,install,addEvent,getBatchDiv,getInstallDiv,setInstallType,listChildFormat,initform,addOptions,
        addMeasure,updateMeasure,delMeasure,childOpen,childClose,toDtlist,returnBatch,initInhomeTree,deleteInstall;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        console.log($container);
        jqueryMap={
            $container  :$container,
            $portlet1    :new ecss.tools.makePortlet($container.find('#batch'),'批次列表','green','',configMap.batch),
            $portlet2    :new ecss.tools.makePortlet($container.find('#list'),'设备列表','green','',configMap.list),
            $modalbatch :new ecss.tools.makeModal($container,'设备批次信息'),
            $formbatch  :new ecss.tools.makeForm($container.find('.modal-body:last'),configMap.formBatchOption),
            $dtpara    :new ecss.tools.makeTable($container.find('#DTpara'),configMap.tableParaOption),
            $dtlist    :new ecss.tools.makeTable($container.find('#DTequiplist'),configMap.tableListOption),
            $modalmeasure:new ecss.tools.makeModal($container,'添加测量信息'),
            $formmeasure:new ecss.tools.makeForm($container.find('.modal-body:last'),configMap.formMeasureOption),
            $modalinstall:new ecss.tools.makeModal($container,'设备装配','modal-lg'),
            $forminstall:new ecss.tools.makeForm(getInstallDiv($container.find('.modal-body:last')),configMap.formInstallOption),
            $dtbatch    :new ecss.tools.makeTable($container.find('#DTequipbatch'),configMap.tableBatchOption),
//            $dtmeasure  :new ecss.tools.makeTable($container.find('#DTmeasure'),configMap.tableMeasureOption),
            $confirm    :new ecss.tools.makeModal($container),
            $inhometree :new ecss.tools.makeTree($container.find('#inhometree'),configMap.inTreeOption,440),
            $outhometree:new ecss.tools.makeTree($container.find('#outhometree'),configMap.outTreeOption,370),
            $duallist   :$container.find('[name=equiplist]').bootstrapDualListbox(configMap.dualListOption),
            $typechoice:$container.find('select[name=type_choice]'),
            $subtypechoice:$container.find('select[name=subtype_choice]'),
            $batchadd   :$container.find('#add'),
            $batchupdate:$container.find('#update'),
            $batchdelete:$container.find('#delete'),
            $install    :$container.find('#install'),
            $returnbatch:$container.find('#returnbatch'),
            $measureadd :$container.find('#addmeasure'),
            $measuredel :$container.find('#delmeasure'),
            $delinstall :$container.find('#deleteinstall'),
            $divbatch   :$container.find('#batch'),
            $divlist    :$container.find('#list'),
            $installtype:$container.find('[name=installtype]'),
            $inhome     :$container.find('#inhome'),
            $outhome    :$container.find('#outhome'),
            $inremark   :$container.find('#inhome [name=remark]'),
            $longitude  :$container.find('#outhome [name=longitude]'),
            $latitude  :$container.find('#outhome [name=latitude]'),
            $outremark  :$container.find('#outhome [name=remark]'),
            $window     :$(window)
        };
    };
    addOptions=function(url,para,$select,text,async){
        modelMap.modeltools.post(url,function(data){
            if(data!=null){
                $select.html(text);
                $.each(data,function(i,item){
                    $select.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,para,'json',async);
    };
    initform=function(){
        var $type=jqueryMap.$formbatch.getElementsByName('type');
        var $type1=jqueryMap.$forminstall.getElementsByName('type');
        $type.html('<option value="#">类型</option>');
        $type1.html('<option value="#">类型</option>');
        modelMap.modeltools.post('getequiptype',function(data){
            if(data!=null){
                $.each(data,function(i,item){
                    $type.append("<option value='"+item+"'>"+item+"</option>");
                    $type1.append("<option value='"+item+"'>"+item+"</option>");
                });
            }
        },null,null,'json');
        var $subtype=jqueryMap.$formbatch.getElementsByName('typeid');
        var $subtype1=jqueryMap.$forminstall.getElementsByName('typeid');
        $subtype.html('<option value="#">子类</option>');
        $subtype1.html('<option value="#">子类</option>');
        $type.change(function(){
            jqueryMap.$dtpara.clear();
            modelMap.modeltools.post('getequipsubtype',function(data){
                if(data!=null){
                    $subtype.html('<option value="#">子类</option>');
                    $.each(data,function(i,item){
                        $subtype.append("<option value='"+item.id+"'>"+item.name+"</option>");
                    });
                }
            },null,$type.val(),'json');
        });
        $type1.change(function(){
            modelMap.modeltools.post('getequipsubtype',function(data){
                if(data!=null){
                    $subtype1.html('<option value="#">子类</option>');
                    $.each(data,function(i,item){
                        $subtype1.append("<option value='"+item.id+"'>"+item.name+"</option>");
                    });
                }
            },null,$type1.val(),'json');
        });
        var settable=function(data){
            $.each(data.split('@'),function(i,item){
                console.log(item);
                if(item!='')
                    console.log(JSON.parse(item));  //未完待续。。。
            })
        };
        $subtype.change(function(){
            jqueryMap.$dtpara.clear();
            modelMap.modelbatch.post('getpara',function(data){
                console.log(data);
                if(data!=null){
                    settable(data.name);
                }
            },null,$subtype.val(),'json');
        });
        $subtype1.change(function(){
            var $equiplist=jqueryMap.$forminstall.getElementsByName('equiplist');
            $equiplist.html('');
            jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
            modelMap.modellist.post('getlistbytype',function(data){
                modelMap.equiplist=data;
                var $batchsel=jqueryMap.$forminstall.getElementsByName('batch');
                $batchsel.html('<option value="#">选择批次</option>');
                $.each(data.map(function(data){return data.batch;}).unique(),function(i,item){
                    $batchsel.append("<option value='"+item+"'>"+item+"</option>");
                });
                $.each(data.map(function(data){return {id:data.uuid,equip:data.equipid}}),function(i,item){
                    $equiplist.append("<option value='"+item.id+"'>"+item.equip+"</option>");
                });
                jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
            },null,$subtype1.val(),'json');
        });
        jqueryMap.$forminstall.getElementsByName('batch').change(function(){
            var $equiplist=jqueryMap.$forminstall.getElementsByName('equiplist');
            $equiplist.html('');
            jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
            var value=$(this).val();
            $.each(modelMap.equiplist.map(function(data){return {batch:data.batch,id:data.uuid,equip:data.equipid}}),function(i,item){
                if(value=='#'||item.batch==value)
                    $equiplist.append("<option value='"+item.id+"'>"+item.equip+"</option>");
            });
            jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
        });
        var $group=jqueryMap.$formmeasure.getElementsByName('group');
        $group.html('<option value="allofsumgroup">全校</option>');
        addOptions('getgroup',null,$group, '<option value="allofsumgroup">全校</option>');
        var $build=jqueryMap.$formmeasure.getElementsByName( 'build');
        $build.html('<option value="#">全区</option>');
        $group.change(function(){
            addOptions('getbuild',$group.val(),$build,'<option value="#">全区</option>');
        });
        var $floor=jqueryMap.$formmeasure.getElementsByName('floor');
        $floor.html('<option value="#">全楼</option>');
        $build.change(function(){
            addOptions('getfloor',$build.val(),$floor, '<option value="#">全楼</option>');
        });
        var $room=jqueryMap.$formmeasure.getElementsByName('room');
        $room.html('<option value="#">全层</option>');
        $floor.change(function(){
            addOptions('getroom',$floor.val(),$room,'<option value="#">全层</option>');
        });
        var $energytype=jqueryMap.$formmeasure.getElementsByName('energyitemcode');
        modelMap.modeltools.post('getenergyitems',function(data){
            console.log(data);
            if(data!=null){
                $.each(data,function(i,item){
                    $energytype.append("<option value='"+item.id+"'>"+item.id+item.name+"</option>");
                });
            }
        },null,null,'json');
        var $superlevel=jqueryMap.$formmeasure.getElementsByName('superior_meter_level');
        var $supermeter=jqueryMap.$formmeasure.getElementsByName('superior_meter');
        $supermeter.html('<option value="#">暂不设置</option>');
        $superlevel.change(function(){
            var level=$(this).val();
            modelMap.modeltools.post('getsupermeter',function(data){
                if(data!=null){
                    $supermeter.html('<option value="#">暂不设置</option>');
                    $.each(data,function(i,item){
                        if(level=='4') $supermeter.append("<option value="+'1,'+item.equipuuid+">"+'校级表-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else if(level=='3') $supermeter.append("<option value="+item.buildgroupid+','+item.equipuuid+">"+item.buildgroup+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else if(level=='2') $supermeter.append("<option value="+item.buildid+','+item.equipuuid+">"+item.buildgroup+'-'+item.build+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else if(level=='1') $supermeter.append("<option value="+item.floorid+','+item.equipuuid+">"+item.buildgroup+'-'+item.build+'-'+item.floor+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else $supermeter.append("<option value="+item.roomid+','+item.equipuuid+">"+item.buildgroup+'-'+item.build+'-'+item.floor+'-'+item.room+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                    });
                }
            },null,level,'json');
        });
    };
    getInstallDiv=function($div){
        $div.html('<div id="form" class="col-md-7"></div>' +
        '<div class="col-md-5 form-horizontal">' +
            '<div class="radio radio-primary radio-inline">' +
                '<label>' +
                '<input type="radio" name="installtype" value="1" checked="">室内' +
                '</label>' +
            '</div>' +
            '<div class="radio radio-primary radio-inline">' +
                '<label>' +
                '<input type="radio" name="installtype" value="0">室外' +
                '</label>' +
            '</div>' +
            '<div class="col-md-12" id="inhome"><div id="inhometree" ></div>' +
                '<div class="form-group"><label class="col-md-3 control-label">备注:</label><div class="col-md-9"><textarea name="remark" rows="3" class="form-control" placeholder="请添加备注.."></textarea></div></div>'+
            '</div>' +
            '<div class="col-md-12" style="display: none" id="outhome"><div id="outhometree" ></div>' +
                '<div class="form-group"><label class="col-md-3 control-label">经度:</label><div class="col-md-9 "><input type="text" name="longitude" class="form-control"></div></div>'+
                '<div class="form-group"><label class="col-md-3 control-label">纬度:</label><div class="col-md-9"><input type="text" name="latitude" class="form-control"></div></div>'+
                '<div class="form-group"><label class="col-md-3 control-label">备注:</label><div class="col-md-9"><textarea name="remark" rows="3" class="form-control" placeholder="请添加备注.."></textarea></div></div>'+
            '</div>'+
        '</div>');
        return $div.find('#form');
    };
    setInstallType=function(){
        if($(this).val()=='1'){
            $('#inhome').show();
            $('#outhome').hide();
        }
        else{
            $('#inhome').hide();
            $('#outhome').show();
        }
    };
    listChildFormat=function(){
      //  return jqueryMap.$container.find('#measure').html();
        return '<div style=“text-align: center;”>' +
            '<button class="btn btn-success btn-sm" id="addmeasure">添加关联</button>'
            +'<button class="btn btn-warning btn-sm" id="updatemeasure">修改关联</button>'
            +'<button class="btn btn-danger btn-sm" id="delmeasure">删除关联</button>'
            +'<table class="table table-hover table-bordered" class="DTmeasure"></table></div>';
    };
    toDtlist=function(data){
        modelMap.batchdata=data;
        ZENG.msgbox.show('正在获取设备列表...',6);
        modelMap.modellist.post('getlist',function(data){
            ZENG.msgbox._hide();
            jqueryMap.$divbatch.hide();
            jqueryMap.$divlist.show();
            jqueryMap.$dtlist.setData(data,true);
            $('#deleteinstall').show();
            $('#returnbatch').show();
        }, function () {
            ZENG.msgbox.show('获取设备列表失败！',5,3000);
        },data.uuid,'json');
    };
    returnBatch=function(){
        jqueryMap.$divbatch.show();
        jqueryMap.$divlist.hide();
    };
    addEvent=function(){
        jqueryMap.$dtlist.setChild(listChildFormat,childOpen,childClose);
        jqueryMap.$dtbatch.setDblClick(toDtlist);
        jqueryMap.$batchadd.click(addBatch);
        jqueryMap.$batchupdate.click(updateBatch);
        jqueryMap.$batchdelete.click(deleteBatch);
        jqueryMap.$install.click(install);
        jqueryMap.$returnbatch.click(returnBatch);
        jqueryMap.$container.on('click','#addmeasure',addMeasure);
        jqueryMap.$container.on('click','#updatemeasure',updateMeasure);
        jqueryMap.$container.on('click','#delmeasure',delMeasure);
        jqueryMap.$delinstall.click(deleteInstall);
        jqueryMap.$inhometree.setInitEvent(initInhomeTree);
        jqueryMap.$container.find('[name=installtype]').click(setInstallType);
        jqueryMap.$container.find('#DTequiplist').on('click','table tbody tr',function(){
            $(this).toggleClass('danger');
            console.log(jqueryMap.$dtmeasure.getData($(this)));
        });
    };
    addBatch=function(){
        jqueryMap.$formbatch.add();
        jqueryMap.$formbatch.getElementsByName('amount').prop('disabled',false);
        jqueryMap.$modalbatch.show(function () {
            modelMap.modelbatch.set(jqueryMap.$formbatch.save());
            modelMap.modelbatch.post('add',function(){
                jqueryMap.$dtbatch.reload();
                jqueryMap.$modalbatch.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$modalbatch.getElementsById('save'),'添加失败！请刷新重试！');
            });
        });
    };
    updateBatch=function(){
        var getsubtype=function(type){
            var $subtype=jqueryMap.$formbatch.getElementsByName('typeid');
            modelMap.modeltools.post('getequipsubtype',function(data){
                if(data!=null){
                    $subtype.html('<option value="#">子类</option>');
                    $.each(data,function(i,item){
                        $subtype.append("<option value='"+item.id+"'>"+item.name+"</option>");
                    });
                }
            },null,type,'json',false);
        };
        jqueryMap.$formbatch.getElementsByName('amount').prop('disabled',true);
        var data=jqueryMap.$dtbatch.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            getsubtype(data[0].type);
            data[0].buydate=data[0].buydate.replace(/-/g,'/');
            modelMap.modelbatch.set(data[0]);
            jqueryMap.$formbatch.update(data[0]);
            jqueryMap.$modalbatch.show(function () {
                jqueryMap.$formbatch.getElementsByName('amount').prop('disabled',false);
                modelMap.modelbatch.set(jqueryMap.$formbatch.save());
                modelMap.modelbatch.post('update',function(){
                    jqueryMap.$dtbatch.reload();
                    jqueryMap.$modalbatch.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modalbatch.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    deleteBatch=function(){
        var data=jqueryMap.$dtbatch.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.batch}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下批次？',names,function(){
                modelMap.modelbatch.post('del',function(){
                    jqueryMap.$dtbatch.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    deleteInstall=function(){
        var data=jqueryMap.$dtlist.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){if(item.installtype!=null) return item.uuid}).join(",");
            var names=data.map(function(item){return item.equipid}).join(",");
            jqueryMap.$confirm.confirm('确认解除安装以下设备？',names,function(){
                modelMap.modellist.post('delinstall',function(){
                    toDtlist(modelMap.batchdata);
                    jqueryMap.$confirm.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'解除安装失败！请刷新重试！');
                },ids);
            });
        }
    };
    install=function(){
        jqueryMap.$modalinstall.show(function(){
            modelMap.installdata={};
            var equipid=jqueryMap.$forminstall.getElementsByName('equiplist').val();
            if(jqueryMap.$installtype.filter(':checked').val()=='1'){
                var path=jqueryMap.$inhometree.get_path(jqueryMap.$inhometree.get_selected()[0]);
                console.log(path);
                if(path.length==5&&equipid!=null){
                    modelMap.installdata={
                        uuid:equipid.join(','),
                        buildid:path[2],
                        floorid:path[3],
                        roomid:path[4],
                        installtype:'1',
                        remark:jqueryMap.$inremark.val()
                    };
                }
            }
            else{
                var path=jqueryMap.$outhometree.get_path(jqueryMap.$outhometree.get_selected()[0]);
                console.log(path);
                if(path.length==3&&equipid!=null) {
                    modelMap.installdata = {
                        uuid: equipid.join(','),
                        buildid: path[2],
                        longitude: jqueryMap.$container.find('[name=longitude]').val(),
                        latitude: jqueryMap.$container.find('[name=latitude]').val(),
                        installtype: '0',
                        remark: jqueryMap.$outremark.val()
                    };
                }
            }
            if(modelMap.installdata.uuid!=null)
                modelMap.modellist.post('install',function(){
                    ZENG.msgbox.show('安装成功！',4,3000);
                    var $equiplist=jqueryMap.$forminstall.getElementsByName('equiplist');
                    $equiplist.find('option:checked').remove();
                    jqueryMap.$duallist.bootstrapDualListbox('refresh', true);
                },function(){
                    ZENG.msgbox.show('安装失败，请重试！',5,3000);
                },modelMap.installdata);
        });
    };
    addMeasure=function(){
        var devicecode=jqueryMap.$dtlist.getChildShown().uuid;
        modelMap.modellist.setprop('devicecode',devicecode);
        jqueryMap.$modalmeasure.show(function () {
            modelMap.modellist.set(jqueryMap.$formmeasure.save());
            modelMap.modellist.post('addmeasure',function(){
                jqueryMap.$modalmeasure.hide();
                ZENG.msgbox.show('添加成功！',4,3000);
            },function(){
                ZENG.msgbox.show('添加失败！',5,3000);
            });
        });
    };
    updateMeasure=function(){
        var data=jqueryMap.$dtmeasure.getDataByClass('danger');
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择并且只能选择一条记录！');
        else {
            modelMap.modellist.set(data[0]);
            jqueryMap.$formmeasure.update(data[0]);
            if(data[0].groupid) {
                jqueryMap.$formmeasure.getElementsByName('group').val(data[0].groupid);
                addOptions('getbuild', data[0].groupid, jqueryMap.$formmeasure.getElementsByName('build'), '<option value="#">全区</option>', false);
            }
            if(data[0].buildid) {
                jqueryMap.$formmeasure.getElementsByName('build').val(data[0].buildid?data[0].buildid:'#');
                addOptions('getfloor', data[0].buildid, jqueryMap.$formmeasure.getElementsByName('floor'), '<option value="#">全楼</option>', false);
            }
            if(data[0].floorid) {
                jqueryMap.$formmeasure.getElementsByName('floor').val(data[0].floorid?data[0].floorid:'#');
                addOptions('getroom', data[0].floorid, jqueryMap.$formmeasure.getElementsByName('room'), '<option value="#">全层</option>', false);
            }
            if(data[0].roomid) {
                jqueryMap.$formmeasure.getElementsByName('room').val(data[0].roomid?data[0].roomid:'#');
            }
            var $supermeter=jqueryMap.$formmeasure.getElementsByName('superior_meter');
            var level=data[0].superior_meter_level.toString();
            modelMap.modeltools.post('getsupermeter',function(data){
                if(data!=null){
                    $supermeter.html('<option value="#">暂不设置</option>');
                    $.each(data,function(i,item){
                        if(level=='4') $supermeter.append("<option value="+'1,'+item.equipuuid+">"+'校级表-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else if(level=='3') $supermeter.append("<option value="+item.buildgroupid+','+item.equipuuid+">"+item.buildgroup+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else if(level=='2') $supermeter.append("<option value="+item.buildid+','+item.equipuuid+">"+item.buildgroup+'-'+item.build+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else if(level=='1') $supermeter.append("<option value="+item.floorid+','+item.equipuuid+">"+item.buildgroup+'-'+item.build+'-'+item.floor+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                        else $supermeter.append("<option value="+item.roomid+','+item.equipuuid+">"+item.buildgroup+'-'+item.build+'-'+item.floor+'-'+item.room+'-'+item.energyitemname+'-'+item.equipid+"</option>");
                    });
                }
            },null,level,'json',false);
            $supermeter.val(data[0].superior_meter?data[0].superior_meter:'#');
            console.log(data[0]);
            jqueryMap.$modalmeasure.show(function () {
                modelMap.modellist.set(jqueryMap.$formmeasure.save());
                modelMap.modellist.post('updatemeasure',function(){
                    jqueryMap.$modalmeasure.hide();
                    ZENG.msgbox.show('更新成功！',4,3000);
                },function(){
                    ZENG.msgbox.show('更新失败！',5,3000);
                });
            });
        }
    };
    delMeasure=function(){
        var data=jqueryMap.$dtmeasure.getDataByClass('danger');
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return (item.group?item.group:'')+(item.build?('-'+item.build):'')+(item.floor?('-'+item.floor):'')+(item.room?('-'+item.room):'');}).join(",");
            jqueryMap.$confirm.confirm('确认删除以下关联？',names,function(){
                modelMap.modellist.post('delmeasure',function(){
                    jqueryMap.$confirm.hide();
                    ZENG.msgbox.show('删除成功！',4,3000);
                },function(){
                    ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'删除失败！请刷新重试！');
                },ids);
            });
        }
    };
    childOpen=function(data){
        if(jqueryMap.$dtmeasure!=null){
            jqueryMap.$dtmeasure.destroy();
            jqueryMap.$dtmeasure=null;
        }
        //jqueryMap.$container.find('#DTequiplist table').before('<div>'+data.remarkinfo+'</div>');
        jqueryMap.$dtmeasure =new ecss.tools.makeTable(jqueryMap.$container.find('#DTequiplist table'),configMap.tableMeasureOption);
        modelMap.modellist.post('getmeasure',function(data){
            jqueryMap.$dtmeasure.setData(data,true);
        },function(){
            ZENG.msgbox.show('打开失败！',5,3000);
        },data.uuid,'json',false);
    };
    childClose=function(){
        jqueryMap.$dtmeasure.destroy();
        jqueryMap.$dtmeasure=null;
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        initform();
        addEvent();
    };
    return{initModule:initModule};
}());