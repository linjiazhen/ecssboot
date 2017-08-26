/**
 * Created by jason on 2015/3/2.
 */
ecss.waterleak=(function(){
    'use strict';
    var initform=function(){
            var $netname=jqueryMap.$form.getElementsByName('netname');
            modelMap.modeltools.post('getnetnames',function(data){
                console.log(data);
                $.each(data,function(i,item){
                    $netname.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
                configMap.urlOption['netid']=$netname.val();
                getEnergy();
            },null,null,'json');
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
            +'</div>'
            +'<div class="col-lg-12" id="condition"></div>'
            +'<div class="col-lg-12" id="display"></div>',
            condition:'<div id="requireform"></div>',
            display:'<div  id="displaychart" class="chartstyle" style="height: 470px;"></div>',
            produce: '<div class="tablecontent0" >'
                +'<table class="table table-hover table-bordered" id="DTproduce"></table>'
            +'</div>',
            consume: '<div class="tablecontent0" >'
                +'<table class="table table-hover table-bordered" id="DTconsume"></table>'
            +'</div>',
            formDisplayOption:[
                [{label:'label',text:'时间单位',size:[12,3,2,1]},{label:'input',type:'radio',name:'timeunit',options:[{value:'year',text:'年'},{value:'month',text:'月'},{value:'day',text:'日'},{value:'hour',text:'小时',checked:true},{value:'minutes',text:'15分钟'}],size:6,valid:{}}
                ,{label:'label',text:'时间选择',size:[12,3,2,1]},{label:'input',type:'daterange',name:'time',size:[12,9,4,4],valid:{notEmpty: {}}}],[{label:'label',text:'水网名称',size:[12,3,2,1]},{label:'select',name:'netname',size:[12,9,4,4],valid:{}}]
            ],
            chartOption:{
                tooltip : {
                    trigger: 'axis'
                },
                calculable : true,
                legend: {
                    data:['进水量','出水量','环网漏水量','末端漏水量','未统计出水量']
                },
                xAxis : [
                    {
                        type : 'category',
                        axisLabel:{
                            rotate:90
                        },
                        splitLine : {show : false},
                        data : []
                    }
                ],
                yAxis : [
                    {
                        type : 'value',
                        name : '水耗展示',
                        splitLine : {show : false},
                        axisLabel : {
                            formatter: '{value} 吨'
                        }
                    }
                ],
                series : [
                    {
                        name:'进水量',
                        type:'bar',
                        data:[]
                    },
                    {
                        name:'出水量',
                        type:'bar',
                        data:[]
                    },
                    {
                        name:'环网漏水量',
                        type:'line',
                        data:[]
                    },
                    {
                        name:'末端漏水量',
                        type:'line',
                        data:[]
                    },
                    {
                        name:'未统计出水量',
                        type:'line',
                        data:[]
                    }
                ]
            },
            timeOptions:{
            },
            urlOption:{},
            tableEquipOption:{
                "columns": [
                    { title:"测量点号","data": "address" },
                    { title:"能耗值","data": "value" },
                    { title:"测量位置","data": "remarkinfo" }
                ],
                "pagingType":'simple',
                "pageLength": 5,
                "dom": 'prt',
                "order": [[ 1, 'desc' ]]
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
        setJqueryMap,   initModule,  getleak,initDetail,setTime,timeFunc,getEnergy,addEvent,showDetail,getpro,getcon;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet1    :new ecss.tools.makePortlet($container.find('#condition'),'展示条件','orange','',configMap.condition),
            $portlet2   :new ecss.tools.makePortlet($container.find('#display'),'水产差展示','green','',configMap.display),
            $form       :new ecss.tools.makeForm($container.find('#requireform'),configMap.formDisplayOption),
            $energychart:new ecss.tools.makeChart('displaychart'),
            $modal     :new ecss.tools.makeModal($container,'用水量详情','modal-lg'),
            $dtdetail    :new ecss.tools.makeTable($container.find('.modal-body').last().html('<div id="list"><div id="produce"></div><div id="consume"></div></div>').find('#DTerror'),configMap.tableErrorOption),
            $portlet3    :new ecss.tools.makePortlet($container.find('#produce'),'生产水表消耗','green','表数：<strong id="producenum"></strong>',configMap.produce),
            $portlet4    :new ecss.tools.makePortlet($container.find('#consume'),'消费水表消耗','green','表数：<strong id="consumenum"></strong>',configMap.consume),
            $dtproduce    :new ecss.tools.makeTable($container.find('#DTproduce'),configMap.tableEquipOption),
            $dtconsume    :new ecss.tools.makeTable($container.find('#DTconsume'),configMap.tableEquipOption),
            $confirm    :new ecss.tools.makeModal($container),
            $conditon   :$container.find('#condition'),
            $display    :$container.find('#display'),
            $daterange       :new ecss.tools.makeDate($container.find('#datepicker'),{isRange:true}),
            $window     :$(window)
        };
    };

    addEvent=function(){
        jqueryMap.$form.getElementsByName('netname').change(getleak);
    };

    timeFunc=function(){
        var start=jqueryMap.$form.getElementsByName('start').val();
        var end=jqueryMap.$form.getElementsByName('end').val();
        if(start!=''&&end!='') {
            if(start>end){
                jqueryMap.$form.getElementsByName('start').val(end);
                start=end;
            }
            configMap.urlOption.startdate = moment(start).format('YYYY/MM/DD');
            if (configMap.timeOptions.minViewMode == 1)
                configMap.urlOption.enddate = moment(end).add(1, 'months').format('YYYY/MM/DD');
            else if (configMap.timeOptions.minViewMode == 2)
                configMap.urlOption.enddate = moment(end).add(1, 'years').format('YYYY/MM/DD');
            else
            if(configMap.urlOption.basetime=='day')
                configMap.urlOption.enddate = moment(end).add(1, 'days').format('YYYY/MM/DD');
            else{
                configMap.urlOption.startdate =start;
                configMap.urlOption.enddate = moment(start).add(1, 'days').format('YYYY/MM/DD');
            }
            getEnergy();
        }
    };

    setTime=function(){
        var basetime=$(this).val();
        configMap.urlOption['basetime']=basetime;
        if(basetime=='minutes'||basetime=='hour'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().format('YYYY/MM/DD');
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(startdate);
            $('.input-group-addon').hide();
            jqueryMap.$form.getElementsByName('end').hide();
        }else
        if(basetime=='day'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().format('YYYY/MM/DD');
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(startdate);
            $('.input-group-addon').show();
            jqueryMap.$form.getElementsByName('end').show();
        }else
        if(basetime=='month'){
            configMap.timeOptions.minViewMode=1;
            configMap.timeOptions.format='yyyy/mm';
            var startdate=moment().format('YYYY/MM');
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(startdate);
            $('.input-group-addon').show();
            jqueryMap.$form.getElementsByName('end').show();
        }else{
            configMap.timeOptions.minViewMode=2;
            configMap.timeOptions.format='yyyy';
            configMap.timeOptions.endDate='yyyy';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().format('YYYY');
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(startdate);
            $('.input-group-addon').show();
            jqueryMap.$form.getElementsByName('end').show();
        }
        timeFunc();
    };
    getpro=function(time){
        modelMap.modelwaternet.post('getprorank',function(data){
            jqueryMap.$container.find('#producenum').html(data.length);
            jqueryMap.$dtproduce.setData(data,true);
        }, function () {
            ZENG.msgbox.show('获取生产水表信息失败！',5,3000);
        },configMap.urlOption.basetime+','+configMap.urlOption.netid+','+time,'json');
    };
    getcon=function(time){
        modelMap.modelwaternet.post('getconrank',function(data){
            jqueryMap.$container.find('#consumenum').html(data.length);
            jqueryMap.$dtconsume.setData(data,true);
        }, function () {
            ZENG.msgbox.show('获取消费水表信息失败！',5,3000);
        },configMap.urlOption.basetime+','+configMap.urlOption.netid+','+time,'json');
    };
    showDetail=function(data){
        console.log(data);
        jqueryMap.$modal.show();
        getpro(data.name);
        getcon(data.name);
    };
    initDetail=function(){
        jqueryMap.$energychart.noData();
        jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
        var startdate=moment().format('YYYY/MM/DD');
        jqueryMap.$form.getElementsByName('start').val(startdate);
        jqueryMap.$form.getElementsByName('end').val(startdate);
        $('.input-group-addon').hide();
        jqueryMap.$form.getElementsByName('end').hide();
        jqueryMap.$form.getElementsByName('timeunit').change(setTime);
        configMap.urlOption={
            startdate:moment().format('YYYY/MM/DD'),
            enddate:moment().add(1, 'days').format('YYYY/MM/DD'),
            basetime:'hour'
        };
        jqueryMap.$energychart.setDblClickEvent(showDetail);
    };
    getleak=function(){
        configMap.urlOption['netid']=$(this).val();
        getEnergy();
    };
    getEnergy=function(){
        modelMap.modelwaternet.post('getleak',function(data){
            console.log(data);
            configMap.chartOption.series[0].data=data.totalin;
            configMap.chartOption.series[1].data=data.totalout;
            var inleak=[];
            $.each(data.datatime,function(i,item){
                if(configMap.urlOption.basetime=='minutes')
                    inleak.push((data.leak[0]-data.leak[1]).toFixed(4));
                else
                    if(configMap.urlOption.basetime=='hour')
                        inleak.push(((data.leak[0]-data.leak[1])*4).toFixed(4));
            });
            var outleak=[];
            $.each(data.datatime,function(i,item){
                if(configMap.urlOption.basetime=='minutes')
                    outleak.push(data.leak[1]);
                else
                if(configMap.urlOption.basetime=='hour')
                    outleak.push((data.leak[1]*4).toFixed());
            });
            var nocal=[];
            $.each(data.datatime,function(i,item){
                if(configMap.urlOption.basetime=='minutes') {
                    var value = data.totalin[i] - data.totalout[i] - data.leak[0];
                    nocal.push(value > 0 ? value.toFixed(4) : 0);
                }
                else
                if(configMap.urlOption.basetime=='hour'){
                    var value = data.totalin[i] - data.totalout[i] - data.leak[0]*4;
                    nocal.push(value > 0 ? value.toFixed(4) : 0);
                }
            });
            configMap.chartOption.series[2].data=inleak;
            configMap.chartOption.series[3].data=outleak;
            configMap.chartOption.series[4].data=nocal;
            configMap.chartOption.xAxis[0].data=data.datatime;
            jqueryMap.$energychart.setData(configMap.chartOption);
        },function(){},configMap.urlOption,'json');
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        initDetail();
        initform();
    };
    return{initModule:initModule};
}());