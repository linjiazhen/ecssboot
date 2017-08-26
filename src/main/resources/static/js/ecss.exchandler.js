/**
 * Created by jason on 2015/3/4.
 */
ecss.exchandler=(function(){
    'use strict';
    var configMap={
            main_html:String()
            +'<div class="col-lg-12">'
            +'<div class="page-title">'
            +'<h4 class="pull-right strong" id="datetime"></h4>'
            +'<ol class="breadcrumb">'
            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
            +'<li class="active">异常处理</li>'
            +'</ol>'
            +'</div>'
            +'<div id="portlet"></div>'
            +'</div>',
            body:String()
            +'<div class="form-group">'
            +'<div class="col-md-2">'
            +'<div class="radio radio-success radio-inline" style="margin-top:5px">'
            +'<label>'
            +'<input type="radio" name="type" value="ammeter" >电'
            +'</label>'
            +'<label>'
            +'<input type="radio" name="type" value="watermeter" checked="">水'
            +'</label>'
            +'</div>'
            +'</div>'
            +'<lable class="col-md-1 control-label level-label">层级:</lable>'
            +'<div class="col-md-2 level-select"><select name="level" class="form-control"><option value="0">房间</option><option value="1">楼层</option><option value="2">楼栋</option></select></div>'
            +'<lable class="col-md-1 control-label">时间选择:</lable>'
            +'<div class="col-md-2"><input type="text" name="datetime" class="form-control" placeholder="日期"/></div>'
            +'<div class="col-md-1"><input type="text" name="starttime" class="form-control" placeholder="开始时间"/></div>'
            +'<div class="col-md-1"><input type="text" name="endtime" class="form-control" placeholder="结束时间"/></div>'
            +'</div>'
            +'<div class="tablecontent amtable" style="margin-top:45px;display: none">'
            +'<div class="buttons">'
            +'<button class="btn btn-success btn-sm" id="addamlist">添加白名单</button>'
            +'<button class="btn btn-success btn-sm" id="amlist">白名单</button>'
            +'</div>'
            +'<table class="table table-hover table-bordered" id="DTamlist"></table>'
            +'</div>'
            +'<div class="tablecontent watertable" style="margin-top:45px">'
            +'<div class="buttons">'
            +'<button class="btn btn-success btn-sm" id="addwaterlist">添加白名单</button>'
            +'<button class="btn btn-success btn-sm" id="waterlist">白名单</button>'
            +'</div>'
            +'<table class="table table-hover table-bordered" id="DTwaterlist"></table>'
            +'</div>',
            tableListOption:{
                "columns": [ { title:"设备编号","data": "equip" },
                    { title:"型号","data": "model"},
                    { title:"设备类型","data": "type"},
                    { title:"启用","data": "use",
                        render: function(data){
                            if(data==1)
                                return "<strong class='text-success'>开启</strong>";
                            else
                                return "<strong class='text-danger'>关闭</strong>";
                        }},
                    { title:'所属网关',"data": "gateway"},
                    { title:'测量点号',"data": "pn" },
                    { title:"状态","data": "status",
                        "render":function ( data ) {
                            if(data==0)
                                return "<strong class='text-success'>正常</strong>";
                            else if(data==1)
                                return "<strong class='text-danger'>离线</strong>";
                            else if(data==3)
                                return "<strong class='text-danger'>底度</strong>";
                            else  if(data==2)
                                return "<strong class='text-danger'>故障</strong>";
                        }},
                    { title:"测量分项","data": "energytype"},
                    { title:"当前表盘数","data": "active"},
                    { title:"能耗(kwh)","data": "energy",
                        "render":function (data) {
                            if(data < 0)
                                return "-";
                            else
                                return data;
                        }},
                    { title:"测量位置","data": "remarkinfo","width": "20%" },
                    { title:"备注","data": "remark"}
                ],
                "scrollY":        "800px",
                "scrollCollapse": true,
                "paging":         false,
                "dom": 'rt',
                "order": [[ 9, 'desc' ]]
            },
            amtableOption:{
                "columns":[
                    {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>","className": 'details-control', "data":   null, orderable: false, searchable:false,sortable:false, "defaultContent": ''},
                    {title:'建筑名称',data:'buildname'},
                    {title:'总电耗 ',data:'energy'},
                    {title:'照明插座',data:'aenergy'},
                    {title:'空调用电',data:'benergy'},
                    {title:'动力用电',data:'cenergy'},
                    {title:'特殊用电',data:'denergy'},
                    {title:'其它用电',data:'zenergy'}
                ],
                "buttons": [
                    {
                        text:'保存',
                        extend:'excel',
                        className:'btn-info saveam',
                        name:'表格'
                    }
                ],
                "order": [[ 2, 'desc' ]]
            },
            watertableOption:{
                "columns": [ {"title":"<div class='checkbox' ><label><input type='checkbox' id='chooseall'></label></div>", "data": null, orderable: false,searchable:false,sortable:false,
                    "defaultContent": "<div class='checkbox' ><label><input type='checkbox' class='choose'></label></div>"},
                    { title:"测量位置","data": "remarkinfo"},
                    { title:"设备编号","data": "equip" },
                    { title:"启用","data": "use",
                        render: function(data){
                            if(data==1)
                                return "<strong class='text-success'>开启</strong>";
                            else
                                return "<strong class='text-danger'>关闭</strong>";
                        }},
                    { title:'所属网关',"data": "gateway"},
                    { title:'测量点号',"data": "pn" },
                    { title:"状态","data": "status",
                        "render":function ( data ) {
                            if(data==0)
                                return "<strong class='text-success'>正常</strong>";
                            else if(data==1)
                                return "<strong class='text-danger'>离线</strong>";
                            else if(data==3)
                                return "<strong class='text-danger'>底度</strong>";
                            else  if(data==2)
                                return "<strong class='text-danger'>故障</strong>";
                        }},
                    { title:"当前表盘数","data": "active",
                        "render":function (data) {
                            if(data < 0)
                                return "-";
                            else
                                return data;
                        }},
                    //{ title:'当前水流',"data": "flow"},
                    //{ title:'当前水压',"data": "press"},
                    { title:'用水量',"data": "energy"},
                    { title:'备注',"data": "remark"}
                ],
                "buttons": [
                    {
                        text:'保存',
                        extend:'excel',
                        className:'btn-info savewater',
                        name:'表格'
                    }
                ],
                "order": [[ 8, 'desc' ]]
            },
            tableAmOption:{
                "columns": [
                    { title:"位置","data": "buildname" },
                    {title: "操作", "data": 'id',className:'disclick',
                        "render": function (data, type, full) {
                            return '<a class="amdelete" id="'+data+'" style="cursor: pointer">移除</a>'
                        }}
                ],
                "pagingType":'simple',
                "dom": 'lprt',
                "order": [[ 0, 'desc' ]]
            },
            tableWaterOption:{
                "columns": [
                    { title:"位置","data": "buildname" },
                    {title: "操作", "data": 'id',className:'disclick',
                        "render": function (data, type, full) {
                            return '<a class="waterdelete" id="'+data+'" style="cursor: pointer">移除</a>'
                        }}
                ],
                "pagingType":'simple',
                "dom": 'lprt',
                "order": [[ 0, 'desc' ]]
            },
            amformDisplayOption:[
                [{label:'label',text:'设备编号',size:2},{label:'input',type:'text',name:'equip',size:4,disable:true},{label:'label',text:'测量位置',size:2},{label:'input',type:'text',name:'location',size:4,disable:true}],
                [{label:'label',text:'设备类型',size:2},{label:'input',type:'text',name:'subtype',size:4,disable:true},{label:'label',text:'备注',size:2},{label:'input',type:'text',name:'remark',size:4,disable:true}],
                [{label:'label',text:'查看选项',size:2},{label:'input',type:'radio',name:'opts',options:[{value:'energy',text:'能耗情况',checked:true},{value:'running',text:'运行情况'}],size:4}],
                [{label:'label',text:'类型选择',size:2},{label:'input',type:'radio',name:'time',options:[{value:'active',text:'表盘读数'},{value:'minutes',text:'15分钟用电',checked:true}],size:5,valid:{}}],
                [{label:'label',text:'时间选择',size:2},{label:'input',type:'date',name:'daytime',size:5,valid:{}}],
                [{label:'input',type:'radio',name:'parameter',options:[{value:'i',text:'电流',checked:true}],size:2},{label:'input',type:'checkbox',name:'i',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}},{label:'input',type:'radio',name:'parameter',options:[{value:'p',text:'有功'}],size:2},{label:'input',type:'checkbox',name:'p',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}}],
                [{label:'input',type:'radio',name:'parameter',options:[{value:'u',text:'电压'}],size:2},{label:'input',type:'checkbox',name:'u',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}},{label:'input',type:'radio',name:'parameter',options:[{value:'np',text:'无功'}],size:2},{label:'input',type:'checkbox',name:'np',options:[{value:'a',text:'A'},{value:'b',text:'B'},{value:'c',text:'C'}],size:4,valid:{}}]
            ],
            waterformDisplayOption:[
                [{label:'label',text:'设备编号',size:2},{label:'input',type:'text',name:'equip',size:4,disable:true},{label:'label',text:'安装位置',size:2},{label:'input',type:'text',name:'location',size:4,disable:true}],
                [{label:'label',text:'设备类型',size:2},{label:'input',type:'text',name:'subtype',size:4,disable:true},{label:'label',text:'备注',size:2},{label:'input',type:'text',name:'remark',size:4,disable:true}],
                [{label:'label',text:'查看选项',size:2},{label:'input',type:'radio',name:'opts',options:[{value:'energy',text:'能耗情况',checked:true},{value:'running',text:'运行情况'}],size:4}],
                [{label:'label',text:'类型选择',size:2},{label:'input',type:'radio',name:'time',options:[{value:'active',text:'表盘读数'},{value:'minutes',text:'15分钟用水',checked:true}],size:5,valid:{}}],
                [{label:'label',text:'时间选择',size:2},{label:'input',type:'date',name:'daytime',size:5,valid:{}}],
                [{label:'label',text:'参数选择',size:2},{label:'input',type:'radio',name:'parameter',options:[{value:'ua',text:'流速',checked:true},{value:'p',text:'水压'}],size:8,valid:{}}]
            ],
            chartOption:{
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
                        scale:true,
                        type : 'value'
                    }
                ],
                series:[]
            },
            runningOption:{
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
                        scale:true,
                        type : 'value'
                    }
                ],
                series:[]
            },
            waterchartOption:{
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
                        scale:true,
                        type : 'value'
                    }
                ],
                series:[]
            },
            waterrunningOption:{
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
                        scale:true,
                        type : 'value'
                    }
                ],
                series:[]
            },
            urlOption:{},
            urlWaterOption:{}
        },
        stateMap={
            $container:null
        },
        modelMap={
            model:new ecss.tools.makeModel(ecss.model.alert.exchandler),
            modelam:new ecss.tools.makeModel(ecss.model.maintenance.ammeter),
            modelwater:new ecss.tools.makeModel(ecss.model.maintenance.watermeter),
            modeltool:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        jqueryMap={},
        setJqueryMap,   initModule,   switchPage,  addEvent,addAmList,amList,addWaterList,waterList,listChildFormat,childOpen,childClose,getAmTableData,getWaterTableData,
        initAmDisplay,initWaterDisplay,amshowDisplay,watershowDisplay,getData,getWaterData;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $dtlist     :null,
            $portlet    :new ecss.tools.makePortlet($container.find('#portlet'),'异常处理','green','',configMap.body),
            $dtamlist  :new ecss.tools.makeTable($container.find('#DTamlist'),configMap.amtableOption),
            $dtwaterlist  :new ecss.tools.makeTable($container.find('#DTwaterlist'),configMap.watertableOption),
            $ammodaldisplay:new ecss.tools.makeModal($container,'电表数据展示','modal-lg'),
            $amformdisplay :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.amformDisplayOption),
            $watermodaldisplay:new ecss.tools.makeModal($container,'水表数据展示','modal-lg'),
            $waterformdisplay :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.waterformDisplayOption),
            $ammodal     :new ecss.tools.makeModal($container,'电表白名单'),
            $dtam        :new ecss.tools.makeTable($container.find('.modal-body').last().html('<table class="table table-hover table-bordered" id="DTam"></table>').find('#DTam'),configMap.tableAmOption),
            $watermodal     :new ecss.tools.makeModal($container,' 水表白名单'),
            $dtwater     :new ecss.tools.makeTable($container.find('.modal-body').last().html('<table class="table table-hover table-bordered" id="DTwater"></table>').find('#DTwater'),configMap.tableWaterOption),
            $daterange  :new ecss.tools.makeDate(stateMap.$container.find('#datepicker'),{isRange:true}),
            $confirm     :new ecss.tools.makeModal($container),
            $addamlist      :$container.find('#addamlist'),
            $amlist         :$container.find('#amlist'),
            $addwaterlist   :$container.find('#addwaterlist'),
            $waterlist      :$container.find('#waterlist'),
            $radio          :$container.find('input[type=radio]'),
            $window     :$(window)
        };
    };
    listChildFormat=function(){
        return '<div style=“text-align: center;”>' +
            '<table class="table table-hover table-bordered" class="DTlist"></table></div>';
    };
    childOpen=function(data){
        var level=jqueryMap.$container.find('[name=level]').val();
        var datetime=jqueryMap.$container.find('[name=datetime]').val();
        var starttime=jqueryMap.$container.find('[name=starttime]').val();
        var endtime=jqueryMap.$container.find('[name=endtime]').val();
        if(jqueryMap.$dtlist!=null){
            jqueryMap.$dtlist.destroy();
            jqueryMap.$dtlist=null;
        }
        //jqueryMap.$container.find('#DTequiplist table').before('<div>'+data.remarkinfo+'</div>');
        jqueryMap.$dtlist =new ecss.tools.makeTable(jqueryMap.$container.find('#DTamlist table'),configMap.tableListOption);
        modelMap.model.post('getamsublist',function(data){
            jqueryMap.$dtlist.setData(data,true);
        },function(){
            ZENG.msgbox.show('打开失败！',5,3000);
        },level+','+data.buildid+','+(datetime+' '+starttime)+','+(datetime+' '+endtime),'json',false);
    };
    childClose=function(){
        jqueryMap.$dtlist.destroy();
        jqueryMap.$dtlist=null;
    };
    addAmList=function(){
        var level=jqueryMap.$container.find('[name=level]').val();
        var data=jqueryMap.$dtamlist.getSelect();
        var ids=data.map(function(item){return item.buildid}).join(",");
        var names=data.map(function(item){return item.buildname}).join(",");
        jqueryMap.$confirm.confirm('确认将以下'+data.length+'个位置加入白名单？',names,function(){
            modelMap.model.post('setwhitelist',function(){
                getAmTableData();
                jqueryMap.$confirm.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'添加失败！请刷新重试！');
            },'1,'+level+','+ids);
        });
    };
    amList=function(){
        var level=jqueryMap.$container.find('[name=level]').val();
        ZENG.msgbox.show('正在获取...',6,1000000);
        modelMap.model.post('getwhitelist',function(data){
            jqueryMap.$dtam.setData(data,true);
            jqueryMap.$ammodal.show();
            jqueryMap.$ammodal.hideFooter();
            ZENG.msgbox.show('数据获取成功！',4,3000);
        },function(){
            ZENG.msgbox.show('数据获取失败！',5,3000);
        },'1,'+level,'json');
    };
    addWaterList=function(){
        var data=jqueryMap.$dtwaterlist.getSelect();
        var ids=data.map(function(item){return item.equipid}).join(",");
        var names=data.map(function(item){return item.remarkinfo}).join(",");
        jqueryMap.$confirm.confirm('确认将以下'+data.length+'个位置加入白名单？',names,function(){
            modelMap.model.post('setwhitelist',function(){
                getWaterTableData();
                jqueryMap.$confirm.hide();
            },function(){
                ecss.tools.popover.show(jqueryMap.$confirm.getElementsById('save'),'添加失败！请刷新重试！');
            },'2,0,'+ids);
        });
    };
    waterList=function(){
        ZENG.msgbox.show('正在获取...',6,1000000);
        modelMap.model.post('getwhitelist',function(data){
            jqueryMap.$dtwater.setData(data,true);
            jqueryMap.$watermodal.show();
            jqueryMap.$watermodal.hideFooter();
            ZENG.msgbox.show('数据获取成功！',4,3000);
        },function(){
            ZENG.msgbox.show('数据获取失败！',5,3000);
        },'2','json');
    };
    switchPage=function(){
        if($(this).val()=='ammeter'){
            $('.amtable').show();
            $('.watertable').hide();
            $('.saveam').show();
            $('.savewater').hide();
            $('.level-label').show();
            $('.level-select').show();
        }
        else
        if($(this).val()=='watermeter'){
            $('.watertable').show();
            $('.amtable').hide();
            $('.saveam').hide();
            $('.savewater').show();
            $('.level-label').hide();
            $('.level-select').hide();
        }
    };
    addEvent=function(){
        $('table').on('click','.amdelete',function(){
            var level=jqueryMap.$container.find('[name=level]').val();
            modelMap.model.post('deletewhitelist',function(data){
                modelMap.model.post('getwhitelist',function(data){
                    jqueryMap.$dtam.setData(data,true);
                },function(){
                    ZENG.msgbox.show('数据获取失败！',5,3000);
                },'1,'+level,'json');
                getAmTableData();
                ZENG.msgbox.show('数据获取成功！',4,3000);
            },function(){
                ZENG.msgbox.show('数据获取失败！',5,3000);
            },$(this).prop('id'));
        });
        $('table').on('click','.waterdelete',function(){
            modelMap.model.post('deletewhitelist',function(data){
                modelMap.model.post('getwhitelist',function(data){
                    jqueryMap.$dtwater.setData(data,true);
                },function(){
                    ZENG.msgbox.show('数据获取失败！',5,3000);
                },'2','json');
                getWaterTableData();
                ZENG.msgbox.show('数据获取成功！',4,3000);
            },function(){
                ZENG.msgbox.show('数据获取失败！',5,3000);
            },$(this).prop('id'));
        });
        jqueryMap.$dtamlist.setChild(listChildFormat,childOpen,childClose);
        $('.savewater').hide();
        jqueryMap.$addamlist.click(addAmList);
        jqueryMap.$amlist.click(amList);
        jqueryMap.$addwaterlist.click(addWaterList);
        jqueryMap.$waterlist.click(waterList);
        jqueryMap.$dtwaterlist.setDblClick(watershowDisplay);
        jqueryMap.$radio.click(switchPage);
        jqueryMap.$container.find('[name=datetime]').val(moment().add(-1,'day').format('YYYY/MM/DD'));
        jqueryMap.$container.find('[name=datetime]').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'YYYY/MM/DD',
            maxDate:moment(),
            stepping:15,
            sideBySide:true
        });
        jqueryMap.$container.find('[name=starttime]').val('02:00');
        jqueryMap.$container.find('[name=starttime]').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'HH:mm',
            maxDate:moment(),
            stepping:15,
            sideBySide:true
        });
        jqueryMap.$container.find('[name=endtime]').val('05:00');
        jqueryMap.$container.find('[name=endtime]').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'HH:mm',
            maxDate:moment(),
            stepping:15,
            sideBySide:true
        });
        jqueryMap.$container.find('[name=datetime]').on('dp.change',function(){
            getAmTableData();
            getWaterTableData();
        });
        jqueryMap.$container.find('[name=level]').on('change',function(){
            getAmTableData();
            getWaterTableData();
        });
        jqueryMap.$container.find('#DTamlist').on('dblclick','table tbody tr',function(){
            console.log(jqueryMap.$dtlist.getData($(this))[0]);
            amshowDisplay(jqueryMap.$dtlist.getData($(this))[0]);
        });
        jqueryMap.$container.find('#DTwaterlist').on('dblclick','tr',function(){
            console.log(jqueryMap.$dtwaterlist.getData($(this))[0]);
            watershowDisplay(jqueryMap.$dtwaterlist.getData($(this))[0]);
        });
    };

    amshowDisplay=function(data){
        configMap.urlOption.equipid=data.equipid;
        configMap.urlOption.modelid=data.equipid;
        jqueryMap.$amformdisplay.getElementsByName('equip').val(data.equip);
        jqueryMap.$amformdisplay.getElementsByName('subtype').val(data.type);
        jqueryMap.$amformdisplay.getElementsByName('location').val(data.remarkinfo);
        jqueryMap.$amformdisplay.getElementsByName('remark').val(data.remark);
        jqueryMap.$ammodaldisplay.show();
        getData();
    };
    watershowDisplay=function(data){
       configMap.urlWaterOption.equipid=data.equipid;
       configMap.urlWaterOption.modelid=data.equipid;
       jqueryMap.$waterformdisplay.getElementsByName('equip').val(data.equip);
       jqueryMap.$waterformdisplay.getElementsByName('subtype').val(data.type);
       jqueryMap.$waterformdisplay.getElementsByName('location').val(data.remarkinfo);
       jqueryMap.$waterformdisplay.getElementsByName('remark').val(data.remark);
       jqueryMap.$watermodaldisplay.show();
       getWaterData();
    };
    initAmDisplay=function(){
        configMap.urlOption={
            equipid:'',
            modelid:'',
            basetime:'minutes',
            para:'i',
            parameter:'',
            startdate:moment().format('YYYY/MM/DD'),
            datetime:moment().format('YYYY/MM/DD'),
            opts:'energy'
        };
        var $form=jqueryMap.$amformdisplay.getContainer();
        $form.append('<div class="form-group col-md-12"><div  id="energychart" style="width: 870px; height: 400px; margin: 0 auto"></div><div  id="runningchart" style="width: 870px; height: 400px; margin: 0 auto"></div></div>');
        jqueryMap.$energychart=new ecss.tools.makeChart('energychart');
        jqueryMap.$runningchart=new ecss.tools.makeChart('runningchart');
        jqueryMap.$runningchart.noData();
        jqueryMap.$amformdisplay.getElementsByName('parameter').closest('.form-group').hide();
        jqueryMap.$amformdisplay.getElementsById('runningchart').hide();
        jqueryMap.$amformdisplay.getContainer().find('[type=checkbox]').prop('checked',false);
        jqueryMap.$amformdisplay.getContainer().find('[type=checkbox]').prop('disabled',true);
        jqueryMap.$amformdisplay.getElementsByName('i').prop('disabled',false);
        jqueryMap.$amformdisplay.getElementsByName('opts').change(function(){
            configMap.urlOption.opts=$(this).val();
            if($(this).val()=='energy'){
                jqueryMap.$amformdisplay.getElementsByName('parameter').closest('.form-group').hide();
                jqueryMap.$amformdisplay.getElementsById('runningchart').hide();
                jqueryMap.$amformdisplay.getElementsById('energychart').show();
                jqueryMap.$amformdisplay.getElementsByName('time').closest('.form-group').show();
            }
            else{
                jqueryMap.$amformdisplay.getElementsByName('parameter').closest('.form-group').show();
                jqueryMap.$amformdisplay.getElementsById('runningchart').show();
                jqueryMap.$amformdisplay.getElementsById('energychart').hide();
                jqueryMap.$amformdisplay.getElementsByName('time').closest('.form-group').hide();
            }
            getData();
        });
        jqueryMap.$amformdisplay.getElementsByName('parameter').change(function(){
            configMap.urlOption.para=$(this).val();
            jqueryMap.$amformdisplay.getContainer().find('[type=checkbox]').prop('checked',false);
            jqueryMap.$amformdisplay.getContainer().find('[type=checkbox]').prop('disabled',true);
            jqueryMap.$amformdisplay.getElementsByName($(this).val()).prop('disabled',false);
            getData();
        });
        jqueryMap.$amformdisplay.getElementsByName('time').change(function(){
            configMap.urlOption.basetime=$(this).val();
            getData();
        });
        jqueryMap.$amformdisplay.getElementsByName('daytime').val(moment().format('YYYY/MM/DD'));
        jqueryMap.$amformdisplay.getElementsByName('daytime').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'YYYY/MM/DD',
            maxDate:moment(),
            sideBySide:true
        });
        jqueryMap.$amformdisplay.getElementsByName('daytime').on('dp.change',function(){
            configMap.urlOption.startdate=$(this).val();
            configMap.urlOption.datetime=$(this).val();
            getData();
        });
        jqueryMap.$amformdisplay.getContainer().find('[type=checkbox]').change(getData);
    };
    initWaterDisplay=function(){
        configMap.urlWaterOption={
            equipid:'',
            modelid:'',
            startdate:moment().format('YYYY/MM/DD'),
            basetime:'minutes',
            para:'',
            parameter:'active',
            datetime:moment().format('YYYY/MM/DD'),
            opts:'energy'
        };
        var $form=jqueryMap.$waterformdisplay.getContainer();
        $form.append('<div class="form-group col-md-12"><div  id="waterenergychart" style="width: 870px; height: 400px; margin: 0 auto"></div><div  id="waterrunningchart" style="width: 870px; height: 400px; margin: 0 auto"></div></div>');
        jqueryMap.$waterenergychart=new ecss.tools.makeChart('waterenergychart');
        jqueryMap.$waterrunningchart=new ecss.tools.makeChart('waterrunningchart');
        jqueryMap.$waterrunningchart.noData();
        jqueryMap.$waterformdisplay.getElementsByName('parameter').closest('.form-group').hide();
        jqueryMap.$waterformdisplay.getElementsById('waterrunningchart').hide();
        jqueryMap.$waterformdisplay.getContainer().find('[type=checkbox]').prop('checked',false);
        jqueryMap.$waterformdisplay.getContainer().find('[type=checkbox]').prop('disabled',true);
        jqueryMap.$waterformdisplay.getElementsByName('i').prop('disabled',false);
        jqueryMap.$waterformdisplay.getElementsByName('opts').change(function(){
            configMap.urlWaterOption.opts=$(this).val();
            if($(this).val()=='energy'){
                jqueryMap.$waterformdisplay.getElementsByName('parameter').closest('.form-group').hide();
                jqueryMap.$waterformdisplay.getElementsById('waterrunningchart').hide();
                jqueryMap.$waterformdisplay.getElementsById('waterenergychart').show();
                jqueryMap.$waterformdisplay.getElementsByName('time').closest('.form-group').show();
            }
            else{
                jqueryMap.$waterformdisplay.getElementsByName('parameter').closest('.form-group').show();
                jqueryMap.$waterformdisplay.getElementsById('waterrunningchart').show();
                jqueryMap.$waterformdisplay.getElementsById('waterenergychart').hide();
                jqueryMap.$waterformdisplay.getElementsByName('time').closest('.form-group').hide();
            }
            getWaterData();
        });
        jqueryMap.$waterformdisplay.getElementsByName('parameter').change(function(){
            configMap.urlWaterOption.parameter=$(this).val();
            getWaterData();
        });
        jqueryMap.$waterformdisplay.getElementsByName('time').change(function(){
            configMap.urlWaterOption.basetime=$(this).val();
            getWaterData();
        });
        jqueryMap.$waterformdisplay.getElementsByName('daytime').val(moment().format('YYYY/MM/DD'));
        jqueryMap.$waterformdisplay.getElementsByName('daytime').datetimepicker({
            locale:moment.locale('zh-cn'),
            format:'YYYY/MM/DD',
            maxDate:moment(),
            sideBySide:true
        });
        jqueryMap.$waterformdisplay.getElementsByName('daytime').on('dp.change',function(){
            configMap.urlWaterOption.startdate=$(this).val();
            configMap.urlWaterOption.datetime=$(this).val();
            getWaterData();
        });
    };
    getData=function(){
        if(configMap.urlOption.opts=='energy'){
            modelMap.modelam.post('getenergy',function(data){
                console.log(data);
                if(data.categories.length>0){
                    data.energy.markPoint={
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    };
                    data.energy.markLine={
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    };
                    data.energy.type='line';
                    configMap.chartOption.legend.data=[data.energy.name];
                    configMap.chartOption.xAxis[0].data=data.categories;
                    configMap.chartOption.series=[data.energy];
                    jqueryMap.$energychart.setData(configMap.chartOption);
                }else{
                    jqueryMap.$energychart.noData();
                }
            },null,configMap.urlOption,'json');
        }
        else{
            configMap.runningOption.legend.data=[];
            configMap.runningOption.series=[];
            var para=jqueryMap.$amformdisplay.getElementsByName('parameter').filter(':checked').val();
            var checks=jqueryMap.$amformdisplay.getElementsByName(para).filter(':checked');
            if(checks.length==0){
                jqueryMap.$runningchart.noData();
            }
            else
                $.each(checks,function(i,item){
                    console.log($(item).val());
                    configMap.urlOption.parameter=para+$(item).val();
                    modelMap.modelam.post('getpara',function(data){
                        console.log(data);
                        if(data.categories.length>0){
                            data.energy.type='line';
                            configMap.runningOption.legend.data.push(data.energy.name);
                            configMap.runningOption.xAxis[0].data=data.categories;
                            configMap.runningOption.series.push(data.energy);
                            jqueryMap.$runningchart.setData(configMap.runningOption);
                        }else{
                            jqueryMap.$runningchart.noData();
                        }

                    },null,configMap.urlOption,'json');
                });
        }
    };
    getWaterData=function(){
        if(configMap.urlWaterOption.opts=='energy'){
            modelMap.modelwater.post('getenergy',function(data){
                console.log(data);
                if(data.categories.length>0){
                    data.energy.markPoint={
                        data : [
                            {type : 'max', name: '最大值'},
                            {type : 'min', name: '最小值'}
                        ]
                    };
                    data.energy.markLine={
                        data : [
                            {type : 'average', name: '平均值'}
                        ]
                    };
                    data.energy.type='line';
                    configMap.waterchartOption.legend.data=[data.energy.name];
                    configMap.waterchartOption.xAxis[0].data=data.categories;
                    configMap.waterchartOption.series=[data.energy];
                    jqueryMap.$waterenergychart.setData(configMap.waterchartOption);
                }else{
                    jqueryMap.$waterenergychart.noData();
                }
            },null,configMap.urlWaterOption,'json');
        }
        else{
            configMap.waterrunningOption.legend.data=[];
            configMap.waterrunningOption.series=[];
            configMap.urlWaterOption.parameter=jqueryMap.$waterformdisplay.getElementsByName('parameter').filter(':checked').val();
            modelMap.modelwater.post('getpara',function(data){
                console.log(data);
                if(data.categories.length>0){
                    data.energy.type='line';
                    configMap.waterrunningOption.legend.data.push(data.energy.name);
                    configMap.waterrunningOption.xAxis[0].data=data.categories;
                    configMap.waterrunningOption.series.push(data.energy);
                    jqueryMap.$waterrunningchart.setData(configMap.waterrunningOption);
                }else{
                    jqueryMap.$waterrunningchart.noData();
                }

            },null,configMap.urlWaterOption,'json');
        }
    };
    getAmTableData= function () {
        var level=jqueryMap.$container.find('[name=level]').val();
        var datetime=jqueryMap.$container.find('[name=datetime]').val();
        var starttime=jqueryMap.$container.find('[name=starttime]').val();
        var endtime=jqueryMap.$container.find('[name=endtime]').val();
        if(starttime<=endtime) {
            modelMap.model.post('getamlist', function (data) {
                jqueryMap.$dtamlist.setData(data, true);
            }, function () {
                ZENG.msgbox.show('读取失败！', 5, 3000);
            }, (datetime+' '+starttime)+','+(datetime+' '+endtime)+','+level, 'json');

        }
    };
    getWaterTableData=function(){
        var datetime=jqueryMap.$container.find('[name=datetime]').val();
        var starttime=jqueryMap.$container.find('[name=starttime]').val();
        var endtime=jqueryMap.$container.find('[name=endtime]').val();
        if(starttime<=endtime) {
            modelMap.model.post('getwaterlist', function (data) {
                jqueryMap.$dtwaterlist.setData(data, true);
            }, function () {
                ZENG.msgbox.show('读取失败！', 5, 3000);
            }, (datetime+' '+starttime)+','+(datetime+' '+endtime), 'json');
        }
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        initAmDisplay();
        initWaterDisplay();
        addEvent();
        getAmTableData();
        getWaterTableData();
    };
    return{initModule:initModule};
}());