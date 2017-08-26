/**
 * Created by jason on 2015/3/4.
 */
ecss.buildpublic=(function(){
    'use strict';
    var configMap= {
        main_html: String()
        + '<div class="col-lg-12">'
            + '<div class="page-title">'
            + '<h4 class="pull-right strong" id="datetime"></h4>'
                + '<ol class="breadcrumb">'
                    + '<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                    + '<li class="active">建筑公示</li>'
                + '</ol>'
            + '</div>'
            +'<div class=" form-group ">'
                +'<div class="radio radio-success radio-inline">'
                    +'<label>'
                        +'<input type="radio" name="type" value="build" checked="">建筑总耗'
                    +'</label>'
                    +'<label>'
                        +'<input type="radio" name="type" value="builditem">建筑分项'
                    +'</label>'
                +'</div>'
            +'</div>'
        + '</div>'
        + '<div class="col-lg-12" id="table"></div>',
        chart_body: '<div class="tile" ><div id="barchart" style="width:450px;height: 700px;margin-left: -50px"></div></div>',
        table_body: '<div class="tablecontent" >'
        + '<table class="table table-hover table-bordered" id="DTbuild"></table>'
        + '<table class="table table-hover table-bordered" id="DTbuilditem"></table>'
        + '</div>',
        buildtableOption: {
            "ajax": { "url": "public.do", "type": "POST" },
            "columns": [
                {title: "建筑编码", "data": "buildcode", "visible":false},
                {title: "建筑名称", "data": "buildname"},
                {title: "总用电量", "data": "totalenergy","render":function ( data) {   return data.toFixed(2); }},
                {title: "单位面积用电量", "data": "areaenergy","render":function ( data) {     return data.toFixed(2);     }},
                {title: "人均用电量", "data": "peopleenergy","render":function ( data, type, full) {
                    if(full.func=='A'||full.func=='I' ||full.func==''){    return data.toFixed(2); }
                    else return "--";
                }},
                {title: "总用水量", "data": "totalwater","render":function ( data) {           return data.toFixed(2);     }},
                {title: "单位面积用水量", "data": "areawater","render":function ( data) {      return data.toFixed(2);     }},
                {title: "人均用水量", "data": "peoplewater","render":function ( data, type, full) {
                    if(full.func=='A'||full.func=='I'||full.func==''){    return data.toFixed(2); }
                    else return "--";
                }}
            ],
            "dom": 'rt',
            "paging": false,
            "order": [[0, 'asc']]
        },
        builditemtableOption: {
            "ajax": { "url": "publicbulids.do", "type": "POST" },
            "columns": [
                {title: "建筑编码", "data": "bulidid", "visible":false},
                {title: "建筑名称", "data": "bulid"},
                {title: "总用电量", "data": "totalenergy","render":function ( data) {       return data.toFixed(2);     }},
                {title: "照明插座用电", "data": "zmczenergy","render":function ( data) {    return data;     }},
                {title: "空调用电", "data": "ktydenergy","render":function ( data) {        return data.toFixed(2);     }},
                {title: "动力用电", "data": "dlydenergy","render":function ( data) {        return data.toFixed(2);     }},
                {title: "特殊用电", "data": "tsydenergy","render":function ( data) {         return data.toFixed(2);     }}
            ],
            "dom": 'rt',
            "paging": false,
            "order": [[0, 'asc']]
        }
        
        },
        stateMap={
            $container:null
        },
        jqueryMap={},
        setJqueryMap,   initModule, getData;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portletchart    :new ecss.tools.makePortlet($container.find('#chart'),'能耗排名','red','',configMap.chart_body),
            $portlettable    :new ecss.tools.makePortlet($container.find('#table'),'2015年度能耗公示','green','',configMap.table_body),
            $dtbuild       :new ecss.tools.makeTable($container.find('#DTbuild'),configMap.buildtableOption),
            $dtbuilditem       :new ecss.tools.makeTable($container.find('#DTbuilditem'),configMap.builditemtableOption),
            $window     :$(window)
        };
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$container.find('[name=type]').change(function(){
            if($(this).val()=='build'){
                jqueryMap.$dtbuild.show();
                jqueryMap.$dtbuilditem.hide();
            }else{
                jqueryMap.$dtbuild.hide();
                jqueryMap.$dtbuilditem.show();
            }
        });
        $.material.init();
        jqueryMap.$dtbuilditem.hide();
    };
    return{initModule:initModule};
}());