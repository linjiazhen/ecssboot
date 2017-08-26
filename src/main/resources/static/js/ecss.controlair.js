/**
 * Created by jason on 2015/1/30.
 */
ecss.controlair=(function(){
    'use strict';
    var configMap={
            main_html:String()
                +'<div class="col-md-12" id="build">'
                    +'<div class="page-title">'
                        +'<ol class="breadcrumb">'
                            +'<li><i class="fa fa-dashboard"></i> <a id="dashboard">后台主页</a></li>'
                            +'<li class="active">中央空调</li>'
                        +'</ol>'
                    +'</div>'
            +'<div class="controlscontent">'
            +'<div class="bottom-btns">'
            +'<button class="btn btn-large btn-success btnns" type="button" id="1snktzj">室内空调主机</button>'
            +'<button class="btn btn-large btn-success btnns" type="button" id="2swktzj">室外空调主机</button>'
            +'<button class="btn btn-large btn-success btnns" type="button" id="3fjpg">风机盘管</button>'
            +'<button class="btn btn-large btn-success btnns" type="button" id="4pfj">排风机</button>'
            +'</div>'
            +'<div class="controlcontent">'
            +'<div class="radiobox">'
            +'<div class="radio radio-primary radio-inline">'
            +'<label>'
            +'<input type="radio" name="machine"  value="1" checked/>1号机'
            +'</label>'
            +'<label>'
            +'<input type="radio" name="machine"  value="2"/>2号机'
            +'</label>'
            +'</div>'
            +' <button class="btn btn-success" style="margin: 0 5px 0 30px;" type="button" id="airstart">一键启动</button>'
            +'<button class="btn btn-danger" type="button" style="display: none;" id="airstop">一键停机</button>'
            +'<button class="btn btn-large btn-info btnns" type="button" id="tablemonitor">详细监测信息</button>'
            +'<button class="btn btn-large btn-warning btnns" type="button" id="tableerror">故障记录</button>'
            +'<button class="btn btn-large btn-success btnns" type="button" id="yasuoji">压缩机</button>'
            +'<button class="btn btn-large btn-success btnns" type="button" id="zhengfaqi">蒸发器</button>'
            +'<button class="btn btn-large btn-success btnns" type="button" id="lengningqi">冷凝器</button>'
            +'</div>'
            +'<table class="table table-bordered" style=" margin: 10px auto 0 auto;">'
            +'<tbody>'
            +'<tr>'
            +'<td>系统远方：待机</td>'
            +'<td>导叶：手动停</td>'
            +'<td>2014/07/30</td>'
            +'</tr>'
            +'<tr>'
            +'<td>无故障</td>'
            +'<td>主电机：运行</td>'
            +'<td>09:02:55</td>'
            +'</tr>'
            +'</tbody>'
            +'</table>'
            +'<div class="yasuoji mainmodule" style="display: block;">'
            +'<div style="position: absolute; top: 0; left: 0;">'
            +'<div class="floatleft mainmodule-left">'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">主机电流</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">导叶开度</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">轴承温度</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">油槽温度</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">供油压力</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">油槽压力</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">供油压差</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">蒸发压力</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷凝压力</p></div>'
            +'</div>'
            +'<div class="floatleft mainmodule-right">'
            +'<div class="mainmodule-right-div-kong" style="color: #5cb95c;"><p></p></div>'
            +' <div class="mainmodule-right-div" style="color: #fff;"><p>0.0%</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>0.0%</p></div>'
            +'<div class="mainmodule-right-div-kong" style="color: #5cb95c;"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>31.8℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>44.4℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>491kPa</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>492kPa</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>0kPa</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>490kPa</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>492kPa</p></div>'
            +'</div>'
            +'</div>'
            +'<div style="height: 240px; position: absolute; top: 120px; left: 780px;">'
            +'<div class="floatleft mainmodule-left" style="width: 150px;height: 240px;">'
            +'<div class="mainmodule-left-div" style="width: 150px;"><p class="floatright">冷媒水温度：进水</p></div>'
            +'<div class="mainmodule-left-div" style="width: 150px;"><p class="floatright">出水</p></div>'
            +'<div class="mainmodule-left-div" style="width: 150px;" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" style="width: 150px;" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" style="width: 150px;"><p class="floatright">冷却水温度：进水</p></div>'
            +'<div class="mainmodule-left-div" style="width: 150px;"><p class="floatright">出水</p></div>'
            +'</div>'
            +'<div class="floatleft mainmodule-right" style="height: 240px;">'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>21.4℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>21.5℃</p></div>'
            +'<div class="mainmodule-right-div-kong" style="color: #5cb95c;"><p></p></div>'
            +'<div class="mainmodule-right-div-kong" style="color: #5cb95c;"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>20.7℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>20.5℃</p></div>'
            +'</div>'
            +'</div>'
            +'</div>'
            +'<div class="zhengfaqi mainmodule" style="display: none;">'
            +'<div class="floatleft mainmodule-left" style="width: 160px;height: 240px;">'
            +'<div class="mainmodule-left-div" style="width: 160px;" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">蒸发压力</p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">蒸发温度</p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">冷媒进水温度</p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">冷媒出水温度</p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">冷媒出水温度设定值</p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">冷媒水泵</p></div>'
            +'<div class="mainmodule-left-div" style="width: 160px;"><p class="floatright">冷媒水流开关</p></div>'
            +'</div>'
            +'<div class="floatleft mainmodule-right">'
            +'<div class="mainmodule-right-div-kong" style="color: #5cb95c;"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: #5cb95c;"><p>337kPa</p></div>'
            +'<div class="mainmodule-right-div" style="color: #5cb95c;"><p>20.7℃</p></div>'
            +'<div class="mainmodule-right-div-kong" style="color: #5cb95c;"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>20.1℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>10.7℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>7.5℃</p></div>'
            +'<div class="mainmodule-right-div-kong" style="color: #fff;"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: red;"><p>运行</p></div>'
            +'<div class="mainmodule-right-div" style="color: green;"><p>通</p></div>'
            +'</div>'
            +'</div>'
            +'<div class="lengningqi mainmodule" style="display: none;">'
            +'<div class="floatleft mainmodule-left">'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div"><p class="floatright">冷却塔</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷却水泵</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷却进水</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷却出水</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright"></p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷媒水泵</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷媒进水</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">冷媒出水</p></div>'
            +'<div class="mainmodule-left-div" ><p class="floatright">水流开关</p></div>'
            +'</div>'
            +'<div class="floatleft mainmodule-right">'
            +'<div class="mainmodule-right-div-kong"><p></p></div>'
            +'<div class="mainmodule-right-div-kong"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: red;"><p>运行</p></div>'
            +'<div class="mainmodule-right-div-kong"><p></p></div>'
            +'<div class="mainmodule-right-div-kong"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: red;"><p>运行</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>28.1℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>31.1℃</p></div>'
            +'<div class="mainmodule-right-div-kong"><p></p></div>'
            +'<div class="mainmodule-right-div" style="color: red;"><p>运行</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>12.6℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: #fff;"><p>17.6℃</p></div>'
            +'<div class="mainmodule-right-div" style="color: green;"><p>通</p></div>'
            +'</div>'
            +'</div>'
            +'</div>'
            +'</div>'
            +'</div>'
            +'</div>',
            tableOption:{
                "columns": [
                    { title:"名称","data": "name" },
                    { title:"值","data": "value" },
                    { title:"备注","data": "remark" },
                    {title:"地址","data": "modbusaddress" , "visible":false}
                ],
                "dom": 'lrtp',
                "order": [[ 3, 'asc' ]]
            },
            tableBtnOption:{
                "columns": [
                    { title:"名称","data": "name" },
                    { title:"运行状态","data": "state","render":function ( data , type, full) {
                        if(data==1)
                            return "<strong class='text-success'>运行</strong>";
                        else
                            return "<strong class='text-danger'>关闭</strong>";
                    } },
                    { title:"远程控制","data": "control","render":function ( data , type, full) {
                        if(data==1)
                            return "<strong class='text-success'>可控</strong>";
                        else
                            return "<strong class='text-danger'>不可控</strong>";
                    } },
                    { title:"控制开关","data": "controladdress","render":function ( data , type, full) {
                        if(full.control==1) {
                            if (full.state == 0)
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" id="'+data+'"></label> </div></div>';
                            else
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" id="'+data+'" checked></label> </div></div>';
                        }
                        else{
                            if (full.state == 0)
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" disabled></label> </div></div>';
                            else
                                return '<div class="disclick"><div class="togglebutton"><label> <input type="checkbox" name="use" checked disabled></label> </div></div>';
                        }
                    }}
                ],
                "dom": 'lrtp',
                "order": [[ 0, 'asc' ]]
            }
        },
        stateMap={
            $container:null
        },
        modelMap={
            modelcontrol:new ecss.tools.makeModel(ecss.model.control.controlair)
        },
        jqueryMap={},
        setJqueryMap,   initModule,   addEvent;
    setJqueryMap=function(){
        var $container=stateMap.$container;
        jqueryMap={
            $container  :$container,
            $modal    :new ecss.tools.makeModal($container,''),
            $dtktxt       :new ecss.tools.makeTable($container.find('.modal-body').last().html('<table class="table table-hover table-bordered" id="DTktxt"></table>').find('#DTktxt'),configMap.tableBtnOption),
            $confirm    :new ecss.tools.makeModal($container),
            $window     :$(window)
        };
    };
    addEvent=function(){
        $('#yasuoji,#zhengfaqi,#lengningqi').click(function () {
            $('.mainmodule').hide();
            $('.'+$(this).prop('id')).show();
        });
        $("#1snktzj,#2swktzj,#3fjpg,#4pfj").click(function () {
            jqueryMap.$modal.setTitle($(this).text());
            jqueryMap.$modal.hideFooter();
            modelMap.modelcontrol.post('getktxt',function (data) {
                jqueryMap.$dtktxt.clear();
                jqueryMap.$dtktxt.setData(data);
                jqueryMap.$modal.show();
            },function () {
                ZENG.msgbox.show('获取数据失败！',5,3000);
            },$(this).prop('id').substr(0,1),'json');
        });
    };
    initModule=function($container){
        stateMap.$container=$container;
        $container.html(configMap.main_html);
        setJqueryMap();
        addEvent();
    };
    return{initModule:initModule};
}());