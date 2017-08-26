/**
 * Created by jason on 2015/3/5.
 */
ecss.waterenergy=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
            +'<div class="page-title">'
            +'<h4 class="pull-right strong" id="datetime"></h4>'
            +'<ol class="breadcrumb">'
            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
            +'<li class="active">水耗展示</li>'
            +'</ol>'
            +'</div>'
            +'</div>'
            +'<div class="col-lg-2" id="energyitem"></div>'
            +'<div class="col-lg-10" id="condition"></div>'
            +'<div class="col-lg-10" id="display"></div>',
            energyitem:'<div id="energyitemtree"></div>',
            condition:'<div id="requireform"></div>',
            display:'<table id="example" class="table table-striped table-bordered nowrap" cellspacing="0" width="100%"></table>'
            +'<div  id="value" class="chartstyle" style="height: 390px;"></div>'
            +'<div  id="chain" class="chartstyle" style="height: 390px;"></div>'
            +'<div  id="yony" class="chartstyle" style="height: 390px;"></div>',
            TreeOption : {
                core:{
                    check_callback:true,
                    data:{
                        url:'getallwateritems.do'
                    }
                },
                checkbox:{
                    three_state:false,
                    tie_selection:false
                },
                plugins:['checkbox','search']
            },
            formOption:[//QAQ:新版适应屏幕
                [{label:'label',text:'展示目标',size:[12,3,2,1]} ,{label:'input',type:'radio',name:'aim',options:[{value:'build',text:'建筑',checked:true},{value:'organ',text:'机构'}],size:[12,9,6,6],valid:{}},
                    {label:'label',text:'展示内容',size:[12,3,2,1]}
                    ,{label:'input',type:'radio',name:'caltype',options:[{value:'total',text:'总能耗',checked:true},{value:'people',text:'人均能耗'},{value:'area',text:'平均面积'}],size:[12,9,4,4],valid:{}}],
                [{label:'label',text:'',size:[0,3,2,1]}
                    ,{label:'select',type:'text',name:'group',size:[12,4,5,2],valid:{}}
                    ,{label:'select',type:'text',name:'build',size:[12,4,5,2],valid:{}}
                    ,{label:'label',text:'',size:[0,3,2,0]}
                    ,{label:'select',type:'text',name:'floor',size:[12,4,5,2],valid:{}}
                    ,{label:'select',type:'text',name:'room',size:[12,4,5,2],valid:{}}],
                [{label:'label',text:'',size:[0,3,2,1]}
                    ,{label:'select',type:'text',name:'first',size:[12,3],valid:{}}
                    ,{label:'select',type:'text',name:'second',size:[12,3],valid:{}}
                    ,{label:'select',type:'text',name:'third',size:[12,3],valid:{}}],
                [{label:'label',text:'时间视图',size:[12,3,2,1]}
                    ,{label:'input',type:'radio',name:'time',options:[{value:'year',text:'年'},{value:'month',text:'月'},{value:'day',text:'日'},{value:'hour',text:'小时',checked:true},{value:'minutes',text:'15分钟'}],size:[12,9,6,6],valid:{}},
                    {label:'label',text:'展示类型',size:[12,3,2,1]}
                    ,{label:'input',type:'radio',name:'showtype',options:[{value:'value',text:'能耗值',checked:true},{value:'chain',text:'环比'},{value:'yony',text:'同比'}],size:[12,7,4,4],valid:{}}],
                [{label:'label',text:'时间区段',size:[12,3,2,1]}
                    ,{label:'input',type:'daterange',name:'daterange',size:[12,9,5,5],valid:{}}]
            ],
            timeOptions:{
                autoclose:true,
                language: 'zh-CN',
                format: 'yyyy/mm/dd',
                weekStart:1,
                endDate:'0d',
                clearBtn:true,
                minViewMode:0,
                isRange:true
            },
            urlOption:{
            },
            chartOption:{
                toolbox: {
                    show : true,
                    orient: 'horizontal',      // 布局方式，默认为水平布局，可选为：'horizontal' ¦ 'vertical'
                    x: 'left',                // 水平安放位置，默认为全图右对齐，可选为： 'center' ¦ 'left' ¦ 'right'¦ {number}（x坐标，单位px）
                    y: 'top',                  // 垂直安放位置，默认为全图顶端，可选为： 'top' ¦ 'bottom' ¦ 'center' ¦ {number}（y坐标，单位px）
                    color : ['#1e90ff','#22bb22','#4b0082','#d2691e'],
                    backgroundColor: 'rgba(0,0,0,0)', // 工具箱背景颜色
                    borderColor: '#ccc',       // 工具箱边框颜色
                    borderWidth: 0,            // 工具箱边框线宽，单位px，默认为0（无边框）
                    padding: 5,                // 工具箱内边距，单位px，默认各方向内边距为5，
                    showTitle: true,
                    feature : {
                        dataZoom : {show : true, title : {dataZoom : '区域缩放', dataZoomReset : '区域缩放-后退'}},
                        //dataView : {show : true, title : '数据视图', readOnly:true, lang : ['数据视图', '关闭', '刷新']},
                        magicType: {show : true, title : {line : '折线图', bar : '柱形图'}, type : ['line', 'bar']},
                        restore : {show : true, title : '还原', color : 'black'},
                        saveAsImage : {show : true, title : '保存为图片', type : 'jpeg', lang : ['点击本地保存']}
                    }
                },
                tooltip:{
                    trigger:'axis'
                },
                calculable : true,
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
                        type : 'value',
                        name : '耗水量',
                        splitLine : {show : false},
                        axisLabel : {
                            formatter: '{value} 吨'
                        }
                    }
                ],
                series:[]
            },
            chainOption:{
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
                        type : 'value',
                        name : '环比',
                        splitLine : {show : false},
                        axisLabel : {
                            formatter: '{value} ％'
                        }
                    }
                ],
                series:[]
            },
            yonyOption:{
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
                        type : 'value',
                        name : '同比',
                        splitLine : {show : false},
                        axisLabel : {
                            formatter: '{value} ％'
                        }
                    }
                ],
                series:[]
            }
        },
        modelMap={
            modelenergy:new ecss.tools.makeModel(ecss.model.energy.energy),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        stateMap={
            $container: null
        },
        jqueryMap={},
        setJqueryMap, initModule,initParameter, addEvent,setModel,addOptions,getTime,getEnergy,timeFunc,setTime,timeFunc,setAim,setCalType,setType,setCalTypehide;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $portlet1    :new ecss.tools.makePortlet($container.find('#energyitem'),'水分项','blue','',configMap.energyitem),
            $portlet2    :new ecss.tools.makePortlet($container.find('#condition'),'查询条件','orange','',configMap.condition),
            $portlet3    :new ecss.tools.makePortlet($container.find('#display'),'水耗展示','green','',configMap.display),
            $energyitemtree  :new ecss.tools.makeTree($container.find('#energyitemtree'),configMap.TreeOption,650),
            $form       :new ecss.tools.makeForm($container.find('#requireform'),configMap.formOption),
            $energychart      :new ecss.tools.makeChart('value'),
            $chainchart      :new ecss.tools.makeChart('chain'),
            $yonychart      :new ecss.tools.makeChart('yony'),
            $confirm    :new ecss.tools.makeModal($container),
            $daterange       :new ecss.tools.makeDate($container.find('#datepicker'),{isRange:true}),
            $window     :$(window)
        };
        jqueryMap.$container.find('.chartstyle').hide();
        jqueryMap.$container.find('#value').show();
    };
    initParameter=function(){
        configMap.urlOption={
            model:'build',
            modellevel:'school',
            modelid:null,
            energytypeid:null,
            energytype:null,
            startdate:moment().format('YYYY/MM/DD'),
            enddate:moment().add(1,'days').format('YYYY/MM/DD'),
            basetime:'hour',
            caltype:'total'
        };
        jqueryMap.$form.getElementsByName('daterange').val(configMap.urlOption.startdate);

    };
    setAim=function () {
        if($(this).val()=='organ'){
            jqueryMap.$form.getElementsByName('group').closest('.form-group').hide();
            jqueryMap.$form.getElementsByName('first').closest('.form-group').show();
        }
        else{
            jqueryMap.$form.getElementsByName('first').closest('.form-group').hide();
            jqueryMap.$form.getElementsByName('group').closest('.form-group').show();
        }
        configMap.urlOption['model']=$(this).val();
        configMap.urlOption.modelid='allofsumgroup';


        configMap.urlOption['caltype']='total';
        jqueryMap.$form.getElementsByName('caltype').filter('[value=total]').prop("checked",true);
        if($(this).val()=='build'&&jqueryMap.$form.getElementsByName('group').val()=='allofsumgroup'){
            jqueryMap.$form.getElementsByName('caltype').filter('[value=people]').parent().show();
            jqueryMap.$form.getElementsByName('caltype').filter('[value=area]').parent().show();
        }
        else{
            jqueryMap.$form.getElementsByName('caltype').filter('[value=people]').parent().hide();
            jqueryMap.$form.getElementsByName('caltype').filter('[value=area]').parent().hide();
        }
        getEnergy();
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
            if(jqueryMap.$form.getElementsByName('start').val()=='') {
                jqueryMap.$form.getElementsByName('start').val(startdate);
                jqueryMap.$form.getElementsByName('end').val(startdate);
            }
            $('.input-group-addon').hide();
            jqueryMap.$form.getElementsByName('end').hide();
        }else
        if(basetime=='day'){
            configMap.timeOptions.minViewMode=0;
            configMap.timeOptions.format='yyyy/mm/dd';
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            var startdate=moment().subtract(10,'days').format('YYYY/MM/DD');
            var enddate=moment().format('YYYY/MM/DD');
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(enddate);
            $('.input-group-addon').show();
            jqueryMap.$form.getElementsByName('end').show();
        }else
        if(basetime=='month'){
            configMap.timeOptions.minViewMode=1;
            configMap.timeOptions.format='yyyy/mm';
            var startdate=moment().subtract(6,'months').format('YYYY/MM');
            var enddate=moment().format('YYYY/MM');
            configMap.timeOptions.endDate='0d';
            jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
            jqueryMap.$form.getElementsByName('start').val(startdate);
            jqueryMap.$form.getElementsByName('end').val(enddate);
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
    setCalType=function(){
        configMap.urlOption['caltype']=$(this).val();
        getEnergy();
    };
    setType=function(){
        configMap.urlOption['showtype']=$(this).val();
        jqueryMap.$container.find('.chartstyle').hide();
        jqueryMap.$container.find('#'+$(this).val()).show();
        if($(this).val()=='value') jqueryMap.$table.show();
        else jqueryMap.$table.hide();
    };
    setModel=function(){
        $.each(configMap.urlOption.model=='build'?jqueryMap.$form.getElementsByName('group').closest('.form-group').find('select'):
            jqueryMap.$form.getElementsByName('first').closest('.form-group').find('select'),function(index,item){
            if($(item).val()=='#') return;
            configMap.urlOption.modelid=$(item).val();
            if($(item).val()!='allofsumgroup')
                configMap.urlOption['modellevel']=$(this).prop('name');
            else
                configMap.urlOption['modellevel']='school';
        });
        configMap.chartOption.legend.data=[];
        configMap.chartOption.series=[];
        configMap.chainOption.legend.data=[];
        configMap.chainOption.series=[];
        configMap.yonyOption.legend.data=[];
        configMap.yonyOption.series=[];
    };
    getTime=function(){
        var basetime=configMap.urlOption.basetime;
        var starttime=moment(configMap.urlOption.startdate);
        var endtime=moment(configMap.urlOption.enddate);
//        console.log(basetime+starttime+endtime);
        if(jqueryMap.$table!=null)
            jqueryMap.$table.destroy();
        $('#example').html('<thead><tr><th>能耗分项</th></tr></thead>');
        var datas=[];
        if(basetime=='minutes'){
            while(starttime<endtime){
                datas.push(starttime.format('HH:mm'));
                $('#example thead tr').append('<th>'+starttime.format('HH:mm')+'</th>');
                starttime.add(15,'minutes');
            }
        }
        else
        if(basetime=='hour'){
            while(starttime<endtime){
                datas.push(starttime.format('HH:00'));
                $('#example thead tr').append('<th>'+starttime.format('HH:00')+'</th>');
                starttime.add(1,'hours');
            }
        }
        else
        if(basetime=='day'){
            while(starttime<endtime){
                datas.push(starttime.format('YYYY/MM/DD'));
                $('#example thead tr').append('<th>'+starttime.format('YYYY/MM/DD')+'</th>');
                starttime.add(1,'days');
            }
        }
        else
        if(basetime=='month'){
            while(starttime<endtime){
                datas.push(starttime.format('YYYY/MM'));
                $('#example thead tr').append('<th>'+starttime.format('YYYY/MM')+'</th>');
                starttime.add(1,'months');
            }
        }
        else
        if(basetime=='year'){
            while(starttime<endtime){
                datas.push(starttime.format('YYYY'));
                $('#example thead tr').append('<th>'+starttime.format('YYYY')+'</th>');
                starttime.add(1,'years');
            }
        }
        $('#example thead tr').append('<th>总耗电量</th>');
//        console.log(datas);
        return datas;
    };
    getEnergy=function(){
        setModel();
        var datatime=getTime();
        jqueryMap.$table= new ecss.tools.makeTable($('#example'), {
            scrollY:        "300px",
            scrollX:        true,
            scrollCollapse: true,
            paging:         false,
            dom: 'rt',
            fixedColumns:   {
                leftColumns: 1,
                rightColumns: 1
            }
        } );
        $.each(jqueryMap.$energyitemtree.get_checked(),function(index,item){
            configMap.urlOption['energytypeid']=item.id;
            configMap.urlOption['energytype']=item.text;
            modelMap.modelenergy.post('getenergy',function(data){
                console.log(data);
                var tmp={};
                $.each(data.categories,function(index,value){
                    tmp[value]=data.energy.data[index];
                });
                var newdata=[];
                $.each(datatime,function(index,value){
                    if(tmp[value])
                        newdata[index]=tmp[value];
                    else
                        newdata[index]=0;
                });
                var tabledata=[item.text].concat(newdata);
                tabledata.push(newdata.reduce(function(a,b){return a+b;}).toFixed(4));
                jqueryMap.$table.setRowData(tabledata);
                var energy={};
                if(data.energy.data!=null&&data.energy.data.length>0){
                    energy.data=newdata.map(function(item){
                        return (item<0?-item:item).toFixed(4);
                    });
                    //energy.data=(function(size){var s=[];while(size--)s.push('0.000');return s})(datatime.length-energy.data.length).concat(energy.data);
                    console.log(energy.data);
                    energy.name=item.text;
                    energy.type='bar';
                    configMap.chartOption.legend.data.push(item.text);
                    configMap.chartOption.xAxis[0].data=datatime;
                    configMap.chartOption.series.push(energy);
                    configMap.chartOption.yAxis[0].name = "总耗水量:"+data.energy.data.reduce(function(a,b){return a+b;}).toFixed(4)+'度';
                    console.log(configMap.chartOption);
                    jqueryMap.$energychart.setData(configMap.chartOption);
                }else{
                    jqueryMap.$energychart.clear();
                    if(configMap.chartOption.series.length==0)
                        jqueryMap.$energychart.noData();
                }
                var chain={};
                if(data.chainenergy.data!=null&&data.chainenergy.data.length>0){
                    chain.data=data.chainenergy.data;
                    chain.name=item.text;
                    chain.type='line';
                    configMap.chainOption.legend.data.push(item.text);
                    configMap.chainOption.xAxis[0].data=datatime;
                    configMap.chainOption.series.push(chain);
                    jqueryMap.$chainchart.setData(configMap.chainOption);
                }else{
                    jqueryMap.$chainchart.clear();
                    if(configMap.chainOption.series.length==0)
                        jqueryMap.$chainchart.noData();
                }
                //var yony={};
                //if(data.yony!=null&&data.yony.length>0){
                //    yony.data=data.yony;
                //    yony.name=item.text;
                //    yony.type='line';
                //    configMap.yonyOption.legend.data.push(item.text);
                //    configMap.yonyOption.xAxis[0].data=datatime;
                //    configMap.yonyOption.series.push(yony);
                //    jqueryMap.$yonychart.setData(configMap.yonyOption);
                //}else{
                //    jqueryMap.$yonychart.clear();
                //    if(configMap.yonyOption.series.length==0)
                //        jqueryMap.$yonychart.noData();
                //}
            },null,configMap.urlOption,'json');
        });
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
    setCalTypehide=function () {
        configMap.urlOption['caltype']='total';
        jqueryMap.$form.getElementsByName('caltype').filter('[value=total]').prop("checked",true);
        if($(this).val()=='allofsumgroup'){
            jqueryMap.$form.getElementsByName('caltype').filter('[value=people]').parent().show();
            jqueryMap.$form.getElementsByName('caltype').filter('[value=area]').parent().show();
        }
        else{
            jqueryMap.$form.getElementsByName('caltype').filter('[value=people]').parent().hide();
            jqueryMap.$form.getElementsByName('caltype').filter('[value=area]').parent().hide();
        }
        getEnergy();
    };
    addEvent=function(){
        jqueryMap.$form.getElementsByName('group').closest('.form-group').find('select').change(setCalTypehide);
        jqueryMap.$form.getElementsByName('first').closest('.form-group').find('select').change(setCalTypehide);
        jqueryMap.$form.getElementsByName('first').closest('.form-group').hide();
        var $group=jqueryMap.$form.getElementsByName('group');
        $group.html('<option value="allofsumgroup">全校</option>');
        addOptions('getgroup',null,$group, '<option value="allofsumgroup">全校</option>');
        var $build=jqueryMap.$form.getElementsByName( 'build');
        $build.html('<option value="#">全区</option>');
        $group.change(function(){
            addOptions('getbuild',$group.val(),$build,'<option value="#">全区</option>');
        });
        var $floor=jqueryMap.$form.getElementsByName('floor');
        $floor.html('<option value="#">全楼</option>');
        $build.change(function(){
            addOptions('getfloor',$build.val(),$floor, '<option value="#">全楼</option>');
        });
        var $room=jqueryMap.$form.getElementsByName('room');
        $room.html('<option value="#">全层</option>');
        $floor.change(function(){
            addOptions('getroom',$floor.val(),$room,'<option value="#">全层</option>');
        });
        var $first=jqueryMap.$form.getElementsByName('first');
        $first.html('<option value="#">全校</option>');
        addOptions('getorgan','j4_2',$first, '<option value="#">全校</option>');
        var $second=jqueryMap.$form.getElementsByName('second');
        $second.html('<option value="#">全学院</option>');
        $first.change(function(){
            addOptions('getorgan',$first.val(),$second,'<option value="#">全学院</option>');
        });
        var $third=jqueryMap.$form.getElementsByName('third');
        $third.html('<option value="#">全部门</option>');
        $second.change(function(){
            addOptions('getorgan',$second.val(),$third,'<option value="#">全部门</option>');
        });
        jqueryMap.$daterange.update(configMap.timeOptions,timeFunc);
        var startdate=moment().format('YYYY/MM/DD');
        jqueryMap.$form.getElementsByName('start').val(startdate);
        jqueryMap.$form.getElementsByName('end').val(startdate);
        $('.input-group-addon').hide();
        jqueryMap.$form.getElementsByName('end').hide();
        jqueryMap.$form.getElementsByName('aim').change(setAim);
        jqueryMap.$form.getElementsByName('time').change(setTime);
        jqueryMap.$form.getElementsByName('caltype').change(setCalType);
        jqueryMap.$form.getElementsByName('showtype').change(setType);
        jqueryMap.$energyitemtree.setCheckEvent(getEnergy);
        jqueryMap.$energyitemtree.setUncheckEvent(getEnergy);
        jqueryMap.$energyitemtree.setInitEvent(function(){
            jqueryMap.$energyitemtree.open_node();
            jqueryMap.$energyitemtree.check_node(['02000']);
        });
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