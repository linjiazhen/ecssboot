
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
                +'<div class=" form-group hidden ">'
                    +'<div class="radio radio-success radio-inline">'
                    +'<label>'
                        +'<input type="radio" name="type" value="build" checked="">建筑'
                    +'</label>'
                    +'<label>'
                        +'<input type="radio" name="type" value="organ">部门'
                    +'</label>'
                +'</div>'
            +'</div>'
        + '</div>'
        + '<div class="col-lg-3 hidden" id="chart"></div>'
        + '<div class="col-lg-12" id="table"></div>',
        chart_body: '<div class="tile" ><div id="barchart" style="width:450px;height: 700px;margin-left: -50px"></div></div>',
        table_body: '<div class="tablecontent" >'
        //+ '<table class="table table-hover table-bordered" id="DTbuild"></table>'
        + '<table class="table table-hover table-bordered" id="DTorgan"></table>'
        + '</div>',
            organtableOption: {
                "ajax": { "url": "publicorgan.do", "type": "POST" },
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
            $portlettable    :new ecss.tools.makePortlet($container.find('#table'),'2015年度建筑分项电耗公示','green','',configMap.table_body),
            //$dtbuild       :new ecss.tools.makeTable($container.find('#DTbuild'),configMap.buildtableOption),
            $dtorgan       :new ecss.tools.makeTable($container.find('#DTorgan'),configMap.organtableOption),
            $window     :$(window)
        };
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        $.material.init();
    };
    return{initModule:initModule};
}());
