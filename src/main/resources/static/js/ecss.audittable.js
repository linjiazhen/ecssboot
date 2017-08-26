/**
 * Created by jason on 2015/3/4.
 */
ecss.audittable=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">审计报表</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div class="col-lg-12" id="condition"></div>'
            +'<div class="col-lg-6" id="energytable"></div>'
            +'<div class="col-lg-6" id="itemstable"></div>',
            condition_body:'<div id="conditions"></div>',
            energy_body:'<div class="tablecontent" >'
            +'<table class="table table-hover table-bordered" id="DTitems">'
            +'<tr><th colspan="2">名称</th><th>分项能耗</th><th>占总能耗百分比</th></tr>'
            +'<tr><td rowspan="5">分项能耗</td><td>照明与插座用电</td><td id="0"></td><td id="p0"></td></tr>'
            +'<tr><td>空调用电</td><td id="1"></td><td id="p1"></td></tr>'
            +'<tr><td>动力用电</td><td id="2"></td><td id="p2"></td></tr>'
            +'<tr><td>特殊用电</td><td id="3"></td><td id="p3"></td></tr>'
            +'<tr><td>分项能耗合计</td><td id="4"></td><td id="p4"></td></tr>'
            +'<tr><td>未注明能耗</td><td>未注明分项能耗</td><td id="5"></td><td id="p5"></td></tr>'
            +'<tr><td>总能耗</td><td>总能耗</td><td id="6"></td><td id="p6"></td></tr>'
            +'</table>'
            + '<table class="table table-hover table-bordered" id="DTenergy">'
            +'</table>'
            +'</div>',
            items_body:'<div><div id="piechart" style="width:100%;height: 400px"></div></div>'
                    +'<div><div id="barchart" style="width:100%;height: 300px"></div></div>',
            conditionOption:[
                [{label:'label',text:'选择目标',size:[12,3,2,1]} ,{label:'input',type:'radio',name:'aim',options:[{value:'build',text:'建筑',checked:true},{value:'organ',text:'机构'}],size:[12,9,3,3],valid:{}},
                    {label:'select',type:'text',name:'group',size:[12,4,5,2],valid:{}},{label:'select',type:'text',name:'build',size:[12,4,5,2],valid:{}},{label:'select',type:'text',name:'first',size:[12,4,5,4],valid:{}}],
                [{label:'label',text:'时间区段',size:[12,3,2,1]}
                ,{label:'input',type:'daterange',name:'daterange',size:[12,9,4,4],valid:{}}]
            ],
            timeOptions:{
                autoclose:true,
                language: 'zh-CN',
                format: 'yyyy/mm',
                weekStart:1,
                endDate:'0d',
                clearBtn:true,
                minViewMode:1,
                isRange:true
            },
            baroption : {
                tooltip : {
                    trigger: 'axis'
                },
                toolbox: {
                    show : false
                },
                legend: {
                    data:['用电量','用水量']
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
                        name : '用电量',
                        splitLine : {show : false},
                        axisLabel : {
                            formatter: '{value} 度'
                        }
                    },
                    {
                        type : 'value',
                        name : '用水量',
                        axisLabel : {
                            formatter: '{value} 吨'
                        }
                    }
                ],
                series : [
                    {
                        name:'用电量',
                        type:'bar',
                        data:[]
                    },
                    {
                        name:'用水量',
                        type:'line',
                        yAxisIndex: 1,
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
                    data:['照明与插座用电','空调用电','动力用电','特殊用电','未注明能耗']
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
                                        return params.name+':'+params.value+'％';
                                    }
                                }
                            }
                        },
                        data:[]
                    }
                ]
            },
            urlOption:{
                model:'build',
                modellevel:'school',
                modelid:'allofsumgroup',
                energytypeid:'01000',
                energytype:null,
                startdate:moment(moment().subtract(12,'month').format('YYYY/MM')).format('YYYY/MM/DD'),
                enddate:moment().format('YYYY/MM/DD'),
                basetime:'month',
                caltype:'total'
            }
        },
        stateMap={
            $container:null
        },
        modelMap={
            modelenergy:new ecss.tools.makeModel(ecss.model.energy.energy),
            modeltool:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        jqueryMap={},
        setJqueryMap,   initModule,  addOptions,  addEvent,setAim,setType,getEquipState,getEnergy,setTime;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portletcond    :new ecss.tools.makePortlet($container.find('#condition'),'查询条件','orange','',configMap.condition_body),
            $portletenergy    :new ecss.tools.makePortlet($container.find('#energytable'),'逐月能耗值','red','',configMap.energy_body),
            $portletitem   :new ecss.tools.makePortlet($container.find('#itemstable'),'分项能耗值','green','',configMap.items_body),
            $condform       :new ecss.tools.makeForm($container.find('#conditions'),configMap.conditionOption),
            $barchart       :new ecss.tools.makeChart('barchart',configMap.baroption),
            $piechart       :new ecss.tools.makeChart('piechart',configMap.pieoption),
            $daterange       :new ecss.tools.makeDate($container.find('#datepicker'),configMap.timeOptions),
            $window         :$(window)
        };
    };
    setAim=function () {
        if($(this).val()=='organ'){
            jqueryMap.$condform.getElementsByName('group').parent().hide();
            jqueryMap.$condform.getElementsByName('build').parent().hide();
            jqueryMap.$condform.getElementsByName('first').show();
            configMap.urlOption['model']='organ';
        }
        else{
            jqueryMap.$condform.getElementsByName('first').hide();
            jqueryMap.$condform.getElementsByName('group').parent().show();
            jqueryMap.$condform.getElementsByName('build').parent().show();
            configMap.urlOption['model']='build';
        }
        getEnergy();
    };
    setType=function(){
        configMap.urlOption['energytypeid']=$(this).val();
        getEnergy();
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
    setTime=function(){
        var starttime=jqueryMap.$condform.getElementsByName('start').val();
        var endtime=jqueryMap.$condform.getElementsByName('end').val();
        if(starttime!=null&& endtime!=null){
            configMap.urlOption.startdate=moment(starttime).format('YYYY/MM/DD');
            configMap.urlOption.enddate=moment(endtime).format('YYYY/MM/DD');
            getEnergy();
        }
    };
    addEvent=function(){
        jqueryMap.$condform.getElementsByName('build').change(function(){
            configMap.urlOption.modelid=$(this).val();
            getEnergy();
        });
        jqueryMap.$condform.getElementsByName('first').change(function(){
            configMap.urlOption.modelid=$(this).val();
            getEnergy();
        });
        jqueryMap.$condform.getElementsByName('first').hide();
        var $group=jqueryMap.$condform.getElementsByName('group');
        $group.html('<option value="allofsumgroup">全校</option>');
        addOptions('getgroup',null,$group, '<option value="allofsumgroup">全校</option>');
        var $build=jqueryMap.$condform.getElementsByName( 'build');
        $build.html('<option value="#">全区</option>');
        $group.change(function(){
            addOptions('getbuild',$group.val(),$build,'<option value="#">全区</option>');
            if($group.val()=='allofsumgroup') {
                configMap.urlOption.modelid = $(this).val();
                getEnergy();
            }
        });
        var $first=jqueryMap.$condform.getElementsByName('first');
        $first.html('<option value="allofsumgroup">全校</option>');
        addOptions('getorgan','j4_2',$first, '<option value="#">全校</option>');
        jqueryMap.$condform.getElementsByName('aim').change(setAim);
        jqueryMap.$condform.getElementsByName('type').change(setType);
        var startdate=moment().subtract(12,'months').format('YYYY/MM');
        var enddate=moment().format('YYYY/MM');
        jqueryMap.$condform.getElementsByName('start').val(startdate);
        jqueryMap.$condform.getElementsByName('end').val(enddate);
        jqueryMap.$condform.getElementsByName('start').change(setTime);
        jqueryMap.$condform.getElementsByName('end').change(setTime);
    };
    getEnergy=function(){
        modelMap.modelenergy.post('gettypeenergy',function(data){
            console.log(data);
            $.each(data,function(i,item){
                $('#'+i).html(item?item.toFixed(4):'--');
                $('#p'+i).html(item?(data[6]!=0?(item/data[6]*100).toFixed(2)+'％':'--'):'--');
            });
            configMap.pieoption.series[0].data=[{value:(data[0]/data[6]*100).toFixed(2),name:'照明与插座用电'},{value:(data[1]/data[6]*100).toFixed(2),name:'空调用电'},
                {value:(data[2]/data[6]*100).toFixed(2),name:'动力用电'},{value:(data[3]/data[6]*100).toFixed(2),name:'特殊用电'},{value:(data[5]/data[6]*100).toFixed(2),name:'未注明能耗'}];
            jqueryMap.$piechart.setData(configMap.pieoption);
        },null,configMap.urlOption,'json',false);
        var amdata;
        configMap.urlOption.energytypeid='01000';
        modelMap.modelenergy.post('getenergy',function(data){
            amdata=data;
        },null,configMap.urlOption,'json',false);
        configMap.urlOption.energytypeid='02000';
        modelMap.modelenergy.post('getenergy',function(data){
            console.log(data);
            $('#DTenergy').html('<tr><th>月份</th><th>月电耗</th><th>月水耗</th></tr>');
            if(data.energy.data.length>0||amdata.energy.data.length>0) {
                var time=data.energy.data.length>amdata.energy.data.length?data.categories:amdata.categories;
                $.each(time, function (i, item) {
                    $('#DTenergy').append('<tr><td>' + item + '</td><td>' + (amdata.energy.data[i]?amdata.energy.data[i]:'--') + '</td><td>' + (data.energy.data[i]?data.energy.data[i]:'--') + '</td></tr>');
                });
                configMap.baroption.xAxis[0].data = time;
                configMap.baroption.series[0].data = amdata.energy.data;
                configMap.baroption.series[1].data = data.energy.data;//.length?data.energy:(function(size){var a=[];for(var i=0;i<size;i++) a.push(0);return a;})(amdata.energy.length);
                console.log(configMap.baroption);
                jqueryMap.$barchart.setData(configMap.baroption);
            }
            else {
                $('#DTenergy').append('<tr><td colspan="3">无数据！</td></tr>');
                jqueryMap.$barchart.noData();
            }
        },null,configMap.urlOption,'json');
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        getEnergy();
    };
    return{initModule:initModule};
}());