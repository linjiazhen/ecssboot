/**
 * Created by jason on 2015/3/4.
 */
ecss.gateway=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">智能网关</li>'
                    +'</ol>'
                +'</div>'
                +'<div id="portlet"></div>'
            +'</div>',
            body:String()
                +'<div class="form-group">'
                    +'<lable class="col-md-1 control-label">条件筛选:</lable>'
                    +'<div class="col-md-2"><select name="batch_choice" class="form-control"></select></div>'
                    +'<div class="col-md-2"><select name="model_choice" class="form-control"></select></div>'
                    +'<div class="col-md-2"><select name="if_used" class="form-control"><option value="">启用状态</option><option value="1">已启用</option><option value="0">未启用</option></select></div>'
                    +'<div class="col-md-2"><select name="what_status" class="form-control"><option value="">工作状态</option><option value="1">在线</option><option value="0">离线</option></select></div>'
                +'</div>'
                +'<div class="tablecontent" class="form-group" style="margin-top:45px">'
                    +'<div class="buttons">'
                        +'<button class="btn btn-warning btn-sm" id="update">编辑</button>'
                        +'<button class="btn btn-success btn-sm" id="processData">数据处理</button>'
                        +'<div class="btn-group">'
                            +'<button type="button" class="btn btn-success btn-sm dropdown-toggle" id="reset" data-toggle="dropdown">复位<span class="caret"></span></button>'
                            +'<ul class="dropdown-menu" role="menu">'
                                +'<li><a class="gatewayreboot">网关硬件重启</a></li>'
                                +'<li><a class="gatewayclear">节点档案清除</a></li>'
                            +'</ul>'
                        +'</div>'
                        +'<div class="btn-group">'
                            +'<button type="button" class="btn btn-info btn-sm dropdown-toggle" id="settime" data-toggle="dropdown">网关校时<span class="caret"></span></button>'
                            +'<ul class="dropdown-menu" role="menu">'
                                +'<li><a class="gatewaytiming">设置网关时间</a></li>'
                                +'<li><a class="getgatewaytime">获取网关时间</a></li>'
                            +'</ul>'
                        +'</div>'
                    +'</div>'
                    +'<table class="table table-hover table-bordered" id="DTgateway"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getallgateways.do", "type": "POST" },
                "columns": [ {"title":"详情","className": 'details-control', "data":   null, orderable: false, searchable:false,sortable:false, "defaultContent": ''},
                    { title:"设备编号","data": "equip" },
                    { title:"设备批次","data": "batch" , "visible":false },
                    { title:"子类","data": "subtype" },
                    { title:"型号","data": "model"},
                    { "data": "use" , "visible":false},
                    { title:"启用","data": "use",
                        render: function(data){
                            if(data==1)
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" checked></label> </div></div>';
                            else
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use"></label> </div></div>';
                        }},
                    { title:"通讯地址","data": "address" },
                    { "data": "state" , "visible":false},
                    { title:"开关状态","data": "state",
                        "render":function ( data ) {
                            if(data==1)
                                return "<strong class='text-success'>在线</strong>";
                            else
                                return "<strong class='text-danger'>离线</strong>";
                        }},
                    { title:"终端IP","data": "zd_ip" },
                    { title:"位置","data": "group",
                        "render":function ( data, type, full, meta ) {
                            return (full.group?full.group:'')+(full.build?('-'+full.build):'')+(full.floor?('-'+full.floor):'')+(full.room?('-'+full.room):'');
                        }},
                    { title:"备注","data": "remark" }
                ],
                "buttons": [
                    {
                        text:'保存',
                        extend:'excel',
                        className:'btn-info',
                        name:'表格'
                    }
                ],
                "multiple":false,
                "order": [[ 1, 'asc' ]],
                "initComplete":function(){
                    var api=this.api();
                    jqueryMap.$batchchoice.html('<option value="">批次选择</option>');
                    api.column(2).data().unique().sort().each( function ( data ) {
                        jqueryMap.$batchchoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$batchchoice.on( 'change', function () {
                        api.column(2).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$modelchoice.html('<option value="">型号选择</option>');
                    api.column(4).data().unique().sort().each( function ( data ) {
                        jqueryMap.$modelchoice.append( '<option value="'+data+'">'+data+'</option>' );
                    } );
                    jqueryMap.$modelchoice.on( 'change', function () {
                        api.column(4).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$usedchoice.on( 'change', function () {
                        api.column(5).search( $(this).val() ).draw();
                    } );
                    jqueryMap.$statuschoice.on( 'change', function () {
                        api.column(8).search( $(this).val() ).draw();
                    } );
                }
            },
            equipTableOption:{
                "ajax": { "url": "getallammeters.do", "type": "POST"},
                "columns": [ {title:"序号", "data":   null, render:function (data, type, full, meta) {
                            return meta.row+1;
                        }},
                    { title:"设备编号","data": "equip" },
                    { title:"类型","data": "type"},
                    { title:'测量点号',"data": "pn" },
                    { title:"数据校验","data": "newest_data",
                        "render":function (data) {
                            if(data < 0)
                                return "无效";
                            else
                                return "有效";
                        }},
                    { title:"最新表盘数","data": "newest_data",
                        "render":function (data) {
                            if(data < 0)
                                return "-";
                            else
                                return data;
                        }},
                    { title:"15分钟耗能",data:"15_data"},
                    { title:"数据时间","data":"newest_data_time"},
                    { title:"采集时间","data":"createtime"},
                    { title:"测量能耗","data":"energytype"},
                    { title:"测量位置","data": "remarkinfo","width": "20%" }
                ],
                // "buttons": [
                //     {
                //         text:'保存',
                //         extend:'excel',
                //         className:'btn-info',
                //         name:'表格'
                //     }
                // ],
                "order": [[ 1, 'asc' ]],
                "initComplete":function(){
                    $.material.init();
                }
            },
            formOption:[
                [{label:'label',text:'设备编号',size:2},{label:'input',type:'text',name:'equip',size:4,disable:true},{label:'label',text:'批号',size:2},{label:'input',type:'text',name:'batch',size:4,disable:true}],
                [{label:'label',text:'类型',size:2},{label:'input',type:'text',name:'type',size:2,disable:true},{label:'input',type:'text',name:'subtype',size:2,disable:true},{label:'label',text:'设备型号',size:2},{label:'input',type:'text',name:'model',size:4,disable:true}],
                [{label:'label',text:'安装位置',size:2},{label:'input',type:'text',name:'location',size:10,disable:true}],
                [{label:'label',text:'终端地址',size:2},{label:'input',type:'text',name:'address',size:2,valid:{notEmpty: {}}},{label:'label',text:'行政区划',size:2},{label:'input',type:'text',name:'code',size:2,valid:{notEmpty: {}}},{label:'label',text:'液晶操作密码',size:2},{label:'input',type:'text',name:'zd_lcd_password',size:2,valid:{notEmpty: {}}}],
                [{label:'label',text:'主站IP',size:2},{label:'input',type:'text',name:'ipmain',size:2,valid:{notEmpty: {}}},{label:'label',text:'主站端口',size:2},{label:'input',type:'text',name:'portmain',size:2,valid:{notEmpty: {}}},{label:'label',text:'终端IP',size:2},{label:'input',type:'text',name:'zd_ip',size:2,valid:{notEmpty: {}}}],
                [{label:'label',text:'主站备用IP',size:2},{label:'input',type:'text',name:'ipbackup',size:2,valid:{notEmpty: {}}},{label:'label',text:'备用端口',size:2},{label:'input',type:'text',name:'portbackup',size:2,valid:{notEmpty: {}}},{label:'label',text:'终端子网掩码',size:2},{label:'input',type:'text',name:'zd_mask',size:2,valid:{notEmpty: {}}}],
                [{label:'label',text:'apn',size:2},{label:'input',type:'text',name:'apn',size:2,valid:{notEmpty: {}}},{label:'label',text:'数传延时RTS(20ms)',size:2},{label:'input',type:'text',name:'delay1',size:2,valid:{notEmpty: {}}},{label:'label',text:'终端网关IP:',size:2},{label:'input',type:'text',name:'zd_gateway',size:2,valid:{notEmpty: {}}}],
                [{label:'label',text:'传输延时(分钟)',size:2},{label:'input',type:'text',name:'delay2',size:2,valid:{notEmpty: {}}},{label:'label',text:'等待响应时间(秒)',size:2},{label:'input',type:'text',name:'waittime',size:2,valid:{notEmpty: {}}},{label:'label',text:'终端MAC',size:2},{label:'input',type:'text',name:'zd_mac',size:2,valid:{notEmpty: {}}}],
                [{label:'label',text:'心跳周期(分钟)',size:2},{label:'input',type:'text',name:'heartbeat',size:2,valid:{notEmpty: {}}},{label:'label',text:'主站确认',size:2},{label:'input',type:'radio',name:'flag',size:4,options:[{value:0,text:'不需要'},{value:1,text:'需要'}],valid:{notEmpty: {}}}]
            ]
        },
        stateMap={
            $container:null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.maintenance.gateway)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  setUse,  updateGateway,processData, addEvent,childFormat,sendCommand;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'网关列表','green','',configMap.body),
            $dtgateway  :new ecss.tools.makeTable($container.find('#DTgateway'),configMap.tableOption),
            $modal      :new ecss.tools.makeModal($container,'网关参数设置','modal-lg'),
            $form       :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOption),
            $equipModal :new ecss.tools.makeModal($container,'数据处理','modal-lg'),
            $dtprocess:new ecss.tools.makeTable($container.find('.modal-body').html('<table class="table table-hover table-bordered" id="DTprocess"></table>').find('#DTprocess'),configMap.equipTableOption),
            $confirm     :new ecss.tools.makeModal($container),
            $batchchoice:$container.find('select[name=batch_choice]'),
            $modelchoice:$container.find('select[name=model_choice]'),
            $usedchoice:$container.find('select[name=if_used]'),
            $statuschoice:$container.find('select[name=what_status]'),
            $gateway      :$container.find('#gateway'),
            $gatewayupdate  :$container.find('#update'),
            $gatewayProcessData:$container.find('#processData'),
            $gatewayclear  :$container.find('.gatewayclear'),
            $gatewayreboot   :$container.find('.gatewayreboot'),
            $gatewaytiming:$container.find('.gatewaytiming'),
            $getgatewaytime:$container.find('.getgatewaytime'),
            $window     :$(window)
        };
    };
    childFormat=function(d){
        var gateway_ver;
        if(d.gateway_ver != null)
            gateway_ver=d.gateway_ver.split(",");
        var flag;
        if(d.flag == 1){
            flag = "需要";
        }
        else
            flag = "不需要";
        return '<table cellpadding="10" cellspacing="10" border="0" style="padding-left:50px;" width="1024px">'+
            '<tr>'+
            '<td style="text-align:right">设备批次：</td>'+
            '<td style="text-align:left">'+d.batch+'</td>'+
            '<td style="text-align:right">最近登陆时间：</td>'+
            '<td style="text-align:left">'+d.login_time+'</td>'+
            '<td style="text-align:right">上次下线时间：</td>'+
            '<td style="text-align:left">'+d.lost_connet_time+'</td>'+
            '<td style="text-align:right">最近心跳时间：</td>'+
            '<td style="text-align:left">'+d.newest_hearbeat_time+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">主站IP：</td>'+
            '<td style="text-align:left">'+d.ipmain+'</td>'+
            '<td style="text-align:right">主站端口：</td>'+
            '<td style="text-align:left">'+d.portmain+'</td>'+
            '<td style="text-align:right">主站备用IP：</td>'+
            '<td style="text-align:left">'+d.ipbackup+'</td>'+
            '<td style="text-align:right">备用端口：</td>'+
            '<td style="text-align:left">'+d.portbackup+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">APN：</td>'+
            '<td style="text-align:left">'+d.apn+'</td>'+
            '<td style="text-align:right">心跳周期(分钟)：</td>'+
            '<td style="text-align:left">'+d.heartbeat+'</td>'+
            '<td style="text-align:right">需主站确认：</td>'+
            '<td style="text-align:left">'+flag+'</td>'+
            '<td style="text-align:right">等待响应时间(秒)：</td>'+
            '<td style="text-align:left">'+d.waittime+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">传输延时(分钟)：</td>'+
            '<td style="text-align:left">'+d.delay2+'</td>'+
            '<td style="text-align:right">数传延时RTS(20ms)：</td>'+
            '<td style="text-align:left">'+d.delay1+'</td>'+
            '<td style="text-align:right">行政区划编码：</td>'+
            '<td style="text-align:left">'+d.code+'</td>'+
            '<td style="text-align:right">液晶操作密码：</td>'+
            '<td style="text-align:left">'+d.zd_lcd_password+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">终端IP：</td>'+
            '<td style="text-align:left">'+d.zd_ip+'</td>'+
            '<td style="text-align:right">终端子网掩码：</td>'+
            '<td style="text-align:left">'+d.zd_mask+'</td>'+
            '<td style="text-align:right">终端网关IP：</td>'+
            '<td style="text-align:left">'+d.zd_gateway+'</td>'+
            '<td style="text-align:right">终端MAC地址：</td>'+
            '<td style="text-align:left">'+d.zd_mac+'</td>'+
            '</tr>'+
            '<tr>'+
            '<td style="text-align:right">厂商编号：</td>'+
            '<td style="text-align:left">'+gateway_ver[0]+'</td>'+
            '<td style="text-align:right">软件版本号：</td>'+
            '<td style="text-align:left">'+gateway_ver[2]+'</td>'+
            '<td style="text-align:right">硬件版本号：</td>'+
            '<td style="text-align:left">'+gateway_ver[6]+'</td>'+
            '<td style="text-align:right">软件发布日期：</td>'+
            '<td style="text-align:left">'+gateway_ver[3]+'</td>'+
            '</tr>'+
            '</table>';
    };
    sendCommand=function(command){
        var data=jqueryMap.$dtgateway.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请至少选择一条记录！');
        else {
            var id=data.map(function(item){return item.uuid}).join(",");
            var names=data.map(function(item){return item.equip}).join(",");
            jqueryMap.$confirm.confirm('确认对您所选择的网关进行操作？',names,function(){
                modelMap.model.post('control',function(){
                    jqueryMap.$dtgateway.reload();
                    jqueryMap.$confirm.hide();
                },function(){
                    ZENG.msgbox.show('操作失败！请刷新重试！', 5, 3000);
                },command+','+id);
            });
        }
    };
    setUse=function(){
        var check=$(this).prop('checked');
        var togglebutton=$(this);
        togglebutton.closest('table').find('.success').removeClass('success');
        togglebutton.closest('tr').addClass('success');
        var data=jqueryMap.$dtgateway.getSelect();
        var id=data[0].uuid;
        var name=data[0].equip;
        jqueryMap.$confirm.confirm('确认'+check?'开启':'关闭'+'以下网关？',name,function(){
            modelMap.model.post('setuse',function(){
                togglebutton.prop('checked',check);
                jqueryMap.$confirm.hide();
            },function(){
                ZENG.msgbox.show('操作失败！请刷新重试！', 5, 3000);
            },id+','+(check?'1':'0'));
        },function(){
            togglebutton.prop('checked',!check);
        });
    };
    addEvent=function(){
        $('.tablecontent .dt-buttons .btn').show();
        jqueryMap.$dtgateway.setChild(childFormat);
        jqueryMap.$gatewayclear.click(function () {sendCommand('01F4');});
        jqueryMap.$gatewayreboot.click(function () {sendCommand('01F1');});
        jqueryMap.$gatewaytiming.click(function () {sendCommand('05F31');});
        jqueryMap.$container.find('#DTgateway tbody').on('change',' input',setUse);
    };
    updateGateway=function(){
        var data=jqueryMap.$dtgateway.getSelect();
        if(data.length==0)
            ecss.tools.popover.show($(this),'请选择记录！');
        else {
            modelMap.model.set(data[0]);
            jqueryMap.$form.update(data[0]);
            jqueryMap.$form.getElementsByName('location').val((data[0].group?data[0].group:'')+(data[0].build?('-'+data[0].build):'')+(data[0].floor?('-'+data[0].floor):'')+(data[0].room?('-'+data[0].room):''));
            jqueryMap.$modal.show(function () {
                modelMap.model.set(jqueryMap.$form.save());
                modelMap.model.setprop('uuid',data.map(function(item){return item.uuid+':'+item.use}).join(","));
                modelMap.model.post('update',function(){
                    jqueryMap.$dtgateway.reload();
                    jqueryMap.$modal.hide();
                },function(){
                    ZENG.msgbox.show('更新失败！请刷新重试！', 5, 3000);
                });
            });
        }
    };
    processData=function () {
        console.log(1);
        jqueryMap.$equipModal.show();
    }
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        jqueryMap.$gatewayupdate.click(updateGateway);
        jqueryMap.$gatewayProcessData.click(processData);
    };
    return{initModule:initModule};
}());