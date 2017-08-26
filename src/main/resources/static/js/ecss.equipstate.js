/**
 * Created by jason on 2015/3/4.
 */
ecss.equipstate=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">设备状态</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div class="col-lg-12" id="condition"></div>'
            +'<div class="col-lg-12" id="chart"></div>'
            +'<div class="col-lg-12" id="table"></div>',
            condition_body:'<div id="conditions"></div>',
            chart_body:'<div class="tile col-lg-4" ><div id="piechart" style="width:500px;height: 300px"></div></div><div class="tile col-lg-8" ><div id="barchart" style="width:1000px;height: 300px;"></div></div>',
            table_body: '<div class="tablecontent">'
                +'<table class="table table-hover table-bordered" id="DTequipalert"></table>'
                +'</div>',
            tableOption:{
                "ajax": { "url": "getequipalerts.do", "type": "POST" },
                "columns": [
                    { title:'设备编号',data:'equipid'},
                    { title:'设备批次',data:'subtype'},
                    { title:"设备类型","data": "type",
                        "render":function ( data , type, full){
                            if(data==1) return '网关';
                            else
                            if(data==2) return '电表';
                            else
                            if(data==3) return '水表';
                        } },
                    { title:"状态","data": "status",
                        "render":function ( data , type, full){
                            if(data==0) return '恢复正常';
                            else
                            if(data==1) return '离线';
                            else
                            if(data==2) return '故障';
                            else
                            if(data==3) return '底度错误';
                        }},
                    { title:"原因","data": "remark"},
                    { title:"测量位置","data": "place"},
                    { title:"预警时间","data": "time"}

                ],
                "order":[[6,'desc'],[4,'desc']]
            },
            conditionOption:[
                [{label:'label',text:'设备类型',size:[12,3,2,1]} ,{label:'input',type:'radio',name:'type',options:[{value:'ammeter',text:'电表',checked:true},{value:'watermeter',text:'水表'}],size:[12,9,5,5],valid:{}}],
                [{label:'label',text:'测量位置',size:[0,3,2,1]}
                    ,{label:'select',type:'text',name:'group',size:[12,4,5,2],valid:{}}
                    ,{label:'select',type:'text',name:'build',size:[12,4,5,2],valid:{}}
                    ,{label:'select',type:'text',name:'floor',size:[12,4,5,2],valid:{}}
                    ,{label:'select',type:'text',name:'room',size:[12,4,5,2],valid:{}}]
            ],
            baroption : {
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data:['在线', '离线','故障','底度']
                },
                toolbox: {
                    show : false
                },
                xAxis : [
                    {
                        type : 'category',
                        splitLine : {show : false},
                        data : []

                    }
                ],
                yAxis : [
                    {
                        type : 'value'

                    }
                ],
                series : [
                    {
                        name:'在线',
                        type:'bar',
                        stack:'1',
                        data:[]
                    },
                    {
                        name:'离线',
                        type:'bar',
                        stack:'1',
                        data:[]
                    },
                    {
                        name:'故障',
                        type:'bar',
                        stack:'1',
                        data:[]
                    },
                    {
                        name:'底度',
                        type:'bar',
                        stack:'1',
                        data:[]
                    }
                ]
            },
            pieoption : {
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient : 'vertical',
                    x : 'left',
                    data:[]
                },
                series : [
                    {
                        selectedMode: 'single',
                        type:'pie',
                        itemStyle : {
                            normal : {
                                label : {
                                    show : true,
                                    formatter : function (params){
                                        return params.name+':'+params.value;
                                    }
                                //    position : 'inner'
                                }
                                //labelLine : {
                                //    show : false
                                //}
                            }
                        },
                        data:[]
                    }
                ]
            },
            urlOption:{
                type:'ammeter',
                level:'school',
                model:'all'
            }
        },
        stateMap={
            $container:null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.alert.equipstate),
            modeltool:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addOptions,  addEvent,setAim,setType,getEquipState,changeBuild,changeOrgan;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portletcond    :new ecss.tools.makePortlet($container.find('#condition'),'查询条件','orange','',configMap.condition_body),
            $portletchart    :new ecss.tools.makePortlet($container.find('#chart'),'状态汇总','red','',configMap.chart_body),
            $portlettable    :new ecss.tools.makePortlet($container.find('#table'),'状态日志','green','',configMap.table_body),
            $condform       :new ecss.tools.makeForm($container.find('#conditions'),configMap.conditionOption),
            $dtequipalert  :new ecss.tools.makeTable($container.find('#DTequipalert'),configMap.tableOption),
            $barchart       :new ecss.tools.makeChart('barchart',configMap.baroption),
            $piechart       :new ecss.tools.makeChart('piechart',configMap.pieoption),
            $window         :$(window)
        };
    };
    setType=function(){
        configMap.urlOption['type']=$(this).val();
        getEquipState();
    };
    addOptions=function(url,para,$select,text){
        modelMap.modeltool.post(url,function(data){
            if(data!=null){
                $select.html(text);
                $.each(data,function(i,item){
                    $select.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,para,'json');
    };
    addEvent=function(){
        jqueryMap.$container.find('#chart .portlet-body').css('height','350px');
        jqueryMap.$condform.getElementsByName('group').closest('.form-group').find('select').change(getEquipState);
        var $group=jqueryMap.$condform.getElementsByName('group');
        $group.html('<option value="all">全校</option>');
        addOptions('getgroup',null,$group, '<option value="all">全校</option>');
        var $build=jqueryMap.$condform.getElementsByName( 'build');
        $build.html('<option value="#">全区</option>');
        $group.change(function(){
            addOptions('getbuild',$group.val(),$build,'<option value="#">全区</option>');
        });
        var $floor=jqueryMap.$condform.getElementsByName('floor');
        $floor.html('<option value="#">全楼</option>');
        $build.change(function(){
            addOptions('getfloor',$build.val(),$floor, '<option value="#">全楼</option>');
        });
        var $room=jqueryMap.$condform.getElementsByName('room');
        $room.html('<option value="#">全层</option>');
        $floor.change(function(){
            addOptions('getroom',$floor.val(),$room,'<option value="#">全层</option>');
        });
        jqueryMap.$condform.getElementsByName('type').change(setType);
    };
    getEquipState=function(){
        $.each(jqueryMap.$condform.getElementsByName('group').closest('.form-group').find('select'),function(index,item){
            if($(item).val()=='#') return;
            configMap.urlOption.model=$(item).val();
            if($(item).val()!='all')
                configMap.urlOption.level=$(this).prop('name');
            else
                configMap.urlOption.level='school';
        });
        var state={'正常':0,'离线':0,'故障':0,'底度':0};
        var typestate={};
        modelMap.model.post('getequipstate',function(data){
            console.log(data);
            if(data.length!=0) {
                $.each(data, function (i, item) {
                    // console.log(item.energytype+':'+item.status);
                    if (typestate[item.energytype] == null) typestate[item.energytype] = [0, 0, 0,0];
                    if (item.status == '0') {
                        state["正常"] = state['正常'] + 1;
                        typestate[item.energytype][0] = typestate[item.energytype][0] + 1;
                    }
                    else if (item.status == '1') {
                        state["离线"] = state['离线'] + 1;
                        typestate[item.energytype][1] = typestate[item.energytype][1] + 1;
                    }
                    else if (item.status == '2'){
                        state["故障"] = state['故障'] + 1;
                        typestate[item.energytype][2] = typestate[item.energytype][2] + 1;
                    }
                    else if (item.status == '3') {
                        state["底度"] = state['底度'] + 1;
                        typestate[item.energytype][3] = typestate[item.energytype][3] + 1;
                    }
                    var z = 0;
                    configMap.pieoption.legend.data = [];
                    configMap.pieoption.series[0].data = [];
                    $.each(state, function (key, value) {
                        configMap.pieoption.legend.data[z] = key;
                        configMap.pieoption.series[0].data[z] = {};
                        configMap.pieoption.series[0].data[z].name = key;
                        configMap.pieoption.series[0].data[z].value = value;
                        z++;
                    });
                    z = 0;
                    configMap.baroption.xAxis[0].data = [];
                    configMap.baroption.series[0].data = [];
                    configMap.baroption.series[1].data = [];
                    configMap.baroption.series[2].data = [];
                    configMap.baroption.series[3].data = [];
                    $.each(typestate, function (key, value) {
                        configMap.baroption.xAxis[0].data[z] = key;
                        $.each(value, function (j, item) {
                            configMap.baroption.series[j].data[z] = item;
                        });
                        z++;
                    });
                });
                jqueryMap.$piechart.setData(configMap.pieoption);
                jqueryMap.$barchart.setData(configMap.baroption);
            }
            else{
                jqueryMap.$piechart.noData();
                jqueryMap.$barchart.noData();
            }
        },null,configMap.urlOption,'json');
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        getEquipState();
    };
    return{initModule:initModule};
}());