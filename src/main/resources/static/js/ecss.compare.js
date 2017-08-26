/**
 * Created by jason on 2015/3/5.
 */
ecss.compare=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
                +'<div class="page-title">'
                    +'<h4 class="pull-right strong" id="datetime"></h4>'
                    +'<ol class="breadcrumb">'
                        +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                        +'<li class="active">能耗对比</li>'
                    +'</ol>'
                +'</div>'
            +'</div>'
            +'<div class="col-lg-2" id="energyitem"></div>'
            +'<div class="col-lg-10" id="condition"></div>'
            +'<div class="col-lg-10" id="display"></div>',
            energyitem:'<div id="energyitemtree"></div>',
            condition:'<div id="requireform"></div>',
            display:'<div  id="chart" style="height:400px"></div>',
            TreeOption : {
                core:{
                    check_callback:true,
                    data:{
                        url:'getallelectricitems.do'
                    }
                //    dblclick_toggle:false
                },
                checkbox:{
                    three_state:false,
                    tie_selection:false
                },
                plugins:['checkbox','search']
            },
            formOption:[//QAQ
                [{label:'label',text:'展示目标',size:[12,3,2,1]}
                    ,{label:'input',type:'radio',name:'aim',options:[{value:'build',text:'建筑',checked:true},{value:'organ',text:'机构'}],size:[9,9,5,5],valid:{}}],
                    //{label:'label',text:'展示内容',size:[12,3,2,1]},
                    //{label:'input',type:'radio',name:'caltype',options:[{value:'total',text:'总能耗',checked:true},{value:'people',text:'人均能耗'},{value:'area',text:'平均面积'}],size:[12,9,5,5],valid:{}}],
                [{label:'label',text:'时间单位',size:[12,3,2,1]}
                    ,{label:'input',type:'radio',name:'time',options:[{value:'year',text:'年'},{value:'month',text:'月'},{value:'day',text:'日',checked:true}],size:[12,9,5,5],valid:{}},
                    {label:'label',text:'时间选择',size:[12,3,2,1]}
                    ,{label:'input',type:'date',name:'date',size:[12,8,5,5],valid:{}}],
                [{label:'label',text:'对比级别',size:[12,3,2,1]}//QAQ:新版
                    ,{label:'input',type:'radio',name:'level',options:[{value:'group',text:'区',checked:true},{value:'build',text:'楼'},{value:'floor',text:'层'},{value:'room',text:'间'}],size:[12,9,5,5],valid:{}}],
                [{label:'label',text:'对比目标',size:[12,3,2,1]},{label:'select',type:'text',name:'group',size:[12,4,2,2],valid:{}}
                    ,{label:'select',type:'text',name:'build',size:[12,4,2,2],valid:{}},{label:'select',type:'text',name:'buildcmp',size:[12,9,6,6],valid:{}}],
                [{label:'label',text:'部门信息',size:[12,3,2,1]}//QAQ新版
                    ,{label:'select',type:'text',name:'first',size:[12,3],valid:{}}
                    ,{label:'select',type:'text',name:'second',size:[12,3],valid:{}}
                    ,{label:'select',type:'text',name:'third',size:[12,3],valid:{}}]
            ],
            timeOptions:{
                autoclose:true,
                language: 'zh-CN',
                format: 'yyyy/mm/dd',
                weekStart:1,
                endDate:'0d',
                clearBtn:true,
                minViewMode:0
            },
            urlOption:{
            },
            selectOption:{
                language:'zh-CN',
                multiple: true,
                closeOnSelect:false,
                placeholder:'点击选择对比区域',
                ajax:{
                    url:'getgroupnames.do',
                    type:'post',
                    processResults:function(data){
                        return{
                            results: $.map(data, function (item) {
                                return {
                                    text: item.name,
                                    id: item.id
                                }
                            })
                        }
                    }
                }
            },
            chartOption : {
                tooltip : {
                    trigger: 'axis'
                },
                legend: {
                    data:[]
                },
                xAxis : [
                    {
                        type:'category',
                        splitLine : {show : false},
                        data: []
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
                    }
                ],
                series : []
            }
        },
        modelMap={
            modelenergy:new ecss.tools.makeModel(ecss.model.energy.energy),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option),
            datas:{}
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap, initModule,initParameter, addEvent,setModel,setModelB,addOptions,getEnergy,setTime,timeFunc,setAim,setCalType,setType,setLevel,showDetail;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet1    :new ecss.tools.makePortlet($container.find('#energyitem'),'能耗分项','blue','',configMap.energyitem),
            $portlet2    :new ecss.tools.makePortlet($container.find('#condition'),'对比条件','orange','',configMap.condition),
            $portlet3    :new ecss.tools.makePortlet($container.find('#display'),'对比展示','green','',configMap.display),
            $energyitemtree  :new ecss.tools.makeTree($container.find('#energyitemtree'),configMap.TreeOption,650),
            $form       :new ecss.tools.makeForm($container.find('#requireform'),configMap.formOption),
            $chart      :new ecss.tools.makeChart('chart',configMap.chartOption),
            $confirm    :new ecss.tools.makeModal($container),
            $date       :new ecss.tools.makeDate($container.find('[name=date]')),
            $window     :$(window),
            panelnum    :0
        };
        jqueryMap.$container.find('.energychart').hide();
        jqueryMap.$container.find('#chart').show();
    };
    initParameter=function(){
        configMap.urlOption={
            model:'build',
            modellevel:'group',
            modelid:'allofsumgroup',
            energytypeid:null,
            energytype:null,
            startdate:moment().format('YYYY/MM/DD'),
            enddate:moment().add(1,'days').format('YYYY/MM/DD'),
            basetime:'day',
            caltype:'total'
        };
        jqueryMap.$form.getElementsByName('date').val(configMap.urlOption.startdate);
    };
    setAim=function () {
        if($(this).val()=='organ'){
            jqueryMap.$form.getElementsByName('group').parent().hide();
            jqueryMap.$form.getElementsByName('build').parent().hide();
            jqueryMap.$form.getElementsByName('level').closest('.form-group').hide();
            configMap.selectOption.ajax.data='j4_2';
            configMap.selectOption.placeholder='请点击选择对比机构';
            configMap.selectOption.ajax.url='getorganbypid.do';
            jqueryMap.$form.getElementsByName('buildcmp').select2(configMap.selectOption);
            jqueryMap.$form.getElementsByName('buildcmp').select2('val','');
        }
        else{
            jqueryMap.$form.getElementsByName('level').closest('.form-group').show();
            jqueryMap.$form.getElementsByName('group').parent().show();
            jqueryMap.$form.getElementsByName('build').parent().show();
        }
        configMap.urlOption['model']=$(this).val();
        configMap.urlOption.modelid=[];
        getEnergy();
    };
    timeFunc=function(){
        var date=jqueryMap.$form.getElementsByName('date').val();
        configMap.urlOption.startdate = moment(date).format('YYYY/MM/DD');
        if (configMap.timeOptions.minViewMode == 1)
            configMap.urlOption.enddate = moment(date).add(1, 'months').format('YYYY/MM/DD');
        else if (configMap.timeOptions.minViewMode == 2)
            configMap.urlOption.enddate = moment(date).add(1, 'years').format('YYYY/MM/DD');
        else
            configMap.urlOption.enddate = moment(date).add(1, 'days').format('YYYY/MM/DD');
        getEnergy();
    };
    setTime=function(){
        var basetime=$(this).val();
        configMap.urlOption['basetime']=basetime;
        if(basetime=='minutes'||basetime=='hour'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$date.update(configMap.timeOptions,timeFunc);
            var date=moment().format('YYYY/MM/DD');
            jqueryMap.$form.getElementsByName('date').val(date);
        }else
        if(basetime=='day'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$date.update(configMap.timeOptions,timeFunc);
            var date=moment().format('YYYY/MM/DD');
            jqueryMap.$form.getElementsByName('date').val(date);
        }else
        if(basetime=='month'){
            configMap.timeOptions.minViewMode=1;
            configMap.timeOptions.format='yyyy/mm';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$date.update(configMap.timeOptions,timeFunc);
            var date=moment().format('YYYY/MM');
            jqueryMap.$form.getElementsByName('date').val(date);
        }else{
            configMap.timeOptions.minViewMode=2;
            configMap.timeOptions.format='yyyy';
            configMap.timeOptions.endDate='yyyy';
            jqueryMap.$date.update(configMap.timeOptions,timeFunc);
            var date=moment().format('YYYY');
            jqueryMap.$form.getElementsByName('date').val(date);
        }
        timeFunc();
    };
    setCalType=function(){//计算类型
        configMap.urlOption['caltype']=$(this).val();
        getEnergy();
    };
    setType=function(){
        configMap.urlOption['showtype']=$(this).val();
        jqueryMap.$container.find('.chartstyle').hide();
        jqueryMap.$container.find('#'+$(this).val()).show();
    };
    setLevel=function(){
        configMap.urlOption.modellevel=$(this).val();
        if($(this).val()=='group'){
            jqueryMap.$form.getElementsByName('group').parent().hide();
            jqueryMap.$form.getElementsByName('build').parent().hide();
            delete configMap.selectOption.ajax.data;
            configMap.selectOption.placeholder='请点击选择对比区域';
            configMap.selectOption.ajax.url='getgroupnames.do';
            jqueryMap.$form.getElementsByName('buildcmp').select2(configMap.selectOption);
        }else
        if($(this).val()=='build'){
            jqueryMap.$form.getElementsByName('group').parent().hide();
            jqueryMap.$form.getElementsByName('build').parent().hide();
            configMap.selectOption.placeholder='请点击选择对比楼栋';
            configMap.selectOption.ajax.url='getbuildnames.do';
            configMap.selectOption.ajax.data="all";
            jqueryMap.$form.getElementsByName('buildcmp').select2(configMap.selectOption);
        }else{
            configMap.selectOption.placeholder='请点击选择对比层间';
            configMap.selectOption.ajax.url='';
            delete configMap.selectOption.ajax.data;
            jqueryMap.$form.getElementsByName('buildcmp').select2(configMap.selectOption);
            jqueryMap.$form.getElementsByName('group').parent().show();
            jqueryMap.$form.getElementsByName('build').parent().show();
            var $group=jqueryMap.$form.getElementsByName('group');
            $group.html('<option>请选择区域</option>');
            addOptions('getgroup',null,$group,'<option>请选择区域</option>');
            var $build=jqueryMap.$form.getElementsByName('build');
            $build.html('<option>请选择楼栋</option>');
        }
        jqueryMap.$form.getElementsByName('buildcmp').select2('val','');
    };
    getEnergy=function(){
        var items=jqueryMap.$energyitemtree.get_checked();
        var itemsname=jqueryMap.$energyitemtree.get_checked().map(function(item){return item.text;});
        var buildids=jqueryMap.$form.getElementsByName('buildcmp').select2('val');
        var buildnames=jqueryMap.$form.getElementsByName('buildcmp').select2('data').map(function(item){return item.text});
        configMap.chartOption.legend.data=itemsname;
        configMap.chartOption.xAxis[0].data=buildnames;
        configMap.chartOption.series=[];
        console.log(items);
        console.log(buildids);
        if(items.length>0&&buildids!=null) {
            $.each(items, function (i, item) {
                var energy = {
                    data: [],
                    name: item.text,
                    type: 'bar'
                };
                $.each(buildids, function (j, build) {
                    configMap.urlOption.modelid = build;
                    configMap.urlOption.energytypeid = item.id;
                    modelMap.modelenergy.post('getenergy', function (data) {
                        if (data.energy.data!=null&&data.energy.data.length > 0)
                            energy.data[j] = data.energy.data[0];
                        else
                            energy.data[j] = 0;
                    }, null, configMap.urlOption, 'json', false);
                });
                configMap.chartOption.series[i] = energy;
            });
            jqueryMap.$chart.setData(configMap.chartOption);
        }
        else
            jqueryMap.$chart.noData();

    };
    addOptions=function(url,para,$select,text){
        modelMap.modeltools.post(url,function(data){
            if(data!=null){
                $select.html(text);
                $.each(data,function(i,item){
                    $select.append("<option value='"+item.id+"'>"+item.name+"</option>");
                });
            }
        },null,para,'json');
    };
    addEvent=function(){
        jqueryMap.$form.getElementsByName('first').closest('.form-group').hide();
        jqueryMap.$form.getElementsByName('organcmp').closest('.form-group').hide();
        jqueryMap.$form.getElementsByName('group').parent().hide();
        jqueryMap.$form.getElementsByName('build').parent().hide();
        var $group=jqueryMap.$form.getElementsByName('group');
        $group.html('<option>请选择区域</option>');
        addOptions('getgroup',null,$group,'<option>请选择区域</option>');
        var $build=jqueryMap.$form.getElementsByName('build');
        $build.html('<option>请选择楼栋</option>');
        $group.change(function(){
            addOptions('getbuild',$group.val(),$build,'<option>请选择楼栋</option>');
        });
        $build.change(function(){
            console.log(jqueryMap.$form.getElementsByName('level').val());
            configMap.selectOption.ajax.data = $(this).val();
            if(jqueryMap.$form.getElementsByName('level').filter(':checked').val()=='floor'){
                configMap.selectOption.ajax.url = 'getfloornames.do';
            }else {
                configMap.selectOption.ajax.url = 'getroomnamesbybuild.do';
            }
            jqueryMap.$form.getElementsByName('buildcmp').select2(configMap.selectOption);
        });
        //var $first=jqueryMap.$form.getElementsByName('first');
        //$first.html('<option value="#">全校</option>');
        //addOptions('getorgan','00',$first,'<option value="#">全校</option>');
        //var $second=jqueryMap.$form.getElementsByName('second');
        //$second.html('<option value="#">全学院</option>');
        //$first.change(function(){
        //    addOptions('getorgan',$first.val(),$second,'<option value="#">全学院</option>');
        //});
        //var $third=jqueryMap.$form.getElementsByName('third');
        //$third.html('<option value="#">全部门</option>');
        //$second.change(function(){
        //    addOptions('getorgan',$second.val(),$third,'<option value="#">全部门</option>');
        //});
        jqueryMap.$form.getElementsByName('buildcmp').select2(configMap.selectOption);
        jqueryMap.$form.getElementsByName('buildcmp').on('change',getEnergy);
        jqueryMap.$form.getElementsByName('organcmp').on('change',getEnergy);
        jqueryMap.$date.update(configMap.timeOptions,timeFunc);
        jqueryMap.$form.getElementsByName('aim').change(setAim);
        jqueryMap.$form.getElementsByName('time').change(setTime);
        jqueryMap.$form.getElementsByName('caltype').change(setCalType);
        jqueryMap.$form.getElementsByName('type').change(setType);
        jqueryMap.$form.getElementsByName('level').change(setLevel);
        jqueryMap.$energyitemtree.setCheckEvent(getEnergy);
        jqueryMap.$energyitemtree.setUncheckEvent(getEnergy);
        jqueryMap.$energyitemtree.setInitEvent(function(){
            jqueryMap.$energyitemtree.check_node('01000');
        });
        jqueryMap.$chart.noData();
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
        initParameter();
    };
    return{initModule:initModule};
}());