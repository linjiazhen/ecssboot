
ecss.organpublic=(function(){
    'use strict';
    var configMap= {
        main_html: String()
            + '<div class="col-lg-12">'
                + '<div class="page-title">'
                + '<h4 class="pull-right strong" id="datetime"></h4>'
                    + '<ol class="breadcrumb">'
                        + '<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        + '<li class="active">部门公示</li>'
                    + '</ol>'
                + '</div>'

            +'</div>'
        + '</div>'
        +'<div class="col-lg-12" id="condition"></div>'
        + '<div class="col-lg-12" id="table"></div>',
            condition:'<div id="requireform"></div>',
        chart_body: '<div class="tile" ><div id="barchart" style="width:450px;height: 700px;margin-left: -50px"></div></div>',
        table_body: '<div class="tablecontent" >'
        + '<table class="table table-hover table-bordered" id="DTorgan"></table>'
        + '<table class="table table-hover table-bordered" id="DTorganitem"></table>'
        + '</div>',
            formOption:[
                [{label:'label',text:'展示目标',size:[12,3,2,1]} ,{label:'input',type:'radio',name:'type',options:[{value:'organ',text:'机构总耗',checked:true},{value:'organitem',text:'机构分项'}],size:[12,9,6,6],valid:{}}],
                [{label:'label',text:'时间视图',size:[12,3,2,1]},{label:'input',type:'radio',name:'time',options:[{value:'year',text:'年'},{value:'month',text:'月',checked:true}],size:[12,9,3,3],valid:{}},{label:'label',text:'时间选择',size:[12,3,2,1]},{label:'input',type:'daterange',name:'daterange',size:[12,9,5,5],valid:{}}]
            ],
            organtableOption: {
                "columns": [
                    {title: "机构编码", "data": "organid", "visible":false},
                    {title: "机构名称", "data": "organ"},
                    {title: "总用电量", "data": "totalenergy","render":function ( data) {       return data.toFixed(2);     }},
                    {title: "单位面积用电量", "data": "areaenergy","render":function () {  return "--";     }},
                    {title: "人均用电量", "data": "peopleenergy","render":function ( ) {    return "--";     }},
                    {title: "总用水量", "data": "totalwater","render":function ( data) {        return data.toFixed(2);     }},
                    {title: "单位面积用水量", "data": "areawater","render":function ( ) {   return "--";     }},
                    {
                        title: "人均用水量", "data": "peoplewater", "render": function () {
                        return "--";
                    }
                    }
                ],
                "dom": 'rt',
                "paging": false,
                "order": [[0, 'asc']]
            },
            organitemtableOption: {
                "columns": [
                    {title: "机构编码", "data": "bulidid", "visible":false},
                    {title: "机构名称", "data": "bulid"},
                    {title: "总用电量", "data": "totalenergy","render":function ( data) {       return data.toFixed(2);     }},
                    {title: "照明插座用电", "data": "zmczenergy","render":function ( data) {    return data;     }},
                    {title: "空调用电", "data": "ktydenergy","render":function ( data) {        return data.toFixed(2);     }},
                    {title: "动力用电", "data": "dlydenergy","render":function ( data) {        return data.toFixed(2);     }},
                    {title: "特殊用电", "data": "tsydenergy","render":function ( data) {         return data.toFixed(2);     }}
                ],
                "dom": 'rt',
                "paging": false,
                "order": [[0, 'asc']]
            },
            urlOption:{
                type:'organ',
                timetype:'month'
            },
            timeOptions:{
                autoclose:true,
                language: 'zh-CN',
                format: 'yyyy/mm/dd',
                weekStart:1,
                endDate:'0d',
                clearBtn:true,
                minViewMode:0,
                isRange:true
            }
        },
        stateMap={
            $container:null
        },
        modelMap={//生成前端框体模型
            model:new ecss.tools.makeModel(ecss.model.public.public)
        },
        jqueryMap={},
        setJqueryMap,   initModule, getData,timeFunc,setDate;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet    :new ecss.tools.makePortlet($container.find('#condition'),'查询条件','orange','',configMap.condition),
            $form       :new ecss.tools.makeForm($container.find('#requireform'),configMap.formOption),
            $portletchart    :new ecss.tools.makePortlet($container.find('#chart'),'能耗排名','red','',configMap.chart_body),
            $portlettable    :new ecss.tools.makePortlet($container.find('#table'),'机构电耗公示','green','',configMap.table_body),
            $dtorgan       :new ecss.tools.makeTable($container.find('#DTorgan'),configMap.organtableOption),
            $dtorganitem   :new ecss.tools.makeTable($container.find('#DTorganitem'),configMap.organitemtableOption),
            $daterange       :new ecss.tools.makeDate($container.find('#datepicker'),{isRange:true}),
            $window     :$(window)
        };
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$container.find('[name=type]').change(function(){
            if($(this).val()=='organ'){
                jqueryMap.$dtorgan.show();
                jqueryMap.$dtorganitem.hide();
            }else{
                jqueryMap.$dtorgan.hide();
                jqueryMap.$dtorganitem.show();
            }
            configMap.urlOption.type=$(this).val();
            getData();
        });
        jqueryMap.$container.find('[name=time]').change(setDate);
        jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
        $.material.init();
        jqueryMap.$dtorganitem.hide();
        setDate();
    };
    setDate=function () {
        configMap.urlOption.timetype=jqueryMap.$container.find('[name=time]:checked').val();
        if(configMap.urlOption.timetype=='month'){
            configMap.timeOptions.minViewMode=1;
            configMap.timeOptions.format='yyyy/mm';
            var startdate=moment().subtract(1,'months').format('YYYY/MM');
            var enddate=moment().format('YYYY/MM');
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(enddate);
        }else{
            configMap.timeOptions.minViewMode=2;
            configMap.timeOptions.format='yyyy';
            configMap.timeOptions.endDate='yyyy';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().format('YYYY');
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(startdate);
        }
        $('.input-group-addon').hide();
        jqueryMap.$form.getElementsByName('end').hide();
        timeFunc();
    };
    timeFunc=function () {
        configMap.urlOption.startDate=jqueryMap.$form.getElementsByName('start').val();
        getData();
    };
    getData=function () {
        if(configMap.urlOption.type=='organ'){
            modelMap.model.post('organ',function (result) {
                console.log(result);
                jqueryMap.$dtorgan.setData(result.data,true);
            },null,configMap.urlOption,'json');
        }else {
            modelMap.model.post('organitem',function (result) {
                console.log(result);
                jqueryMap.$dtorganitem.setData(result.data,true);
            },null,configMap.urlOption,'json');
        }

    };
    return{initModule:initModule};
}());
