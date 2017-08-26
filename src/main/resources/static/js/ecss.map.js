/**
 * Created by jason on 2015/1/30.
 */
ecss.map=(function(){
    'use strict';
    var configMap={
            main_html:String()
                +'<div id="allmap" ></div>'
                +'<div>'
                +'<div class="btn-group">'
                    +'<div class="btn btn-material-yellow" id="electric">电网</div>'
                    +'<div class="btn btn-white" id="water">水网</div>'
                +'</div>'
                +'<div class="btn btn-material-green" id="school" style="float: right;">学校概况</div>'
                +'</div>'
                +'<div id="info" style="display: none"></div>',
            content_school:String()+'<p style="text-indent:28px">福建工程学院是福建省人民政府举办的以工为主，涵盖工、管、文、理、经、法、艺等多学科的全日制普通本科高校，是入选教育部首批“卓越工程师教育培养计划”的试点高校，是福建省重点建设高校，2013年，经国务院学院学位委员会批准为硕士学位授权单位。</p>'
            +'<p style="text-indent:28px">学校办学历史溯源于1896年清末著名乡贤名士陈璧、孙葆瑨、力钧，著名闽绅林纾、末代帝师陈宝琛创办的“苍霞精舍”，解放前为享有盛誉的“福建高工”。办学以来，已培养了14万多名面向基层一线的应用型人才，校友遍及海内外，在建筑、机械、电子电气等各行业做出不凡贡献，素有福建“建筑业的黄埔军校”、“机电工程师的摇篮”美誉。</p>'
            +'<p style="text-indent:28px">学校位于福建省省会福州市，占地面积136.88公顷，校舍建筑面积62.24万平方米，其中大学城新校区107.20公顷，已投入使用的建筑面积41.95万平方米。</p>'
            +'<p style="text-indent:28px">学校传承“大机电、大土木”优势，构建服务海峡西岸经济区建设的五大专业群，即装备制造业、电子信息和高新技术产业、建筑类产业、交通运输和物流业、文化产业专业群。现有教师总数1396人，50个本科专业，6个硕士研究生专业，拥有1个省级特色重点学科，5个省一级重点学科。学校现设机械与汽车工程学院、材料科学与工程学院、信息科学与工程学院、土木工程学院、建筑与城乡规划学院、管理学院、生态环境与城市建设学院、交通运输学院、人文学院、法学院、数理学院、思想政治理论课教研部、体育教研部等13个院系（部）及继续教育学院，另有3个经省教育厅批准成立的"校企合作"办学机构：国脉信息学院、软件学院、海峡工学院。全日制在校生26691余人。</p>',
            content_build:String()
                +'<div class="scontent" style="display: none">'
                +'<div style="height: 180px">'
                    +'<div class="text-center" id="title" style="border-bottom: 1px solid #35bd32;"></div>'
                    +'<div class="bg-success">'
                        +'<p>总计层数:<ins id="floor"></ins>层</p>'
                        +'<p>建筑面积:<ins id="area"></ins>平方米</p>'
                        +'<p>所属区域:<ins id="group"></ins></p>'
                        +'<p>建筑类型:<ins id="func"></ins></p>'
                    +'</div>'
                    +'<div class="bg-info">'
                        +'<div>本月电量:<ins id="monthammeter">0</ins>度<a style="margin-left: 10px">详情>>></a></div>'
                    +'</div>'
                +'</div>'
                +'</div>',
            content_organ:String()
                +'<div class="scontent" style="display: none">'
                +'<div style="height: 80px">'
                +'<div class="text-center" id="title" style="border-bottom: 1px solid #35bd32;"></div>'
                +'<div class="bg-success">'
                +'<p>部门类型:<ins id="type"></ins></p>'
                +'</div>'
                +'<div class="bg-info">'
                +'<div>本月电量:<ins id="monthammeter">0</ins>度<a style="margin-left: 10px">详情>>></a></div>'
                +'</div>'
                +'</div>'
                +'</div>',
            content_water:String()
                +'<div class="scontent" style="display: none">'
                +'<div style="height: 200px">'
                +'<div class="text-center" id="title" style="border-bottom: 1px solid #35bd32;"></div>'
                +'<div class="bg-success">'
                +'<p>表盘数:<ins id="consum"></ins></p>'
                +'<p>水流:<ins id="flow"></ins></p>'
                +'<p>水压:<ins id="u"></ins></p>'
                +'<p>采集时间:<ins id="datatime"></ins></p>'
                +'<p>位置:<ins id="remark"></ins></p>'
                +'</div>'
                +'<div class="bg-info">'
                +'<div>本月水量:<ins id="monthwater">0</ins>吨<a style="margin-left: 10px">详情>>></a></div>'
                +'</div>'
                +'</div>'
                +'</div>',
            formBuildOption:[
                [{label:'label',text:'区域',size:3},{label:'select',name:'group',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'楼栋',size:3},{label:'select',name:'build',size:8,valid:{notEmpty: {}}}]
            ],
            formOrganOption:[
                [{label:'label',text:'类型',size:3},{label:'select',name:'type',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'名称',size:3},{label:'select',name:'name',size:8,valid:{notEmpty: {}}}]
            ],
            formWaterOption:[
                [{label:'label',text:'网关',size:3},{label:'select',name:'gateway',size:8,valid:{notEmpty: {}}}],
                [{label:'label',text:'设备号',size:3},{label:'select',name:'equipid',size:8,valid:{notEmpty: {}}}]
            ]
        },
        stateMap={
        },
        modelMap={
            modelenergy:new ecss.tools.makeModel(ecss.model.energy.energy),
            modelwater:new ecss.tools.makeModel(ecss.model.maintenance.watermeter),
            model:new ecss.tools.makeModel(ecss.model.home.map),
            modeltools:new ecss.tools.makeModel(ecss.model.tools.option)
        },
        option = {
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
                    magicType: {show : true, title : {line : '折线图', bar : '柱形图'}, type : ['line', 'bar']},
                    saveAsImage : {show : true, title : '保存为图片', type : 'jpeg', lang : ['点击本地保存']}
                }
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
                    name : null,
                    splitLine : {show : false},
                    axisLabel : {
                        formatter: '{value} 度'
                    }
                }
            ],
            series:[{
                name:'消耗量',
                type:'bar',
                data:[]
            }]
        },
        jqueryMap={},
        setJqueryMap,   initModule, initMap,    addOverViewAndMarker,getAllPoints,setContents, panelShow,txtMenuItem,addOptions,addMenu,changetype,showDetail,showChart,schooldetail;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $window     :$(window),
            $buildmodal      :new ecss.tools.makeModal($container,'标记楼栋'),
            $buildform  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formBuildOption),
            $organmodal      :new ecss.tools.makeModal($container,'标记机构'),
            $organform  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formOrganOption),
            $watermodal      :new ecss.tools.makeModal($container,'标记水表'),
            $waterform  :new ecss.tools.makeForm($container.find('.modal-body').last(),configMap.formWaterOption),
            $schoolmodal      :new ecss.tools.makeModal($container,'福建工程学院概况'),
            panelnum    :0,
            panellist   :[],
            $charts     :[],
            buildid     :null,
            organid     :null,
            waterid     :null
        };
        stateMap={
            floor:{},
            addpoint:{},
            type:'electric',
            subtype:null,
            para:{
                model:null,
                modellevel:'build',
                modelid:null,
                energytypeid:'01000',
                energytype:'电',
                startdate:null,
                enddate:null,
                basetime:null,
                caltype:'total'
            }
        };
    };
    changetype=function(e){
        var id=$(this).prop('id');
        $(this).parent().find('.btn-material-yellow').toggleClass('btn-material-yellow btn-white');
        $(this).toggleClass('btn-material-yellow btn-white');
        stateMap.type=id;
        stateMap.subtype=null;
        console.log(stateMap.type);
        getAllPoints(e,'build');
    };
    panelShow=function(){
        jqueryMap.panellist[jqueryMap.panelnum]=$.jsPanel({
            title: '能耗展示-'+stateMap.name,
            bootstrap: 'success',
            content:'<form id="p'+jqueryMap.panelnum+'" class="col-lg-offset-4">'+
            '<div class="radio radio-success radio-inline " style="margin-top: 0"> '+
            '<label> '+
            '<input type="radio" name="view" value="month"/>年'+
            '</label> '+
            '</div>'+
            '<div class="radio radio-success radio-inline">'+
            '<label>'+
            '<input type="radio" name="view" value="day" checked/>月'+
            '</label>'+
            '</div>'+
            '<div class="radio radio-success radio-inline">'+
            '<label>'+
            '<input type="radio" name="view" value="hour"/>日'+
            '</label>'+
            '</div>'+
            '</form>' +
            '<div  id="'+jqueryMap.panelnum+'" style="width: 100%; height: 90%; margin: 0 auto"></div>',
            size:     {width: 800, height: 450},
            controls: { buttons: "closeonly", iconfont: "bootstrap" },
            resizable: false,
            callback: function () {
                $.material.init();
                jqueryMap.$charts[jqueryMap.panelnum]=new ecss.tools.makeChart(jqueryMap.panelnum,option);
                showChart($('#p'+jqueryMap.panelnum+' input[name="view"]:checked').val(),jqueryMap.panelnum);
                $('#p'+jqueryMap.panelnum+' input[name="view"]').change(function(){  //年月日视图切换
                    showChart($(this).val(),$(this).parents('form').prop('id').substring(1));
                });
                jqueryMap.panelnum++;
            }
        });
    };
    //type:显示时间类型  id:对应的对话框
    showChart=function(type,id){
        console.log(id);
        stateMap.para.basetime=type;
        if(type=='hour'){
            stateMap.para.startdate=moment().format('YYYY/MM/DD');
            stateMap.para.enddate=moment().add(1,'days').format('YYYY/MM/DD');
        }
        else
        if(type=='day'){
            stateMap.para.startdate=moment().subtract(30,'days').format('YYYY/MM/DD');
            stateMap.para.enddate=moment().add(1,'days').format('YYYY/MM/DD');
        }
        else{
            stateMap.para.startdate=moment(moment().format('YYYY/MM')).subtract(12,'month').format('YYYY/MM/DD');
            stateMap.para.enddate=moment(moment().format('YYYY/MM')).add(1,'month').format('YYYY/MM/DD');
        }
        if(stateMap.type=='water'){
            stateMap.para.energytype='水';
            stateMap.para.energytypeid='02000';
            modelMap.modelenergy.post('getwaterenergymap', function (data) {
                console.log(data);
                if(data.energy.data!=null&&data.energy.data.length>0){
                    option.tooltip={
                        trigger: 'axis',
                        formatter: function (params,ticket,callback) {
                            var res = '耗水量 : ' + params[0].value+'<br/>';
                            return res;
                        }
                    };
                    option.xAxis[0].data=data.categories;
                    option.series[0].data=data.energy.data;
                    option.yAxis[0].name = "总耗水量:"+data.energy.data.reduce(function(a,b){return a+b;}).toFixed(4)+'吨';
                    option.yAxis[0].axisLabel = {
                        formatter: '{value} 吨'
                    };
                    jqueryMap.$charts[id].setData(option);
                }else{
                    jqueryMap.$charts[id].noData();
                }
            }, null, stateMap.para, 'json');
        }
        else {
            stateMap.para.energytype='电';
            stateMap.para.energytypeid='01000';
            modelMap.modelenergy.post('getenergy', function (data) {
                if (data.energy.data != null && data.energy.data.length > 0) {
                    option.tooltip={
                        trigger: 'axis',
                        formatter: function (params,ticket,callback) {
                            var res = '耗电量 : ' + params[0].value+'<br/>';
                            if(stateMap.floor[params[0].name])
                            $.each(stateMap.floor[params[0].name],function(key,value){
                                res+=key+':'+value+'<br/>';
                            });
                            return res;
                        }
                    };
                    option.xAxis[0].data = data.categories;
                    option.yAxis[0].name = "总耗电量:"+data.energy.data.reduce(function(a,b){return a+b;}).toFixed(4)+'度';
                    option.yAxis[0].axisLabel = {
                        formatter: '{value} 度'
                    };
                    option.series[0].data = data.energy.data;
                    jqueryMap.$charts[id].setData(option);
                } else {
                    jqueryMap.$charts[id].noData();
                }
                modelMap.modeltools.post('getfloor',function(data){
                    stateMap.floor={};
                    var para={};
                    $.extend(para,stateMap.para);
                    para.modellevel='floor';
                    $.each(data,function(i,item){
                        para.modelid=item.id;
                        modelMap.modelenergy.post('getenergy',function(data){
                            $.each(data.categories,function(i,time){
                                if(!stateMap.floor[time]) stateMap.floor[time]={};
                                if(!stateMap.floor[time][item.name])
                                    stateMap.floor[time][item.name]=data.energy.data[i];
                                else
                                    stateMap.floor[time][item.name]+=data.energy.data[i];
                            });
                        },null,para,'json',false);
                    });
                },null,stateMap.para.modelid,'json');
            }, null, stateMap.para, 'json');
        }
    };
    addOverViewAndMarker=function() {
        var map=jqueryMap.map;
        var overView = new BMapLib.OverViewControl({offset: new BMap.Size(0, 0),isOpen:false});
        map.addControl(overView); // 添加缩略地图控件
        var minMap = overView._map;

        var div = document.createElement("div");
        div.style.position = "absolute";
        div.style.border = "solid 2px #444";
        var child = document.createElement("div");
        child.style.width = "50px";
        child.style.height = "40px";
        child.style.background = "#444";
        child.style.opacity = "0.2";
        div.appendChild(child);
        var marker = new BMapLib.RichMarker(div, map.getCenter(), {
            "enableDragging": true
        });
        minMap.addOverlay(marker);
        marker.addEventListener("dragend", function (e) {
            map.setCenter(marker.getPosition());
        });
        minMap.addEventListener("click", function (e) {
            map.setCenter(e.point);
            marker.setPosition(e.point);
        });
        jqueryMap.marker=marker;
    };
    initMap=function(){
        var tileLayer = new BMap.TileLayer();
        tileLayer.getTilesUrl = function (tileCoord, zoom) {
            var x = tileCoord.x;
            var y = tileCoord.y;
            return 'images/tiles/' + zoom + '/tile' + x + '_' + y + '.png';
        };
        var MyMap = new BMap.MapType('MyMap', tileLayer, {minZoom: 1, maxZoom: 6});
        var map = new BMap.Map('allmap', {mapType: MyMap});
        map.centerAndZoom(new BMap.Point(0, 0), 5);
        map.enableScrollWheelZoom();
        map.setDefaultCursor('default');
        jqueryMap.map=map;
        addOverViewAndMarker();
        map.addEventListener("dragend", function () {
            jqueryMap.marker.setPosition(map.getCenter());
        });
        map.addEventListener("zoomend", function () {
            jqueryMap.marker.setPosition(map.getCenter());
        });
        map.addEventListener("tilesloaded", function () {
            $(".anchorBL").hide();
            $(".BMap_cpyCtrl").hide();
        });
    };
    setContents=function(item){
        stateMap.name=item.name;
        jqueryMap.infoWindow.setContent($('.scontent').html());
        jqueryMap.infoWindow.redraw();
        if(stateMap.type=='electric') {
            stateMap.para.energytype='电';
            stateMap.para.energytypeid='01000';
            if (stateMap.subtype == 'build') {
                modelMap.model.post('getbuild', function (data) {
                    $('#title').html(data.name);
                    $('#floor').html(data.upfloor + data.zerofloor + data.downfloor);
                    $('#func').html(data.buildtype);
                    $('#area').html(data.area);
                    $('#group').html(data.group);
                    stateMap.para.model = 'build';
                    stateMap.para.modelid = data.id;
                    stateMap.para.basetime = 'month';
                    stateMap.para.startdate = moment().format('YYYY/MM/01');
                    stateMap.para.enddate = moment().add(1, 'month').format('YYYY/MM/01');
                    modelMap.modelenergy.post('getenergy', function (data) {
                        if (data.energy != null)
                            $('#monthammeter').html(data.energy.data[0]);
                        else
                            $('#monthammeter').html(0);
                    }, null, stateMap.para, 'json');
                }, null, item.id, 'json');
            }
            else if (stateMap.subtype == 'organ') {
                modelMap.model.post('getorgan', function (data) {
                    $('#title').html(data.text);
                    $('#type').html(data.type);
                    stateMap.para.model = 'organ';
                    stateMap.para.modelid = data.id;
                    stateMap.para.basetime = 'month';
                    stateMap.para.startdate = moment().format('YYYY/MM/01');
                    stateMap.para.enddate = moment().add(1, 'month').format('YYYY/MM/01');
                    modelMap.modelenergy.post('getenergy', function (data) {
                        if (data.energy != null)
                            $('#monthammeter').html(data.energy.data[0]);
                        else
                            $('#monthammeter').html(0);
                    }, null, stateMap.para, 'json');
                }, null, item.id, 'json');
            }
        }else{
            stateMap.para.energytype='水';
            stateMap.para.energytypeid='02000';
            modelMap.model.post('getwaterinfo', function (data) {
                var s=data.datatime.split(',');
                $('#title').html(s[1]);
                $('#consum').html(data.consum);
                $('#u').html(data.u);
                $('#flow').html(data.flow);
                $('#datatime').html(s[0]);
                $('#remark').html(s[1]);
                stateMap.para.model='water';
                stateMap.para.modelid = data.id;
                stateMap.para.basetime='month';
                stateMap.para.startdate = moment().format('YYYY/MM/01');
                stateMap.para.enddate = moment().add(1, 'month').format('YYYY/MM/01');
                modelMap.modelenergy.post('getwatermonthenergy', function (data) {
                    console.log(data);
                    $('#monthwater').html(data);
                }, null, stateMap.para.modelid+','+moment().format('YYYY-MM'), 'json');
            }, null, item.id, 'json');
        }
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
    txtMenuItem = [
        {
            text: '添加标记',
            callback: function () {
                if(stateMap.subtype=='build') {
                    jqueryMap.$buildmodal.show(function () {
                        modelMap.model.post('addbuildpoint', function () {
                            stateMap.subtype=null;
                            getAllPoints(null,'build');
                            ZENG.msgbox.show('添加成功！', 4, 3000);
                            jqueryMap.$buildmodal.hide();
                        }, function () {
                            ZENG.msgbox.show('添加失败！', 5, 3000);
                        }, jqueryMap.$buildform.save().build + "," + stateMap.addpoint.lng + "," + stateMap.addpoint.lat);
                    });
                }
                else
                if(stateMap.subtype=='organ') {
                    jqueryMap.$organmodal.show(function () {
                        modelMap.model.post('addorganpoint', function () {
                            stateMap.subtype=null;
                            getAllPoints(null,'organ');
                            ZENG.msgbox.show('添加成功！', 4, 3000);
                            jqueryMap.$organmodal.hide();
                        }, function () {
                            ZENG.msgbox.show('添加失败！', 5, 3000);
                        }, jqueryMap.$organform.save().name + "," + stateMap.addpoint.lng + "," + stateMap.addpoint.lat);
                    });
                }
                else
                if(stateMap.type=='water'){
                    jqueryMap.$watermodal.show(function () {
                        modelMap.model.post('addwaterpoint', function () {
                            getAllPoints();
                            ZENG.msgbox.show('添加成功！', 4, 3000);
                            jqueryMap.$watermodal.hide();
                        }, function () {
                            ZENG.msgbox.show('添加失败！', 5, 3000);
                        }, jqueryMap.$waterform.save().equipid + "," + stateMap.addpoint.lng + "," + stateMap.addpoint.lat);
                    });
                }
            }
        },{
            text:'删除标记',
            callback:function(e,ee,maker){
                if(stateMap.subtype=='build') {
                    modelMap.model.post('deletebuildpoint', function () {
                        stateMap.subtype=null;
                        getAllPoints(null,'build');
                        ZENG.msgbox.show('删除成功！', 4, 3000);
                    }, function () {
                        ZENG.msgbox.show('删除失败！', 5, 3000);
                    }, maker.getLabel().content);
                }
                else
                if(stateMap.subtype=='organ') {
                    modelMap.model.post('deleteorganpoint', function () {
                        stateMap.subtype=null;
                        getAllPoints(null,'organ');
                        ZENG.msgbox.show('删除成功！', 4, 3000);
                    }, function () {
                        ZENG.msgbox.show('删除失败！', 5, 3000);
                    }, maker.getLabel().content);
                }
                else
                if(stateMap.type=='water'){
                    console.log(maker.getLabel().content);
                    modelMap.model.post('deletewaterpoint', function () {
                        getAllPoints();
                        ZENG.msgbox.show('删除成功！', 4, 3000);
                    }, function () {
                        ZENG.msgbox.show('删除失败！', 5, 3000);
                    }, maker.getLabel().content);
                }
            }
        }];
    addMenu=function(){

        var $group = jqueryMap.$buildform.getElementsByName('group');
        $group.html('<option value="">选择区域</option>');
        addOptions('getgroup', null, $group, '<option value="allofsumgroup">选择区域</option>');
        var $build = jqueryMap.$buildform.getElementsByName('build');
        $build.html('<option value="">选择建筑</option>');
        $group.change(function () {
            addOptions('getbuild', $(this).val(), $build, '<option value="">选择建筑</option>');
        });

        var $type = jqueryMap.$organform.getElementsByName('type');
        $type.html('<option value="">选择类型</option>');
        addOptions('getorgantypes', null, $type, '<option value="allofsumgroup">选择类型</option>');
        var $name = jqueryMap.$organform.getElementsByName('name');
        $name.html('<option value="">选择机构</option>');
        $type.change(function () {
            addOptions('getorgansbytype', $(this).val(), $name, '<option value="">选择机构</option>');
        });

        var $gateway = jqueryMap.$waterform.getElementsByName('gateway');
        $gateway.html('<option value="">选择网关</option>');
        addOptions('getgatewayinwater', null, $gateway, '<option value="allofsumgroup">选择区域</option>');
        var $equipid = jqueryMap.$waterform.getElementsByName('equipid');
        $equipid.html('<option value="">选择设备</option>');
        $gateway.change(function () {
            addOptions('getequipbygateway', $(this).val(), $equipid, '<option value="">选择设备</option>');
        });
        console.log(ecss.menu.menus);
        if(ecss.menu.menus!=null&&ecss.menu.menus.length>7) {
            var menu = new BMap.ContextMenu();
            menu.addItem(new BMap.MenuItem(txtMenuItem[0].text, txtMenuItem[0].callback, 100));
            jqueryMap.map.addContextMenu(menu);
            jqueryMap.map.addEventListener("rightclick", function (e) {
                stateMap.addpoint.lng = e.point.lng;
                stateMap.addpoint.lat = e.point.lat;
            });
        }
    };
    getAllPoints=function(event,id){
        var url;
        if(stateMap.type=='electric'){
            if(id=='build') {
                if(stateMap.subtype=='build') return;
                url='getallpoints';
                stateMap.subtype='build';
                $('#info').html(configMap.content_build);
            }
            else
            if(id=='organ') {
                if(stateMap.subtype=='organ') return;
                url = 'getallorganpoints';
                stateMap.subtype='organ';
                $('#info').html(configMap.content_organ);
            }
            else{
                if(stateMap.subtype=='build')
                    url='getpoints';
                else
                    url='getorganpoints';
            }
        }
        else
        if(stateMap.type=='water'){
            if(id=='water') return;
            url='getwaterpoints';
            $('#info').html(configMap.content_water);
        }
        modelMap.model.post(url,function(data){
            jqueryMap.map.clearOverlays();
            if(data.length!=0) {
                var index = Math.round(data.length / 2)-1;
                jqueryMap.map.centerAndZoom(new BMap.Point(data[index].longitude, data[index].latitude), 5);
            }
            $.each(data,function(i,item) {
                if(item.latitude!=0) {
                    var point = new BMap.Point(item.longitude, item.latitude);
                    var marker1 = new BMap.Marker(point);
                    jqueryMap.map.addOverlay(marker1);
                    var label = new BMap.Label(item.name, {offset: new BMap.Size(20, -10)});
                    marker1.setLabel(label);
                    marker1.addEventListener('click', function (event) {
                        setContents(item);
                        jqueryMap.map.openInfoWindow(jqueryMap.infoWindow, event.point);
                        $('.bg-info a').click(panelShow);
                    });
                    if (ecss.menu.menus != null && ecss.menu.menus.length > 7) {
                        var markerMenu = new BMap.ContextMenu();
                        markerMenu.addItem(new BMap.MenuItem(txtMenuItem[1].text, txtMenuItem[1].callback.bind(marker1), 100));
                        marker1.addContextMenu(markerMenu);
                    }
                }
            });
        },null,id,'json');
        jqueryMap.infoWindow=new BMap.InfoWindow(jqueryMap.$container.find('.scontent'),{enableMessage:false});
    };
    schooldetail=function(){
        jqueryMap.$schoolmodal.show();
        jqueryMap.$schoolmodal.setBody(configMap.content_school);
        jqueryMap.$schoolmodal.hideFooter();
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        initMap();
        addMenu();
        stateMap.type='electric';
        getAllPoints(null,'build');
        $('.btn-group').children().click(changetype);
        $('#school').click(schooldetail);
        $.gevent.subscribe(jqueryMap.$container,'getPoints',getAllPoints);
    };
    return{initModule:initModule};
}());