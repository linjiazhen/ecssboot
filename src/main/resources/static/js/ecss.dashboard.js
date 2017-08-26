/**
 * Created by jason on 2015/1/30.
 */
ecss.dashboard=(function(){
    'use strict';
    var labelBottom = {
        normal : {
            color: '#ccc',
            label : {
                show : false
            },
            labelLine : {
                show : false
            }
        },
        emphasis: {
            color: 'rgba(0,0,0,0)'
        }
        },
        labelFromatter = {
            normal : {
                label : {
                    formatter : function (params){
                        return params.value + '%'
                    },
                    position : 'center'
                },
                labelLine : {
                    show : false
                }
            }
        },
        configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li class="active"><i class="fa fa-dashboard"></i> 后台主页</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div id="circles">'
                +'<div class="col-lg-2 col-sm-6" id="circletile1"></div>'
                +'<div class="col-lg-2 col-sm-6" id="circletile2"></div>'
                +'<div class="col-lg-2 col-sm-6" id="circletile3"></div>'
                +'<div class="col-lg-2 col-sm-6" id="circletile4"></div>'
                +'<div class="col-lg-2 col-sm-6" id="circletile5"></div>'
                +'<div class="col-lg-2 col-sm-6" id="circletile6"></div>'
            +'</div>'
            +'<div class="col-lg-3" id="barcharts"></div>'
            +'<div class="col-lg-9">' +
                '<div  id="linecharts"></div>'
                +'<div class="col-lg-3 tile yellow" style="height: 235px"><h4>照明插座用电</h4><div id="chart1" style="width: 100%;height: 100%"></div></div>'
                +'<div class="col-lg-3 tile blue" style="height: 235px"><h4>空调用电</h4><div id="chart2" style="width: 100%;height: 100%"></div></div>'
                +'<div class="col-lg-3 tile red" style="height: 235px"><h4>动力用电</h4><div id="chart3" style="width: 100%;height: 100%"></div></div>'
                +'<div class="col-lg-3 tile purple" style="height: 235px"><h4>特殊用电</h4><div id="chart4" style="width: 100%;height: 100%"></div></div>'
            +'</div>',
            linechartbody:'<div id="linechart" style="width: 100%;height: 320px"></div>',
            barchartbody:'<div id="barchart" style="width: 400px;height: 570px;margin-left: -25px"></div>',
            baroption : {
                tooltip : {
                    trigger: 'axis'
                },
                grid:{
                    borderWidth:0
                },
                xAxis : [
                    {
                        show:false,
                        type : 'value'
                    }
                ],
                yAxis : [
                    {
                        type : 'category',
                        splitLine : {show : false},
                        data : []
                    }
                ],
                series : [
                    {
                        name:'能耗值',
                        type:'bar',
                        itemStyle : { normal: {label : {show: true, position: 'Right'}}},
                        data:[]
                    }
                ]
            },
            lineoption : {
                tooltip : {
                    trigger: 'axis'
                },
                calculable : true,
                legend: {
                    data:['用电','用水']
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
                        type : 'value',
                        name : '耗电量',
                        splitLine : {show : false},
                        axisLabel : {
                            formatter: '{value} 度'
                        }
                    },
                    {
                        type : 'value',
                        name : '耗水量',
                        axisLabel : {
                            formatter: '{value} 吨'
                        }
                    }
                ],
                series : [
                    {
                        name:'用电',
                        type:'bar',
                        data:[]
                    },
                    {
                        name:'用水',
                        type:'line',
                        yAxisIndex: 1,
                        data:[]
                    }
                ]
            },
            option : {
                series : [
                    {
                        type:'pie',
                        radius : ['50%', '70%'],
                        data:[
                            {value:0,itemStyle : labelFromatter},
                            {value:0,itemStyle : labelBottom}
                        ]
                    }
                ]
            }
        },
        stateMap={
            $container: null
        },
        modelMap={
            modelenergy:new ecss.tools.makeModel(ecss.model.energy.energy),
            model:new ecss.tools.makeModel(ecss.model.home.loginout)
        },
        jqueryMap={},
        setJqueryMap,   initModule,getNumbers,getBulidTypeEnergy,getEnergy,getTypeEnergy;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $cirtile1    :new ecss.tools.makeCircleTile($container.find('#circletile1'),'build','building','建筑',0,'dark-blue'),
            $cirtile2    :new ecss.tools.makeCircleTile($container.find('#circletile2'),'organ','sitemap','机构',0,'purple'),
            $cirtile3    :new ecss.tools.makeCircleTile($container.find('#circletile3'),'gateway','retweet','网关',0,'green'),
            $cirtile4    :new ecss.tools.makeCircleTile($container.find('#circletile4'),'ammeter','bolt','电表',0,'orange'),
            $cirtile5    :new ecss.tools.makeCircleTile($container.find('#circletile5'),'watermeter','tint','水表',0,'blue'),
            $cirtile6    :new ecss.tools.makeCircleTile($container.find('#circletile6'),'alert','exclamation-triangle','违规',0,'red'),
            $portlet     :new ecss.tools.makePortlet($container.find('#barcharts'),'建筑类别能耗','purple','',configMap.barchartbody),
            $portlet1     :new ecss.tools.makePortlet($container.find('#linecharts'),'全校能耗','green','',configMap.linechartbody),
            $barchart    :new ecss.tools.makeChart('barchart',configMap.baroption),
            $linechart   :new ecss.tools.makeChart('linechart',configMap.lineoption),
            $chart1    :new ecss.tools.makeChart('chart1',configMap.option),
            $chart2    :new ecss.tools.makeChart('chart2',configMap.option),
            $chart3    :new ecss.tools.makeChart('chart3',configMap.option),
            $chart4    :new ecss.tools.makeChart('chart4',configMap.option),
            $panel      :$container.find('.panels'),
            $datetime   :$container.find('#datetime'),
            $window     :$(window)
        };
    };
    getNumbers=function(){
        modelMap.model.post('recordsnum',function(data){
            jqueryMap.$cirtile1.setNumber(data.build);
            jqueryMap.$cirtile2.setNumber(data.organ);
            jqueryMap.$cirtile3.setNumber(data.gateway);
            jqueryMap.$cirtile4.setNumber(data.ammeter);
            jqueryMap.$cirtile5.setNumber(data.watermeter);
            jqueryMap.$cirtile6.setNumber(data.warning);
        },null,null,'json');
    };
    getBulidTypeEnergy=function(){
        modelMap.modelenergy.post('getfuncenergy',function(data){
            console.log(data);
            if(data.energy!=null&&data.energy.length>0){
                configMap.baroption.yAxis[0].data=data.datatime;
                configMap.baroption.series[0].data=data.energy;
                jqueryMap.$barchart.setData(configMap.baroption);
            }else{
                jqueryMap.$linechart.clear();
                if(configMap.lineoption.series.length==0)
                    jqueryMap.$linechart.noData();
            }
        },null,{basetime: "month",caltype: "total",enddate: moment().format('YYYY/MM/DD'),energytype: "电",energytypeid: "01000",model: "build",modelid: "allofsumgroup",modellevel: "group",startdate: (moment().format('YYYY')+'/01/01')},'json');
    };
    getEnergy=function(){
        modelMap.modelenergy.post('getenergy',function(data){
            console.log(data);
            if(data.energy.data!=null&&data.energy.data.length>0){
                //configMap.lineoption.xAxis[0].data=data.datatime;
                //configMap.lineoption.series[0].data=data.energy;
                configMap.lineoption.xAxis[0].data=data.categories;
                configMap.lineoption.series[0].data=data.energy.data;
                jqueryMap.$linechart.setData(configMap.chartOption);
            }else{
                jqueryMap.$linechart.clear();
                if(configMap.lineoption.series.length==0)
                    jqueryMap.$linechart.noData();
            }
        },null,{basetime: "month",caltype: "total",enddate: moment().format('YYYY/MM/DD'),energytype: "电",energytypeid: "01000",model: "build",modelid: "allofsumgroup",modellevel: "group",startdate: (moment().format('YYYY')+'/01/01')},'json');
        modelMap.modelenergy.post('getwaterenergy',function(data){
            console.log(data);
            if(data.energy.data!=null&&data.energy.data.length>0){
                configMap.lineoption.series[1].data=data.energy.data;
                jqueryMap.$linechart.setData(configMap.chartOption);
            }else{
                jqueryMap.$linechart.clear();
                if(configMap.lineoption.series.length==0)
                    jqueryMap.$linechart.noData();
            }
        },null,{basetime: "month",caltype: "total",enddate: moment().format('YYYY/MM/DD'),energytype: "水",energytypeid: "02000",model: "build",modelid: "allofsumgroup",modellevel: "group",startdate:(moment().format('YYYY')+'/01/01')},'json');
    };
    getTypeEnergy=function(){
        modelMap.modelenergy.post('gettypeenergy',function(data){
            console.log(data);
            $.each(data,function(i,item){
                if(i<4) {
                    configMap.option.series[0].data[0].value = (item/data[4]*100.0).toFixed(2);
                    configMap.option.series[0].data[1].value = (100.0 - item/data[4]*100.0).toFixed(2);
                    jqueryMap['$chart' + (i + 1)].setData(configMap.option);
                }
            });
        },null,{basetime: "month",caltype: "total",enddate: moment().format('YYYY/MM/DD'),energytype: "电",energytypeid: "01000",model: "build",modelid: "allofsumgroup",modellevel: "group",startdate:(moment().format('YYYY')+'/01/01')},'json');
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        jqueryMap.$panel.click(function(){
            $.gevent.publish('loadpage',$(this).attr('id'));
        });
        //if(ecss.menu.menus[2].code!='20')
        //    $('#circles').hide();
        moment.locale('zh-cn');
        setInterval(function () {
            jqueryMap.$datetime.html(moment().format('YYYY年 MMM Do h:mm:ss a'));
        }, 1000);
        getNumbers();
        getBulidTypeEnergy();
        getEnergy();
        getTypeEnergy();
    };
    return{initModule:initModule};
}());