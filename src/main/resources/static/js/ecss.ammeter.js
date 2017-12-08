/**
 * Created by jason on 2015/3/4.
 */
ecss.ammeter=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
            +'<div class="page-title">'
            +'<h4 class="pull-right strong" id="datetime"></h4>'
            +'<ol class="breadcrumb">'
            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
            +'<li class="active">智能电表</li>'
            +'</ol>'
            +'</div>'
            +'<div id="portlet"></div>'
            +'</div>',
            body:String()
            +'<div class="form-group">'
            +'<lable class="col-md-1 control-label">条件筛选:</lable>'
            +'<div class="col-md-2"><select name="batch_choice" class="form-control"></select></div>'
            +'<div class="col-md-2"><select name="subtype_choice" class="form-control"></select></div>'
            +'<div class="col-md-2"><select name="gateway_choice" class="form-control"></select></div>'
            +'<div class="col-md-2"><select name="if_used" class="form-control"><option value="">启用状态</option><option value="1">已启用</option><option value="0">未启用</option></select></div>'
            +'<div class="col-md-2"><select name="what_status" class="form-control"><option value="">工作状态</option><option value="0">正常</option><option value="1">离线</option><option value="3">底度</option><option value="2">故障</option><option value="4">关闭</option></select></div>'
            +'</div>'
            +'<div class="tablecontent" style="margin-top:45px">'
            +'<div class="buttons">'
            +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
            +'<button class="btn btn-info btn-sm" id="refresh_para">刷新档案至网关</button>'
            +'<div class="btn-group">'
            +'<button type="button" class="btn btn-info btn-sm dropdown-toggle" id="datacollect" data-toggle="dropdown">数据采集<span class="caret"></span></button>'
            +'<ul class="dropdown-menu" role="menu">'
            +'<li><a id="realdata">实时数据</a></li>'
            +'<li><a id="inputdata">人工录入</a></li>'
            +'<li><a id="setactive">设置底度</a></li>'
            +'</ul>'
            +'</div>'
            +'</div>'
            +'<table class="table table-hover table-bordered" id="DTammeter"></table>'
            +'</div>',
            tableOption:{
                "ajax": { "url": "getallammeters.do", "type": "POST"},
                "columns": [ {"title":"详情","className": 'details-control', "data":   null, orderable: false, searchable:false,sortable:false, "defaultContent": ''},
                    { title:"设备编号","data": "equip" },
                    { title:"设备批次","data": "batch" , "visible":false },
                    { title:"子类","data": "subtype", "visible":false },
                    { title:"型号","data": "model"},
                    { title:"启用","data": "use" , "visible":false},
                    { title:"启用","data": "use",
                        render: function(data){
                            if(data=='1')
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div>';
                            else
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use"></label> </div></div>';
                        }},
                    { title:'所属网关',"data": "gateway"},
                    { title:'测量点号',"data": "pn" },
                    { title:"通讯地址","data": "address" },
                    { title:"开关状态","data": "on_off","visible":false,
                        "render":function ( data, type ) {
                            if(data=='0')
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" ></label> </div></div>';
                            else if(data=='1')
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div>';
                            else
                                return "无";
                        }},
                    { title:"状态","data": "status" , "visible":false},
                    { title:"波特率" , data:"port","visible":false,
                        "render":function(data, type, full, meta){
                            var speedport_reg;
                            switch(full.port){
                                case 15:  speedport_reg = "载波/无线";break;
                                case 2:  speedport_reg = "RS485-1";break;
                                case 3:  speedport_reg = "RS485-2";break;
                                case 4:  speedport_reg = "RS485-3";break;
                                case 5:  speedport_reg = "RS485-4";break;
                                case 6:  speedport_reg = "RS485-5";break;
                                case 7:  speedport_reg = "RS485-6";break;
                                case 8:  speedport_reg = "RS485-7";break;
                                case 9:  speedport_reg = "RS485-8";break;
                                default: speedport_reg = "未设置";break;
                            }
                            switch(full.speed) {
                                case 16:
                                    break;
                                case 32:
                                    speedport_reg = speedport_reg + ",600";
                                    break;
                                case 64:
                                    speedport_reg = speedport_reg + ",1200";
                                    break;
                                case 96:
                                    speedport_reg = speedport_reg + ",2400";
                                    break;
                                case 128:
                                    speedport_reg = speedport_reg + ",4800";
                                    break;
                                case 160:
                                    speedport_reg = speedport_reg + ",7200";
                                    break;
                                case 192:
                                    speedport_reg = speedport_reg + ",9600";
                                    break;
                                default:
                                    break;
                            }
                            return speedport_reg;
                        }},
                    { title:"状态","data": "status",
                        "render":function ( data ) {
                            if(data==0)
                                return "<strong class='text-success'>正常</strong>";
                            else if(data==1)
                                return "<strong class='text-danger'>离线</strong>";
                            else if(data==3)
                                return "<strong class='text-danger'>底度</strong>";
                            else  if(data==2)
                                return "<strong class='text-danger'>故障</strong>";
                            else  if(data==4)
                                return "<strong class='text-info'>关闭</strong>";
                        }},
                    { title:"最新表盘数(kwh)","data": "newest_data",
                        "render":function (data) {
                            if(data < 0)
                                return "-";
                            else
                                return data;
                        }},
                    { title:"测量位置","data": "remarkinfo","width": "20%" },
                    { title:"备注","data": "remark"},
                    {title: "坏值", "data": 'uuid',className:'disclick',
                        "render": function (data, type, full) {
                            return '<a class="error" id="'+data+'" style="cursor: pointer">查看</a>'
                        }}
                ],
                "buttons": [
                    {
                        text:'保存',
                        extend:'excel',
                        className:'btn-info',
                        name:'表格'
                    }
                ],
                "order": [[ 5, 'desc' ],[ 11, 'asc' ],[ 7, 'asc' ],[ 8, 'asc' ]],
                "initComplete":function(){
                    var api=this.api();
                    jqueryMap.$batchchoice.html('<option value="">批次选择</option>');
                    api.column(2).data().unique().sort().each( function ( data ) {
                        jqueryMap.$batchchoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$batchchoice.on( 'change', function () {
                        api.column(2).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$subtypechoice.html('<option value="">类型选择</option>');
                    api.column(3).data().unique().sort().each( function ( data ) {
                        jqueryMap.$subtypechoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$subtypechoice.on( 'change', function () {
                        api.column(3).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$gatewaychoice.html('<option value="">网关选择</option>');
                    api.column(7).data().unique().sort().each( function ( data ) {
                        jqueryMap.$gatewaychoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$gatewaychoice.on( 'change', function () {
                        api.column(7).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$usedchoice.on( 'change', function () {
                        api.column(5).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$statuschoice.on( 'change', function () {
                        api.column(11).search( $(this).val() ).draw();
                    } );
                    $.material.init();
                }
            },
            tableErrorOption:{
                "columns": [
                    { title:"时间","data": "time" },
                    { title:"表盘数","data": "value" }
                ],
                "pagingType":'simple',
                "dom": 'lprt',
                "order": [[ 0, 'desc' ]]
            },
            formOption:[
                [{label:'label',text:'设备编号',size:2},{label:'input',type:'text',name:'equip',size:4,disable:true},{label:'label',text:'批号',size:2},{label:'input',type:'text',name:'batch',size:4,disable:true}],
                [{label:'label',text:'类型',size:2},{label:'input',type:'text',name:'type',size:2,disable:true},{label:'input',type:'text',name:'subtype',size:2,disable:true},{label:'label',text:'设备型号',size:2},{label:'input',type:'text',name:'model',size:4,disable:true}],
                [{label:'label',text:'安装位置',size:2},{label:'input',type:'text',name:'location',size:10,disable:true}],
                [{label:'label',text:'所属网关',size:2},{label:'select',name:'gatewayid',size:4,valid:{notEmpty: {}}},{label:'label',text:'测量点号',size:2},{label:'input',type:'text',name:'pn',size:4,valid:{notEmpty: {}}}],
                [{label:'label',text:'通信速率',size:2},{label:'select',name:'port',size:2,options:[{value:'15',text:'载波/无线'},{value:'2',text:'RS485-1'},{value:'3',text:'RS485-2'},{value:'4',text:'RS485-3'},{value:'5',text:'RS485-4'},{value:'6',text:'RS485-5'},{value:'7',text:'RS485-6'},{value:'8',text:'RS485-7'},{value:'9',text:'RS485-8'}],valid:{notEmpty: {}}},
                    {label:'select',name:'speed',size:2,options:[{value:'16',text:'无需设置'},{value:'32',text:'600'},{value:'64',text:'1200'},{value:'96',text:'2400'},{value:'128',text:'4800'},{value:'160',text:'7200'},{value:'192',text:'9600'}],valid:{notEmpty: {}}},
                    {label:'label',text:'通讯协议',size:2},{label:'select',type:'text',name:'protocol',size:4,options:[{value:'30',text:'DLT645-2007'},{value:'1',text:'DLT645-97'},{value:'32',text:'纳宇'},{value:'33',text:'方遒'},{value:'34',text:'智恒(水表)'}],valid:{notEmpty: {}}}],
                [{label:'label',text:'通讯地址',size:2},{label:'input',type:'text',name:'address',size:4,valid:{notEmpty: {}}},{label:'label',text:'示值整数位及小数点位个数',size:2},{label:'input',type:'text',name:'dataformat',size:4,valid:{notEmpty: {}}}],
                [{label:'label',text:'费率个数',size:2},{label:'input',type:'text',name:'ratecount',size:4,valid:{notEmpty: {}}},{label:'label',text:'曲线冻结补抄时间',size:2},{label:'input',type:'text',name:'last_15',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'用户大类及小类号',size:2},{label:'select',type:'text',name:'classnumber',size:4,options:[{value:'96',text:'重点表'},{value:'80',text:'非重点表'},{value:'64',text:'水表'}],valid:{notEmpty: {}}},{label:'label',text:'日冻结补抄时间',size:2},{label:'input',type:'text',name:'last_day',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'通讯密码',size:2},{label:'input',type:'text',name:'password',size:4,valid:{notEmpty: {}}},{label:'label',text:'月冻结补抄时间',size:2},{label:'input',type:'text',name:'last_mon',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'备注',size:2},{label:'input',type:'text',name:'remarkinfo',size:6,valid:{}},{label:'label',text:'有断闸',size:2},{label:'input',type:'text',name:'cut',size:2,valid:{notEmpty: {}}}]
            ],
            formOptions:[
                [{label:'label',text:'设备编号',size:2},{label:'input',type:'text',name:'equip',size:4,disable:true},{label:'label',text:'批号',size:2},{label:'input',type:'text',name:'batch',size:4,disable:true}],
                [{label:'label',text:'类型',size:2},{label:'input',type:'text',name:'type',size:2,disable:true},{label:'input',type:'text',name:'subtype',size:2,disable:true},{label:'label',text:'设备型号',size:2},{label:'input',type:'text',name:'model',size:4,disable:true}],
                [{label:'label',text:'所属网关',size:2},{label:'select',name:'gatewayid',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}},{label:'label',text:'测量点号',size:2},{label:'input',type:'text',name:'pn',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'通信端口号',size:2},{label:'select',name:'port',size:4,options:[{value:'15',text:'载波/无线'},{value:'2',text:'RS485-1'},{value:'3',text:'RS485-2'},{value:'4',text:'RS485-3'},{value:'5',text:'RS485-4'},{value:'6',text:'RS485-5'},{value:'7',text:'RS485-6'},{value:'8',text:'RS485-7'},{value:'9',text:'RS485-8'}],disable:true,adds:'checkbox',valid:{notEmpty: {}}},
                    {label:'label',text:'通讯协议',size:2},{label:'select',type:'text',name:'protocol',size:4,options:[{value:'30',text:'DLT645-2007'},{value:'1',text:'DLT645-97'},{value:'32',text:'纳宇'},{value:'33',text:'方遒'},{value:'34',text:'智恒(水表)'}],disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'通讯波特率',size:2},{label:'select',name:'speed',size:4,options:[{value:'16',text:'无需设置'},{value:'32',text:'600'},{value:'64',text:'1200'},{value:'96',text:'2400'},{value:'128',text:'4800'},{value:'160',text:'7200'},{value:'192',text:'9600'}],disable:true,adds:'checkbox',valid:{notEmpty: {}}},{label:'label',text:'示值整数位及小数点位个数',size:2},{label:'input',type:'text',name:'dataformat',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'通讯地址',size:2},{label:'input',type:'text',name:'address',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}},{label:'label',text:'曲线冻结补抄时间',size:2},{label:'input',type:'text',name:'last_15',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'用户大类及小类号',size:2},{label:'select',type:'text',name:'classnumber',size:4,disable:true,adds:'checkbox',options:[{value:'96',text:'重点表'},{value:'80',text:'非重点表'},{value:'64',text:'水表'}],valid:{notEmpty: {}}},{label:'label',text:'日冻结补抄时间',size:2},{label:'input',type:'text',name:'last_day',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'通讯密码',size:2},{label:'input',type:'text',name:'password',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}},{label:'label',text:'月冻结补抄时间',size:2},{label:'input',type:'text',name:'last_mon',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'费率个数',size:2},{label:'input',type:'text',name:'ratecount',size:4,disable:true,adds:'checkbox',valid:{notEmpty: {}}}],
                [{label:'label',text:'备注',size:2},{label:'input',type:'text',name:'remarkinfo',size:10,disable:true,adds:'checkbox',valid:{}}]
            ],
            formData1Option:[
                [{label:'label',text:'有功电能',size:2},{label:'input',type:'text',name:'active',size:4,disable:true},{label:'label',text:'有功功率',size:2},{label:'input',type:'text',name:'p',size:4,disable:true}],
                [{label:'label',text:'无功电能',size:2},{label:'input',type:'text',name:'reactive',size:4,disable:true},{label:'label',text:'无功功率',size:2},{label:'input',type:'text',name:'np',size:4,disable:true}],
                [{label:'label',text:'电压',size:2},{label:'input',type:'text',name:'ua',size:4,disable:true},{label:'label',text:'功率因数',size:2},{label:'input',type:'text',name:'glys',size:4,disable:true}],
                [{label:'label',text:'电流',size:2},{label:'input',type:'text',name:'ia',size:4,disable:true},{label:'label',text:'采集时间',size:2},{label:'input',type:'text',name:'datatime',size:4,disable:true}]
            ],
            formData3Option:[
                [{label:'label',text:'有功电能',size:2},{label:'input',type:'text',name:'active',size:2,disable:true},{label:'label',text:'无功电能',size:2},{label:'input',type:'text',name:'reactive',size:2,disable:true},{label:'label',text:'采集时间',size:2},{label:'input',type:'text',name:'datatime',size:2,disable:true}],
                [{label:'label',text:'A相电压',size:2},{label:'input',type:'text',name:'ua',size:2,disable:true},{label:'label',text:'B相电压',size:2},{label:'input',type:'text',name:'ub',size:2,disable:true},{label:'label',text:'C相电压',size:2},{label:'input',type:'text',name:'uc',size:2,disable:true}],
                [{label:'label',text:'A相电流',size:2},{label:'input',type:'text',name:'ia',size:2,disable:true},{label:'label',text:'B相电流',size:2},{label:'input',type:'text',name:'ib',size:2,disable:true},{label:'label',text:'C相电流',size:2},{label:'input',type:'text',name:'ic',size:2,disable:true}],
                [{label:'label',text:'总有功功率',size:2},{label:'input',type:'text',name:'p',size:2,disable:true},{label:'label',text:'总无功功率',size:2},{label:'input',type:'text',name:'np',size:2,disable:true},{label:'label',text:'功率因数',size:2},{label:'input',type:'text',name:'glys',size:2,disable:true}],
                [{label:'label',text:'A相有功功率',size:2},{label:'input',type:'text',name:'pa',size:2,disable:true},{label:'label',text:'A相无功功率',size:2},{label:'input',type:'text',name:'npa',size:2,disable:true},{label:'label',text:'A相功率因数',size:2},{label:'input',type:'text',name:'glysa',size:2,disable:true}],
                [{label:'label',text:'B相有功功率',size:2},{label:'input',type:'text',name:'pb',size:2,disable:true},{label:'label',text:'B相无功功率',size:2},{label:'input',type:'text',name:'npb',size:2,disable:true},{label:'label',text:'B相功率因数',size:2},{label:'input',type:'text',name:'glysb',size:2,disable:true}],
                [{label:'label',text:'C相有功功率',size:2},{label:'input',type:'text',name:'pc',size:2,disable:true},{label:'label',text:'C相无功功率',size:2},{label:'input',type:'text',name:'npc',size:2,disable:true},{label:'label',text:'C相功率因数',size:2},{label:'input',type:'text',name:'glysc',size:2,disable:true}]
            ],
            formDisplayOption:[
                [{label:'label',text:'设备编号',size:2},{label:'input',type:'text',name:'equip',size:4,disable:true},{label:'label',text:'测量位置',size:2},{label:'input',type:'text',name:'location',size:4,disable:true}],
                [{label:'label',text:'设备类型',size:2},{label:'input',type:'text',name:'subtype',size:4,disable:true},{label:'label',text:'备注',size:2},{label:'input',type:'text',name:'remark',size:4,disable:true}],
                [{label:'label',text:'查看选项',size:2},{label:'input',type:'radio',name:'opts',options:[{value:'energy',text:'能耗情况',checked:true},{value:'running',text:'运行情况'}],size:4}],
                [{label:'label',text:'时间选择',size:2},{label:'input',type:'daterange',name:'daterange',size:5,valid:{}}],
                [{label:'label',text:'类型选择',size:2},{label:'input',type:'radio',name:'time',options:[{value:'real',text:'实时数据'},{value:'active',text:'表盘读数'},{value:'minutes',text:'15分钟能耗',checked:true}],size:8,valid:{}}],
                [{label:'input',type:'radio',name:'parameter',options:[{value:'i',text:'电流',checked:true}],size:2},{label:'input',type:'checkbox',name:'i',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}},{label:'input',type:'radio',name:'parameter',options:[{value:'p',text:'有功'}],size:2},{label:'input',type:'checkbox',name:'p',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}}],
                [{label:'input',type:'radio',name:'parameter',options:[{value:'u',text:'电压'}],size:2},{label:'input',type:'checkbox',name:'u',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}},{label:'input',type:'radio',name:'parameter',options:[{value:'np',text:'无功'}],size:2},{label:'input',type:'checkbox',name:'np',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}}]
            ],
            formInputData:[
                [{label:'label',text:'采集时间',size:3},{label:'input',type:'date',name:'time',size:8}],
                [{label:'label',text:'表盘数',size:3},{label:'input',type:'text',name:'active',size:8}]
            ],
            formSetActive:[
                [{label:'label',text:'时间',size:3},{label:'input',type:'date',name:'lasttime',size:8}],
                [{label:'label',text:'底度',size:3},{label:'input',type:'text',name:'lastactive',size:8}]
            ],
            chartOption:{
                tooltip:{
                    trigger:'axis'
                },
                legend:{
                    data:[]
                },
                xAxis:[{
                    type:'category',
                    axisLabel:{
                        rotate:90
                    },
                    splitLine : {show : false},
                    data: []
                }],
                yAxis : [
                    {
                        scale:true,
                        type : 'value'
                    }
                ],
                series:[]
            },
            runningOption:{
                tooltip:{
                    trigger:'axis'
                },
                legend:{
                    data:[]
                },
                xAxis:[{
                    type:'category',
                    axisLabel:{
                        rotate:90
                    },
                    splitLine : {show : false},
                    data: []
                }],
                yAxis : [
                    {
                        scale:true,
                        type : 'value'
                    }
                ],
                series:[]
            },
            timeOptions:{
            },
            urlOption:{}
        },
        stateMap={
            $container:null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.maintenance.ammeter),
            modeltool:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  setUse, setOnOff, getRealData, updateAmmeter,  addEvent,childFormat,sendCommand,
        initDisplay,showDisplay,setTime,timeFunc,inputData,getData,setactive,getReals;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'电表列表','green','',configMap.body),
            $dtammeter  :new ecss.tools.makeTable($container.find('#DTammeter'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'电表参数设置','modal-lg'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $modals      :new ecss.tools.makeModal($container,'多电表参数设置','modal-lg'),
            $forms       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOptions),
            $modal1      :new ecss.tools.makeModal($container,'单相电表实时数据','modal-lg'),
            $form1       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formData1Option),
            $modal3      :new ecss.tools.makeModal($container,'三相电表实时数据','modal-lg'),
            $form3       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formData3Option),
            $modaldisplay:new ecss.tools.makeModal($container,'电表数据展示','modal-lg'),
            $formdisplay :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formDisplayOption),
            $modalinput   :new ecss.tools.makeModal($container,'表盘数录入'),
            $forminput     :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formInputData),
            $modalactive   :new ecss.tools.makeModal($container,'设置底度'),
            $formactive     :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formSetActive),
            $errormodal     :new ecss.tools.makeModal($container,'坏值列表'),
            $dterror        :new ecss.tools.makeTable($container.find('.modal-body').last().html('<table class="table table-hover table-bordered" id="DTerror"></table>').find('#DTerror'),configMap.tableErrorOption),
            $daterange  :new ecss.tools.makeDate(stateMap.$container.find('#datepicker'),{isRange:true}),
            $confirm     :new ecss.tools.makeModal($container),
            $ammeter      :$container.find('#ammeter'),
            $ammeterupdate  :$container.find('#update'),
            $ammeterrefresh  :$container.find('#refresh_para'),
            $ammeterrealdata  :$container.find('#realdata'),
            $ammeterinputdata  :$container.find('#inputdata'),
            $ammetersetactive  :$container.find('#setactive'),
            $batchchoice:$container.find('select[name=batch_choice]'),
            $subtypechoice:$container.find('select[name=subtype_choice]'),
            $gatewaychoice:$container.find('select[name=gateway_choice]'),
            $usedchoice:$container.find('select[name=if_used]'),
            $statuschoice:$container.find('select[name=what_status]'),
            $window     :$(window)
        };
    };
    childFormat=function(d){
        $.each(d,function(key,value){
            if(value==null)
                d[key]='未设置';
        });
/////////////////////////////////////////
        console.log(d);
        var classnumber;
        if(d.classnumber==96)
            classnumber = '重点表';
        else if(d.classnumber==80)
            classnumber = '非重点表';
        else if(d.classnumber==0)
            classnumber = '默认用户/电表';
        else
            classnumber = '未设置';
/////////////////////////////////////////
        var newest_data_reg="";
        if(d.newest_data < 0)
            newest_data_reg = "-";
        else
            newest_data_reg = d.newest_valid_data + " kwh";
/////////////////////////////////////////
        console.log(d.protocol);
        var	protocol_reg="";
        if(d.protocol==30)
            protocol_reg = 'DLT645-2007';
        else if(d.protocol==1)
            protocol_reg = 'DLT645-97';
        else if(d.protocol==32)
            protocol_reg = '纳宇';
        else if(d.protocol==33)
            protocol_reg = '方遒';
        else if(d.protocol==34)
            protocol_reg = '智恒(水表)';
        else
            protocol_reg = '未设置';
/////////////////////////////////////////
        var speedport_reg ="";
        switch(d.port){
            case 15:  speedport_reg = "载波/无线";break;
            case 2:  speedport_reg = "RS485-1";break;
            case 3:  speedport_reg = "RS485-2";break;
            case 4:  speedport_reg = "RS485-3";break;
            case 5:  speedport_reg = "RS485-4";break;
            case 6:  speedport_reg = "RS485-5";break;
            case 7:  speedport_reg = "RS485-6";break;
            case 8:  speedport_reg = "RS485-7";break;
            case 9:  speedport_reg = "RS485-8";break;
            default: speedport_reg = "未设置";break;
        }
        switch(d.speed){
            case 16:  break;
            case 32:  speedport_reg = speedport_reg+",600";break;
            case 64:  speedport_reg = speedport_reg+",1200";break;
            case 96:  speedport_reg = speedport_reg+",2400";break;
            case 128:  speedport_reg = speedport_reg+",4800";break;
            case 160:  speedport_reg = speedport_reg+",7200";break;
            case 192:  speedport_reg = speedport_reg+",9600";break;
            default: break;
        }

        return '<table cellpadding="10" cellspacing="10" border="0" style="padding-left:50px;margin-left:50px;margin-top:25px;margin-bottom:25px" width="1000px">'+
            '<tr>'+
            '<td style="text-align:right">设备批次：</td>'+
            '<td style="text-align:left">'+d.batch+'</td>'+
            '<td style="text-align:right">通信协议：</td>'+
            '<td style="text-align:left">'+protocol_reg+'</td>'+
            '<td style="text-align:right">通讯密码：</td>'+
            '<td style="text-align:left">'+d.password+'</td>'+
            '<td style="text-align:right">通信端口：</td>'+
            '<td style="text-align:left">'+speedport_reg+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">设备类型：</td>'+
            '<td style="text-align:left">'+d.subtype+'</td>'+
            '<td style="text-align:right">费率个数：</td>'+
            '<td style="text-align:left">'+d.ratecount+'</td>'+
            '<td style="text-align:right">示值整数位及小数点位个数：</td>'+
            '<td style="text-align:left">'+d.dataformat+'</td>'+
            '<td style="text-align:right">用户大类及小类号：</td>'+
            '<td style="text-align:left">'+classnumber+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">最新底度：</td>'+
            '<td style="text-align:left">'+newest_data_reg+'</td>'+
            '<td style="text-align:right">最新底度时间：</td>'+
            '<td style="text-align:left">'+d.newest_valid_data_time+'</td>'+
            '<td style="text-align:right">最新数据时间：</td>'+
            '<td style="text-align:left">'+d.newest_data_time+'</td>'+
            '<td style="text-align:right">最近操作时间：</td>'+
            '<td style="text-align:left">'+d.newest_operate_time+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">安装位置：</td>'+
            '<td colspan="5" style="text-align:left">'+ (d.group?d.group:'')+(d.build?('-'+d.build):'')+(d.floor?('-'+d.floor):'')+(d.room?('-'+d.room):'')+'</td>'+
            '<td style="text-align:right">备注：</td>'+
            '<td colspan="5" style="text-align:left">'+ d.remark+'</td>'+
            '</tr>'+
            '</table>';
    };
    sendCommand=function(){
        var data=jqueryMap.$dtammeter.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var ids=data.map(function(item){return item.uuid+':'+item.use}).join(",");
            var names=data.map(function(item){return item.equip}).join(",");
            jqueryMap.$confirm.confirm('确认重新下载您所勾选的'+data.length+'节点档案至网关？',names,function(){
                modelMap.model.post('refresh',function(){
                    jqueryMap.$confirm.hide();
                    ZENG.msgbox.show('档案下传成功！',4,3000);
                },function(){
                    ZENG.msgbox.show('档案下传失败！',5,3000);
                },ids);
            });
        }
    };
    getRealData=function(){
        var checkdata=function(data){
            $.each(data,function(key,value){
                if(value<0) data[key]='-';
            });
            return data;
        };
        var data=jqueryMap.$dtammeter.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'只能选择一条记录！');
        else
        if(data.typeid=='0000'){
            ZENG.msgbox.show('正在获取实时数据！',6);
            modelMap.model.post('getreal',function(d){
                jqueryMap.$form1.update(checkdata(d));
                ZENG.msgbox._hide();
                jqueryMap.$modal1.show(function () {
                    ZENG.msgbox.show('正在获取实时数据！',6);
                    modelMap.model.post('getreal',function(d){
                        jqueryMap.$form1.update(checkdata(d));
                    },function(){
                        ZENG.msgbox.show('实时数据获取失败！',5,3000);
                    },data[0].uuid,'json');
                });
            },function(){
                ZENG.msgbox.show('实时数据获取失败！',5,3000);
            },data[0].uuid,'json');
        }
        else {
            ZENG.msgbox.show('正在获取实时数据！',6);
            modelMap.model.post('getreal',function(d){
                console.log(d);
                ZENG.msgbox._hide();
                jqueryMap.$form3.update(checkdata(d));
                jqueryMap.$modal3.show(function () {
                    ZENG.msgbox.show('正在获取实时数据！',6);
                    modelMap.model.post('getreal',function(d){
                        jqueryMap.$form3.update(checkdata(d));
                    },function(){
                        ZENG.msgbox.show('实时数据获取失败！',5,3000);
                    },data[0].uuid,'json');
                });
            },function(){
                ZENG.msgbox.show('实时数据获取失败！',5,3000);
            },data[0].uuid,'json');
        }
    };
    setUse=function(){
        var check=$(this).prop('checked');
        var togglebutton=$(this);
        togglebutton.closest('tr').addClass('success');
        var data=jqueryMap.$dtammeter.getSelect();
        var ids=data.map(function(item){return item.uuid}).join(":");
        var names=data.map(function(item){return item.equip}).join(",");
        jqueryMap.$confirm.confirm('确认'+check?'开启':'关闭'+'以下'+data.length+'台网关？',names,function(){
            modelMap.model.post('setuse',function(){
                togglebutton.closest('tbody').find('.success input[name=use]').prop('checked',check);
                jqueryMap.$confirm.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'设置失败！请刷新重试！');
            },ids+','+(check?'1':'0'));
        },function(){
            togglebutton.prop('checked',!check);
        });
    };
    setOnOff=function(){
        var check=$(this).prop('checked');
        var togglebutton=$(this);
        togglebutton.closest('tr').addClass('success');
        var data=jqueryMap.$dtammeter.getSelect();
        var ids=data.map(function(item){return item.uuid}).join(":");
        var names=data.map(function(item){return item.equip}).join(",");
        jqueryMap.$confirm.confirm('确认'+check?'开启':'关闭'+'以下'+data.length+'台网关？',names,function(){
            modelMap.model.post('setonoff',function(){
                togglebutton.closest('tbody').find('.success input[name=on_off]').prop('checked',check);
                jqueryMap.$confirm.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'设置失败！请刷新重试！');
            },ids+'@'+names+'@'+(check?'1':'0'));
        },function(){
            togglebutton.prop('checked',!check);
        });
    };
    inputData=function(){
        var data=jqueryMap.$dtammeter.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择一条记录！');
        else
        if(data.length==1){
            jqueryMap.$modalinput.show(function () {
                var formdata=jqueryMap.$forminput.save();
                console.log(data[0]);
                console.log(formdata);
                modelMap.model.post('inputamdata',function(){
                    jqueryMap.$modalinput.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'录入失败！请刷新重试！');
                },data[0].uuid+','+formdata.time+','+formdata.active);
            });
        }
    };
    setactive=function(){
        var data=jqueryMap.$dtammeter.getSelect();
        if(data.length!=1)
            ecss.tools.popover.show($(this),'请选择一条记录！');
        else
        if(data.length==1){
            jqueryMap.$modalactive.show(function () {
                ZENG.msgbox.show('正在设置...',6,1000000);
                var formdata=jqueryMap.$formactive.save();
                modelMap.model.post('setlastactive',function(){
                    jqueryMap.$modalactive.hide();
                    jqueryMap.$dtammeter.reload();
                    ZENG.msgbox.show('设置成功！',4,3000);
                },function(){
                    ZENG.msgbox.show('设置失败！',5,3000);
                },data[0].uuid+','+formdata.lasttime+','+formdata.lastactive);
            });
        }
    };
    addEvent=function(){
        $('.tablecontent .dt-buttons .btn').show();
        $('table').on('click','.error',function(){
            ZENG.msgbox.show('正在获取...',6,1000000);
            modelMap.model.post('getamerrorvalues',function(data){
                jqueryMap.$dterror.setData(data,true);
                jqueryMap.$errormodal.show();
                jqueryMap.$errormodal.hideFooter();
                ZENG.msgbox.show('数据获取成功！',4,3000);
            },function(){
                ZENG.msgbox.show('数据获取失败！',5,3000);
            },$(this).prop('id'),'json');
        });
        jqueryMap.$dtammeter.setChild(childFormat);
        jqueryMap.$ammeterrefresh.click(sendCommand);
        jqueryMap.$ammeterrealdata.click(getRealData);
        jqueryMap.$dtammeter.setDblClick(showDisplay);
        jqueryMap.$ammeterupdate.click(updateAmmeter);
        jqueryMap.$ammeterinputdata.click(inputData);
        jqueryMap.$ammetersetactive.click(setactive);
        jqueryMap.$forminput.getElementsByName('time').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'YYYY/MM/DD HH:mm',
            maxDate:moment(),
            stepping:15,
            sideBySide:true
        });
        jqueryMap.$formactive.getElementsByName('lasttime').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'YYYY/MM/DD HH:mm',
            maxDate:moment(),
            stepping:15,
            sideBySide:true
        });
        jqueryMap.$container.find('#DTammeter tbody').on('change',' input[name=use]',setUse);
        jqueryMap.$container.find('#DTammeter tbody').on('change',' input[name=on_off]',setOnOff);
        modelMap.modeltool.post('getgateway',function(data){
            jqueryMap.$form.getElementsByName('gatewayid').html('<option value="#">暂不分配</option>');
            $.each(data,function ( i,item ) {
                jqueryMap.$form.getElementsByName('gatewayid').append( '<option value="'+item.id+'">'+item.name+'</option>' );
            } );
            jqueryMap.$forms.getElementsByName('gatewayid').html('<option value="#">暂不分配</option>');
            $.each(data,function ( i,item ) {
                jqueryMap.$forms.getElementsByName('gatewayid').append( '<option value="'+item.id+'">'+item.name+'</option>' );
            } );
        },null,null,'json');
    };
    updateAmmeter=function(){
        var data=jqueryMap.$dtammeter.getSelect();
        console.log(data);
        if(data.length==0)
            ecss.tools.popover.show($(this),'请选择记录！');
        else
        if(data.length>1){
            modelMap.model.set(data[0]);
            jqueryMap.$forms.update(data[0]);
            jqueryMap.$modals.show(function () {
                modelMap.model.set(jqueryMap.$forms.save());
                modelMap.model.setprop('uuid',data.map(function(item){return item.uuid+':'+item.use}).join(","));
                modelMap.model.post('update',function(){
                    jqueryMap.$dtammeter.reload();
                    jqueryMap.$modals.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
        else {
            modelMap.model.set(data[0]);
            jqueryMap.$form.update(data[0]);
            jqueryMap.$form.getElementsByName('location').val((data[0].group?data[0].group:'')+(data[0].build?('-'+data[0].build):'')+(data[0].floor?('-'+data[0].floor):'')+(data[0].room?('-'+data[0].room):''));
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.setprop('uuid',data.map(function(item){return item.uuid+':'+item.use}).join(","));
                modelMap.model.post('update',function(){
                    jqueryMap.$dtammeter.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ecss.tools.popover.show(jqueryMap.$modal.getElementsById('save'),'修改失败！请刷新重试！');
                });
            });
        }
    };
    showDisplay=function(data){
        configMap.urlOption.equipid=data.uuid;
        configMap.urlOption.modelid=data.uuid;
        jqueryMap.$formdisplay.getElementsByName('equip').val(data.equip);
        jqueryMap.$formdisplay.getElementsByName('subtype').val(data.subtype);
        jqueryMap.$formdisplay.getElementsByName('location').val(data.remarkinfo);
        jqueryMap.$formdisplay.getElementsByName('remark').val(data.remark);
        jqueryMap.$modaldisplay.show();
        getData();
    };
    initDisplay=function(){
        configMap.urlOption={
            equipid:'',
            modelid:'',
            startdate:moment().format('YYYY/MM/DD'),
            enddate:moment().add(1,'days').format('YYYY/MM/DD'),
            basetime:'minutes',
            para:'i',
            parameter:'',
            datetime:moment().format('YYYY/MM/DD'),
            opts:'energy'
        };
        var $form=jqueryMap.$formdisplay.getContainer();
        $form.append('<div class="form-group col-md-12"><div  id="energychart" style="width: 870px; height: 400px; margin: 0 auto"></div><div  id="runningchart" style="width: 870px; height: 400px; margin: 0 auto"></div></div>');
        jqueryMap.$energychart=new ecss.tools.makeChart('energychart');
        jqueryMap.$runningchart=new ecss.tools.makeChart('runningchart');
        jqueryMap.$runningchart.noData();
        jqueryMap.$formdisplay.getElementsByName('parameter').closest('.form-group').hide();
        jqueryMap.$formdisplay.getElementsById('runningchart').hide();
        jqueryMap.$formdisplay.getContainer().find('[type=checkbox]').prop('checked',false);
        jqueryMap.$formdisplay.getContainer().find('[type=checkbox]').prop('disabled',true);
        jqueryMap.$formdisplay.getElementsByName('i').prop('disabled',false);
        jqueryMap.$formdisplay.getElementsByName('opts').change(function(){
            configMap.urlOption.opts=$(this).val();
            if($(this).val()=='energy'){
                jqueryMap.$formdisplay.getElementsByName('parameter').closest('.form-group').hide();
                jqueryMap.$formdisplay.getElementsById('runningchart').hide();
                jqueryMap.$formdisplay.getElementsById('energychart').show();
                jqueryMap.$formdisplay.getElementsByName('time').closest('.form-group').show();
            }
            else{
                jqueryMap.$formdisplay.getElementsByName('parameter').closest('.form-group').show();
                jqueryMap.$formdisplay.getElementsById('runningchart').show();
                jqueryMap.$formdisplay.getElementsById('energychart').hide();
                jqueryMap.$formdisplay.getElementsByName('time').closest('.form-group').hide();
            }
            setTime('minutes');
        });
        jqueryMap.$formdisplay.getElementsByName('parameter').change(function(){
            configMap.urlOption.para=$(this).val();
            jqueryMap.$formdisplay.getContainer().find('[type=checkbox]').prop('checked',false);
            jqueryMap.$formdisplay.getContainer().find('[type=checkbox]').prop('disabled',true);
            jqueryMap.$formdisplay.getElementsByName($(this).val()).prop('disabled',false);
            getData();
        });
        jqueryMap.$formdisplay.getElementsByName('time').change(function(){setTime($(this).val());});
        jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
        var startdate=moment().format('YYYY/MM/DD');
        jqueryMap.$formdisplay.getElementsByName('start').val(startdate);
        jqueryMap.$formdisplay.getElementsByName('end').val(startdate);
        $('.input-group-addon').hide();
        jqueryMap.$formdisplay.getElementsByName('end').hide();
        jqueryMap.$formdisplay.getContainer().find('[type=checkbox]').change(getData);
    };
    timeFunc=function(start,end){
        var start=jqueryMap.$formdisplay.getElementsByName('start').val();
        var end=jqueryMap.$formdisplay.getElementsByName('end').val();
        if(start!=''&&end!='') {
            if(start>end){
                jqueryMap.$form.getElementsByName('start').val(end);
                start=end;
            }
            configMap.urlOption.startdate = moment(start).format('YYYY/MM/DD');
            configMap.urlOption.datetime=moment(start).format('YYYY/MM/DD');
            if (configMap.timeOptions.minViewMode == 1)
                configMap.urlOption.enddate = moment(end).add(1, 'months').format('YYYY/MM/DD');
            else
            if(configMap.urlOption.basetime=='day')
                configMap.urlOption.enddate = moment(end).add(1, 'days').format('YYYY/MM/DD');
            else{
                configMap.urlOption.startdate =start;
                configMap.urlOption.enddate = moment(start).add(1, 'days').format('YYYY/MM/DD');
            }
            getData();
        }
    };
    setTime=function(basetime){
        configMap.urlOption['basetime']=basetime;
        if(basetime=='minutes'||basetime=='hour'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().format('YYYY/MM/DD');
            jqueryMap.$formdisplay.getElementsByName('start').val(startdate);
            jqueryMap.$formdisplay.getElementsByName('end').val(startdate);
            $('.input-group-addon').hide();
            jqueryMap.$formdisplay.getElementsByName('end').hide();
        }else
        if(basetime=='day'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().format('YYYY/MM/DD');
            jqueryMap.$formdisplay.getElementsByName('start').val(startdate);
            jqueryMap.$formdisplay.getElementsByName('end').val(startdate);
            $('.input-group-addon').show();
            jqueryMap.$formdisplay.getElementsByName('end').show();
        }else
        if(basetime=='month'){
            configMap.timeOptions.minViewMode=1;
            configMap.timeOptions.format='yyyy/mm';
            var startdate=moment().format('YYYY/MM');
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            jqueryMap.$formdisplay.getElementsByName('start').val(startdate);
            jqueryMap.$formdisplay.getElementsByName('end').val(startdate);
            $('.input-group-addon').show();
            jqueryMap.$formdisplay.getElementsByName('end').show();
        }
        timeFunc();
    };
    getReals=function () {
        modelMap.model.post('getreal',function(d){
            configMap.chartOption.xAxis[0].data.push(d.datatime);
            configMap.chartOption.series[0].data.push(d.active);
            if(configMap.urlOption.basetime=='real'){
                jqueryMap.$energychart.setData(configMap.chartOption);
                getReals();
            }
        },null,configMap.urlOption.equipid,'json');
    };
    getData=function(){
        if(configMap.urlOption.opts=='energy'){
            if(configMap.urlOption.basetime!='real'){
                modelMap.model.post('getenergy',function(data){
                    console.log(data);
                    if(data.categories.length>0){
                        data.energy.markPoint={
                            data : [
                                {type : 'max', name: '最大值'},
                                {type : 'min', name: '最小值'}
                            ]
                        };
                        data.energy.markLine={
                            data : [
                                {type : 'average', name: '平均值'}
                            ]
                        };
                        data.energy.type='line';
                        configMap.chartOption.legend.data=[data.energy.name];
                        configMap.chartOption.xAxis[0].data=data.categories;
                        configMap.chartOption.series=[data.energy];
                        jqueryMap.$energychart.setData(configMap.chartOption);
                    }else{
                        jqueryMap.$energychart.noData();
                    }
                },null,configMap.urlOption,'json');
            }else {
                jqueryMap.$energychart.noData();
                configMap.chartOption.legend.data=['实时数据获取'];
                configMap.chartOption.xAxis[0].data=[];
                configMap.chartOption.series=[{
                    markPoint:{
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    },
                    markLine:{
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    },
                    type:'line',
                    data:[]
                }];
                getReals();
            }

        }
        else{
            configMap.runningOption.legend.data=[];
            configMap.runningOption.series=[];
            var para=jqueryMap.$formdisplay.getElementsByName('parameter').filter(':checked').val();
            var checks=jqueryMap.$formdisplay.getElementsByName(para).filter(':checked');
            if(checks.length==0){
                jqueryMap.$runningchart.noData();
            }
            else
                $.each(checks,function(i,item){
                    console.log($(item).val());
                    configMap.urlOption.parameter=para+$(item).val();
                    modelMap.model.post('getpara',function(data){
                        console.log(data);
                        if(data.categories.length>0){
                            data.energy.type='line';
                            configMap.runningOption.legend.data.push(data.energy.name);
                            configMap.runningOption.xAxis[0].data=data.categories;
                            configMap.runningOption.series.push(data.energy);
                            jqueryMap.$runningchart.setData(configMap.runningOption);
                        }else{
                            jqueryMap.$runningchart.noData();
                        }

                    },null,configMap.urlOption,'json');
                });
        }
    };

    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        initDisplay();
        addEvent();
    };
    return{initModule:initModule};
}());